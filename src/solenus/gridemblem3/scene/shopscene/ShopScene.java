/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.shopscene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.ui.menu.CharacterSelectMenu;

/**
 *
 * @author Chris
 */
public class ShopScene extends Scene
{
    public static final int BACK = -2;
    public static final int NOTHING = -1;
    
    private PlayerData data;
    private CharacterSelectMenu csm;
    
    
    public ShopScene()
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
            /*
            STATES:
                1) Unit selection
                2) Buy stuff
                3) Sell stuff
            */
            switch(controlState)
            {
                case 1:
                    csm.respondControls(im);
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
                1) Unit selection
                2) Buy stuff
                3) Sell stuff
            */
            switch(controlState)
            {
                case 1:
                    switch(csm.runFrame())
                    {
                        case CharacterSelectMenu.BACK:
                            return BACK;
                        case CharacterSelectMenu.NOTHING:
                            break;
                        default:
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
}
