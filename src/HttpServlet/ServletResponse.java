package HttpServlet;

import Models.Cookie;
import enums.STATUS_CODE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface ServletResponse {
    void sendError(STATUS_CODE code) throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getContentType();

    void setContentType(String contentType);

    Writer getWriter() throws IOException;

    int getStatus();

    void setStatus(int status);

    void setHeaders(String header, String value);

    void addCookie(Cookie cookie);

    void sendHeaders() throws IOException;
}
