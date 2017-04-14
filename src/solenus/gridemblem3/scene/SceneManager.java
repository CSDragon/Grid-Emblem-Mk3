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
import solenus.gridemblem3.scene.gameoverscene.GameOverScene;
import java.awt.Graphics2D;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.scene.dialoguescene.EventManager;
import solenus.gridemblem3.ui.menu.SaveMenu;

/**
 * The top level scene for the game.
 * @author Chris
 */
public class SceneManager extends Scene
{
    
    public static final int TOPMENU = 0;
    
    //Game flow
    public static final int NEWLEVEL = 1;
    public static final int PREHQSKIT = 2;
    public static final int HQSCENE = 3;
    public static final int MIDDLESKIT = 4;
    public static final int MAPSCENE = 5;
    public static final int POSTMAPSKIT = 6;
    public static final int POSTMAPSAVE = 7;
    public static final int GAMEOVER = 8;
    //and retrn to new level
    
    
    private MapScene ms;
    private TopMenuScene tms;
    private DialogueScene ds;
    private HQScene hs;
    private GameOverScene gs;
    
    private SaveMenu saveMenu; 
    
    private PlayerData playerArmy;
    private EventManager eventManager;
    
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
        gs = new GameOverScene(this);
        
        saveMenu = new SaveMenu();
    }
        
    /**
     * Responds to controls.
     * @param im the input 
     */
    @Override
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
                8) Game Over screen
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
                case MIDDLESKIT:
                case POSTMAPSKIT:
                    ds.respondControls(im);
                    break;
                    
                case POSTMAPSAVE:
                    saveMenu.respondControls(im);
                    break;
                    
                case GAMEOVER:
                    gs.respondControls(im);
                    break;
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * @return The state of this scene that the parent scene needs to know.
     */
    @Override
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
                            eventManager = new EventManager(playerArmy);
                            controlStateTransition(MAPSCENE);
                            break;
                        case TopMenuScene.CONTINUE:
                            playerArmy = new PlayerData(tms.getFileNum());
                            eventManager = new EventManager(playerArmy);
                            switch(playerArmy.getSaveLoc())
                            {
                                case PlayerData.BASESAVE:
                                    controlStateTransition(HQSCENE);
                                    break;
                                case PlayerData.PREBATTLESAVE:
                                    controlStateTransition(MAPSCENE);
                                    break;
                                case PlayerData.POSTBATTLESAVE:
                                    controlStateTransition(NEWLEVEL);
                                    break;
                            } 
                            break;
                    }
                    break;
                
                case NEWLEVEL:
                    playerArmy.nextLevel();
                    eventManager = new EventManager(playerArmy);
                    if(eventManager.getPreHQEvent())
                        controlStateTransition(PREHQSKIT);
                    else
                        controlStateTransition(HQSCENE);
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
                            if(!playerArmy.getWatchedMidScene() && eventManager.getMidEvent())
                                controlStateTransition(MIDDLESKIT);
                            else
                                controlStateTransition(MAPSCENE);
                            break;
                    }
                    break;
                    
                case MIDDLESKIT:
                    switch(ds.runFrame())
                    {
                        case DialogueScene.FINISH:
                            playerArmy.setWatchedMidScene(true);
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
                            if(eventManager.getPostBattleEvent())
                                controlStateTransition(POSTMAPSKIT);
                            else
                                controlStateTransition(POSTMAPSAVE);
                            break;
                        case MapScene.GAMEOVER:
                            controlStateTransition(GAMEOVER);
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
                            
                        case SaveMenu.NOTHING:
                            break;
                            
                        default:
                            playerArmy.saveFile(saveMenu.getCursorLoc()+1);
                            break;
                    }
                    break;
                    
                case GAMEOVER:
                    switch(gs.runFrame())
                    {
                        case GameOverScene.RESTART:
                            controlStateTransition(TOPMENU);
                            break;
                        
                        case GameOverScene.NOTHING:
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
    @Override
    public void animate()
    {
        if(active)
        {
            tms.animate();
            ms.animate();
            ds.animate();
            hs.animate();
            gs.animate();
            
            saveMenu.animate();
        }
    }
    
    /**
     * Draws the scene.
     * @param g2 The graphics
     */
    @Override
    public void draw(Graphics2D g2)
    {
        if(visible)
        {
            tms.draw(g2);
            ms.draw(g2);
            hs.draw(g2);
            ds.draw(g2);
            gs.draw(g2);
            
            saveMenu.draw(g2);
        }

    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    @Override
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
            case MIDDLESKIT:
            case POSTMAPSKIT:
                ds.end();
                break;
                
            case POSTMAPSAVE:
                saveMenu.end();
                break;
                
            case GAMEOVER:
                gs.end();
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
                hs.start(playerArmy, eventManager);
                break;
                
            case PREHQSKIT:
            case MIDDLESKIT:
            case POSTMAPSKIT:
                ds.start(eventManager.getGameFlowEvent(controlState)); //TODO: Get this an activeDialogue.
                break;
                
            case POSTMAPSAVE: 
                saveMenu.start();
                break;
                
            case GAMEOVER:
                gs.start();
                break;
        }
    }

    //</editor-fold>
    
    /**
     * resizes the scene.
     */
    @Override
    public void resize()
    {
        super.resize();
        ms.resize();
        tms.resize();
        ds.resize();
        hs.resize();
    }
    
}


