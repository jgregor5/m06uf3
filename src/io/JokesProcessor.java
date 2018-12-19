package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author julian
 */
public class JokesProcessor extends BaseLineProcessor {
    
    public static void main(String[] args) {
        
        processIterable("data/jokes.json");
    }
    
    private static void processIterable(String filename) {
        
        RowIterableParser parser = new MyIterableParser();
        RowProcessor processor = new PrintProcessor();

        try {
            InputStream is = new FileInputStream(filename);
            iterateStream(is, parser, processor);

        } catch (IOException ex) {
            throw new RuntimeException("querying duckduck", ex);
        }
    }
    
    private static class MyIterableParser implements RowIterableParser {

        @Override
        public Iterable<Map<String, Object>> parse(String content) {
            
            JSONParser parser = new JSONParser();            
            JSONArray array = null;
            try {
                array = (JSONArray) parser.parse(content);
            } catch (ParseException ex) {
                throw new RuntimeException("parsing json", ex);
            }
            
            List<Map<String, Object>> list = new ArrayList<>();
            
            for (Object item: array) {
                JSONObject object = (JSONObject) item;
                Map<String, Object> map = new HashMap<>();
                map.putAll(object);                
                list.add(map);
            }
            return list;
        }
        
    }
    
    private static class PrintProcessor implements RowProcessor {

        @Override
        public void process(Map<String, Object> row) {
            System.out.println(row);
        }

    }
}
