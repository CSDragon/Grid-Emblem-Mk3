/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;

/**
 * A JPanel object that can detect mouse movements and clicks. Used mainly to close the game in borderless windowed mode. (Not actually implemented) 
 * @author Chris
 */
public class MotionPanel extends JPanel
{
    private Point initialClick;
    private JFrame parent;
    private boolean active;

    public MotionPanel(JFrame _parent, boolean a)
    {
        //boot it up
        active = a;
        setLayout(null);
        setSize(GridEmblemMk3.WIDTH, 25);
        setLocation(0, 0);
        setPreferredSize(new Dimension(GridEmblemMk3.WIDTH, 25));
        
        parent = _parent;
        setOpaque(false);
        
        addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e) 
            {
                initialClick = e.getPoint();
            }
            
            public void mouseClicked(MouseEvent e)
            {
                if(active && e.getY() < 30)
                    parent.setLocationRelativeTo(null);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseDragged(MouseEvent e) 
            {
                if(active && e.getY() < 30)
                {

                    // get location of Window
                    int thisX = parent.getLocation().x;
                    int thisY = parent.getLocation().y;

                    // Determine how much the mouse moved since the initial click
                    int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
                    int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

                    // Move window to this position
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    parent.setLocation(X, Y);
                }
            }
        });
    }
    
    public void setActive(boolean b)
    {
        active = b;
    }
}