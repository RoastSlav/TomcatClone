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

public class HttpServletResponse implements ServletResponse {
    Socket conn;
    HashMap<String, String> headers = new HashMap<>();
    STATUS_CODE status;
    String protocol;
    String contentType;
    ArrayList<Cookie> cookies = new ArrayList<>();

    public HttpServletResponse(Socket conn) {
        this.conn = conn;
    }

    @Override
    public void sendError(STATUS_CODE code) throws IOException {
        status = code;
        Writer writer = getWriter();
        writer.write("There was an error: " + code);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        sendHeaders();
        return conn.getOutputStream();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Writer getWriter() throws IOException {
        sendHeaders();
        return new OutputStreamWriter(getOutputStream());
    }

    @Override
    public int getStatus() {
        return status.value;
    }

    @Override
    public void setStatus(int status) {
        this.status = STATUS_CODE.fromValue(status);
    }

    @Override
    public void setHeaders(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public void sendHeaders() throws IOException {
        OutputStream clientOutput = conn.getOutputStream();
        clientOutput.write((protocol + " " + status + "\r\n").getBytes());
        for (Map.Entry<String, String> header : headers.entrySet())
            clientOutput.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
    }
}
