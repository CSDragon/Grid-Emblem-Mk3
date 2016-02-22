/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

/**
 *
 * @author Chris
 */
public class HQMenu extends GenericMenu
{
    /**
     * Undo Generic Menu later.
     */
    public HQMenu()
    {
        super(new String[]{"Inventory", "Shop", "Skills", "Support", "Conversations", "Save", "End"});
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
                    
                }
            }
        }
        
        return -1;
    }
    
}
