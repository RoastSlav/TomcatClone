package HttpServlet.Servlets;

import HttpServlet.*;
import enums.STATUS_CODE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class StaticContentServlet extends HttpServlet {
    private static final String INDEX_FILE = "\\index.html";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        Path path = Path.of(pathInfo);
        if (!path.toFile().exists()) {
            response.sendError(STATUS_CODE.NOT_FOUND);
            return;
        }

        if (path.toFile().isDirectory())
            handleDirectory(request, response, path);
        else
            handleFile(request, response, path);
    }

    private void handleDirectory(HttpServletRequest request, HttpServletResponse response, Path path) throws IOException {
        Path indexFile = Paths.get(path.toString(), INDEX_FILE);
        if (Files.exists(indexFile)) {
            response.setHeaders("last-modified", String.valueOf(Files.getLastModifiedTime(indexFile)));
            response.setHeaders("content-type", Files.probeContentType(indexFile));
            OutputStream outputStream = response.getOutputStream();
            Files.copy(indexFile, outputStream);
            outputStream.flush();
        } else {
            response.setHeaders("last-modified", String.valueOf(Files.getLastModifiedTime(path)));
            response.setHeaders("content-type", "text/html");
            Writer writer = response.getWriter();
            writer.write("<html><body>");
            Iterator<Path> files = Files.list(path).iterator();
            while (files.hasNext()) {
                Path next = files.next();
                String fileName = "<a href=\"" + next.toString() + "\">" + next.getFileName().toString() + "</a> </br>";
                writer.write(fileName);
            }
            writer.write("</body></html>");
            writer.flush();
        }
    }

    private void handleFile(HttpServletRequest request, HttpServletResponse response, Path path) throws IOException {
        response.setHeaders("last-modified", String.valueOf(Files.getLastModifiedTime(path)));
        response.setHeaders("content-type", Files.probeContentType(path));
        OutputStream outputStream = response.getOutputStream();
        Files.copy(path, outputStream);
        outputStream.flush();
    }
}
