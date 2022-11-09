package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class XMLVisitor implements Visitor {

    private String representation = null;

    public XMLVisitor() {
    }

    @Override
    public void visit(Circle circle) {
    this.representation = "<shape><type>circle</type><x>"+circle.getX()+"</x><y>"+circle.getX()+"</y></shape>";
    }

    @Override
    public void visit(Square square) {
        this.representation = "<shape><type>square</type><x>"+square.getX()+"</x><y>"+square.getX()+"</y></shape>";

    }

    @Override
    public void visit(Triangle triangle) {
        this.representation = "<shape><type>triangle</type><x>"+triangle.getX()+"</x><y>"+triangle.getX()+"</y></shape>";
    }

    /**
     * @return the representation in JSon example for a Triangle:
     *
     *         <pre>
     * {@code
     *  <shape>
     *    <type>triangle</type>
     *    <x>-25</x>
     *    <y>-25</y>
     *  </shape>
     * }
     * </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}
