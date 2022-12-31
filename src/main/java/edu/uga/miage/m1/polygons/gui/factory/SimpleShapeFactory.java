package edu.uga.miage.m1.polygons.gui.factory;

import edu.uga.miage.m1.polygons.gui.shapes.*;

public class  SimpleShapeFactory {
     private SimpleShapeFactory() { /* */ }
    public static SimpleShape createSimpleShape(String shapeType, int x, int y) {
        if (shapeType == null || shapeType.isEmpty() || x < 0 || y < 0)  {
            return null;
        }
        if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square(x, y);
        }
        if (shapeType.equalsIgnoreCase("TRIANGLE")) {
            return new Triangle(x, y);
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle(x,y);
        }
        if (shapeType.equalsIgnoreCase("CHAMI")) {
            return new Chami(x,y);
        }
        return null;
    }
}

