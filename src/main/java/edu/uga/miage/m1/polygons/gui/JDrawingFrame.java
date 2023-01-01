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


import edu.uga.miage.m1.polygons.gui.factory.SimpleShapeFactory;
import edu.uga.miage.m1.polygons.gui.persistence.*;
import edu.uga.miage.m1.polygons.gui.persistence.UndoListener;
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

    //list of ShapeLabel
    private static List<ShapeWrapper> jShapeList = new ArrayList<>();

    private static final MemoryShapes memoryShapes = new MemoryShapes();
    private transient SelectionRectangle selectionShape;
    private transient ShapeWrapper shapeToMove = null;
    /**
     * selected shapes to move
     */
    transient ArrayList<ShapeWrapper> selectedShapes;

    public enum Shapes { GRUMPY, SQUARE, TRIANGLE, CIRCLE, CHAMI, EXPORT, IMPORT, UNDO, REDO }
    @Serial
    private static final long serialVersionUID = 1L;
    private final JToolBar jToolBar;
    private Shapes selectedShape;
    private static JPanel m_panel=new JPanel();
    private JLabel jLabel;
    private transient final ActionListener mReusableActionListener = new ShapeActionListener();

    private transient UndoListener undoListener;
    transient KeyListener kl = new KeyListenerImpl(this);

    /**
     * Default constructor that populates the main window.
     * @param frameName
     **/
    public JDrawingFrame(String frameName)
    {

        super(frameName);
        // Instantiates components
        jToolBar = new JToolBar("Toolbar");

        m_panel.setBackground(Color.WHITE);
        m_panel.setMinimumSize(new Dimension(600, 400));
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

        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/square.png"))));
        addShape(Shapes.TRIANGLE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/triangle.png"))));
        addShape(Shapes.CIRCLE, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/circle.png"))));
        addShape(Shapes.CHAMI, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/chamis.png"))));
        addShape(Shapes.GRUMPY, new ImageIcon(Objects.requireNonNull(getClass().getResource("images/grumpy.jpg"))));
        addExportButton();
        addImportButton();
         undoListener = new UndoListener(this);
        addUndoButton();
        addRedoButton();
        setPreferredSize(new Dimension(600, 400));

        selectedShapes = new ArrayList<>();


    }

    //add undo button
    private void addUndoButton() {
        JButton btn = new JButton();
        btn.setText("Annuler");
        m_buttons.put(Shapes.IMPORT,btn);
        btn.setActionCommand("UNDO");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoListener.makeCopy(true);
            }
        });
        jToolBar.add(btn);
        jToolBar.validate();
        repaint();
    }

    private void addRedoButton() {
        JButton btn = new JButton();
        btn.setText("Refaire");
        m_buttons.put(Shapes.IMPORT,btn);
        btn.setActionCommand("REDO");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoListener.makeCopy(false);
            }
        });
        jToolBar.add(btn);
        jToolBar.validate();
        repaint();
    }
    public static MemoryShapes getMemoryShapes() {
        return memoryShapes;
    }

    public void setList(List<ShapeWrapper> list){
        for (int i = 0; i < jShapeList.size(); i++) {
            jShapeList.get(i).erase(m_panel);
        }
        repaint();
        jShapeList.clear();
        jShapeList = list;
        jShapeList.forEach(s -> s.draw(m_panel));
        m_panel.repaint();
    }


    public void redrawAll() {
        repaint();
    }

    public void  export() throws App.MyRuntimeException {
        ArrayList<JSonVisitor> arrayList = new ArrayList<>();
        ArrayList<XMLVisitor> listeXML = new ArrayList<>();
        jShapeList.forEach(s -> {
            SimpleShape simpleShape = s.getShape();
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
      parse le DOM et crée les formes à partir du fichier XML à App.getXmlDoc()
     */
    public void addImportedXMLShapes(){
        eraseCanva();
        NodeList nodeList = App.getXmlDoc().getElementsByTagName("shape");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String type = element.getElementsByTagName("type").item(0).getTextContent();
                int x = Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent());
                int y = Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent());
                ShapeWrapper shapeWrapper = SimpleShapeFactory.createShapeString(type, x, y);
                shapeWrapper.draw(m_panel);
                jShapeList.add(shapeWrapper);
            }
        }
        repaint();
        save();
    }

    public void eraseCanva(){
        jShapeList.forEach(s -> s.erase(m_panel));
        jShapeList.clear();
        repaint();
    }

    public void addImportedJSONShapes() throws FileNotFoundException {
        eraseCanva();
        JsonArray jsonShapesArray = App.getJsonObject().getJsonArray("shapes");
        for (JsonValue jo : jsonShapesArray) {
            JsonObject shape = jo.asJsonObject();
            String typeStr = shape.getString("type");
            int xInt = shape.getInt("x");
            int yInt = shape.getInt("y");
            ShapeWrapper shapeWrapper = SimpleShapeFactory.createShapeString(typeStr, xInt, yInt);
            shapeWrapper.draw(m_panel);
            jShapeList.add(shapeWrapper);
        }
        repaint();
        save();
    }


    public void addImportButton(){
        JButton btn = new JButton();
        btn.setText("importer");
        m_buttons.put(Shapes.IMPORT,btn);
        btn.setActionCommand("IMPORT");
        btn.addActionListener(e -> {
            try {
                App.FileFormat importedFormat = App.importDispatcher();
                if (importedFormat == null) {
                    return;
                }
                if (importedFormat.equals(App.FileFormat.JSON)) {
                    addImportedJSONShapes();
                } else if (importedFormat.equals(App.FileFormat.XML)) {
                    addImportedXMLShapes();
                }

            } catch (FileNotFoundException ex) {
                throw new App.MyRuntimeException("Aucun fichier importé");
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
        btn.addActionListener(e -> {
            try {
                export();
            } catch (App.MyRuntimeException ex) {
                throw new App.MyRuntimeException(ex.toString());
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
        save();
    }



    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
    **/
    public void mouseClicked(MouseEvent evt) {
        if (m_panel.contains(evt.getX(), evt.getY())) {

            ShapeWrapper shapeWrapper = SimpleShapeFactory.createShapeEnum(selectedShape, evt.getX()-5, evt.getY()-5);
            shapeWrapper.draw(m_panel);
            jShapeList.add(shapeWrapper);
            repaint();
            if (memoryShapes.isUndoing()) {
                memoryShapes.clearStackFromCurrentIndex();
                memoryShapes.setUndoing(false);
            }
            save();
        }
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
    **/
    public void mouseEntered(MouseEvent evt)
    {
    	// Do nothing.
    }


    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
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
    public boolean isInsideLabel(ShapeWrapper sw, int x, int y) {
        return (sw.getX() + 50 > x && sw.getX() < x) && (sw.getY() + 50 > y && sw.getY() < y);
    }
    Point initialPoint = new Point();
    Point initialJPoint = new Point();
    boolean isTranslating = false;
    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mousePressed(MouseEvent evt)
    {
        initialPoint =evt.getPoint();
        initialJPoint = evt.getPoint();

        oldX = evt.getX();
        oldY = evt.getY();

        if (!selectedShapes.isEmpty()){
            isTranslating= true;
            return;
        }

        for (ShapeWrapper shape : jShapeList) {
            if (isInsideLabel(shape, evt.getX(), evt.getY())) {
                shapeToMove = shape;
                repaint();
                break;
            }
        }

        if (shapeToMove == null) {
            this.selectionShape = new SelectionRectangle(evt.getX(), evt.getY());
            update(getGraphics());
        }

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
    public void mouseDragged(MouseEvent evt) {

        if (shapeToMove != null) {
            int dx = evt.getX() - initialJPoint.x;
            int dy = evt.getY() - initialJPoint.y;
            shapeToMove.setLocation(shapeToMove.getX() + dx, shapeToMove.getY() + dy);
            initialJPoint = evt.getPoint();
            repaint();
            return;
        }
        if (isTranslating) {
            int dx = evt.getX() - initialJPoint.x;
            int dy = evt.getY() - initialJPoint.y;
            for (ShapeWrapper shape : selectedShapes) {
                shape.setLocation(shape.getX() + dx, shape.getY() + dy);
            }
            initialJPoint = evt.getPoint();
            repaint();
            return;
        }
        if (selectionShape != null) {
            int width = evt.getX() - selectionShape.getX();
            int height = evt.getY() - selectionShape.getY();
            selectionShape.drawSelection((Graphics2D) m_panel.getGraphics(), width, height);
//            repaint();
        }
    }


    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * @param evt The associated mouse event.
    **/
    public void mouseReleased(MouseEvent evt)
    {

        if (m_panel.contains(evt.getX(),evt.getY()) && (shapeToMove != null)) {
            int dx = evt.getX() - initialJPoint.x;
            int dy = evt.getY() - initialJPoint.y;
            shapeToMove.setLocation(shapeToMove.getX() + dx, shapeToMove.getY() + dy);
            shapeToMove.draw(m_panel);
            initialJPoint = evt.getPoint();
            shapeToMove = null;
            repaint();
            if (memoryShapes.isUndoing()) {
                memoryShapes.clearStackFromCurrentIndex();
                memoryShapes.setUndoing(false);
            }
            save();
            return;
        }

        if(isTranslating){
            isTranslating = false;
            selectedShapes.clear();
            selectionShape = null;
            save();
        }
        repaint();

        if (this.selectionShape != null && shapeToMove == null) {
            int width = evt.getX() - selectionShape.getX();
            int height = evt.getY() - selectionShape.getY();
            selectionShape.drawSelection((Graphics2D) m_panel.getGraphics(), width, height);
            repaint();
            this.addSelectedShapes();
        }

        if (memoryShapes.isUndoing()) {
            memoryShapes.clearStackFromCurrentIndex();
            memoryShapes.setUndoing(false);
        }


    }

    //retourne vrai si la shape est dans la selection
    public boolean inSelection(ShapeWrapper shape, SelectionRectangle selection) {
        return  selection.getNewX() < shape.getX() && shape.getX() < selection.getNewX() + selection.getWidth() &&
                selection.getNewY() < shape.getY() && shape.getY() < selection.getNewY() + selection.getLength();
    }
    public void addSelectedShapes() {
        jShapeList.forEach(s -> {
            if (this.inSelection(s, this.selectionShape)) {
                selectedShapes.add(s);
            }
        });
    }

    public static void save() {
        memoryShapes.push(jShapeList);
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