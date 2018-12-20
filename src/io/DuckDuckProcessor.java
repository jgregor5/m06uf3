package io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class DuckDuckProcessor extends BaseLineProcessor {

    public static void main(String[] args) {

        processQuery("angry birds");
    }

    private static void processQuery(String query) {

        RowIterableParser parser = new DuckDuckParser2();
        RowProcessor processor = new PrintProcessor();

        try {
            iterateStream(getConnectionInputStream(buildQuery(query)), parser, processor);

        } catch (IOException ex) {
            throw new RuntimeException("querying duckduck", ex);
        }
    }
    
    private static String buildQuery(String query) {
        return "https://duckduckgo.com/lite?q=" + query.toLowerCase().replace(' ', '+');
    }

    private static InputStream getConnectionInputStream(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:47.0) Gecko/20100101 Firefox/47.0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        return conn.getInputStream();
    }

    private static class DuckDuckParser implements RowIterableParser {

        @Override
        public Iterable<Map<String, Object>> parse(String content) {

            Document document = Jsoup.parse(content);
            //System.out.println(document);
            List<Map<String, Object>> list = new ArrayList<>();

            int count = 0;
            Elements links = document.select("tr:not(.result-sponsored) a.result-link");
            
            for (Element link : links) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("position", ++count);
                map.put("url", link.attr("href"));
                list.add(map);
            }

            return list;
        }
    }

    private static class DuckDuckParser2 implements RowIterableParser {

        @Override
        public Iterable<Map<String, Object>> parse(String content) {

            Document document = Jsoup.parse(content);
            //System.out.println(document);
            List<Map<String, Object>> list = new ArrayList<>();

            Elements tables = document.select("table:has(.result-link)");
            if (tables.size() != 1) {
                throw new RuntimeException("tables size is " + tables.size());
            }
            
            int count = 0;
            Elements trs = tables.get(0).select("tr:not(.result-sponsored)");
            String link = null, text = null, description = null;

            for (Element tr : trs) {

                Element a = tr.selectFirst("a.result-link");
                if (a != null) {
                    link = a.attr("href");
                    text = a.text();
                }

                Element td = tr.selectFirst("td.result-snippet");
                if (td != null) {
                    description = td.text();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("position", ++count);
                    map.put("text", text);
                    map.put("url", link);
                    map.put("description", description);
                    list.add(map);
                }
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
