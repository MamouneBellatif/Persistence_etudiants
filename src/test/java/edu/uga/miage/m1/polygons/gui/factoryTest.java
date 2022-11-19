package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.factory.SimpleShapeFactory;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    @Test
    void shouldReturnSquare() {
        SimpleShape square = SimpleShapeFactory.createSimpleShape("SQUARE", 10, 10);
        assertEquals(square.getClass(), Square.class);
        assertEquals(square.getX(), 10);
        assertEquals(square.getY(), 10);
    }

    @Test
    void shouldReturnNullWrongCoordinates() {
        SimpleShape shape = SimpleShapeFactory.createSimpleShape("SQUARE", -10, 10);
        assertNull(shape);
    }

    @Test
    void shouldReturnNullWrongShape() {
        SimpleShape shape = SimpleShapeFactory.createSimpleShape("KALABI-YAU", -10, 10);
        assertNull(shape);
    }

    @Test
    void shouldReturnCircle() {
        SimpleShape shape = SimpleShapeFactory.createSimpleShape("CIRCLE", 10, 10);
        assertEquals(shape.getClass(), Circle.class);
        assertEquals(shape.getX(), 10);
        assertEquals(shape.getY(), 10);
    }

    @Test
    void shouldReturnCorrectTriangle() {
        SimpleShape shape = SimpleShapeFactory.createSimpleShape("TRIANGLE", 10, 10);
        assertEquals(shape.getClass(), Triangle.class);
        assertEquals(shape.getX(), 10);
        assertEquals(shape.getY(), 10);
    }
}
