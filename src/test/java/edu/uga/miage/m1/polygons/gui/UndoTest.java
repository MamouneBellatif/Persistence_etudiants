package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UndoTest {

//

    @Test
    void shouldReturnCorrectPrecedentListe() {
        //>create an arrayList of shapes then create a square(10,10) and add it to the list then a circle(20,20) and add it to the list

        //>create a memoryShape object and push the list to it
        ArrayList<SimpleShape> shapes = new ArrayList<>();
        MemoryShapes memoryShapes = new MemoryShapes();
        Square square = new Square(10,10);
        Circle circle = new Circle(20,20);
        shapes.add(square);
        shapes.add(circle);
        memoryShapes.push(shapes);
        shapes.get(0).setX(20);
        memoryShapes.push(shapes);
        shapes.get(0).setX(40);
        memoryShapes.push(shapes);

        ArrayList<SimpleShape> undoList = memoryShapes.undo();
        assertEquals(undoList.get(0).getX(),20);

    }

    @Test
    void UndoListShouldBeDifferentfromActualList(){
        ArrayList<SimpleShape> shapes = new ArrayList<>();
        MemoryShapes memoryShapes = new MemoryShapes();
        Square square = new Square(10,10);
        Circle circle = new Circle(20,20);
        shapes.add(square);
        shapes.add(circle);
//        memoryShapes.push(shapes);
        square.setX(20);
        memoryShapes.push(shapes);

        ArrayList<SimpleShape> undoList = memoryShapes.undo();
        assertNotEquals(undoList,shapes);
    }


}