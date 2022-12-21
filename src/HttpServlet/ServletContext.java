package HttpServlet;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class ServletContext {
    private String path;
    private String docBase;
    private static final HashMap<String, HttpFilter> filters = new HashMap<>();
    private static final HashMap<String, HttpServlet> servlets = new HashMap<>();

    public ServletContext(String path, String docBase) {
        this.path = path;
        this.docBase = docBase;
    }

    public String getContextPath() {
        return docBase;
    }

    private void parseWebXmlForContext() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(docBase + "/WEB-INF/web.xml");
        NodeList filters = document.getElementsByTagName("filter");
        parseFilters(filters);
        NodeList filterMappings = document.getElementsByTagName("filter-mapping");
        parseFilterMappings(filterMappings, context);
        NodeList servlets = document.getElementsByTagName("servlet");
        parseServlets(servlets, context);
        NodeList servletMappings = document.getElementsByTagName("servlet-mapping");
        parseServletMappings(servletMappings, context);
    }

    private void parseFilters(NodeList filters) {
        for (int i = 0; i < filters.getLength(); i++) {
            String filterName = filters.item(i).getAttributes().getNamedItem("filter-name").getNodeValue();
            String filterClass = filters.item(i).getAttributes().getNamedItem("filter-class").getNodeValue();
            context.addFilter(filterName, filterClass);
        }
    }
}
