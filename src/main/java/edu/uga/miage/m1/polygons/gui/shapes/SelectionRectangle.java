package edu.uga.miage.m1.polygons.gui.shapes;
import edu.uga.miage.m1.polygons.gui.persistence.CloneVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.abs;

public class SelectionRectangle implements SimpleShape {

        int m_x;

        private int new_x;
        int m_y;

        private int new_y;

        private int width;
        private int length;
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




    //On inverse les coordonnées si le rectangle est dessiné dans le sens inverse parceque les coordonnées ne oeuvent être négative
    public void drawSelection(Graphics2D g2, int width, int length) {
            new_x = m_x ;
            new_y = m_y ;
            if(width<0){
                new_x = m_x + width;
                width = -width;
            }
            if (length<0){
                new_y = m_y + length;
                length = -length;
            }
        Color color = new Color(206,234,255,127);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(new_x, new_y, color, new_x , new_y, color);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(new_x, new_y, width, length));
        g2.setColor(new Color(206,234,255,127));
        BasicStroke wideStroke = new BasicStroke(2.0f);
        g2.setStroke(wideStroke);
        g2.draw(new Rectangle2D.Double(new_x, new_y, width, length));

        this.width= abs(width);
        this.length = abs(length);
    }

        public int getWidth() {
            return width;
        }

        public int getLength() {
            return length;
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

        @Override
        public void accept(CloneVisitor v) {
            //juste pour l'interface
        }



    public int getNewX(){
            return new_x;
        }

        public int getNewY(){
            return new_y;
        }
    }
