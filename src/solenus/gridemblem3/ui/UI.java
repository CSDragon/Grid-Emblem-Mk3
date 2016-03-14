/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris
 */
public abstract class UI 
{
    protected boolean visible = false;
    protected boolean active = false;
    
    protected int xLoc;
    protected int yLoc;
    protected int height;
    protected int width;
    protected int centerX;
    protected int centerY;
    
    protected Set<Integer> controlStateVisibility;
    
    public UI()
    {
        controlStateVisibility = new HashSet<>();
    }
    
    /**
     * renders the scene.
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
        }
    }
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        //always check this
        if(visible)
        {
        }
    }
    
    
    public void start()
    {
        active = true;
        visible = true;
    }
    
    public void end()
    {
        active = false;
        visible = false;
    }
    
    /**
     * Starts the UI from where it left off. Differs from start in how it's extended.
     */
    public void resume()
    {
        active = true;
        visible = true;
    }
    
    public void setVisible(boolean b)
    {
        visible = b;
    }
    
    /**
     * Sets location relative the the center of the screen.
     * @param x The xLoc we're setting it at.
     * @param y The yLoc we're setting it at.
     */
    public void setLocation(int x, int y)
    {
        xLoc = x;
        yLoc = y;
    }
    
    /**
     * Sets location reletive to the center of the screen.
     * @param p the (x,y) coord we're setting it at.
     */
    public void setLocation(Point p)
    {
        xLoc = p.x;
        yLoc = p.y;
    }

    /**
     * @return the controlStateVisibility
     */
    public Set<Integer> getControlStateVisibility() 
    {
        return controlStateVisibility;
    }
    
    
    
    
}
