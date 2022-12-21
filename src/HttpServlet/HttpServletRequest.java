package HttpServlet;

import Models.Cookie;
import enums.HTTP_REQUEST_METHOD;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpServletRequest {
    public String path;
    HTTP_REQUEST_METHOD method;
    String protocol;
    HashMap<String, String> headers = new HashMap<>();
    ArrayList<Cookie> cookies = new ArrayList<>();
    InputStream inputStream = null;

    public Cookie[] getCookies() {
        return cookies.toArray(Cookie[]::new);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method.toString();
    }

    public String getPathInfo() {
        return path;
    }

    public HttpSession getSession() {
        return this.getSession(true);
    }

    public HttpSession getSession(boolean create) {
        HttpSession session = null;
        for (Cookie cookie : cookies) {
            if (cookie.cookieName.equals("JSESSIONID")) {
                session = HttpSession.sessions.get(cookie.value);
            }
        }
        if (session == null && create) {
            session = new HttpSession();
            HttpSession.sessions.put(session.id, session);
        }
        return session;
    }
}
