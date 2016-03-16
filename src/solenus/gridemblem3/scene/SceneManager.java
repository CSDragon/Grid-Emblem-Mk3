/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import solenus.gridemblem3.scene.topmenuscene.TopMenuScene;
import solenus.gridemblem3.scene.dialoguescene.DialogueScene;
import solenus.gridemblem3.scene.hqscene.HQScene;
import solenus.gridemblem3.scene.mapscene.MapScene;
import java.awt.Graphics2D;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.ui.menu.SaveMenu;

/**
 * The top level scene for the game.
 * @author Chris
 */
public class SceneManager extends Scene
{
    
    public static final int TOPMENU = 0;
    
    //Gameplay order
    public static final int NEWLEVEL = 1;
    public static final int PREHQSKIT = 2;
    public static final int HQSCENE = 3;
    public static final int POSTHQSKIT = 4;
    public static final int MAPSCENE = 5;
    public static final int POSTMAPSKIT = 6;
    public static final int POSTMAPSAVE = 7;
    //and retrn to new level
    
    
    private MapScene ms;
    private TopMenuScene tms;
    private DialogueScene ds;
    private HQScene hs;
    
    private SaveMenu saveMenu; 
    
    private PlayerData playerArmy;
    
    /**
     * Creates the scene manager and its children
     */
    public SceneManager()
    {
        super();
        
        //Make the child scenes.
        ms = new MapScene(this);
        tms = new TopMenuScene(this);
        ds = new DialogueScene(this);
        hs = new HQScene(this);
        
        saveMenu = new SaveMenu();
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
                0) Startup/Top Menu
                1) New Level
                2) Pre-HQ Skit
                3) HQ Scene
                4) Post-HQ/Pre-Map Skit
                5) Map
                6) Post-Map Skit
                7) Post-Map Save
            */
            switch(getControlState())
            {
                case TOPMENU:
                    tms.respondControls(im);
                    break;
                
                case MAPSCENE:
                    ms.respondControls(im);
                    break;
                
                case HQSCENE:
                    hs.respondControls(im);
                    break;
                
                case PREHQSKIT:
                case POSTHQSKIT:
                case POSTMAPSKIT:
                    ds.respondControls(im);
                    break;
                    
                case POSTMAPSAVE:
                    saveMenu.respondControls(im);
                    break;
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
                0) Startup/Top Menu
                1) New Level
                2) Pre-HQ Skit
                3) HQ Scene
                4) Post-HQ/Pre-Map Skit
                5) Map
                6) Post-Map Skit
                7) Post-Map Save
            */
            switch(getControlState())
            {
                case TOPMENU:
                    switch(tms.runFrame())
                    {
                        case TopMenuScene.NEWGAME:
                            playerArmy = new PlayerData();
                            playerArmy.newGame();
                            controlStateTransition(MAPSCENE);
                            break;
                        case TopMenuScene.CONTINUE:
                            loadGame(tms.getFileNum());
                            break;
                    }
                    break;
                
                case NEWLEVEL:
                    controlStateTransition(PREHQSKIT);
                    break;
                    
                case PREHQSKIT:
                    switch(ds.runFrame())
                    {
                        case DialogueScene.FINISH:
                            controlStateTransition(HQSCENE);
                            break;
                    }
                    break;
                
                case HQSCENE:
                    switch(hs.runFrame())
                    {
                        case HQScene.GOTOMAP:
                            controlStateTransition(MAPSCENE);
                            break;
                    }
                    break;
                    
                case POSTHQSKIT:
                    switch(ds.runFrame())
                    {
                        case DialogueScene.FINISH:
                            controlStateTransition(MAPSCENE);
                            break;
                    }
                    break;
                
                case MAPSCENE:
                    switch(ms.runFrame())
                    {
                        case MapScene.RETURNTOBASE:
                            controlStateTransition(HQSCENE);
                            break;
                        case MapScene.VICTORY:
                            controlStateTransition(POSTMAPSKIT);
                            break;
                    }
                    break;
                    
                case POSTMAPSKIT:
                    switch(ds.runFrame())
                    {
                        case DialogueScene.FINISH:
                            controlStateTransition(POSTMAPSAVE);
                            break;
                    }
                    break;
                    
                case POSTMAPSAVE:
                    switch(saveMenu.runFrame())
                    {
                        case SaveMenu.BACK:
                            controlStateTransition(NEWLEVEL);
                            break;
                    }
                    break;
            }
        }
        
        return NOTHING;
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
            
            saveMenu.animate();
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
            
            saveMenu.draw(g2);
        }

    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    public void start()
    {
        super.start();
        tms.start();
    }
    
    /**
     * Transitions the scene from one state to another.
     * @param newState The state we're transitioning to.
     */
    public void controlStateTransition(int newState)
    {
        //Prepare the transition
        int oldState = controlState;
        controlState = newState;
        
        //disable stuff from the old scene.
        switch(oldState)
        {
            case TOPMENU:
                tms.end();
                break;
                
            case MAPSCENE:
                ms.end();
                break;
                
            case HQSCENE:
                hs.end();
                break;
                
            case PREHQSKIT:
            case POSTHQSKIT:
            case POSTMAPSKIT:
                ds.end();
                break;
                
            case POSTMAPSAVE:
                saveMenu.end();
                break;
        }
        
        //set the new controlState
        controlState = newState;
        
        //And get the new state's parts ready.
        switch(controlState)
        {
            case TOPMENU:
                tms.start();
                break;
                
            case MAPSCENE:
                ms.start(playerArmy);
                break;
                
            case HQSCENE:
                hs.start(playerArmy);
                break;
                
            case PREHQSKIT:
            case POSTHQSKIT:
            case POSTMAPSKIT:
                ds.start("test"); //TODO: Get this an activeDialogue.
                break;
                
            case POSTMAPSAVE: 
                saveMenu.start();
                break;
        }
    }

    //</editor-fold>

    /**
     * Loads the player's data from a file 
     * @param fileNum The file number to load from.
     */
    public void loadGame(int fileNum) //TODO: Make this load to postmap too.
    {
        playerArmy = new PlayerData(fileNum);
        if(playerArmy.isInBase())
            controlStateTransition(HQSCENE);
        else
            controlStateTransition(MAPSCENE);
            
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


