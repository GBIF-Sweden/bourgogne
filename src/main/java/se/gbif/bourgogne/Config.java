/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Class for reading a config file
 * @author korbinus
 */
public class Config {
    private Integer id;
    private String name;
    private String format;
    private String URI;
    
    void read(File config) throws ParserConfigurationException, SAXException, IOException, ParseException, XPathExpressionException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        XPathExpression expr = null;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(config);
        
        // Create a XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();
        
        // Create a XPathObject
        XPath xpath = xFactory.newXPath();
        
        // Run the query and get values
        id = Integer.parseInt((String)xpath.compile("//config/id/text()").evaluate(doc, XPathConstants.STRING));
        name = (String)xpath.compile("//config/name/text()").evaluate(doc, XPathConstants.STRING);
        format = (String)xpath.compile("//config/format/text()").evaluate(doc, XPathConstants.STRING);
        URI = (String)xpath.compile("//config/URI/text()").evaluate(doc, XPathConstants.STRING);
    }
    
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return format
     */
    public String getFormat() {
        return format;
    }
    
    /**
     * @return URI
     */
    public String getURI() {
        return URI;
    }
}
