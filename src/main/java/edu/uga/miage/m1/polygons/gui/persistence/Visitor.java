package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.*;

/**
 * You must define a method for each type of Visitable.
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public interface Visitor {
	 void visit(Circle circle);
	 void visit(Square square);
	 void visit(Triangle triangle);
	 void visit(Chami chami);
	void visit(MyGrumpy grumpy);
}