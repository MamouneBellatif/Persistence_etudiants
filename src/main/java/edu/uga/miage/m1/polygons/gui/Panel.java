package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel implements Runnable{

    public Panel(){
        super();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        for (SimpleShape shape : JDrawingFrame.getSsL()) {
            shape.draw(g2d);
        }
        g2d.dispose();
    }

    @Override
    public void run() {
            this.repaint();
    }
}
