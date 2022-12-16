package HttpServlet;

import java.util.HashMap;

public class HttpSession {
    public static final HashMap<String, HttpSession> sessions = new HashMap<>();
    String id;
    HashMap<String, Object> attributes = new HashMap<>();

    public Object getAttribute(String userName) {
        return attributes.get(userName);
    }
}
