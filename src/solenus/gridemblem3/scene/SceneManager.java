/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Dimension;
import solenus.gridemblem3.GridEmblemMk3;

/**
 * The top level scene for the game.
 * @author Chris
 */
public class SceneManager extends Scene
{
    
    private MapScene ms;
    private TopMenuScene tms;
    private DialogueScene ds;
    private HQScene hs;
    
    /**
     * Creates the scene manager and its children
     */
    public SceneManager()
    {
        //boot it up
        setLayout(null);
        setSize(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT);
        setLocation(0, 0);
        setPreferredSize(new Dimension(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT));
        start();
        
        //Make the child scenes.
        ms = new MapScene(this);
        childScenes.add(ms);
        add(ms);
        
        tms = new TopMenuScene(this);
        childScenes.add(tms);
        add(tms);
        
        ds = new DialogueScene(this);
        childScenes.add(ds);
        add(ds);
        
        hs = new HQScene(this);
        childScenes.add(hs);
        add(hs);
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
            
            /*
            STATES:
                0) Game has just opened up.
                1) TopMenuScene
                2) MapScene
            */
            switch(controlState)
            {
                case 0:
                    cst0to1();
                    break;
                case 1:
                    switch(tms.runFrame())
                    {
                        case 1:
                            cst1to2();
                            break;
                    }
                case 2:
                    switch(ms.runFrame())
                    {
                        
                    }
            }
        }
        
        return -1;
    }
    
    
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * Changes from game bootup to the top menu
     */
    public void cst0to1()
    {
        controlState = 1;
        tms.start();
        targetScene = tms;
    }
    
    
    /**
     * Changes from top menu to game map. Probably should never be used outside debugging, but we'll see.
     */
    public void cst1to2()
    {
        controlState = 2;
        tms.end();
        ms.start();
        targetScene = ms;
    }
    
    //</editor-fold>

    
}


