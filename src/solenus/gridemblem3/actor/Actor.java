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
    protected boolean passable;
    protected String name;
    
    /**
     * Creates an actor with a sprite.
     * @param spriteName The name of the sprtie we're loading in.
     */
    public Actor(String spriteName)
    {
        super(spriteName);
        name = spriteName;
        passable = false;
    }
    
    /**
     * Creates an actor with no sprite.
     */
    public Actor()
    {
        super();
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
        x = _x;
        y = _y;
        xCur = _x;
        yCur = _y;
    }
    
    public void renderCam (Graphics2D g2, Camera c)
    {
        super.renderCam(g2,c);
    }
    
    public boolean isPassable()
    {
        return passable;
    }
}
