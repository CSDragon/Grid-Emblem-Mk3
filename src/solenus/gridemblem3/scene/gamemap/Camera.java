/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.gamemap;

import solenus.gridemblem3.GridEmblemMk3;

/**
 *
 * @author Chris
 */
public class Camera 
{
    private double x;
    private double y;
    
    private double minX;
    private double minY;
    
    
    public Camera(double _x, double _y)
    {
        x = _x;
        y = _y;
        
        resize();
    }
    
    public void moveToRenderable(GridRenderable c, Map m)
    {
        double xtemp;
        double ytemp;
        
        //move towards the cursor
        xtemp = c.getXCur() + (x - c.getXCur())/1.2;
        if(Math.abs(xtemp - c.getXCur()) < .01)
            xtemp = c.getXCur();
        ytemp = c.getYCur() + (y - c.getYCur())/1.2;
        if(Math.abs(ytemp - c.getYCur()) < .01)
            ytemp = c.getYCur();
        
        //respect the edges of the screen
        if(xtemp < minX)
            xtemp = minX;
        if(xtemp > m.getWidth()-1 - minX)
            xtemp = m.getWidth()-1 - minX;
        if(ytemp < minY)
            ytemp = minY;
        if(ytemp > m.getHeight()-1 - minY)
            ytemp = m.getHeight()-1 - minY;
        
        //imposes a speed limit on the camera
        if(xtemp - x > 0.45)
            xtemp = x + 0.45;
        if(xtemp - x < -0.45)
            xtemp = x - 0.45;
        
        if(ytemp - y > 0.45)
            ytemp = y + 0.45;
        if(ytemp - y < -0.45)
            ytemp = y - 0.45;
        
        //and commit the changes to final
        x = xtemp;
        y = ytemp;
    }
    
    /**
     * Moves the camera instantly to a location.
     * @param x The x coord
     * @param y The y coord
     */
    public void moveInstantly(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    /**
     * changes the camera boundries to match the scene size.
     */
    public void resize()
    {
        minX = (GridEmblemMk3.HALFWIDTH-(GridEmblemMk3.GRIDSIZE*2.5))/(double)GridEmblemMk3.GRIDSIZE;
        minY = (GridEmblemMk3.HALFHEIGHT-(GridEmblemMk3.GRIDSIZE*2.5))/(double)GridEmblemMk3.GRIDSIZE;
    }
}
