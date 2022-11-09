package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    private String representation = null;

    public JSonVisitor() {
    }

    @Override
    public void visit(Circle circle) {
        this.representation = "  { \"type\": \"circle\", \"x\": " + circle.getX() + ", \"y\": " + circle.getY() + " }";
    }

    @Override
    public void visit(Square square) {
        this.representation = " { \"type\": \"square\", \"x\": " + square.getX() + ", \"y\": " + square.getY() + " }";
    }

    @Override
    public void visit(Triangle triangle) {
        this.representation = " {\"type\": \"triangle\", \"x\": " + triangle.getX() + ", \"y\": " + triangle.getY() + " }";
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
