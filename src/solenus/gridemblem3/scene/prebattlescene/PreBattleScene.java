/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.prebattlescene;

import java.awt.Graphics2D;
import java.util.ArrayList;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.scene.mapscene.Map;
import solenus.gridemblem3.scene.inventoryscene.InventoryScene;
import solenus.gridemblem3.scene.skillsscene.SkillsScene;
import solenus.gridemblem3.ui.menu.PreBattleMenu;
import solenus.gridemblem3.ui.menu.SaveMenu;

/**
 *
 * @author Chris
 */
public class PreBattleScene extends Scene
{
    public static final int SELECTUNITS = 0;
    public static final int VIEWMAP = 1;
    public static final int INVENTORY = 2;
    public static final int SKILLS = 3;
    public static final int SAVE = 4;
    public static final int START = 5;
    public static final int RETURNTOBASE = 6;
    public static final int UPDATEUNITS = 7;
    
    //data
    private PlayerData data;
    private Map map;
    
    //ui
    private PreBattleMenu pbm;
    private InventoryScene invenScene;
    private SaveMenu saveMenu;
    private ChooseUnitsChecklistMenu cucm;
    private SkillsScene skillsScene; 
    
    public PreBattleScene(Scene parent)
    {
        super(parent);
        pbm = new PreBattleMenu();
        invenScene = new InventoryScene();
        saveMenu = new SaveMenu();
        cucm = new ChooseUnitsChecklistMenu();
        skillsScene = new SkillsScene();
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
            /*
            STATES
                1) Pre-Battle Menu
                2) Inventory Scene
                3) Selecting Units
                4) Save menu
                5) Skills Scene
            */
            switch(getControlState())
            {
                case 1:
                    pbm.respondControls(im);
                    break;
                case 2:
                    invenScene.respondControls(im);
                    break;
                case 3: 
                    cucm.respondControls(im);
                    break;
                case 4:
                    saveMenu.respondControls(im);
                    break;
                case 5:
                    skillsScene.respondControls(im);
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
            STATES
                1) Pre-Battle Menu
                2) Inventory Scene
                3) Selecting Units
                4) Save menu
            */
            switch(getControlState())
            {
                case 1:
                    switch(pbm.runFrame())
                    {
                        case PreBattleMenu.SELECTUNITS:
                            cst1to3();
                            break;
                        case PreBattleMenu.INVENTORY:
                            cst1to2();
                            break;
                        case PreBattleMenu.VIEWMAP:
                            return VIEWMAP;
                        case PreBattleMenu.SAVE:
                            cst1to4();
                            break;
                        case PreBattleMenu.START:
                            return START;
                        case PreBattleMenu.RETURNTOBASE:
                            return RETURNTOBASE;
                        case PreBattleMenu.SKILLS:
                            cst1to5();
                            break;
                    }
                    break;
                case 2:
                    switch(invenScene.runFrame())
                    {
                        case InventoryScene.BACK:
                            cst2to1();
                    }
                    break;
                case 3:
                    switch(cucm.runFrame())
                    {
                        case ChooseUnitsChecklistMenu.BACK:
                            cst3to1();
                            break;
                        case ChooseUnitsChecklistMenu.CONFIRM:
                            cst3to1();
                            return UPDATEUNITS;
                    }
                case 4:
                    int s = saveMenu.runFrame();
                    switch(s)
                    {
                        case SaveMenu.BACK:
                            cst4to1();
                            break;
                            
                        case SaveMenu.NOTHING:
                            break;
                            
                        default:
                            data.saveFile(s+1);
                            break;
                    }
                    break;
                    
                case 5:
                    switch(skillsScene.runFrame())
                    {
                        case SkillsScene.BACK:
                            cst5to1();
                            break;
                    }
            }
        }
        
        return NOTHING;
    }
    
    /**
     * animates the scene's objects 1 frame
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
            pbm.animate();
            invenScene.animate();
            skillsScene.animate();
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
            pbm.draw(g);
            invenScene.draw(g);
            saveMenu.draw(g);
            cucm.draw(g);
            skillsScene.draw(g);
        }
    }
    
    //</editor-fold>
    
    public void start(PlayerData pd, Map m)
    {
        super.start();
        data = pd;
        map = m;
        controlState = 1;
        pbm.start();
        
        //this was a flaw in my design;
        cucm.start(pd, m);
        cucm.end();
    }
    
    public ArrayList<Unit> getSelectedUnits()
    {
        return cucm.getSelectedUnits();
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    public void cst1to2()
    {
        controlState = 2;
        pbm.end();
        invenScene.start(data);
    }
    
    public void cst1to3()
    {
        controlState = 3;
        pbm.end();
        cucm.resume();
        
    }
    
    public void cst1to4()
    {
        controlState = 4;
        pbm.end();
        saveMenu.start();
    }
    
    public void cst1to5()
    {
        controlState = 5;
        pbm.end();
        skillsScene.start(data);
    }
    
    public void cst2to1()
    {
        controlState = 1;
        invenScene.end();
        pbm.resume();
    }
    
    public void cst3to1()
    {
        controlState = 1;
        cucm.end();
        pbm.resume();
    }
    
    public void cst4to1()
    {
        controlState = 1;
        saveMenu.end();
        pbm.resume();
    }
    
    public void cst5to1()
    {
        controlState = 1;
        skillsScene.end();
        pbm.resume();
    }
    
    //</editor-fold>

    
    
    
}
