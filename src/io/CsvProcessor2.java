package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 *
 * @author julian
 */
public class CsvProcessor2 extends BaseLineProcessor {
    
    public static void main(String[] args) {
        
        processCiutadans("data/ciutadans.csv");
    }
    
    public static void processCiutadans(String filename) {
        
        CiudadansRowParser ciutadansParser = new CiudadansRowParser();
        MinMaxEdatProcessor minMaxEdatProcessor = new MinMaxEdatProcessor();

        try {
            FileInputStream fis = new FileInputStream(filename);            
            readStream(fis, true, ciutadansParser, minMaxEdatProcessor);
            
            System.out.println("min: " + minMaxEdatProcessor.getMin());
            System.out.println("max: " + minMaxEdatProcessor.getMax());
            
        } catch (FileNotFoundException ex) {
            System.out.println("file not found: " + filename);
        }
    }
    
    // PARSER
    
    private static class CiudadansRowParser implements RowParser {

        @Override
        public Map<String, Object> parse(String row) {

            return null;
        }

    }
    
    // PROCESSOR
    
    private static class MinMaxEdatProcessor implements RowProcessor {

        @Override
        public void process(Map<String, Object> row) {
            
        }
        
        public int getMin() {
            return 0;
        }
        
        public int getMax() {
            return 0;
        }
    }
}
