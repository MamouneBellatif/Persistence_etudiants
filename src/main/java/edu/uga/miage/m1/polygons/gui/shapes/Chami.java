package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.App;
import edu.uga.miage.m1.polygons.gui.persistence.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Chami implements SimpleShape {

    private int x;
    private int y;
    private final BufferedImage image;

    private static final String PATH = "../images/chamis.png";
    public Chami(int x, int y) {

        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(PATH)));
        } catch (IOException ex) {
            throw new App.MyRuntimeException(ex.getMessage());
        }

    }


    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x-25, y-25, null);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }


    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void accept(JSonVisitor v) {
        v.visit(this);
    }

    public void accept(XMLVisitor v) {
        v.visit(this);
    }

    public void accept(CloneVisitor v) {
        v.visit(this);
    }


}

