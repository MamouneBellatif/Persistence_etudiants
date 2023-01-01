package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.*;

import java.util.ArrayList;
import java.util.List;

public class MemoryShapes {
    private ArrayList<List<ShapeWrapper>> memoryStack;
    private int currentIndex;

    private boolean isUndoing;
    public MemoryShapes() {
        memoryStack = new ArrayList<>();
        currentIndex = -1;
        isUndoing = false;
    }

    public void push(List<ShapeWrapper> shapes) {
        ArrayList<ShapeWrapper> finalSavedList = new ArrayList<>();
        shapes.forEach(shape -> {
            finalSavedList.add(shape.duplicate());
        });
        memoryStack.add(finalSavedList);
        currentIndex++;

    }


    public void clearStackFromCurrentIndex() {
        System.out.println("clear");
        if (currentIndex >= 0) {
//            List<ShapeWrapper> bufferStack = memoryStack.get(memoryStack.size()-1);
            memoryStack.subList(currentIndex, memoryStack.size()-1).clear();
            memoryStack.trimToSize();
//            memoryStack.add(bufferStack);
            currentIndex=memoryStack.size()-1;
        }
    }

    public List<ShapeWrapper> undo() {
        currentIndex--;
        if (currentIndex <= -1 || currentIndex >= memoryStack.size()) {
            currentIndex++;
            return null;
        }

        setUndoing(true);
        return memoryStack.get(currentIndex);
    }

    public List<ShapeWrapper> redo() {
        if(currentIndex==memoryStack.size()-1){
            return null;
        }
        currentIndex++;
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
            currentIndex--;
             return null;
        }

        return memoryStack.get(currentIndex);
    }

    public boolean isUndoing(){
        return isUndoing;
    }
    public void setUndoing(boolean b){
        this.isUndoing = b;
    }
}

