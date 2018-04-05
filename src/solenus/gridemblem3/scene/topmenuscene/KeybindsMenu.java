/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.topmenuscene;

import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class KeybindsMenu extends GenericMenu
{
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int A = 4;
    public static final int B = 5;
    public static final int X = 6;
    public static final int Y = 7;
    public static final int L = 8;
    public static final int R = 9;
    public static final int CONFIRM = 10;
    
    private int controlState;
    
    public KeybindsMenu()
    {
        super(new String[]{"Up", "Down", "Left", "Right", "A", "B", "X", "Y", "L", "R", "Confirm"});
    }
    
    /**
     * Responds to controls
     * @param in the input manager
     */
    public void respondControls(InputManager in)
    {
        if(active)
        {
            switch(controlState)
            {
                case 0:
                    upTrigger   = (in.getUp()   == 1);
                    downTrigger = (in.getDown() == 1);
                    aTrigger    = (in.getA()    == 1);
                    bTrigger    = (in.getB()    == 1);
                    break;
                    
                case 1:
                    switch(in.rebindKey(cursorLoc))
                    {
                        case InputManager.SUCCESS:
                            controlState = 0;
                            break;
                        case InputManager.FAILURE:
                            controlState = 0;
                            break;
                    }
                    break;
            }
        }
    }
    
    
    /**
     * progresses the game
     * @return the exit status
     */
    public int runFrame()
    {
        if(active)
        {
            switch(controlState)
            {
                //Normal functionality
                case 0:
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
                        if(cursorLoc != CONFIRM)
                            controlState = 1;
                        return cursorLoc;
                    }
                break;
                    
                //Key Rebind mode
                case 1:
                    
                    break;
                    
            }
        }
        
        return NOTHING;
    }
}
