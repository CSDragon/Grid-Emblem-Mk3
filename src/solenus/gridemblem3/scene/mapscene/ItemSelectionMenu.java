/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.ui.menu.GenericMenu;


/**
 *
 * @author Chris
 */
public class ItemSelectionMenu extends GenericMenu
{
    private Unit selectedUnit;
    
    private ArrayList<Usable> itemList;
    private Usable activeItem;
    
    /**
     * Makes the menu respond to game logic
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
                activeItem = itemList.get(cursorLoc);
                resetTriggers();
                return 1;
            }
        }
        
        return NOTHING;
    }
    

    public void start(Unit u)
    {
        super.start();
        selectedUnit = u;
        checkItems();
    }
    
    /**
     * Finds what weapons the unit can use to fight.
     */
    public void checkItems()
    {
        itemList = selectedUnit.getInventory();
        numCommands = itemList.size();
        actions = new String[numCommands];
        for(int i = 0; i<numCommands; i++)
            actions[i] = itemList.get(i).getName();
    }
    

    public Usable getItem()
    {
        return activeItem;
    }
    
}
