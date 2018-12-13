package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * @author julian
 */
public class BaseLineProcessor {

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
    
    // INTERFACES

    public interface RowParser {

        Map<String, Object> parse(String row);
    }

    public interface RowProcessor {

        void process(Map<String, Object> row);
    }
}
