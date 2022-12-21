package HttpServlet;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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

public class ServletConfig {
    private static final int DEFAULT_THREAD_COUNT = 1;
    private static final int DEFAULT_PORT = 8085;
    private static final Logger logger = Logger.getLogger(ServletConfig.class);
    private static final HashMap<String, ServletContext> contexts = new HashMap<>();
    private static int port = DEFAULT_PORT;
    private static String protocol;
    private static String serviceName;
    protected ServletConfig() {
        File xmlFile = Path.of("conf/server.xml").toFile();
        if (!xmlFile.exists()) {
            System.err.println("Server configuration file was not found!");
            System.exit(7);
        }

        Document serverXml;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            serverXml = documentBuilder.parse(xmlFile);

            Node serviceList = serverXml.getElementsByTagName("Connector").item(0);
            String portString = serviceList.getAttributes().getNamedItem("port").getNodeValue();
            port = Integer.parseInt(portString);

            protocol = serviceList.getAttributes().getNamedItem("protocol").getNodeValue();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            logger.fatal("Error while parsing Server.xml file", e);
            throw new RuntimeException("Error while parsing Server.xml file", e);
        }

        NodeList contextList = serverXml.getElementsByTagName("Context");
        for (int i = 0; i < contextList.getLength(); i++) {
            Node contextNode = contextList.item(i);
            NamedNodeMap attributes = contextNode.getAttributes();
            Node pathNode = attributes.getNamedItem("path");
            if (pathNode == null) {
                logger.warn("Context path was not specified");
                continue;
            }

            Node docBaseNode = attributes.getNamedItem("docBase");
            if (docBaseNode == null) {
                logger.warn("Context docBase was not specified");
                continue;
            }

            String path = pathNode.getNodeValue();
            String docBase = docBaseNode.getNodeValue();
            contexts.put(path, new ServletContext(path, docBase));
        }
    }

    public static ServletContext getServletContext(String name) {
        return contexts.get(name);
    }
}
