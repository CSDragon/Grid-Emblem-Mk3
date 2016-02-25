/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.ui.menu.PreBattleMenu;

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
    
    private PreBattleMenu pbm;
    
    public PreBattleScene()
    {
        pbm = new PreBattleMenu();
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
            */
            switch(controlState)
            {
                case 1:
                    pbm.respondControls(im);
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
            */
            switch(controlState)
            {
                case 1:
                    switch(pbm.runFrame())
                    {
                        case PreBattleMenu.START:
                            return START;
                        case PreBattleMenu.RETURNTOBASE:
                            return RETURNTOBASE;
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
        }
    }
    
    //</editor-fold>
    
    public void start()
    {
        super.start();
        controlState = 1;
        pbm.start();
    }
    
    
    
}