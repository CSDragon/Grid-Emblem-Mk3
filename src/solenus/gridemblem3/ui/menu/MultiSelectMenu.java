/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public abstract class MultiSelectMenu<T> extends Menu
{
    public static final int CONFIRM = 1;
    
    protected boolean[] selected;
    protected T[] actions;
    
    
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
                cursorLoc = numCommands;
            if(cursorLoc > numCommands)
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
                if(cursorLoc == numCommands)
                {
                    resetTriggers();
                    return CONFIRM;
                }
                
                selected[cursorLoc] = !selected[cursorLoc];
            }
        }
        
        return NOTHING;
    }
    
    public void start(int numOptions)
    {
        super.start();
        numCommands = numOptions;
        selected = new boolean[numCommands];
    }
    
    public ArrayList<T> getSelectedObjects()
    {
        ArrayList<T> ret = new ArrayList<>();
        
        for(int i = 0; i < numCommands; i++)
            if(selected[i])
                ret.add(actions[i]);
        
        return ret;
    }
}
