package HttpServlet;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ServletContext {
    private static final Logger logger = Logger.getLogger(ServletContext.class);
    private String path;
    private String docBase;
    private static final HashMap<String, HttpFilter> filters = new HashMap<>();
    private static final HashMap<String, HttpServlet> servlets = new HashMap<>();

    public ServletContext(String path, String docBase) {
        this.path = path;
        this.docBase = docBase;
        try {
            parseWebXmlForContext();
        } catch (Exception e) {
            logger.warn("Error while parsing web.xml file in " + docBase, e);
        }
    }

    public String getContextPath() {
        return docBase;
    }

    private void parseWebXmlForContext() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = Path.of(docBase, "WEB-INF/web.xml").toFile();
        if (!xmlFile.exists()) {
            logger.warn("Web configuration file was not found for servlet context " + path);
            //todo what to do with the context in this case?
            return;
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        NodeList filters = document.getElementsByTagName("filter");
        parseFilters(filters);

        NodeList filterMappings = document.getElementsByTagName("filter-mapping");
        parseFilterMappings(filterMappings);

        NodeList servlets = document.getElementsByTagName("servlet");
        parseServlets(servlets);

        NodeList servletMappings = document.getElementsByTagName("servlet-mapping");
        parseServletMappings(servletMappings);
    }

    private void parseFilters(NodeList filtersNodeList) {
        for (int i = 0; i < filtersNodeList.getLength(); i++) {
            Node filterNameNode = filtersNodeList.item(i).getAttributes().getNamedItem("filter-name");
            if (filterNameNode == null) {
                logger.warn("Filter name was not specified");
                continue;
            }

            Node filterClassNode = filtersNodeList.item(i).getAttributes().getNamedItem("filter-class");
            if (filterClassNode == null) {
                logger.warn("Filter class was not specified");
                continue;
            }

            String filterName = filterNameNode.getNodeValue();
            String filterClass = filterClassNode.getNodeValue();
            filters.put(filterName, new HttpFilter(filterName, filterClass));
        }
    }

    private void parseFilterMappings(NodeList filterMappings) {
        for (int i = 0; i < filterMappings.getLength(); i++) {
            Node filterNameNode = filterMappings.item(i).getAttributes().getNamedItem("filter-name");
            if (filterNameNode == null) {
                logger.warn("Filter name was not specified");
                continue;
            }

            Node urlPatternNode = filterMappings.item(i).getAttributes().getNamedItem("url-pattern");
            if (urlPatternNode == null) {
                logger.warn("Url pattern was not specified");
                continue;
            }

            String filterName = filterNameNode.getNodeValue();
            String urlPattern = urlPatternNode.getNodeValue();
            filters.get(filterName).addUrlPattern(urlPattern);
        }
    }

    private void parseServlets(NodeList servletsNodeList) {
        for (int i = 0; i < servletsNodeList.getLength(); i++) {
            Node servletNameNode = servletsNodeList.item(i).getAttributes().getNamedItem("servlet-name");
            if (servletNameNode == null) {
                logger.warn("Servlet name was not specified");
                continue;
            }

            Node servletClassNode = servletsNodeList.item(i).getAttributes().getNamedItem("servlet-class");
            if (servletClassNode == null) {
                logger.warn("Servlet class was not specified");
                continue;
            }

            String servletName = servletNameNode.getNodeValue();
            String servletClass = servletClassNode.getNodeValue();

            try {
                Class<?> aClass = Class.forName(servletName);
                HttpServlet servlet = (HttpServlet) aClass.getDeclaredConstructor().newInstance();
                servlets.put(servletName, servlet);
            } catch (Exception e) {
                logger.warn("Servlet class " + servletClass + " was not found");
            }
        }
    }

    private void parseServletMappings(NodeList servletMappings) {
        for (int i = 0; i < servletMappings.getLength(); i++) {
            Node servletNameNode = servletMappings.item(i).getAttributes().getNamedItem("servlet-name");
            if (servletNameNode == null) {
                logger.warn("Servlet name was not specified");
                continue;
            }

            Node urlPatternNode = servletMappings.item(i).getAttributes().getNamedItem("url-pattern");
            if (urlPatternNode == null) {
                logger.warn("Url pattern was not specified");
                continue;
            }

            String servletName = servletNameNode.getNodeValue();
            String urlPattern = urlPatternNode.getNodeValue();
            servlets.get(servletName).addUrlPattern(urlPattern);
        }
    }
}
