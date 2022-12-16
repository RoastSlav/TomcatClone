package HttpServlet;

import Models.Cookie;
import enums.HTTP_REQUEST_METHOD;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpServletRequest implements ServletRequest {
    public String path;
    HTTP_REQUEST_METHOD method;
    String protocol;
    HashMap<String, String> headers = new HashMap<>();
    ArrayList<Cookie> cookies = new ArrayList<>();
    InputStream inputStream = null;

    @Override
    public Cookie[] getCookies() {
        return cookies.toArray(Cookie[]::new);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public String getHeader(String header) {
        return headers.get(header);
    }

    @Override
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getMethod() {
        return method.toString();
    }

    @Override
    public String getPathInfo() {
        return path;
    }

    @Override
    public HttpSession getSession() {
        return this.getSession(true);
    }

    @Override
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
