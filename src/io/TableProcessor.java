package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author julian
 */
public class TableProcessor extends BaseLineProcessor {
    
    public static void main(String[] args) {
        processIterable("data/taula.html");
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
            
            Document document = Jsoup.parse(content);
            //System.out.println(document);
            List<Map<String, Object>> list = new ArrayList<>();

            Elements trs = document.select("tr");
            
            for (Element tr : trs) {
                Map<String, Object> map = new LinkedHashMap<>();
                
                Elements tds = tr.select("td");
                if (tds.isEmpty()) { // header
                    continue;
                }
                
                int idx = 0;
                for (Element td: tds) {
                    switch (idx) {
                        case 0: map.put("nom", td.text()); break;
                        case 1: map.put("edat", Integer.parseInt(td.text())); break;
                        case 2: map.put("sou", Double.parseDouble(td.text())); break;
                        case 3: map.put("ciutat", td.text()); break;
                        case 4: map.put("estudis", Integer.parseInt(td.text())); break;
                    }
                    
                    idx ++;
                }
                
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
