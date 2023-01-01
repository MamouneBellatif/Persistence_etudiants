package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.*;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    public static final String Y_JSON = ", \"y\": ";
    private String representation = null;


    @Override
    public void visit(Circle circle) {
        this.representation = "  { \"type\": \"circle\", \"x\": " + circle.getX() + Y_JSON + circle.getY() + " }";
    }

    @Override
    public void visit(Square square) {
        this.representation = " { \"type\": \"square\", \"x\": " + square.getX() + Y_JSON + square.getY() + " }";
    }

    @Override
    public void visit(Triangle triangle) {
        this.representation = " {\"type\": \"triangle\", \"x\": " + triangle.getX() + Y_JSON + triangle.getY() + " }";
    }
    @Override
    public void visit(Chami chami) {
        this.representation = " {\"type\": \"chami\", \"x\": " + chami.getX() + Y_JSON + chami.getY() + " }";
    }
    @Override
    public void visit(MyGrumpy grumpy) {
        this.representation = " {\"type\": \"grumpy\", \"x\": " + grumpy.getX() + Y_JSON + grumpy.getY() + " }";
    }

    /**
     * @return the representation in JSon example for a Circle
     *
     *         <pre>
     * {@code
     *  {
     *     "shape": {
     *     	  "type": "circle",
     *        "x": -25,
     *        "y": -25
     *     }
     *  }
     * }
     *         </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}
