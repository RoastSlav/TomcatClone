package HttpServlet;

import enums.STATUS_CODE;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class ServletDispatcher {
    private static final Logger logger = Logger.getLogger(ServletDispatcher.class);
    //Servlet path, servlet name
    public final HashMap<String, String> servletPaths = new HashMap<>();
    public final HashMap<String, HttpServlet> servlets = new HashMap<>();

    public void registerServlet(String path, String name) {
        servletPaths.put(path, name);
    }

    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletName = servletPaths.get(request.path);
        if (servletName == null) {
            logger.warn("A non existent servlet was requested: " + request.path);
            response.sendError(STATUS_CODE.INTERNAL_SERVER_ERROR);
        }
        try {
            HttpServlet servlet;
            if (servlets.containsKey(servletName)) {
                servlet = servlets.get(servletName);
            } else {
                Class<?> aClass = Class.forName(servletName);
                servlet = (HttpServlet) aClass.getDeclaredConstructor().newInstance();
                servlets.put(servletName, servlet);
                servlet.init();
            }
            servlet.service(request, response);
        } catch (Exception e) {
            logger.error("Error while dispatching request", e);
            response.sendError(STATUS_CODE.INTERNAL_SERVER_ERROR);
        }
    }


}
