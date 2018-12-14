package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author julian
 */
public class XmlProcessor extends DefaultHandler  {
    
    private String tag, started;
    private List<String> subtags;
    private Map<String, Object> values;
    
    public static void main(String[] args) {
        
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            
            XmlProcessor processor = new XmlProcessor("ciutada", new String[]{
            "nom", "edat", "sou", "ciutat", "estudis"});
                        
            saxParser.parse(new FileInputStream("data/ciutadans.xml"), processor);
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("reading xml", e);
        }
    }
    
    public XmlProcessor(String tag, String[] subtags) {
        this.tag = tag;
        this.subtags = Arrays.asList(subtags);
        this.started = null;
    }
        
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        if (qName.equals(tag)) {
            values = new HashMap<>();
        }
        else {
            if (subtags.contains(qName)) {
                started = qName;
            }
            else {
                started = null;
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException { 
        
        if (qName.equals(tag)) {
            System.out.println(values);
        }
    }
    
    @Override
    public void characters(char ch[], int start, int length) throws SAXException { 
        
        if (started == null) {
            return;
        }
        
        String strValue = new String(ch, start, length);
        Object value = strValue;        
        
        switch (started) {
                case "nom":
                    break;
                case "edat": 
                    value = Integer.parseInt(strValue);
                    break;
                case "sou": 
                    value =  Double.parseDouble(strValue);
                    break;
                case "ciutat": 
                    break;
                case "estudis": 
                    value = Integer.parseInt(strValue);
                    break;
                default:
                    value = null;
            }
        
        if (value != null) {
            values.put(started, value);        
        }
        started = null;
    }
}
