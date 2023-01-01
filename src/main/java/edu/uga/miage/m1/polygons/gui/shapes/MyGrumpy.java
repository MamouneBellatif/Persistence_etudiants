package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.ShapesLib.Shapes.Grumpy;
import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;


public class MyGrumpy extends Grumpy implements SimpleShape, Visitable {

    public MyGrumpy(int x, int y) {
        super(x, y);
    }


    /**
     * @param v
     */
    @Override
    public void accept(JSonVisitor v) {
        v.visit(this);
    }

    /**
     * @param v
     */
    @Override
    public void accept(XMLVisitor v) {
        v.visit(this);
    }

    /**
     * @param v
     */
    @Override
    public void accept(CloneVisitor v) {
        v.visit(this);
    }


}

