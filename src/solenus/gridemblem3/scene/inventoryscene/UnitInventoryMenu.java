/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class UnitInventoryMenu extends GenericMenu
{
    public static final int BOTHMODE = 0;
    public static final int WEAPONMODE = 1;
    public static final int USABLEMODE = 2;
    
    private int mode;
    private Unit inventoryUnit;
    
    public UnitInventoryMenu()
    {
        super(new String[]{});
    }
    
    public UnitInventoryMenu(Unit u, int x, int y)
    {
        super(getInventories(u), x, y);
        
        inventoryUnit = u;
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
            
            switch(mode)
            {
                case BOTHMODE:
                    if(cursorLoc < 0)
                        cursorLoc = numCommands-1;
                    if(cursorLoc >= numCommands)
                        cursorLoc = 0;
                    break;
                case WEAPONMODE:
                    if(cursorLoc < 0)
                        cursorLoc = Unit.WEAPON_LIMIT-1;
                    if(cursorLoc >= Unit.WEAPON_LIMIT)
                        cursorLoc = 0;
                    break;
                case USABLEMODE:
                    if(cursorLoc < Unit.WEAPON_LIMIT)
                        cursorLoc = numCommands-1;
                    if(cursorLoc >= numCommands)
                        cursorLoc = Unit.WEAPON_LIMIT;
                    break;
            }
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
    
    public static String[] getInventories(Unit u)
    {
        String[] ret;
        
        ret = new String[Unit.WEAPON_LIMIT + Unit.INVENTORY_LIMIT];
        for(int i = 0; i< ret.length; i++)
            ret[i] = new String();
        
        for(int i = 0; i< u.getWeaponInventory().size(); i++)
            ret[i] = u.getWeaponInventory().get(i).getName();
        
        for(int i = 0; i< u.getInventory().size(); i++)
            ret[i+Unit.WEAPON_LIMIT] = u.getInventory().get(i).getName();
        
        return ret;
    }
    
    public Item getItemAt(int loc)
    {
        try
        {
            if(loc < Unit.WEAPON_LIMIT)
            {
                return inventoryUnit.getWeaponInventory().get(loc);
            }
            else
                return inventoryUnit.getInventory().get(loc-Unit.WEAPON_LIMIT);
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public void refresh()
    {
        actions = getInventories(inventoryUnit);
    }
    
    public void setMode(int mode)
    {
        this.mode = mode;
        switch(mode)
        {
            case 1:
                if(cursorLoc >= Unit.WEAPON_LIMIT)
                    cursorLoc = 0;
                break;
            case 2:
                if(cursorLoc < Unit.WEAPON_LIMIT)
                    cursorLoc = Unit.WEAPON_LIMIT;
        }
    }
}
