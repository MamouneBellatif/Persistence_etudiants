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

    private boolean isUndoing;
    public MemoryShapes() {
        memoryStack = new ArrayList<>();
        currentIndex = -1;
        isUndoing = false;
    }

    public void push(ArrayList<SimpleShape> shapes) {
        ArrayList<SimpleShape> finalSavedList = new ArrayList<>();
        shapes.forEach(shape -> {
            CloneVisitor cloneVisitor = new CloneVisitor();
            shape.accept(cloneVisitor);
            finalSavedList.add(cloneVisitor.getClone());
        });

        memoryStack.add(finalSavedList);
        currentIndex++;
        System.out.printf("currentIndex" + currentIndex);
    }

    public void resetStack(){
        memoryStack = new ArrayList<>();
        currentIndex = -1;
    }
    public void clearStackFromCurrentIndex() {
        System.out.println("Clearing stack from index " + currentIndex);
        ArrayList<SimpleShape> bufferStack = memoryStack.get(memoryStack.size()-1);
        if (currentIndex >= 0) {
            memoryStack.subList(currentIndex, memoryStack.size()).clear();
            memoryStack.trimToSize();
            memoryStack.add(bufferStack);
//            currentIndex=memoryStack.size()-1;
            currentIndex=memoryStack.size()-1;
        }
    }

    public ArrayList<SimpleShape> undo() {
        System.out.println("index: " + currentIndex);
        if (currentIndex <= 0 || currentIndex >= memoryStack.size()) {
            return null;
        }
        currentIndex--;
        return memoryStack.get(currentIndex);
    }

    public ArrayList<SimpleShape> redo() {
        if(currentIndex==memoryStack.size()-1){
            return null;
        }
        currentIndex++;
        if (currentIndex < 0 || currentIndex >= memoryStack.size()) {
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

