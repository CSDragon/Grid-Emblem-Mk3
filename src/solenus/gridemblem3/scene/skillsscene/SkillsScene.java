/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.skillsscene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.ui.menu.CharacterSelectMenu;

/**
 *
 * @author Chris
 */
public class SkillsScene extends Scene
{
    private CharacterSelectMenu csm;
    private SkillMenu skillMenu;
    private PlayerData data;
    
    public SkillsScene()
    {
        super();
    }
    
    
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
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
                STATES:
                    1) Character select menu.
                    2) Skill Menu
                */
                case 1:
                    csm.respondControls(im);
                    break;
                case 2:
                    skillMenu.respondControls(im);
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
            switch(controlState)
            {
                /*
                STATES:
                    1) Character select menu.
                    2) Skill Menu
                */
                case 1:
                    csm.runFrame();
                    break;
                case 2:
                    csm.runFrame();
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
        //always check this
        if(active)
        {
            csm.animate();
            skillMenu.animate();
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
            csm.draw(g);
            skillMenu.draw(g);
        }
    }
    
    //</editor-fold>
    
    public void start(PlayerData pd)
    {
        data = pd;
        
        controlState = 1;
        csm = new CharacterSelectMenu(data.getUnitList());
        csm.start();
    }
    
    
    
}
