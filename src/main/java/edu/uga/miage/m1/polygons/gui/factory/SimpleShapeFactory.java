package edu.uga.miage.m1.polygons.gui.factory;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

 public class  SimpleShapeFactory {
     private SimpleShapeFactory() { /* */ }
    public static SimpleShape createSimpleShape(String shapeType, int x, int y) {
        if (shapeType == null || shapeType.isEmpty() || x < 0 || y < 0)  {
            return null;
        }
        if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square(x, y);
        } else if (shapeType.equalsIgnoreCase("TRIANGLE")) {
            return new Triangle(x, y);
        } else if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle(x,y);
        }
        return null;
    }
}

