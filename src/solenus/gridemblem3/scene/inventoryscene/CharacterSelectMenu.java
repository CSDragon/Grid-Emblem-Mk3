/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.util.ArrayList;
import solenus.gridemblem3.ui.menu.GenericMenu;
import solenus.gridemblem3.ui.menu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class CharacterSelectMenu extends GenericScrollingMenu
{
    public CharacterSelectMenu(String[] unitNames)
    {
        super(unitNames, 5);
    }
 
    
    public void start()
    {
        super.start();
        
    }
}
