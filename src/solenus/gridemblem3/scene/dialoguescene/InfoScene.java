/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.dialoguescene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InfoScene extends Scene
{
    private PlayerData data;
    private InfoMenu infoMenu;
    private DialogueScene dialogueScene;
    
    private EventManager eventManager;
    
    private int activeEvent;
    
    
    public InfoScene()
    {
        super();
        
        dialogueScene = new DialogueScene(this);
    }
    
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
            switch(controlState)
            {
                /*
                STATES
                    1) Info Menu
                    2) Dialogue Scene
                    3) Give Rewards.
                    4) Save that we've seen the event.
                */
                case 1:
                    infoMenu.respondControls(im);
                    break;
                    
                case 2:
                    dialogueScene.respondControls(im);
                    break;
            }
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
            switch(controlState)
            {
                /*
                STATES
                    1) Info Menu
                    2) Dialogue Scene
                    3) Give Rewards.
                    4) Save that we've seen the event.
                */
                case 1:
                    switch(infoMenu.runFrame())
                    {
                        case InfoMenu.BACK:
                            return BACK;
                        
                        case InfoMenu.NOTHING:
                            break;
                        
                        default:
                            activeEvent = infoMenu.getCursorLoc();
                            if(!data.getEventsWatched().get(activeEvent))
                                cst1to2();
                            break;
                    }
                
                case 2:
                    switch(dialogueScene.runFrame())
                    {
                        case DialogueScene.FINISH:
                            controlState = 3;
                            break;
                    }
                    break;
                case 3:
                    data.getWeaponConvoy().addAll(eventManager.getInfoEvents().get(activeEvent).getWeaponRewards());
                    data.getItemConvoy().addAll(eventManager.getInfoEvents().get(activeEvent).getItemRewards());
                    cst3to4();
                    break;
                case 4:
                    controlState = 1;
                    data.watchedEvent(activeEvent);
                    break;
            }
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
            infoMenu.animate();
            dialogueScene.animate();
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
            infoMenu.draw(g);
            dialogueScene.draw(g);
        }
    }

    //</editor-fold>
    
    /**
     * Starts the scene up.
     * @param pd the player's save file
     */
    public void start(PlayerData pd, EventManager em)
    {
        super.start();
        controlState = 1;
        eventManager = em;
        
        data = pd;
        
        infoMenu = new InfoMenu(eventManager.getInfoEvents(), data.getEventsWatched());
        infoMenu.start();
    }
    
    public void cst1to2()
    {
        controlState = 2;
        infoMenu.end();
        dialogueScene.start(eventManager.getInfoEvents().get(activeEvent).getFileName());
    }
    
    public void cst3to4()
    {
        controlState = 4;
        infoMenu.resume();
        dialogueScene.end();
    }
    
}
