package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.ShapeJLabel;

import javax.swing.*;
import java.awt.*;

public class JLabelChami extends JLabel {
    static int i = 0;
    ImageIcon icon;
    public JLabelChami(int x, int y) {
        this.icon = new ImageIcon("src/main/resources/edu/uga/miage/m1/polygons/gui/images/chamis.png");
        Image image = this.icon.getImage();
        Image resizedImg = image.getScaledInstance(50, 50, 4);
        ImageIcon resizedChami = new ImageIcon(resizedImg);
        this.setIcon(resizedChami);
        this.setSize(resizedChami.getIconWidth(), resizedChami.getIconHeight());
        this.setLocation(x, y);
    }
}
