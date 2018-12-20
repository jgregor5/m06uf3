package io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class WallapopProcessor extends BaseLineProcessor {

    public static void main(String[] args) {

        String catId = "15000"; // electronica
        String latitud = "41.54329"; 
        String longitud = "2.10942";
        
        processQuery("mac mini", catId, latitud, longitud);
    }

    private static void processQuery(String query, String catId, String latitud, String longitud) {

        RowIterableParser parser = new WallapopParser();
        RowProcessor processor = new PrintProcessor();

        try {
            InputStream is = getConnectionInputStream(
                    buildQuery(query, catId), 
                    buildCookie(latitud, longitud));
            
            iterateStream(is, parser, processor);

        } catch (IOException ex) {
            throw new RuntimeException("querying wallapop", ex);
        }
    }
    
    private static String buildQuery(String query, String catId) {        
        query = query.replace(" ", "%20");        
        return "https://es.wallapop.com/rest/items?_p=1&kws=" + query + 
                "&catIds=" + catId + "&dist=400&publishDate=any&orderBy=distance&orderType=asc";
    }
    
    private static String buildCookie(String latitud, String longitud) {
        return "searchLat=" + latitud + "; searchLng=" + longitud + ";";
    }

    private static InputStream getConnectionInputStream(String urlStr, String cookie) throws IOException {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        return conn.getInputStream();
    }

    private static class WallapopParser implements RowIterableParser {

        private JSONParser parser;

        public WallapopParser() {
            this.parser = new JSONParser();
        }

        @Override
        public Iterable<Map<String, Object>> parse(String content) {

            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(content);
            } catch (ParseException ex) {
                throw new RuntimeException("parsing wallapop", ex);
            }
            
            JSONArray items = (JSONArray) json.get("items");
            List<Map<String, Object>> list = new ArrayList<>();
            
            for (Object item : items) {
                JSONObject jobj = (JSONObject) item;
                JSONObject location = (JSONObject) jobj.get("location");
                
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("title", jobj.get("title"));
                map.put("price", jobj.get("price"));
                map.put("location", location.get("city"));
                map.put("description", jobj.get("description"));

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
