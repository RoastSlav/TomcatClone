package HttpServlet;

import enums.STATUS_CODE;

import java.io.IOException;

public abstract class HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(STATUS_CODE.METHOD_NOT_ALLOWED);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(STATUS_CODE.METHOD_NOT_ALLOWED);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(STATUS_CODE.METHOD_NOT_ALLOWED);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(STATUS_CODE.METHOD_NOT_ALLOWED);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (request.method) {
            case GET -> doGet(request, response);
            case POST -> doPost(request, response);
            case PUT -> doPut(request, response);
            case DELETE -> doDelete(request, response);
        }
    }

    public void init() {

    }
}
