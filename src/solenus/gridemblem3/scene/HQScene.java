/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.scene.infoscene.InfoScene;
import solenus.gridemblem3.scene.inventoryscene.InventoryScene;
import solenus.gridemblem3.scene.shopscene.ShopScene;
import solenus.gridemblem3.scene.skillsscene.SkillsScene;
import solenus.gridemblem3.ui.menu.HQMenu;
import solenus.gridemblem3.ui.menu.SaveMenu;


/**
 *
 * @author Chris
 */
public class HQScene extends Scene
{
    public static final int SAVE = 1;
    public static final int GOTOMAP = 2;
    
    private HQMenu hqMenu;
    private SaveMenu save;
    private InventoryScene invenScene;
    private ShopScene shopScene;
    private SkillsScene skillsScene;
    private InfoScene infoScene;
    
    PlayerData playerArmy;
    private int saveFile;
    
    public HQScene(Scene parent)
    {
        super(parent);
        hqMenu = new HQMenu();
        save = new SaveMenu();
        invenScene = new InventoryScene();
        shopScene = new ShopScene();
        skillsScene = new SkillsScene();
        infoScene = new InfoScene();
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
            /*
             STATES:
                0) HQMenu
                1) Save Menu
                2) Inventory Scene
                3) Shop Scene
                4) Skills Scene
                5) Support Scene
                6) Info Scene
             */
            switch(getControlState())
            {
                case 0:
                    hqMenu.respondControls(im);
                    break;
                case 1:
                    save.respondControls(im);
                    break;
                case 2:
                    invenScene.respondControls(im);
                    break;
                case 3:
                    shopScene.respondControls(im);
                    break;
                case 4:
                    skillsScene.respondControls(im);
                    break;
                case 6:
                    infoScene.respondControls(im);
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
            switch(getControlState())
            {
                /*
                 STATES:
                    0) HQMenu
                    1) Save Menu
                    2) Inventory Scene
                    3) Shop Scene
                    4) Skills Scene
                    5) Support Scene
                    6) Info Scene
                 */
                case 0:
                    switch(hqMenu.runFrame())
                    {
                        case HQMenu.END:
                            return GOTOMAP;
                        case HQMenu.SAVE:
                            cst0to1();
                            break;
                        case HQMenu.INVENTORY:
                            cst0to2();
                            break;
                        case HQMenu.SHOP:
                            cst0to3();
                            break;
                        case HQMenu.SKILLS:
                            cst0to4();
                            break;
                        case HQMenu.EVENTS:
                            cst0to6();
                            break;
                    }
                    break;
                case 1:
                    int s = save.runFrame();
                    switch(s)
                    {
                        case SaveMenu.BACK:
                            cst1to0();
                            break;
                            
                        case SaveMenu.NOTHING:
                            break;
                            
                        default:
                            playerArmy.saveFile(s+1);
                            break;
                    }
                    break;
                case 2:
                    switch(invenScene.runFrame())
                    {
                        case InventoryScene.BACK:
                            cst2to0();
                            break;
                    }
                case 3:
                    switch(shopScene.runFrame())
                    {
                        case ShopScene.BACK:
                            cst3to0();
                            break;
                    }
                    break;
                
                case 4:
                    switch(skillsScene.runFrame())
                    {
                        case SkillsScene.BACK:
                            cst4to0();
                            break;
                    }
                    break;
                    
                case 6:
                    switch(infoScene.runFrame())
                    {
                        case InfoScene.BACK:
                            cst6to0();
                            break;
                    }
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
            shopScene.animate();
            skillsScene.animate();
            infoScene.animate();
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
            hqMenu.draw(g);
            save.draw(g);
            invenScene.draw(g);
            shopScene.draw(g);
            skillsScene.draw(g);
            infoScene.draw(g);
        }
    }
    
    /**
     * Starts the scene up.
     * @param pd The army data.
     */    
    public void start(PlayerData pd)
    {
        super.start();
        playerArmy = pd;
        hqMenu.start();
    }
    
    //</editor-fold>

    
    /**
     * resizes the scene to the current app size
     */
    public void resize()
    {
        width = GridEmblemMk3.WIDTH;
        height = GridEmblemMk3.HEIGHT;
    }

    
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * Goes from the HQ Menu to the save menu.
     */
    public void cst0to1()
    {
        controlState = 1;
        hqMenu.end();
        save.start();
    }
    
    public void cst0to2()
    {
        controlState = 2;
        hqMenu.end();
        invenScene.start(playerArmy);
    }
    
    public void cst0to3()
    {
        controlState = 3;
        hqMenu.end();
        shopScene.start(playerArmy);
    }
    
    public void cst0to4()
    {
        controlState = 4;
        hqMenu.end();
        skillsScene.start(playerArmy);
    }
    
    public void cst0to6()
    {
        controlState = 6;
        hqMenu.end();
        infoScene.start(playerArmy.getMapNum());
    }
    
    public void cst1to0()
    {
        controlState = 0;
        save.end();
        hqMenu.resume();
    }
    
    public void cst2to0()
    {
        controlState = 0;
        invenScene.end();
        hqMenu.resume();
    }
    
    public void cst3to0()
    {
        controlState = 0;
        shopScene.end();
        hqMenu.resume();
    }
    
    public void cst4to0()
    {
        controlState = 0;
        skillsScene.end();
        hqMenu.resume();
    }
    
    public void cst6to0()
    {
        controlState = 0;
        infoScene.end();
        hqMenu.resume();
    }
    
    //</editor-fold>

    //<editor-fold desc="Getters and Setters">
    
    /**
     * @return the saveFile
     */
    public int getSaveFile() 
    {
        return saveFile;
    }
    
    //</editor-fold>
}
