package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

import java.util.ArrayList;

public class MemoryShapes {
    private ArrayList<ArrayList<SimpleShape>> memoryStack;
    private int currentIndex;

    public MemoryShapes() {
        memoryStack = new ArrayList<>();
        currentIndex = -1;
    }

    public void push(ArrayList<SimpleShape> shapes) {
        memoryStack.add(shapes);
        currentIndex++;
    }

    public ArrayList<SimpleShape> cancel() {
        //if currentIndex is outside of bounds, return null
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
            return null;
        }
        return memoryStack.get(currentIndex - 1);
    }

    public ArrayList<SimpleShape> redo() {
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
             return null;
        }
            return memoryStack.get(currentIndex + 1);
    }

}

