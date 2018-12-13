package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author julian
 */
public class CsvProcessor {
    
    public static void main(String[] args) {
        
        readCsv("data/ciutadans.csv", true);
    }
    
    private static void readCsv(String filename, boolean hasHeader) {
        
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            
            String columnNames = hasHeader? br.readLine() : null;
            
            String line;
            while ((line = br.readLine()) != null) {
                
                String[] camps = line.split(",");
                if (camps.length != 5) {
                    continue;
                }
                
                int edat = Integer.parseInt(camps[1]);
                if (edat > max) {
                    max = edat;
                }
                if (edat < min) {
                    min = edat;
                }
                 
                Map<String, Object> map = new HashMap<>();
                map.put("nom", camps[0]);
                map.put("edat", edat);
                map.put("sou", Double.parseDouble(camps[2]));
                map.put("ciutat", camps[3]);
                map.put("estudis", Integer.parseInt(camps[4]));
                System.out.println(map);
            }
            
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("file not found: " + filename, ex);
        } catch (IOException ex) {
            throw new RuntimeException("reading csv: " + filename, ex);
        }
        
        System.out.println("min: " + min);
        System.out.println("max: " + max);
    }
}
