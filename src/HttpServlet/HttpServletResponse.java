package HttpServlet;

import Models.Cookie;
import enums.STATUS_CODE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpServletResponse {
    Socket conn;
    HashMap<String, String> headers = new HashMap<>();
    STATUS_CODE status;
    String protocol;
    String contentType;
    ArrayList<Cookie> cookies = new ArrayList<>();

    public HttpServletResponse(Socket conn) {
        this.conn = conn;
    }

    public void sendError(STATUS_CODE code) throws IOException {
        status = code;
        Writer writer = getWriter();
        writer.write("There was an error: " + code);
        writer.flush();
    }

    public OutputStream getOutputStream() throws IOException {
        sendHeaders();
        return conn.getOutputStream();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Writer getWriter() throws IOException {
        sendHeaders();
        return new OutputStreamWriter(getOutputStream());
    }

    public int getStatus() {
        return status.value;
    }

    public void setStatus(int status) {
        this.status = STATUS_CODE.fromValue(status);
    }

    public void setHeaders(String header, String value) {
        headers.put(header, value);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void sendHeaders() throws IOException {
        OutputStream clientOutput = conn.getOutputStream();
        clientOutput.write((protocol + " " + status + "\r\n").getBytes());
        for (Map.Entry<String, String> header : headers.entrySet())
            clientOutput.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
    }
}
