/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

import java.util.ArrayList;
import solenus.gridemblem3.party.PartyUnit;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class CharacterSelectMenu extends GenericScrollingMenu
{
    public static final int CONVOYMODE = -3;
    
    private ArrayList<PartyUnit> units;
    boolean convoyMode;
    
    /**
     * The standard constructor. 
     * @param units The list of units
     */
    public CharacterSelectMenu(ArrayList<PartyUnit> units)
    {
        this(units, false);
    }
    
     /**
     * 
     * @param units The list of units.
     * @param convoyMode Should it include a convoy.
     */
    public CharacterSelectMenu(ArrayList<PartyUnit> units, boolean convoyMode)
    {
        super(unitsToString(units, convoyMode), 5);
        this.units = units;
        this.convoyMode = convoyMode;
    }
    
    /**
     * Translate the list of units to a list of strings.
     * @param u The unit list.
     * @param convoyMode Whether or not we should include the convoy.
     * @return Weather or not the convoy should be included.
     */
    public static String[] unitsToString(ArrayList<PartyUnit> u, boolean convoyMode)
    {
        String[] ret;
        if(convoyMode)
        {
            ret = new String[u.size()+1];
            ret[u.size()]  = "Convoy";
        }
        else
            ret = new String[u.size()];

        for(int i = 0; i< u.size(); i++)
            ret[i] = u.get(i).getName();
        
        return ret;
    }
    
    /**
     * Gets the unit in units at location i.
     * @param i The location to look
     * @return The unit at that location.
     */
    public PartyUnit getUnitAt(int i)
    {
        if((convoyMode && i == units.size()) || i >= units.size())
            return null;
        return units.get(i);
    }
    
    /**
     * Gets the unit at the cursorLoc
     * @return The unit at that location
     */
    public PartyUnit getSelectedUnit()
    {
        return getUnitAt(cursorLoc);
    }
}
