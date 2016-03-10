/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.skillsscene;

import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class SkillsReserveScrollingMenu extends GenericScrollingMenu
{
    private Unit selectedUnit;

    public SkillsReserveScrollingMenu(Unit u, int ncv) 
    {
        super(arrayListToString(u.getSkillsReserve()), ncv);
        selectedUnit = u;
    }
    
    /**
     * Refreshes the skill list
     */
    public void refresh()
    {
        actions = arrayListToString(selectedUnit.getSkillsReserve());
        
        numCommands = actions.length;
        
        if(numCommands < numChoicesVisible)
            numCommands = numChoicesVisible;
    }
    
}
