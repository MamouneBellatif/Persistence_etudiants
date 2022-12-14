package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;

import java.awt.Graphics2D;

import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.ShapeJLabel;

import javax.swing.*;

/**
 * This interface defines the <tt>SimpleShape</tt> extension. This extension
 * is used to draw shapes. 
 * 
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public interface SimpleShape
{

    /**
     * Method to draw the shape of the extension.
     * @param g2 The graphics object used for painting.
     **/
    void draw(Graphics2D g2);

    int getX();
    
    int getY();

    void setX(int x);

    void setY(int y);

    void accept(JSonVisitor v);
    void accept(XMLVisitor v);

    void accept(CloneVisitor v);


}