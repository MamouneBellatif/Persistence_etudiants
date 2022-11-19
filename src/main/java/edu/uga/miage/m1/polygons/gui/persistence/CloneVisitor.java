package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.factory.SimpleShapeFactory;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

//Nous servira pour l'undo
public class CloneVisitor implements Visitor {

    private SimpleShape clone;

    public CloneVisitor() {
        clone=null;
    }

    @Override
    public void visit(Circle circle) {
        this.clone = SimpleShapeFactory.createSimpleShape("CIRCLE", circle.getX(), circle.getY());
    }

    @Override
    public void visit(Square square) {
        this.clone = SimpleShapeFactory.createSimpleShape("SQUARE", square.getX(), square.getY());
    }

    @Override
    public void visit(Triangle triangle) {
        this.clone = SimpleShapeFactory.createSimpleShape("TRIANGLE", triangle.getX(), triangle.getY());
    }


    /**
     * @return the clone
     */
    public SimpleShape getClone() {
        return clone;
    }



}
