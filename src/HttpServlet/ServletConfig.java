package HttpServlet;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class ServletConfig {
    private static final HashMap<String, ServletContext> contexts = new HashMap<>();
    private static String port;
    private static String serviceName;
    protected ServletConfig(String webXmlPath) throws ParserConfigurationException, IOException, SAXException {
        parseXml(webXmlPath);
    }

    public static ServletContext getServletContext(String name) {
        return contexts.get(name);
    }

    private void buildContext(String name) {
        ServletContext context = new ServletContext(name);
        contexts.put(name, context);
    }

    public void parseXml(String webXmlPath, ServletContext context) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(webXmlPath);
        NodeList filters = document.getElementsByTagName("filter");
        parseFilters(filters, context);
        NodeList filterMappings = document.getElementsByTagName("filter-mapping");
        parseFilterMappings(filterMappings, context);
        NodeList servlets = document.getElementsByTagName("servlet");
        parseServlets(servlets, context);
        NodeList servletMappings = document.getElementsByTagName("servlet-mapping");
        parseServletMappings(servletMappings, context);
    }

    private void parseFilters(NodeList filters, ServletContext context) {
        for (int i = 0; i < filters.getLength(); i++) {
            String filterName = filters.item(i).getAttributes().getNamedItem("filter-name").getNodeValue();
            String filterClass = filters.item(i).getAttributes().getNamedItem("filter-class").getNodeValue();
            context.addFilter(filterName, filterClass);
        }
    }
}
