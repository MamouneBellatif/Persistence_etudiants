package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.ShapeJLabel;
import edu.uga.miage.m1.polygons.gui.factory.SimpleShapeFactory;
import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;

import javax.swing.*;

public class ShapeWrapper  {
    private final SimpleShape shape;
    private final ShapeJLabel jShape;
    private final JDrawingFrame.Shapes type;

    private final boolean isChami;
    JLabelChami jChami;
    public ShapeWrapper(SimpleShape shape, ShapeJLabel jShape, JDrawingFrame.Shapes type) {
        isChami = false;
        this.shape = shape;
        this.jShape = jShape;
        this.type = type;
    }

    public ShapeWrapper(SimpleShape shape, JLabelChami chami) {
        isChami = true;
        this.shape = shape;
        this.jChami = chami;
        this.type = JDrawingFrame.Shapes.CHAMI;
        this.jShape = null;
    }

    public void draw(JPanel panel) {
        if (!isChami){
            panel.add(jShape);
            return;
        }
        panel.add(jChami);
    }

    public void erase(JPanel panel) {
        if (!isChami){
            panel.remove(jShape);
            return;
        }
        panel.remove(jChami);
    }


    public void setLocation(int x, int y){
        shape.setX(x);
        shape.setY(y);
        if (!isChami){
             jShape.setLocation(x, y);
             return;
         }
             jChami.setLocation(x, y);
    }

    public int getX(){
        if (!isChami){
            return jShape.getX();
        }
        return jChami.getX();
    }

    public int getY(){
        if (!isChami){
            return jShape.getY();
        }
        return jChami.getY();
    }

    public ShapeJLabel getJShape(){
        return jShape;
    }

    public JLabelChami getJChami(){
        return jChami;
    }

    public SimpleShape getShape() {
        return shape;
    }

    public ShapeWrapper duplicate() {
        CloneVisitor v = new CloneVisitor();
        shape.accept(v);
        if (isChami){
         JLabelChami jLabelChami= new JLabelChami(getX(),getY());
         return new ShapeWrapper(v.getClone(),jLabelChami);
        }
        ShapeJLabel shapeLabel = SimpleShapeFactory.createShapeJLabel(type.toString(),getX(),getY());
        return new ShapeWrapper(v.getClone(),shapeLabel, type);
    }

    public JDrawingFrame.Shapes getType() {
        return type;
    }



}
