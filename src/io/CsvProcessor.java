package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author julian
 */
public class CsvProcessor {
    
    public static void main(String[] args) {
        
        readCsv("data/ciutadans.csv", true);
    }
    
    private static void readCsv(String filename, boolean hasHeader) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            
            String columnNames = hasHeader? br.readLine() : null;
            
            String line;
            while ((line = br.readLine()) != null) {
                
                System.out.println(line);
            }
            
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("file not found: " + filename, ex);
        } catch (IOException ex) {
            throw new RuntimeException("reading csv: " + filename, ex);
        }
    }
}
