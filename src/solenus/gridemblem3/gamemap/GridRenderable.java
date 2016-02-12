/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.gamemap;

import java.awt.Graphics2D;
import solenus.gridemblem3.GridEmblemMk3;
import java.awt.Point;
import solenus.gridemblem3.render.Sprite;
import solenus.gridemblem3.render.Rendering;

/**
 * 
 * @author Chris
 */
public abstract class GridRenderable 
{
    protected Sprite sprite;
    //location on the grid
    protected int x;
    protected int y;
    //temporary location used for rendering, most times this will be the same as X and Y
    //moving objects, for instnace, are placed at the (x,y) of their destination, and xCur and yCur move their sprite.
    protected double xCur;
    protected double yCur;
    
    //how much of 1 unit the object travels
    protected double speed = 1/6.0;
    protected boolean moving = false;

    
    protected boolean visible;
    
    
    /**
     * generic constructor
     * @param name the name of the sprite this object represents
     */
    public GridRenderable(String name)
    {
        visible = true;
        sprite = new Sprite(name);
    }
    
    /**
     * animates the sprite
     */
    public void Animate()
    {
        sprite.animate();
    }
    
    /**
     * sets the destination to point(x,y)
     * @param _x the x coord where we want to go
     * @param _y the y coord where we want to go
     */
    public void setDest(int _x, int _y)
    {
        x = _x;
        y = _y;
    }
    
    /**
     * sets the destination to point p
     * @param p the destination
     */
    public void setDest(Point p)
    {
        setDest(p.x, p.y);
    }
    
    /**
     * Move over time from where it is visually (xCur, yCur) to where it actually is (x,y).
     */
    public void moveToDest()
    {
        if(xCur != x)
        {
            if(xCur > x)
                xCur -= speed;
            else
                xCur += speed;
            if(Math.abs(xCur - x) < .01)
                xCur = x;
        }
        if(yCur != y)
        {
            if(yCur > y)
                yCur -= speed;
            else
                yCur += speed;
            if(Math.abs(yCur - y) < .01)
                yCur = y;
        }
    }
    
    /**
     * Moves the renderable to this exact coordinate
     * @param _x the x to move to
     * @param _y the y to move to
     */
    public void moveInstantly(int _x, int _y)
    {
        x = _x;
        xCur = _x;
        y = _y;
        yCur = _y;
    }
    
    /**
     * Moves the renderable to this exact coordinate
     * @param p the point to move to
     */
    public void moveInstantly(Point p)
    {
        moveInstantly(p.x,p.y);
    }
    
    public boolean isMoving()
    {
        return !(x==xCur && y ==yCur);
    }
    
    /**
     * Render the sprite based on the camera
     * @param g2 the graphics
     * @param c  the camera
     */
    public void renderCam (Graphics2D g2, Camera c)
    {
        if(visible)
            Rendering.renderGrid(sprite, c, g2, xCur, yCur);
    }
    
    
    public int distanceTo(GridRenderable other)
    {
        return(Math.abs(x - other.getX()) + Math.abs(y - other.getY()));
    }
    
    
    
    //<editor-fold desc="getters and setters">
    
    /**
     * Show or hide the sprite
     * @param b ...
     */
    public void setVisible(boolean b)
    {
        visible = b;
    }
    
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public double getXCur()
    {
        return xCur;
    }
    
    public double getYCur()
    {
        return yCur;
    }
    
    public Sprite getSprite()
    {
        return sprite;
    }
    
    /**
     * Returns where the object is ACTUALLY at, not where it appears to be (returns x,y not xCur,yCur)
     * @return The point the object is actually at
     */
    public Point getCoord()
    {
        return new Point(x,y);
    }
    
    //</editor-fold>
    
}
