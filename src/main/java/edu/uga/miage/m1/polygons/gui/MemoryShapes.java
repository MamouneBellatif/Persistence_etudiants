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
        ArrayList<SimpleShape> savedList = new ArrayList<>(shapes);
//        savedList.addAll(shapes);
        memoryStack.add(savedList);
        currentIndex++;
        System.out.printf("currentIndex" + currentIndex);
    }

    public void clearStackFromCurrentIndex() {
        memoryStack.subList(currentIndex + 1, memoryStack.size()-1).clear();

    }

    public ArrayList<SimpleShape> undo() {
        System.out.println("index: " + currentIndex);
        //if currentIndex is outside of bounds, return null
        if (currentIndex <= 0 || currentIndex >= memoryStack.size()) {
            return null;
        }
//        memoryStack.get(currentIndex - 1).size();

        currentIndex--;
//        System.out.println("undo size: "+memoryStack.get(currentIndex - 1));
        return memoryStack.get(currentIndex);
    }

    public ArrayList<SimpleShape> redo() {
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
             return null;
        }
            return memoryStack.get(currentIndex + 1);
    }

}

