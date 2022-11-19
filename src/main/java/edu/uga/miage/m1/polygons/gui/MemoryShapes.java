package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;

import java.util.ArrayList;

public class MemoryShapes {
    private ArrayList<ArrayList<SimpleShape>> memoryStack;
    private int currentIndex;

    public MemoryShapes() {
        memoryStack = new ArrayList<>();
        currentIndex = -1;
    }

    public void push(ArrayList<SimpleShape> shapes) {
        ArrayList<SimpleShape> savedList = new ArrayList<>();
        ArrayList<SimpleShape> finalSavedList = savedList;;
        shapes.forEach(shape -> {
            CloneVisitor cloneVisitor = new CloneVisitor();
            shape.accept(new CloneVisitor());
            finalSavedList.add(cloneVisitor.getClone());
        });

//        savedList.addAll(shapes);
        memoryStack.add(finalSavedList);
        currentIndex++;
        System.out.printf("currentIndex" + currentIndex);
    }

    public void clearStackFromCurrentIndex() {
        memoryStack.subList(currentIndex + 1, memoryStack.size()-1).clear();

    }

    public ArrayList<SimpleShape> undo() {
        System.out.println("index: " + currentIndex);
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
            return null;
        }
        currentIndex--;
        return memoryStack.get(currentIndex);
    }

    public ArrayList<SimpleShape> redo() {
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
             return null;
        }
        currentIndex++;
            return memoryStack.get(currentIndex);
    }

}

