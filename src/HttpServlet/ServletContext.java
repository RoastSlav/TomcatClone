package HttpServlet;

import java.util.HashMap;

public class ServletContext {
    private final String name;
    private String contextPath;
    HashMap<String, Object> attributes = new HashMap<>();
    HashMap<String, String> params = new HashMap<>();

    public ServletContext(String name) {
        this.name = name;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Object getAttribute(String userName) {
        return attributes.get(userName);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public String getInitParameter(String name) {
        return params.get(name);
    }

    public String[] getInitParameterNames() {
        return params.keySet().toArray(String[]::new);
    }
}
