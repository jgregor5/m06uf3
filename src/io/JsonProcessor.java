package io;

import static io.BaseLineProcessor.readStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 *
 * @author julian
 */
public class JsonProcessor extends BaseLineProcessor {
    
    public static void processCiutadans(String filename) {
        
        CiudadansRowParser parser = new CiudadansRowParser();
        WriteXmlProcessor processor = new WriteXmlProcessor();

        try {
            FileInputStream fis = new FileInputStream(filename);            
            readStream(fis, true, parser, processor);
            
        } catch (FileNotFoundException ex) {
            System.out.println("file not found: " + filename);
        }
        
        processor.close();
    }
    
    private static class CiudadansRowParser implements RowParser {

        @Override
        public Map<String, Object> parse(String row) {
            
            return null;
        }
        
    }
    
    private static class WriteXmlProcessor implements RowProcessor {

        public WriteXmlProcessor() {
            // abre el fichero y escribe cabecera
        }
        
        @Override
        public void process(Map<String, Object> row) {

            // escribir un registro XML
        }
        
        public void close() {
            // escribe pie y cierra el fichero
        }
    }
}
