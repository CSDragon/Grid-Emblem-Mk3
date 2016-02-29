/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */
public abstract class Menu extends UI
{
    public static final int BACK = -2;
    public static final int NOTHING = -1;
    
    protected int cursorLoc;
    protected int numCommands;
    
    protected boolean upTrigger;
    protected boolean downTrigger;
    protected boolean leftTrigger;
    protected boolean rightTrigger;
    protected boolean aTrigger;
    protected boolean bTrigger;
    
    
    /**
     * Responds to controls
     * @param in the input manager
     */
    public void respondControls(InputManager in)
    {
        if(active)
        {
            upTrigger   = (in.getUp()   == 1);
            downTrigger = (in.getDown() == 1);
            aTrigger    = (in.getA()    == 1);
            bTrigger    = (in.getB()    == 1);
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
                return cursorLoc;
            }
        }
        
        return NOTHING;
    }
    
    /**
     * Progresses all the sprites forward one frame.
     */
    public void animate()
    {
        if(active)
        {
            
        }
    }
    
    /**
     * Draws the menu
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            
        }
    }
    
    /**
     * Resets the triggers so there's no bugs.
     */
    public void resetTriggers()
    {
        upTrigger    = false;
        downTrigger  = false;
        leftTrigger  = false;
        rightTrigger = false;
        aTrigger     = false;
        bTrigger     = false;
    }
    
    /**
     * Starts up the menu
     */
    public void start()
    {
        super.start();
        cursorLoc = 0;
    }
    
    
    
    public int getCursorLoc()
    {
        return cursorLoc;
    }
    
    public void setCursorLoc(int cl)
    {
        cursorLoc = cl;
    }
    
}
