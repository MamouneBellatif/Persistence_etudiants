package edu.uga.miage.m1.polygons.gui.factory;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.ShapesLib.Shapes.Grumpy;
import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.*;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.shapes.*;

import javax.swing.*;

public class  SimpleShapeFactory {

     public SimpleShapeFactory() { /* */ }
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
        if (shapeType.equalsIgnoreCase("GUMPY")) {
            return new MyGrumpy(x,y);
        }
        return null;
    }
    public static ShapeWrapper createShapeString(String shapeType, int x, int y){
        if (shapeType == null || x < 0 || y < 0)  {
            return null;
        }
        if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new ShapeWrapper(new Square(x,y), new JLabelSquare(x, y), JDrawingFrame.Shapes.SQUARE);
        }
        if (shapeType.equalsIgnoreCase("TRIANGLE")) {
            return new ShapeWrapper(new Triangle(x,y), new JLabelTriangle(x, y), JDrawingFrame.Shapes.TRIANGLE);
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new ShapeWrapper(new Circle(x,y), new JLabelCircle(x, y), JDrawingFrame.Shapes.CIRCLE);
        }
        if (shapeType.equalsIgnoreCase("CHAMI")) {
            return new ShapeWrapper(new Chami(x,y), new JLabelChami(x, y));
        }
        if (shapeType.equalsIgnoreCase("GRUMPY")) {
            return new ShapeWrapper(new MyGrumpy(x,y), new JLabelGrumpyCat(x, y), JDrawingFrame.Shapes.GRUMPY);
        }
        return null;
    }
    public static ShapeWrapper createShapeEnum(JDrawingFrame.Shapes shapeType, int x, int y){
        if (shapeType == null || x < 0 || y < 0)  {
            return null;
        }
        if (shapeType.equals(JDrawingFrame.Shapes.SQUARE)) {
            return new ShapeWrapper(new Square(x,y), new JLabelSquare(x, y), shapeType);
        }
        if (shapeType.equals(JDrawingFrame.Shapes.TRIANGLE)) {
            return new ShapeWrapper(new Triangle(x,y), new JLabelTriangle(x, y), shapeType);
        }
        if (shapeType.equals(JDrawingFrame.Shapes.CIRCLE)) {
            return new ShapeWrapper(new Circle(x,y), new JLabelCircle(x, y), shapeType);
        }
        if (shapeType.equals(JDrawingFrame.Shapes.GRUMPY)) {
            return new ShapeWrapper(new MyGrumpy(x,y), new JLabelGrumpyCat(x, y), shapeType);
        }
        if (shapeType.equals(JDrawingFrame.Shapes.CHAMI)) {
            return new ShapeWrapper(new Chami(x,y), new JLabelChami(x, y));
        }

        return null;
    }

    public static ShapeJLabel createShapeJLabel(String shapeType, int x, int y) {
        if (shapeType == null || shapeType.isEmpty() || x < 0 || y < 0)  {
            return null;
        }
        if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new JLabelSquare(x, y);
        }
        if (shapeType.equalsIgnoreCase("TRIANGLE")) {
            return new JLabelTriangle (x, y);
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new JLabelCircle(x,y);
        }
        if (shapeType.equalsIgnoreCase("GRUMPY")) {
            return new JLabelGrumpyCat(x,y);
        }

        return null;
    }
}


