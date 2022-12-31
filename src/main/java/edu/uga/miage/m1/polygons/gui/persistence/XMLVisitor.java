package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.*;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class XMLVisitor implements Visitor {

    public static final String X_Y = "</x><y>";
    public static final String Y_SHAPE = "</y></shape>";
    private String representation = null;



    @Override

    public void visit(Circle circle) {
    this.representation = "<shape><type>circle</type><x>"+circle.getX()+ X_Y +circle.getY()+ Y_SHAPE;
    }

    @Override
    public void visit(Square square) {
        this.representation = "<shape><type>square</type><x>"+square.getX()+ X_Y +square.getY()+Y_SHAPE;

    }

    @Override
    public void visit(Triangle triangle) {
        this.representation = "<shape><type>triangle</type><x>"+triangle.getX()+ X_Y +triangle.getY()+Y_SHAPE;
    }

    @Override
    public void visit(Chami chami) {
        this.representation = "<shape><type>chami</type><x>"+chami.getX()+ X_Y +chami.getY()+Y_SHAPE;
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
