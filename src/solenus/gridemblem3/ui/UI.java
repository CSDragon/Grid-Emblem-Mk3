/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui;

import java.awt.Graphics2D;

/**
 *
 * @author Chris
 */
public abstract class UI 
{
    protected boolean visible = false;
    protected boolean active = false;
    
    
    
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
    
    public void setVisible(boolean b)
    {
        visible = b;
    }
    
    
    
    
}
