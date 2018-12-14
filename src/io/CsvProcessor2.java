package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
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
        public Map<String, Object> parse(String line) {

            String[] camps = line.split(",");
            
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("nom", camps[0]);
            map.put("edat", Integer.parseInt(camps[1]));
            map.put("sou", Double.parseDouble(camps[2]));
            map.put("ciutat", camps[3]);
            map.put("estudis", Integer.parseInt(camps[4]));

            return map;
        }

    }
    
    // PROCESSOR
    
    private static class MinMaxEdatProcessor implements RowProcessor {

        private int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        
        @Override
        public void process(Map<String, Object> row) {
            
            int edat = (int) row.get("edat");
            if (edat > max) {
                max = edat;
            }
            if (edat < min) {
                min = edat;
            }
        }
        
        public int getMin() {
            return min;
        }
        
        public int getMax() {
            return max;
        }
    }
}
