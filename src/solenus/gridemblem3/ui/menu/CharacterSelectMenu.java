/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class CharacterSelectMenu extends GenericScrollingMenu
{
    private ArrayList<Unit> units;
    
    /**
     * 
     * @param units 
     */
    public CharacterSelectMenu(ArrayList<Unit> units)
    {
        super(unitsToString(units), 5);
        this.units = units;
    }
    
    /**
     * 
     * @param u
     * @return 
     */
    public static String[] unitsToString(ArrayList<Unit> u)
    {
        String[] ret = new String[u.size()];
        
        for(int i = 0; i< u.size(); i++)
            ret[i] = u.get(i).getName();
        
        return ret;
    }
    
    /**
     * Gets the unit in units at location i.
     * @param i The location to look
     * @return The unit at that location.
     */
    public Unit getUnitAt(int i)
    {
        return units.get(i);
    }
}
