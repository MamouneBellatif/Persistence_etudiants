package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.shapes.ShapeWrapper;

import java.util.ArrayList;
import java.util.List;

public class UndoListener  {

    protected JDrawingFrame frame;

    public UndoListener(JDrawingFrame jDrawingFrame){
        frame = jDrawingFrame;
    }

    public void makeCopy(boolean isUndo){
        List<ShapeWrapper> newList;
        if (isUndo) {
            newList =  JDrawingFrame.getMemoryShapes().undo();
        } else {
            newList = JDrawingFrame.getMemoryShapes().redo();
        }
        if (newList != null) {
            List<ShapeWrapper> list = new ArrayList<>();
            for (ShapeWrapper shapeWrapper : newList) {
                list.add(shapeWrapper.duplicate());
            }
            frame.eraseCanva();
            frame.setList(list);
            frame.repaint();
        }
    }

}
