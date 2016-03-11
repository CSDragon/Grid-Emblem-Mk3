/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.InputManager;

/**
 * Base class for all scenes
 *
 * @author Chris
 */
public abstract class Scene
{
    public static final int NOTHING = -1;
    public static final int BACK = -2;
    
    protected Scene parent;
    protected boolean active;
    protected boolean visible;
    protected int controlState;
    
    protected int width;
    protected int height;
    protected int xLoc;
    protected int yLoc;
    
    //<editor-fold desc="Constructors">
    
    /**
     * Simple constructor with a parent.
     * @param _parent the parent scene for this scene.
     */
    public Scene(Scene _parent)
    {
        this();

        //set parent and children
        parent = _parent;
    }
    
    /**
     * Simple constructor for no parent.
     */
    public Scene()
    {
        width = GridEmblemMk3.WIDTH;
        height = GridEmblemMk3.HEIGHT;
        
        xLoc = 0;
        yLoc = 0;
    }
    
    //</editor-fold>
    
    // <editor-fold desc="Scene control methods">
    
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * most scene subclasses must override this, and check if they are active.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
        }
        
        return NOTHING;
    }
    
    /**
     * animates the scene's objects 1 frame
     * most scene subclasses must override this, and check if they are active.
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
        }
    }
    
    /**
     * Draws the scene
     * @param g 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
        }
    }

    //</editor-fold>
    
    /**
     * resizes the scene to the current app size
     */
    public void resize()
    {
        width = GridEmblemMk3.WIDTH;
        height = GridEmblemMk3.HEIGHT;
    }

    //<editor-fold desc="on/off control">
    
    public void visible()
    {
        visible = true;
    }
    
    public void invisible()
    {
        visible = false;
    }
    
    public void start()
    {
        active = true;
        visible = true;
    }
    
    public void end() //Used to terminate a scene.
    {
        active = false;
        visible = false;
    }
    
    public void stop() //Used to temporarily halt the scene.
    {
        active = false;
        visible = false;
    }
    
    public void resume() //Used to get back to a scene after it has been halted
    {
        active = true;
        visible = true;
    }
    
    public boolean getAcitve()
    {
        return active;
    }
    
    public boolean getVisible()
    {
        return visible;
    }
    
    /**
     * @return the controlState
     */
    public int getControlState() 
    {
        return controlState;
    }

    //</editor-fold>

}
