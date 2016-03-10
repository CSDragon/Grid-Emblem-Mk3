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
                    if(im.getLeft() == 1|| im.getRight() == 1)
                        controlState = 3;
                    else
                        skillsMenu.respondControls(im);
                    break;
                    
                case 3:
                    if(im.getLeft() == 1 || im.getRight() == 1)
                        controlState = 2;
                    else
                        srsm.respondControls(im);
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
                    3) Skill bank menu
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
                            if(selectedUnit != null)
                                cst1to2();
                            break;
                    }
                    break;
                case 2:
                    switch(skillsMenu.runFrame())
                    {
                        case SkillsMenu.BACK:
                            cstXto1();
                            break;
                            
                        case SkillsMenu.NOTHING:
                            break;
                            
                        default:
                            if(skillsMenu.getSelectedAction() != null)
                            {
                                selectedUnit.moveSkillToReserve(skillsMenu.getSelectedAction());
                                skillsMenu.refresh();
                                srsm.refresh();
                            }
                            break;
                    }
                    break;
                    
                case 3:
                    switch(srsm.runFrame())
                    {
                        case SkillsReserveScrollingMenu.BACK:
                            cstXto1();
                            break;
                            
                        case SkillsReserveScrollingMenu.NOTHING:
                            break;
                            
                        default:
                            if(srsm.getSelectedAction() != null)
                            {
                                selectedUnit.moveSkillFromReserve(srsm.getSelectedAction());
                                skillsMenu.refresh();
                                srsm.refresh();
                            }
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
        controlState = 2;
        csm.end();

        skillsMenu = new SkillsMenu(selectedUnit);
        skillsMenu.start();
        skillsMenu.setLocation(-150, 100);
        
        srsm = new SkillsReserveScrollingMenu(selectedUnit, 5);
        srsm.start();
        srsm.setLocation(150, -100);
    }
    
    public void cstXto1()
    {
        controlState = 1;
        skillsMenu.end();
        srsm.end();
        
        csm.resume();
    }
    
}
