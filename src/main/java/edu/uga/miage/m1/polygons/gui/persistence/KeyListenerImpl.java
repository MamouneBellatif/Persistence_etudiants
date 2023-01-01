package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.shapes.ShapeWrapper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class KeyListenerImpl implements KeyListener {

    JDrawingFrame frame;

    public KeyListenerImpl(JDrawingFrame jDrawingFrame) {
        this.frame = jDrawingFrame;
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

    public void undoOrRedo(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
            makeCopy(true);
        } else if (e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
            makeCopy(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        undoOrRedo(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        undoOrRedo(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

}