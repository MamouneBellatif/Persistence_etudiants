package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

import javax.json.*;
import java.io.*;

import java.util.ArrayList;
import javax.json.JsonReader;

/**
 * Hello world!
 * 
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public class App 
{
    
    public static void jsonToFile(String json) {
        String path = "export.json";
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xmlToFile(String xml) {
        String path = "export.xml";
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(xml.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static JsonObject shapeJsonBuilder(ArrayList<JSonVisitor> arrayList) {
        StringBuilder jsonString = new StringBuilder("{ \"shapes\":[");
        for (JSonVisitor jv: arrayList){
            jsonString.append(jv.getRepresentation());
            if (arrayList.indexOf(jv)<arrayList.size()-1) {
                jsonString.append(',');
            }
        }
        jsonString.append("] }");
        App.jsonToFile(jsonString.toString());
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString.toString()));
        System.out.println(jsonReader.toString());
        return jsonReader.readObject();
    }

    public static String xmlBuilder(ArrayList<XMLVisitor> arrayList) {
        StringBuilder jsonString = new StringBuilder("<shapes>");
        for (XMLVisitor jv: arrayList){
            jsonString.append(jv.getRepresentation());

        }
        jsonString.append("</shapes>");
        App.xmlToFile(jsonString.toString());
        System.out.println(jsonString.toString());
        return jsonString.toString();
    }

    public static JsonObject importJSON() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("export.json");
        JsonReader reader = Json.createReader(inputStream);
        JsonObject json = reader.readObject();
        reader.close();
        return json;
    }

    public static void main( String[] args ) throws InterruptedException, FileNotFoundException {

        System.out.println(App.importJSON());


		GUIHelper.showOnFrame("test");
	}
}
