package edu.uga.miage.m1.polygons.gui;
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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;


/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 * 
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public class JDrawingFrame extends JFrame
    implements MouseListener, MouseMotionListener
{
    ArrayList<SimpleShape> ssL = new ArrayList<>();
    int indexToMove;
    SimpleShape toMove;
    private enum Shapes {SQUARE, TRIANGLE, CIRCLE,EXPORT,IMPORT,FILECHOOSER};
    private static final long serialVersionUID = 1L;
    private JToolBar m_toolbar;
    private Shapes m_selected;
    private JPanel m_panel;
    private JLabel m_label;
    private ActionListener m_reusableActionListener = new ShapeActionListener();

    public void importJSON(){

    }
    public void  export(){
        System.out.println("export");
        ArrayList<JSonVisitor> arrayList = new ArrayList<>();
        ArrayList<XMLVisitor> listeXML = new ArrayList<>();
        ssL.forEach(simpleShape -> {
            JSonVisitor jSonVisitor = new JSonVisitor();
            simpleShape.accept(jSonVisitor);
            arrayList.add(jSonVisitor);
            XMLVisitor xmlVisitor = new XMLVisitor();
            simpleShape.accept(xmlVisitor);
            listeXML.add(xmlVisitor);
        });
//        for (int i=0; i<ssL.size();i++) {
//            JSonVisitor v = new JSonVisitor();
//            XMLVisitor xv = new XMLVisitor();
//            ssL.get(i).accept(v);
//            ssL.get(i).accept(xv);
//            arrayList.add(v);
//            listeXML.add(xv);
//        }
        App.shapeJsonBuilder(arrayList);
        App.xmlBuilder(listeXML);
    }
    /**
     * Tracks buttons to manage the background.
     */
    private Map<Shapes, JButton> m_buttons = new HashMap<>();

    /**
     * Default constructor that populates the main window.
     * @param frameName 
    **/
    public JDrawingFrame(String frameName)
    {
        super(frameName);
        // Instantiates components
        m_toolbar = new JToolBar("Toolbar");
        m_panel = new JPanel();
        m_panel.setBackground(Color.WHITE);
        m_panel.setMinimumSize(new Dimension(400, 400));
        m_panel.setLayout(null);
        m_panel.addMouseListener(this);
        m_panel.addMouseMotionListener(this);
        m_label = new JLabel(" ", JLabel.LEFT);
        
        // Fills the panel
        setLayout(new BorderLayout());
        add(m_toolbar, BorderLayout.NORTH);
        add(m_panel, BorderLayout.CENTER);
        add(m_label, BorderLayout.SOUTH);
        
        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(getClass().getResource("images/square.png")));
        addShape(Shapes.TRIANGLE, new ImageIcon(getClass().getResource("images/triangle.png")));
        addShape(Shapes.CIRCLE, new ImageIcon(getClass().getResource("images/circle.png")));

        addExportButton();
        addImportButton();
        setPreferredSize(new Dimension(400, 400));


    }


    public void addImportedShapes() throws FileNotFoundException {
        this.eraseCanvas();
        JsonArray jsonShapesArray = App.importJSON().getJsonArray("shapes");
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        System.out.println(m_panel);
        for (JsonValue jo : jsonShapesArray) {
            JsonObject shape = jo.asJsonObject();

            System.out.println("drawing: ");
            String typeStr = shape.getString("type");
            System.out.println(typeStr);

            int xInt = shape.getInt("x");
            System.out.println(xInt);

            int yInt = shape.getInt("y");
            System.out.println(yInt);

            switch (typeStr.toUpperCase())
            {
                case "CIRCLE":
                    SimpleShape s = new Circle(xInt, yInt);
                    s.draw(g2);
                    ssL.add(s);
                    break;

                case "SQUARE":
                    SimpleShape s3 = new Square(xInt, yInt);
                    s3.draw(g2);
                    ssL.add(s3);
                    break;

                case "TRIANGLE":
                    SimpleShape s2 =new Triangle(xInt, yInt);
                    s2.draw(g2);
                    ssL.add(s2);
                    break;
            }
        }
    }

    public void addImportButton(){
        JButton btn = new JButton();
        btn.setText("importer");
        m_buttons.put(Shapes.IMPORT,btn);
        btn.setActionCommand("IMPORT");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addImportedShapes();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        m_toolbar.add(btn);
        m_toolbar.validate();
        repaint();
    }
    public void addExportButton(){
        JButton btn = new JButton();
        btn.setText("export");
        m_buttons.put(Shapes.EXPORT,btn);
        btn.setActionCommand("EXPORT");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });

        m_toolbar.add(btn);
        m_toolbar.validate();
        repaint();
    }
	/**
     * Injects an available <tt>SimpleShape</tt> into the drawing frame.
     * @param icon The icon associated with the injected <tt>SimpleShape</tt>.
    **/
    private void addShape(Shapes shape, ImageIcon icon)
    {
        JButton button = new JButton(icon);
		button.setBorderPainted(false);
        m_buttons.put(shape, button);
        button.setActionCommand(shape.toString());
        button.addActionListener(m_reusableActionListener);

        if (m_selected == null)
        {
            button.doClick();

        }

        m_toolbar.add(button);
        m_toolbar.validate();
        repaint();
    }


    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
    **/
    public void mouseClicked(MouseEvent evt)
    {
        if (m_panel.contains(evt.getX(), evt.getY()))
        {
        	Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        	switch(m_selected)
        	{
		    	case CIRCLE:
                    SimpleShape s=new Circle(evt.getX(), evt.getY());
                                    s.draw(g2);
                                    ssL.add(s);
                break;
	    		case TRIANGLE: 		SimpleShape s2 =new Triangle(evt.getX(), evt.getY());
                                        s2.draw(g2);
                                        ssL.add(s2);
									break;
        		case SQUARE: 		SimpleShape s3 = new Square(evt.getX(), evt.getY());
                                    s3.draw(g2);
                                    ssL.add(s3);
                break;
        		default: 			System.out.println("No shape named " + m_selected);
 
        	}
        }
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
    **/
    public void mouseEntered(MouseEvent evt)
    {
    	
    }

    private void eraseCanvas()
    {
    	Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
    	g2.setColor(Color.WHITE);
    	g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
    	g2.setColor(Color.BLACK);
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
    **/
    public void mouseExited(MouseEvent evt)
    {
    	m_label.setText(" ");
    	m_label.repaint();
    }


    public boolean isInside(int eventX, int eventY, int shapeX, int shapeY) {
        //return is eventX and eventY inside the shape by a margin of 25
        return (eventX >= shapeX - 50 && eventX <= shapeX + 50) && (eventY >= shapeY - 25 && eventY <= shapeY + 25);
    }
    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mousePressed(MouseEvent evt)
    {
//        System.out.println("mouse pressed");
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        ssL.forEach(s -> {
            if (isInside(evt.getX(), evt.getY(), s.getX(), s.getY())) {
                System.out.println("contains");
//                s.erase(g2);
                toMove=s;
            }
        });
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mouseReleased(MouseEvent evt)
    {
        if (m_panel.contains(evt.getX(),evt.getY()) && toMove != null) {
//            toMove.erase((Graphics2D) m_panel.getGraphics());
            this.eraseCanvas();
            toMove.setX(evt.getX());
            toMove.setY(evt.getY());
            toMove.draw((Graphics2D) m_panel.getGraphics());
            ssL.forEach(shape -> {
                    shape.draw((Graphics2D) m_panel.getGraphics());
            });
            toMove=null;
//            toMove.draw((Graphics2D) m_panel.getGraphics());
        }
//        repaint();
    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * @param evt The associated mouse event.
    **/
    public void mouseDragged(MouseEvent evt)
    {
//        System.out.println("mouse dragged");
        ssL.forEach(s -> {
            if (isInside(evt.getX(), evt.getY(), s.getX(), s.getY())) {
                System.out.println("IN");
//                toMove=s;
//                s.erase((Graphics2D) m_panel.getGraphics());
//                s.setX(evt.getX());
//                s.setY(evt.getY());
//                s.draw((Graphics2D) m_panel.getGraphics());
            }
        });
    }

    //move the shape when dragged
//    public void moveShape(MouseEvent evt) {
//        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
//        toMove.erase(g2);
//        toMove.setX(evt.getX());
//        toMove.setY(evt.getY());
//        toMove.draw(g2);
//    }
    /**
     * Implements an empty method for the <tt>MouseMotionListener</tt>
     * interface.
     * @param evt The associated mouse event.
    **/
    public void mouseMoved(MouseEvent evt)
    {
    	modifyLabel(evt);
    }
    
    private void modifyLabel(MouseEvent evt) {
    	m_label.setText("(" + evt.getX() + "," + evt.getY() + ")");    	
    }

    /**
     * Simple action listener for shape tool bar buttons that sets
     * the drawing frame's currently selected shape when receiving
     * an action event.
    **/
    private class ShapeActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
        	// Itère sur tous les boutons
        	Iterator<Shapes> keys = m_buttons.keySet().iterator();
        	while (keys.hasNext()) {
        		Shapes shape = keys.next();
				JButton btn = m_buttons.get(shape);

				if (evt.getActionCommand().equals(shape.toString())) {
					btn.setBorderPainted(true);
					m_selected = shape;
		        } else {
					btn.setBorderPainted(false);
				}
				btn.repaint();
			}
        }
    }
}