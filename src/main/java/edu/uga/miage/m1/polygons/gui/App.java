package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;

import javax.json.*;
import java.awt.*;
import java.io.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
            String xmlStr  = PrettyUtils.xmlStringPrettify(xml);
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
                    out.write(xmlStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


    /**
     * Construit un string du JSON pour l'export
     * @param arrayList Liste de JSONVisitor
     */
    public static void shapeJsonBuilder(List<JSonVisitor> arrayList) {
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

    /**
     * Construit un string du JSON pour l'export
     * @param arrayList Liste de XMLVisitor
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws TransformerException
     * @throws SAXException
     */
    public static void xmlBuilder(List<XMLVisitor> arrayList) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        StringBuilder xmlStringBuilder = new StringBuilder("<shapes>");
        for (XMLVisitor jv: arrayList){
            xmlStringBuilder.append(jv.getRepresentation());
        }
        xmlStringBuilder.append("</shapes>");
        App.xmlToFile(xmlStringBuilder.toString());
    }
    public enum FileFormat {
        JSON, XML
    }
    private static JsonObject jsonObject = null;
    private static Document xmlDoc = null;
    public static JsonObject getJsonObject() {
        return jsonObject;
    }
    public static Document getXmlDoc() {
        return xmlDoc;
    }
    public static FileFormat importDispatcher() {
        //choose either as json or xml file
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Importer un fichier");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Xml et json acceptés","xml","json"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            String extension = FilenameUtils.getExtension(path);
            if (extension.equals("xml")) {
                try {
                    xmlDoc =importXml(path);
                    return FileFormat.JSON;
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
            } else if (extension.equals("json")) {
                try {
                    App.jsonObject = importJSON(path);
                    return FileFormat.JSON;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public static JsonObject importJSON(String path) throws FileNotFoundException, MyRuntimeException {
        //choose a file to import
            JsonReader reader = Json.createReader(new FileReader(path));
            JsonObject jsonObject = reader.readObject();
            reader.close();
            return jsonObject;
    }



    public static Document importXml(String path) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public static void main( String[] args ){
        EventQueue.invokeLater(() -> GUIHelper.showOnFrame("test"));

    }

    private static class PrettyUtils {

        /**
         * Prettifier pour string de json
         * source : <a href="https://stackoverflow.com/questions/4105795/pretty-print-json-in-java">...</a>
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

