/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class UnitInventoryMenu extends GenericMenu
{
    public UnitInventoryMenu()
    {
        super(new String[]{});
    }
    
    public UnitInventoryMenu(Unit u, int x, int y)
    {
        super(getInventories(u), x, y);
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
    
}
