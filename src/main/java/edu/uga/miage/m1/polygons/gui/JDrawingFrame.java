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


import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serial;
import java.util.*;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import edu.uga.miage.m1.polygons.gui.ShapesLib.Shapes.Grumpy;
import edu.uga.miage.m1.polygons.gui.ShapesLib.Shapes.SuperShape;
import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.JLabelCircle;
import edu.uga.miage.m1.polygons.gui.ShapesLib.ShapesJLabel.ShapeJLabel;
import edu.uga.miage.m1.polygons.gui.factory.SimpleShapeFactory;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.*;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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


    private static MemoryShapes memoryShapes = new MemoryShapes();
    private static ArrayList<SimpleShape> ssL = new ArrayList<>();


    private transient SelectionRectangle selectionShape;
    int indexToMove;
    transient SimpleShape toMove;
    /**
     * selected shapes to move
     */
    private transient ArrayList<SimpleShape> selectedShapes = new ArrayList<>();


    private enum Shapes {SUPERSQUARE, GRUMPY, SQUARE, TRIANGLE, CIRCLE, CHAMI, EXPORT, IMPORT, FILECHOOSER}

    @Serial
    private static final long serialVersionUID = 1L;
    private final JToolBar jToolBar;
    private Shapes selectedShape;
//    private static JPanel m_panel;
    private static final Panel m_panel = new Panel();
    private JLabel jLabel;
    private transient final ActionListener mReusableActionListener = new ShapeActionListener();

    //method that listen to ctrl+z


    //ssl getter
    public static List<SimpleShape> getSsL() {
        return ssL;
    }


    private static final transient KeyListener kl = new KeyListener(){
        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("keyTyped");
            if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()){
                System.out.println("ctrl+z");
                ArrayList<SimpleShape> undo = memoryShapes.undo();
                if(undo != null){
                    System.out.println("undo not null");
                    JDrawingFrame.ssL=undo;
                    JDrawingFrame.eraseCanvas();
                    redrawAll();
                    memoryShapes.setUndoing(true);
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //undo
//                System.out.println("keyPressed");
            if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()){
                System.out.println("ctrl+z");
                ArrayList<SimpleShape> undo = memoryShapes.undo();
                if(undo != null){
                    System.out.println("undo not null");
                    JDrawingFrame.ssL=undo;
                    JDrawingFrame.eraseCanvas();
                    redrawAll();
                    memoryShapes.setUndoing(true);
                }
            }
            else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()){
                System.out.println("ctrl+y");
                ArrayList<SimpleShape> redo = memoryShapes.redo();
                if(redo != null){
                    System.out.println("redo not null");
                    JDrawingFrame.ssL=redo;
                    JDrawingFrame.eraseCanvas();
                    redrawAll();
                    memoryShapes.setUndoing(true);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };
    /**
     * Default constructor that populates the main window.
     * @param frameName
     **/
    public JDrawingFrame(String frameName)
    {
        super(frameName);
        // Instantiates components
        jToolBar = new JToolBar("Toolbar");

//        m_panel = new JPanel();
        m_panel.setBackground(Color.WHITE);
        m_panel.setMinimumSize(new Dimension(400, 400));
        m_panel.setLayout(null);
        m_panel.addMouseListener(this);
        this.setFocusable(true);

        this.addKeyListener(kl);
        this.setFocusable(true);
        m_panel.addMouseMotionListener(this);
        jLabel = new JLabel(" ", SwingConstants.LEFT);

        // Fills the panel
        setLayout(new BorderLayout());
        add(jToolBar, BorderLayout.NORTH);
        add(m_panel, BorderLayout.CENTER);
        add(jLabel, BorderLayout.SOUTH);

        JLabelCircle circle = new JLabelCircle(0,0);

        SuperShape grumpy = new Grumpy(0,0);
        ShapeJLabel JGrumpy = grumpy.getShapeJlabel();
        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/square.png"))));
        addShape(Shapes.TRIANGLE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/triangle.png"))));
        addShape(Shapes.CIRCLE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/circle.png"))));
        addShape(Shapes.CHAMI, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/chamis.png"))));
        addShape(Shapes.GRUMPY, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/grumpy.jpg"))));
        addExportButton();
        addImportButton();
        setPreferredSize(new Dimension(400, 400));
        save();
    }

    public static MemoryShapes getMemoryShapes() {
        return memoryShapes;
    }


    public void  export(String fileName) throws App.MyRuntimeException {
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

        App.shapeJsonBuilder(arrayList);
        try {
            App.xmlBuilder(listeXML);
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            throw new App.MyRuntimeException(e.toString());
        }

    }
    /**
     * Tracks buttons to manage the background.
     */
    private final Map<Shapes, JButton> m_buttons = new HashMap<>();

    /**
     * Pars the DOM tree and creates the shapes from the XML file at App.getXmlDoc()
     */
    public void addImportedXMLShapes(){
        //Parse the DOM tree and creates the shapes from the XML file at App.getXmlDoc()
        eraseCanvas();
        ssL.clear();
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        NodeList nodeList = App.getXmlDoc().getElementsByTagName("shapes");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String type = element.getAttribute("type");
                int x = Integer.parseInt(element.getAttribute("x"));
                int y = Integer.parseInt(element.getAttribute("y"));
                SimpleShape simpleShape = SimpleShapeFactory.createSimpleShape(type, x, y);
                simpleShape.draw(g2);
                ssL.add(simpleShape);
                save();
            }
        }
    }

    public void addImportedJSONShapes() throws FileNotFoundException {
        eraseCanvas();
        ssL.clear();
        JsonArray jsonShapesArray = App.getJsonObject().getJsonArray("shapes");
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        for (JsonValue jo : jsonShapesArray) {
            JsonObject shape = jo.asJsonObject();
            String typeStr = shape.getString("type");
            int xInt = shape.getInt("x");
            int yInt = shape.getInt("y");

            SimpleShape simpleShape = SimpleShapeFactory.createSimpleShape(typeStr, xInt, yInt);
            simpleShape.draw(g2);
            ssL.add(simpleShape);
            save();
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
                    App.FileFormat importedFormat = App.importDispatcher();
                    if (importedFormat == App.FileFormat.JSON) {
                        addImportedJSONShapes();
                    } else if (importedFormat == App.FileFormat.XML) {
                        addImportedXMLShapes();
                    }
                } catch (FileNotFoundException ex) {
                    throw new App.MyRuntimeException(ex.toString());
                }
            }
        });

        jToolBar.add(btn);
        jToolBar.validate();
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
                try {
                    export("export.xml");
                } catch (App.MyRuntimeException ex) {
                    throw new App.MyRuntimeException(ex.toString());
                }
            }
        });

        jToolBar.add(btn);
        jToolBar.validate();
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
        button.addActionListener(mReusableActionListener);

        if (selectedShape == null)
        {
            button.doClick();
        }

        jToolBar.add(button);
        jToolBar.validate();
        repaint();
    }


    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
    **/
    public void mouseClicked(MouseEvent evt)
    {
        if (m_panel.contains(evt.getX(), evt.getY())) {
            Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
            SimpleShape simpleShape = SimpleShapeFactory.createSimpleShape(selectedShape.toString(), evt.getX(), evt.getY());
            //utiliser GrmpyJLabel si grumpy
            simpleShape.draw(g2);
            JLabelCircle circle = new JLabelCircle(evt.getX(), evt.getY());
            ssL.add(simpleShape);
            save();
            if (memoryShapes.isUndoing()) {
                memoryShapes.clearStackFromCurrentIndex();
                memoryShapes.setUndoing(false);
            }
            this.setFocusable(true);
            m_panel.addMouseMotionListener(this);
            jLabel = new JLabel(" ", JLabel.LEFT);
        }
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
    **/
    public void mouseEntered(MouseEvent evt)
    {
    	
    }

    public static void eraseCanvas()
    {
       m_panel.update(m_panel.getGraphics());

    }

    //method that redraw all the shapes in the list every millisecond
    @Override
    public void paintComponents(Graphics g) {
        repaint();
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) m_panel.getGraphics();
        for (SimpleShape shape : ssL) {
            shape.draw( g2d);
        }
        g2d.dispose();
    }


    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
    **/
    public void mouseExited(MouseEvent evt)
    {
    	jLabel.setText(" ");
    	jLabel.repaint();
    }


        //return is eventX and eventY inside the shape by a margin of 25
    public boolean isInside(int eventX, int eventY, int shapeX, int shapeY) {
        return (eventX >= shapeX - 25 && eventX <= shapeX + 25) && (eventY >= shapeY - 25 && eventY <= shapeY + 25);
    }
    Point initialPoint = new Point();
    boolean isTranslating = false;
    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mousePressed(MouseEvent evt)
    {
        initialPoint =evt.getPoint();
        oldX = evt.getX();
        oldY = evt.getY();
        save();
        for (SimpleShape s : ssL) {
            if (isInside(evt.getX(), evt.getY(), s.getX(), s.getY())) {
                toMove = s;
                repaint();
                break;
            }
        }
        if (toMove == null) {
            this.selectionShape = new SelectionRectangle(evt.getX(), evt.getY());
//            this.selectionShape .drawSelection((Graphics2D) m_panel.getGraphics(),evt.getX(), evt.getY());
        }

        if (!selectedShapes.isEmpty()){
            isTranslating= true;
//            save();
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
          save();
            if (memoryShapes.isUndoing()) {
                memoryShapes.clearStackFromCurrentIndex();
                memoryShapes.setUndoing(false);
            }
        }

        if (m_panel.contains(evt.getX(),evt.getY()) && toMove != null) {
            toMove.setX(evt.getX());
            toMove.setY(evt.getY());
            eraseCanvas();
            redrawAll();
            toMove=null;
           save();
            if (memoryShapes.isUndoing()) {
                memoryShapes.clearStackFromCurrentIndex();
                memoryShapes.setUndoing(false);
            }
        }

        //si on a initié la selection, on ajoute les shapes
        if (this.selectionShape != null && toMove == null) {
            this.addSelectedShapes();
        }
    }

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
    }
    public static void redrawAll() {
        ssL.forEach(s -> {
            s.draw((Graphics2D) m_panel.getGraphics());
        });

    }

    public static void save() {
        memoryShapes.push(ssL);
        System.out.println("saved");

    }

    int pressedX;
    int pressedY;
    int oldX;
    int oldY;
    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * @param evt The associated mouse event.
    **/
    public void mouseDragged(MouseEvent evt)
    {
            if (toMove != null) {
                int dx = evt.getX() - initialPoint.x;
                int dy = evt.getY() - initialPoint.y;
                toMove.setX(toMove.getX() + dx);
                toMove.setY(toMove.getY() + dy);
                initialPoint = evt.getPoint();
//                m_panel.repaint();
                repaint();
//                toMove.setX(evt.getX());
//                toMove.setY(evt.getY());
//                m_panel.repaint();
//                toMove.draw((Graphics2D) m_panel.getGraphics());
//                eraseCanvas();
//                redrawAll();

            }
        else {
                //deplacement de la forme
                if (isTranslating) {
                    System.out.println("translating");
                    selectedShapes.forEach(s -> {
                        int dragFactorX = 1;
                        int dragFactorY = 1;
                        if (evt.getX() - this.oldX < 0) {
                            dragFactorX = -1;
                        }
                        if (evt.getY() - this.oldY < 0) {
                            dragFactorY = -1;
                        }
                        System.out.println("dragging selected shapes");
                        s.setX(s.getX() + (dragFactorX));
                        s.setY(s.getY() + (dragFactorY));
                    });
//                this.eraseCanvas();
//                this.redrawAll();
                } else {
                    //selection rectangle
                    if (selectionShape != null) {
//                eraseCanvas();
//                redrawAll();
                        int width = evt.getX() - selectionShape.getX();
                        int height = evt.getY() - selectionShape.getY();
                        selectionShape.drawSelectionTest((Graphics2D) m_panel.getGraphics(), width, height);
                    }
                }
            }
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
    	jLabel.setText("(" + evt.getX() + "," + evt.getY() + ")");
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
					selectedShape = shape;
		        } else {
					btn.setBorderPainted(false);
				}
				btn.repaint();
			}
        }
    }
}