/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.InputManager;

/**
 * Base class for all scenes
 *
 * @author Chris
 */
public abstract class Scene extends JPanel implements Comparable<Scene>
{
    protected ArrayList<Scene> childScenes;
    protected Scene targetScene;
    protected Scene parent;
    protected boolean active;
    protected boolean visible;
    protected int visibleLayer;
    protected int controlState;
    
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
        
        //this should be overridden almost every time. It exists only so we don't run into clipping or visibilty problems in tests.
        visibleLayer = parent.getVisibleLayer()+1;
    }
    
    /**
     * Simple constructor for no parent.
     */
    public Scene()
    {
        //everything set to null, false or 0.
        //initialize childScenes as empty.
        childScenes = new ArrayList<Scene>();
        
        setLayout(null);
        setVisible(false);
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
            //always do this
            if(targetScene != null)
                targetScene.respondControls(im);
            
            
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
            //always do this.
            runChildren();
        }
        
        return -1;
    }
    
    /**
     * renders the scene.
     * most scene subclasses must override this, and check if they are visible.
     */
    public void animate()
    {   
        //always check this
        if(visible)
        {
            
            //always do this.
            drawChildren();
        }
    }
    
    /**
     * resizes the scene to the current app size
     */
    public void resize()
    {
        setSize(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT);
        setPreferredSize(new Dimension(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT));
        for(Scene c : childScenes)
            c.resize();
    }

    
    //</editor-fold>
    
    //<editor-fold desc="Child Management">
    
    /**
     * Adds a scene to allScenes, but makes sure to check it isn't a duplicate. Sorts the list by visibility
     * @param s The scene to be added
     * @return if it succeeds
     */
    public boolean addScene(Scene s)
    {
        if(!childScenes.contains(s))
        {
            childScenes.add(s);
            Collections.sort(childScenes);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a scene from the 
     * @param s
     * @return If it succeeds
     */
    public boolean removeScene(Scene s)
    {
        return childScenes.remove(s);
    }
    
    
    /**
     * Runs all scenes.
     */
    public void runChildren()
    {
        for (Scene s : childScenes) 
        {
            s.runFrame();
        }
            
    }
    
    /**
     * draws all scenes
     */
    public void drawChildren()
    {
        for (Scene s : childScenes)
        {
            s.animate();
        }
    }
    
    /**
     * This scene gives control priority to one of its child
     * @param s The scene to receive priority
     */
    public void givePriority(Scene s)
    {
        targetScene = s;
    }
    
    /**
     * This scene takes back control priority for itself.
     */
    public void getPriority()
    {
        targetScene = null;
    }
    
    //</editor-fold>
    
    // <editor-fold desc="Comparable">
    @Override
    public int compareTo(Scene other)
    {
        if(visibleLayer < other.getVisibleLayer())
            return -1;
        else if(visibleLayer == other.getVisibleLayer())
            return 0;
        else 
            return 1;
    }
    // </editor-fold>
    
    //<editor-fold desc="on/off control">
    public void visible()
    {
        visible = true;
        setVisible(true);
    }
    
    public void invisible()
    {
        visible = false;
        setVisible(false);
    }
    
    public void start()
    {
        active = true;
        visible = true;
        setVisible(true);
    }
    
    public void stop() //Used to temporarily halt the scene.
    {
        active = false;
        visible = false;
        setVisible(false);
    }
    
    public void end() //Used to terminate a scene.
    {
        active = false;
        visible = false;
        setVisible(false);
    }
    
    public boolean getAcitve()
    {
        return active;
    }
    
    public boolean getVisible()
    {
        return visible;
    }
    
    public int getVisibleLayer()
    {
        return visibleLayer;
    }
    //</editor-fold>
}
