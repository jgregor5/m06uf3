package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author julian
 */
public abstract class BaseLineProcessor {

    public static void readStream(InputStream is, boolean ignoreFirstLine,
            RowParser parser, RowProcessor... processors) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String columnNames = ignoreFirstLine ? br.readLine() : null;

            int count = 0;
            String line;
            
            while ((line = br.readLine()) != null) {

                Map<String, Object> map = parser.parse(line);

                for (RowProcessor processor : processors) {
                    processor.process(map);
                }
                
                count ++;
            }
            
            System.out.println("lines read: " + count);

        } catch (IOException ex) {
            throw new RuntimeException("reading csv: " + is, ex);
        }
    }
    
    public static void readXml(InputStream is, String tag, String[] subtags, 
            FieldParser parser, RowProcessor... processors) {
        
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            
            XmlRowHandler handler = new XmlRowHandler("ciutada", new String[]{
                "nom", "edat", "sou", "ciutat", "estudis"
            }, parser, processors);
            
            saxParser.parse(is, handler);
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("reading xml", e);
        }
    }
    
    // INTERFACES

    public interface RowParser {

        Map<String, Object> parse(String row);
    }

    public interface RowProcessor {

        void process(Map<String, Object> row);
    }
    
    public interface FieldParser {
        
        Object parse(String key, String value);
    }
}
