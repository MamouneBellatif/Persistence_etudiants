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
import edu.uga.miage.m1.polygons.gui.shapes.*;


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
    private SelectionRectangle selectionShape;
    int indexToMove;
    SimpleShape toMove;
    ArrayList<SimpleShape> selectedShapes = new ArrayList<>();
    private int selectX;
    private int selectY;

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
        ssL.clear();
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
        return (eventX >= shapeX - 25 && eventX <= shapeX + 25) && (eventY >= shapeY - 25 && eventY <= shapeY + 25);
    }

    boolean isTranslating = false;
    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mousePressed(MouseEvent evt)
    {
        oldX = evt.getX();
        oldY = evt.getY();
        System.out.println(selectedShapes.size());
        if (!selectedShapes.isEmpty()){
            isTranslating= true;
            System.out.println("not empty");
        }
        else {
            ssL.forEach(s -> {
                if (isInside(evt.getX(), evt.getY(), s.getX(), s.getY())) {
                    System.out.println("contains");
                    toMove = s;
                }
            });
            if (toMove == null) {
                this.selectionShape = new SelectionRectangle(evt.getX(), evt.getY());
//            this.selectionShape .drawSelection((Graphics2D) m_panel.getGraphics(),evt.getX(), evt.getY());
            }
        }


    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mouseReleased(MouseEvent evt)
    {
        if(isTranslating){
            isTranslating = false;
            selectedShapes.clear();
            eraseCanvas();
            redrawAll();
            selectionShape = null;
        }
        if (m_panel.contains(evt.getX(),evt.getY()) && toMove != null) {
//            toMove.erase((Graphics2D) m_panel.getGraphics());
            toMove.setX(evt.getX());
            toMove.setY(evt.getY());
            toMove.draw((Graphics2D) m_panel.getGraphics());
            this.eraseCanvas();
            this.redrawAll();
            toMove=null;
        }

        //si on a initié la selection, on ajoute les shapes
        if (this.selectionShape != null) {
            this.addSelectedShapes();
        }

//        if (!selectedShapes.isEmpty()){
//            selectedShapes.clear();
//        }



//        isTranslating= false;
    }

//    public moveSelectedShapes(int x, int y) {
//        this.selectedShapes.forEach(s -> {
//            s.setX(x);
//            s.setY(y);
//            s.draw((Graphics2D) m_panel.getGraphics());
//        });
//    }

    //retourne vrai si la shape est dans la selection
    public boolean inSelection(SimpleShape shape, SelectionRectangle selection) {
        return  selection.getNewX() < shape.getX() && shape.getX() < selection.getNewX() + selection.getWidth() &&
                selection.getNewY() < shape.getY() && shape.getY() < selection.getNewY() + selection.getLength();
    }
    public void addSelectedShapes() {
        ssL.forEach(s -> {
            if (this.inSelection(s, this.selectionShape)) {
                selectedShapes.add(s);
                System.out.println("shape in selection");
            }
        });
        System.out.println(selectedShapes.size());
    }
    public void redrawAll() {
        this.ssL.forEach(shape -> shape.draw((Graphics2D) m_panel.getGraphics()));
    }

    int oldX;
    int oldY;
    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * @param evt The associated mouse event.
    **/
    public void mouseDragged(MouseEvent evt)
    {
//        System.out.println("mouse dragged");
        //deplacement de la forme
        if(isTranslating) {
            System.out.println("translating");
            selectedShapes.forEach(s ->{
                int dragFactorX = 1;
                int dragFactorY= 1;
                if(evt.getX()-this.oldX<0){
                    dragFactorX = -1;
                }
                if (evt.getX()-this.oldY < 0){
                    dragFactorY = -1;
                }
                System.out.println("dragging selected shapes");
                s.setX(s.getX()+(dragFactorX));
//                s.setX(s.getX()+(evt.getX()-this.oldX));
                s.setY(s.getY()+(dragFactorY));
//                s.setY(s.getY()+(evt.getY()-this.oldY));
            });
                this.eraseCanvas();
                this.redrawAll();
        }
        else {
            if (toMove != null) {
                toMove.setX(evt.getX());
                toMove.setY(evt.getY());
                this.eraseCanvas();
                redrawAll();
            }

            //selection rectangle
            if (selectionShape != null) {
                eraseCanvas();
                redrawAll();
                int width = evt.getX() - selectionShape.getX();
//                System.out.println("width: " + width);
                int height = evt.getY() - selectionShape.getY();
//                System.out.println("height: " + height);
                selectionShape.drawSelectionTest((Graphics2D) m_panel.getGraphics(), width, height);
            }
        }
//        if(!selectedShapes.isEmpty()) {

    }


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