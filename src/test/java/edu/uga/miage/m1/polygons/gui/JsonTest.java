package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private enum shapes {
        CIRCLE, SQUARE, TRIANGLE
    }
    public JSonVisitor jsonShapeFactory(shapes shape , int x, int y) {
        JSonVisitor jsonVisitor = new JSonVisitor();
        switch (shape) {
            case CIRCLE:
                Circle circle = new Circle(x, y);
                circle.accept(jsonVisitor);
                break;
            case SQUARE:
                Square square = new Square(x, y);
                square.accept(jsonVisitor);
                break;
            case TRIANGLE:
                Triangle triangle = new Triangle(x, y);
                triangle.accept(jsonVisitor);
                break;
        }
        return jsonVisitor;
    }


    @Test
    void squareJSONtest() {
        JSonVisitor jsonVisitor = jsonShapeFactory(shapes.SQUARE, 10, 10);
        assertEquals(" { \"type\": \"square\", \"x\": 10, \"y\": 10 }", jsonVisitor.getRepresentation());
    }

    @Test
    void circleJSONtest() {
        JSonVisitor jsonVisitor = jsonShapeFactory(shapes.CIRCLE, 10, 10);
        assertEquals("  { \"type\": \"circle\", \"x\": 10, \"y\": 10 }", jsonVisitor.getRepresentation());
    }

    @Test
    void triangleJSONtest() {
        JSonVisitor jsonVisitor = jsonShapeFactory(shapes.TRIANGLE, 10, 10);
        assertEquals(" {\"type\": \"triangle\", \"x\": 10, \"y\": 10 }", jsonVisitor.getRepresentation());
    }


}