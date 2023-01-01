/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.persistence.*;

import javax.swing.*;

/**
 * This class implements the square <tt>SimpleShape</tt> extension.
 * It simply provides a <tt>draw()</tt> that paints a square.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class Square extends JLabel implements SimpleShape, Visitable {

     int mX;

    int mY;


    public Square(int x, int y) {
        mX = x;
        mY = y;
    }

    /**
     * Implements the <tt>SimpleShape.draw()</tt> method for painting
     * the shape.
     * @param g2 The graphics object used for painting.
     */
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(mX, mY, Color.BLUE, mX + (float) 50, mY, Color.WHITE);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(mX - (double) 25, mY - (double) 25, 50, 50));
        g2.setColor(Color.black);
        g2.draw(new Rectangle2D.Double(mX - (double) 25, mY - (double) 25, 50, 50));
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    @Override
    public void accept(JSonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(XMLVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public void accept(CloneVisitor visitor) {
        visitor.visit(this);
    }



    @Override
    public int getX() {
        return mX;
    }

    @Override
    public int getY() {
        return mY;
    }

    @Override
    public void setX(int x) {
        mX = x;
    }

    @Override
    public void setY(int y) {
        mY = y;
    }

}
