package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;

import javax.json.*;
import java.io.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.google.gson.*;
import javax.swing.filechooser.FileFilter;


/**
 * Hello world!
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public class App
{

    public static void jsonToFile(String json) {
        String path = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sauvegarder un fichier Json");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        PrettyUtils.strictFileFilter(chooser, "json");
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath();
        }
        // force la bonne extension
        path = FilenameUtils.removeExtension(path) + ".json";
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(PrettyUtils.jsonPrettify(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public static void xmlToFile(String xml) throws ParserConfigurationException, IOException, TransformerException, SAXException {

        xml = PrettyUtils.xmlStringPrettify(xml);

            JFileChooser chooser = new JFileChooser();
            String path = null;
            chooser.setDialogTitle("Sauvegarde xml");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            PrettyUtils.strictFileFilter(chooser, "xml");
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getAbsolutePath();
            }
            // force la bonne extension
            path = FilenameUtils.removeExtension(path) + ".xml";
                try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                    out.write(xml.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


    public static void shapeJsonBuilder(ArrayList<JSonVisitor> arrayList) {
        StringBuilder jsonString = new StringBuilder("{ \"shapes\":[");
        for (JSonVisitor jv: arrayList){
            jsonString.append(jv.getRepresentation());
            if (arrayList.indexOf(jv)<arrayList.size()-1) {
                jsonString.append(',');
            }
        }
        jsonString.append("] }");
        App.jsonToFile(jsonString.toString());
    }

    public static String xmlBuilder(ArrayList<XMLVisitor> arrayList) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        StringBuilder xmlStringBuilder = new StringBuilder("<shapes>");
        for (XMLVisitor jv: arrayList){
            xmlStringBuilder.append(jv.getRepresentation());
        }
        xmlStringBuilder.append("</shapes>");
        App.xmlToFile(xmlStringBuilder.toString());
        //format the xml string to make it more readable

        return xmlStringBuilder.toString();
    }

    public static JsonObject importJSON() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("export.json");
        JsonReader reader = Json.createReader(inputStream);
        JsonObject json = reader.readObject();
        reader.close();
        return json;
    }

    public static void main( String[] args ) throws InterruptedException, FileNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUIHelper.showOnFrame("test");
            }
        });

    }

    private static class PrettyUtils {

        /**
         * Prettifier pour string de json
         * source : <a href="https://stackoverflow.com/questions/4105795/pretty-print-json-in-java">...</a>
         * @param jsonString
         * @return String
         */
        public static String jsonPrettify(String jsonString) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement json = JsonParser.parseString(jsonString);
            return gson.toJson(json);
        }

        /**
         * Prettifer pour le fichier xml
         * source: <a href="https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java">...</a>
         */
        public static String xmlStringPrettify(String xmlString) throws ParserConfigurationException, IOException, SAXException, TransformerException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            return result.getWriter().toString();
        }

        public static void strictFileFilter(JFileChooser chooser, String extension) {
            chooser.setFileFilter(new FileFilter() {
                public String getDescription() {
                    return extension;
                }
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    String ext = FilenameUtils.getExtension(f.getName());
                    return ext.equals(extension);
                }
            });
        }

    }

    public static class MyRuntimeException extends RuntimeException {
        public MyRuntimeException(String errorMessage) {
            Logger logger = Logger.getLogger("MyRuntimeException");
            logger.log(new LogRecord(Level.FINE, errorMessage));
        }
    }
}

