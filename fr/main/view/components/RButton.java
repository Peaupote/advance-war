package fr.main.view.components;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class RButton extends JButton {
    
    public RButton(String label) {
        super(label);

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,
                                            size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
        this.setBackground(Color.RED);
    }


    protected void paintComponent(Graphics g) {
        if (getModel().isArmed())
            g.setColor(Color.GREEN);
        else
            g.setColor(getBackground());

        g.fillOval(0, 0, getSize().width - 1,
                         getSize().height - 1);

        super.paintComponent(g);
    }


    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width - 1,
                         getSize().height - 1);
    }

    Shape shape;
    public boolean contains(int x, int y) {

        if (shape == null ||
                !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }
 }
