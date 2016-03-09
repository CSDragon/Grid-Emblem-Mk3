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
    public SkillsMenu(ArrayList<String> skills)
    {
        //create the menu
        super(arrayListToString(skills));
        
        //Make sure it's exactly 5
        numCommands = Unit.SKILL_LIMIT;
    }
}
