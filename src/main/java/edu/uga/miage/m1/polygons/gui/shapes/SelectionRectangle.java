package edu.uga.miage.m1.polygons.gui.shapes;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public class SelectionRectangle implements SimpleShape {

        int m_x;

        int m_y;

        public SelectionRectangle(int x, int y) {
            m_x = x;
            m_y = y;
        }

        /**
        * Draw a rectangle for mouse selection
        * @param g2 The graphics object used for painting.
        */
        public void draw(Graphics2D g2) {
            int m_x = this.m_x-25;
            int m_y = this.m_y-25;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(m_x, m_y, Color.RED, m_x + 50, m_y, Color.WHITE);
            g2.setPaint(gradient);
            g2.fill(new Rectangle2D.Double(m_x, m_y, 50, 50));
            BasicStroke wideStroke = new BasicStroke(2.0f);
            g2.setColor(new Color(206,234,255,127));
            g2.setStroke(wideStroke);
            g2.draw(new Rectangle2D.Double(m_x, m_y, 50, 50));
        }

        //redraw the rectangle when the mouse is dragged
    public void draw(Graphics2D g2, int width, int length) {
        int m_x = this.m_x-25;
        int m_y = this.m_y-25;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(m_x, m_y, Color.RED, m_x + width, m_y, Color.WHITE);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(m_x, m_y, width, length));
        BasicStroke wideStroke = new BasicStroke(2.0f);
        g2.setColor(Color.black);
        g2.setStroke(wideStroke);
        g2.draw(new Rectangle2D.Double(m_x, m_y, width, length));
    }


        @Override
    public int getX() {
    return m_x;
    }

    @Override
    public int getY() {
    return m_y;
    }

    @Override
    public void setX(int x) {
        this.m_x = x;
    }

    @Override
    public void setY(int y) {
        this.m_y = y;
    }

    @Override
    public void accept(JSonVisitor v) {
        //placeholder
    }
    @Override
    public void accept(XMLVisitor v) {
            //placeholder
    }
}
