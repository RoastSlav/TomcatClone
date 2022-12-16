package HttpServlet;

import Models.Cookie;
import enums.HTTP_REQUEST_METHOD;
import enums.STATUS_CODE;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Scanner;

public class HttpTask implements Runnable {
    private static final Logger logger = Logger.getLogger(HttpTask.class);
    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
    static CommandLine cmd;
    Socket connection;

    public HttpTask(Socket connection, CommandLine cmd) {
        this.connection = connection;
        HttpTask.cmd = cmd;
    }

    private static HttpServletRequest parseRequest(String requestString) throws ParseException {
        String[] methodAndHeaders = requestString.split("\r\n");
        HttpServletRequest httpRequest = new HttpServletRequest();

        String[] methodAndPath = methodAndHeaders[0].split(" ");
        String httpMethod = methodAndPath[0];
        try {
            httpRequest.method = HTTP_REQUEST_METHOD.valueOf(httpMethod);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid methodAndPath type: " + httpMethod);
            throw new ParseException("Invalid methodAndPath type: " + httpMethod, 0);
        }
        httpRequest.path = methodAndPath[1];
        httpRequest.protocol = methodAndPath[2];

        for (int i = 1; i < methodAndHeaders.length; i++) {
            String header = methodAndHeaders[i];
            String[] nameAndValues = header.split(": ");

            if (nameAndValues[0].equals("Cookie")) {
                String[] values = nameAndValues[1].split(";");
                Cookie cookie = new Cookie();
                for (String value : values) {
                    String[] keyAndValue = value.split("=");
                    if (keyAndValue[0].equals("expires"))
                        cookie.expires = dateFormat.parse(keyAndValue[1]);
                    else if (keyAndValue[0].equals("path")) {
                        cookie.path = keyAndValue[1];
                    }
                    cookie.value = keyAndValue[1];
                }
                httpRequest.addCookie(cookie);
            } else
                httpRequest.addHeader(nameAndValues[0], nameAndValues[1]);
        }
        return httpRequest;
    }

    @Override
    public void run() {
        StringBuilder requestString = new StringBuilder();
        HttpServletRequest request;
        HttpServletResponse response = new HttpServletResponse(connection);
        try {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(""))
                    break;
                requestString.append(line).append("\r\n");
            }


            //TODO: REMOVE THIS!!
            if (requestString.isEmpty())
                return;

            request = parseRequest(requestString.toString());
            request.inputStream = connection.getInputStream();
        } catch (Exception e) {
            logger.error("Error while receiving request", e);
            try {
                response.sendError(STATUS_CODE.INTERNAL_SERVER_ERROR);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }

        try {
            ServletDispatcher servletDispatcher = new ServletDispatcher();
            servletDispatcher.dispatch(request, response);
        } catch (Exception e) {
            logger.error("There was an error sending the response", e);
            try {
                response.sendError(STATUS_CODE.INTERNAL_SERVER_ERROR);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
