/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.topmenuscene;

import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class GraphicsMenu extends GenericMenu
{
    public static final int RES720P  = 0;
    public static final int RES900P  = 1;
    public static final int RES1080P = 2;
    public static final int WINDOWED = 3;
    public static final int BORDERLESS = 4;

    
    
    public GraphicsMenu()
    {
        super(new String[]{"720p", "900p", "1080p", "Windowed", "Borderless"});
    }
    
    /**
     * progresses the game
     * @return the exit status
     */
    public int runFrame()
    {
        if(active)
        {
            if(upTrigger)
                cursorLoc--;
            if(downTrigger)
                cursorLoc++;
            if(cursorLoc < 0)
                cursorLoc = numCommands-1;
            if(cursorLoc >= numCommands)
                cursorLoc = 0;

            //If B, exit the unit action box
            if(bTrigger)
            {
                resetTriggers();
                return BACK;
            }
            
            //If A,select which enemy to attack
            if(aTrigger)
            {
                resetTriggers();
                switch(cursorLoc)
                {
                    case RES720P:
                        GridEmblemMk3.setResolution(1280, 720);
                        break;
                    case RES900P:
                        GridEmblemMk3.setResolution(1600, 900);
                        break;
                    case RES1080P:
                        GridEmblemMk3.setResolution(1920, 1080);
                        break;
                    case WINDOWED:
                        GridEmblemMk3.setBorderless(false);
                        break;
                    case BORDERLESS:
                        GridEmblemMk3.setBorderless(true);
                        break;
                        
                }
                return cursorLoc;
            }
        }
        
        return NOTHING;
    }
    
}
