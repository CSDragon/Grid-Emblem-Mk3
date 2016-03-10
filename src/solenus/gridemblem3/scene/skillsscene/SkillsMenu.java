/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.skillsscene;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
class SkillsMenu extends GenericMenu
{
    private Unit selectedUnit;
    
    public SkillsMenu(Unit u)
    {
        //create the menu
        super(arrayListToString(u.getSkills()));
        selectedUnit = u;
        
        //Make sure it's exactly 5
        numCommands = Unit.SKILL_LIMIT;
    }
    
    /**
     * Refreshes the skill list.
     */
    public void refresh()
    {
        actions = arrayListToString(selectedUnit.getSkills());
    }
}
