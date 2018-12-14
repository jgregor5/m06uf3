package io;

import io.BaseLineProcessor.FieldParser;
import io.BaseLineProcessor.RowProcessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author julian
 */
public class XmlRowHandler extends DefaultHandler {
    
    private String tag, started;
    private List<String> subtags;
    private Map<String, Object> values;
    private FieldParser parser;
    private RowProcessor[] processors;
    private int count;
    
    public XmlRowHandler(String tag, String[] subtags, FieldParser parser, 
            RowProcessor... processors) {        
        this.tag = tag;
        this.subtags = Arrays.asList(subtags);
        this.started = null;
        this.parser = parser;
        this.processors = processors;
        this.count = 0;
    }
    
    @Override
    public void endDocument() {
        System.out.println("elements read: " + count);
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
            for (RowProcessor processor: this.processors) {
                processor.process(values);    
                count ++;
            }
        }
    }
    
    @Override
    public void characters(char ch[], int start, int length) throws SAXException { 
        
        if (started == null) {
            return;
        }
        
        Object value = parser.parse(started, new String(ch, start, length));
        if (value != null) {
            values.put(started, value);    
        }
        
        started = null;
    }
}
