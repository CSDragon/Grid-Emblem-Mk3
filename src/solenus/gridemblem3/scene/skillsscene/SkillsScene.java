/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.skillsscene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.ui.menu.CharacterSelectMenu;

/**
 *
 * @author Chris
 */
public class SkillsScene extends Scene
{
    private CharacterSelectMenu csm;
    private SkillsMenu skillsMenu;
    private SkillsReserveScrollingMenu srsm;
    private PlayerData data;
    private Unit selectedUnit;
    
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
                    skillsMenu.respondControls(im);
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
                    switch(csm.runFrame())
                    {
                        case CharacterSelectMenu.BACK:
                            return BACK;
                        case CharacterSelectMenu.NOTHING:
                            break;
                        default:
                            selectedUnit = csm.getSelectedUnit();
                            cst1to2();
                            break;
                    }
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
            if(skillsMenu != null)
                skillsMenu.animate();
            if(srsm != null)
                srsm.animate();
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
            if(skillsMenu != null)
                skillsMenu.draw(g);
            if(srsm != null)
                srsm.draw(g);
        }
    }
    
    //</editor-fold>
    
    public void start(PlayerData pd)
    {
        super.start();
        data = pd;
        
        controlState = 1;
        csm = new CharacterSelectMenu(data.getUnitList());
        csm.start();
    }
    
    public void cst1to2()
    {
        csm.end();
        skillsMenu = new SkillsMenu(selectedUnit.getSkills());
        skillsMenu.start();
        
        srsm = new SkillsReserveScrollingMenu(selectedUnit.getSkillsReserve(), 5);
        skillsMenu.start();
    }
    
    
    
}
