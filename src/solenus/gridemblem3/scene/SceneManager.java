/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.InputManager;

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
    
    private PlayerData playerArmy;
    
    /**
     * Creates the scene manager and its children
     */
    public SceneManager()
    {
        super();
        //boot it up
        start();
        
        //Make the child scenes.
        ms = new MapScene(this);
        tms = new TopMenuScene(this);
        ds = new DialogueScene(this);
        hs = new HQScene(this);
    }
        
    /**
     * Responds to controls.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        if(active)
        {   
            /*
            STATES:
                0) Game has just opened up.
                1) TopMenuScene
                2) MapScene
                3) DialogScene
                4) HQ Scene
            */
            switch(controlState)
            {
                case 1:
                    tms.respondControls(im);
                    break;
                case 2:
                    ms.respondControls(im);
                    break;
                case 3:
                    ds.respondControls(im);
                    break;
                case 4:
                    hs.respondControls(im);
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
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
                3) DialogScene
                4) HQ Scene
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
                        case 2:
                            loadGame(tms.getFileNum());
                            break;
                    }
                
                case 2:
                    switch(ms.runFrame())
                    {
                        case MapScene.RETURNTOBASE:
                            cst2to4();
                            break;
                    }
                    break;
                
                case 3:
                    switch(ds.runFrame())
                    {

                    }
                    break;
                    
                case 4:
                    switch(hs.runFrame())
                    {
                        case HQScene.GOTOMAP:
                            cst4to2();
                            break;
                    }
                    break;
            }
        }
        
        return -1;
    }
    
    /**
     * animates the scene's objects 1 frame
     */
    public void animate()
    {
        if(active)
        {
            tms.animate();
            ms.animate();
            ds.animate();
            hs.animate();
        }
    }
    
    /**
     * Draws the scene.
     * @param g2 The graphics
     */
    public void draw(Graphics2D g2)
    {
        if(visible)
        {
            tms.draw(g2);
            ms.draw(g2);
            hs.draw(g2);
            ds.draw(g2);
        }

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
    }
    
    /**
     * Changes from top menu to game map. Probably should never be used outside debugging, but we'll see.
     */
    public void cst1to2()
    {
        controlState = 2;
        tms.end();
        playerArmy = new PlayerData();
        playerArmy.newGame();
        ms.start(playerArmy);
    }
    
    public void cst1to3()
    {
        controlState = 3;
        tms.end();
        ds.start("test");
    }
    
    public void cst1to4()
    {
        controlState = 4;
        tms.end();
        hs.start(playerArmy);
    }
    
    public void cst2to4()
    {
        controlState = 4;
        ms.end();
        hs.start(playerArmy);
    }
    
    public void cst4to2()
    {
        controlState = 2;
        hs.end();
        ms.start(playerArmy);
    }
    
    //</editor-fold>

    /**
     * Loads the player's data from a file
     * @param fileNum The file number to load from.
     */
    public void loadGame(int fileNum)
    {
        playerArmy = new PlayerData(fileNum);
        if(playerArmy.isInBase())
            cst1to4();
        else
            cst1to2();
            
    }
    
    /**
     * resizes the scene.
     */
    public void resize()
    {
        super.resize();
        ms.resize();
        tms.resize();
        ds.resize();
        hs.resize();
    }
    
}


