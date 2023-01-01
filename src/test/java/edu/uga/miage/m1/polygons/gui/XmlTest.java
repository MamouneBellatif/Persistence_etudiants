//package edu.uga.miage.m1.polygons.gui;
//import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
//import edu.uga.miage.m1.polygons.gui.shapes.Circle;
//import edu.uga.miage.m1.polygons.gui.shapes.Square;
//import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class XmlTest {
//
//    private enum shapes {
//        CIRCLE, SQUARE, TRIANGLE
//    }
//
//    public XMLVisitor xmlShapeFactory(shapes shae, int x, int y) {
//        XMLVisitor xmlVisitor = new XMLVisitor();
//        switch (shae) {
//            case CIRCLE:
//                Circle circle = new Circle(x, y);
//                circle.accept(xmlVisitor);
//                break;
//            case SQUARE:
//                Square square = new Square(x, y);
//                square.accept(xmlVisitor);
//                break;
//            case TRIANGLE:
//                Triangle triangle = new Triangle(x, y);
//                triangle.accept(xmlVisitor);
//                break;
//        }
//        return xmlVisitor;
//    }
//
//    @Test
//    void squareXMLtest() {
//        XMLVisitor xmlVisitor = xmlShapeFactory(shapes.SQUARE, 10, 10);
//        assertEquals("<shape><type>square</type><x>10</x><y>10</y></shape>", xmlVisitor.getRepresentation());
//    }
//
//    @Test
//    void circleXMLtest() {
//        XMLVisitor xmlVisitor = xmlShapeFactory(shapes.CIRCLE, 10, 10);
//        assertEquals("<shape><type>circle</type><x>10</x><y>10</y></shape>", xmlVisitor.getRepresentation());
//    }
//
//    @Test
//    void triangleXMLtest() {
//        XMLVisitor xmlVisitor = xmlShapeFactory(shapes.TRIANGLE, 10, 10);
//        assertEquals("<shape><type>triangle</type><x>10</x><y>10</y></shape>", xmlVisitor.getRepresentation());
//    }
//}
