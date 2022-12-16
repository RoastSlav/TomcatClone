package HttpServlet;

import Models.Cookie;

import java.util.HashMap;

public interface ServletRequest {
    Cookie[] getCookies();

    void addCookie(Cookie cookie);

    String getHeader(String header);

    void addHeader(String header, String value);

    HashMap<String, String> getHeaders();

    String getMethod();

    String getPathInfo();

    HttpSession getSession();

    HttpSession getSession(boolean create);
}
