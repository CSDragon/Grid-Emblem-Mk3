/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.skillsscene;

import java.util.ArrayList;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class SkillsReserveScrollingMenu extends GenericScrollingMenu
{

    public SkillsReserveScrollingMenu(ArrayList<String> skills, int ncv) 
    {
        super(arrayListToString(skills), ncv);
    }
    
}
