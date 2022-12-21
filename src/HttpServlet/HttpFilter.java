package HttpServlet;

import java.io.IOException;
import java.util.ArrayList;

public class HttpFilter {
    protected String name;
    protected final ArrayList<String> urlPattern = new ArrayList<>();

    public HttpFilter(String name) {
        this.name = name;
        init();
    }

    private void addUrlPattern(String urlPattern) {
        this.urlPattern.add(urlPattern);
    }

    public void init()  {}
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {

    }
}
