package io;

import io.BaseLineProcessor.FieldParser;
import io.BaseLineProcessor.RowProcessor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 *
 * @author julian
 */
public class XmlProcessor2 extends BaseLineProcessor {
    
    public static void main(String[] args) {
        
        processCiutadans("data/ciutadans.xml");
    }
    
    public static void processCiutadans(String filename) {

        FieldParser parser = new CiutadansFieldParser();
        RowProcessor printProcessor = new CiutadansPrintProcessor();

        try {
            readXml(new FileInputStream(filename), "ciutada", new String[]{
                "nom", "edat", "sou", "ciutat", "estudis"
            }, parser, printProcessor);

        } catch (FileNotFoundException ex) {
            throw new RuntimeException("reading ciutadans", ex);
        }
    }
    
    private static class CiutadansFieldParser implements FieldParser {

        @Override
        public Object parse(String key, String value) {
            
            return null;
        }
    }
    
    private static class CiutadansPrintProcessor implements RowProcessor {

        @Override
        public void process(Map<String, Object> row) {
            System.out.println(row);
        }
    }
}
