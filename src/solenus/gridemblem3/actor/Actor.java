/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.actor;
import java.awt.Graphics2D;
import solenus.gridemblem3.gamemap.Camera;
import solenus.gridemblem3.gamemap.GridRenderable;

/**
 * An actor is any object that can be on the map.
 *
 * @author Chris
 */
public abstract class Actor extends GridRenderable
{
    protected boolean gridded;
    protected boolean passable;
    
    
    public Actor(String spriteName)
    {
        super(spriteName);
        
        passable = false;
    }
    
    /**
     * Tells the actor it's on the grid at (x,y)
     * 
     * @param _x the x coord we're placing it at
     * @param _y the y coord we're placing it at
     */
    public void placeOnGrid(int _x, int _y)
    {
        gridded = true;
        x = _x;
        y = _y;
        xCur = _x;
        yCur = _y;
    }
    
    
    /**
     * Tells the actor it has been removed from the grid.
     */
    public void removeFromGrid()
    {
        gridded = false;
    }
    
    public boolean getGridded()
    {
        return gridded;
    }
    
    public void renderCam (Graphics2D g2, Camera c)
    {
        if(gridded)
            super.renderCam(g2,c);
    }
    
    public boolean isPassable()
    {
        return passable;
    }
}
