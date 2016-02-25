/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

/**
 *
 * @author Chris
 */
public class PrebattleMenu extends GenericMenu
{
    public static final int SELECTUNITS = 0;
    public static final int VIEWMAP = 1;
    public static final int INVENTORY = 2;
    public static final int SKILLS = 3;
    public static final int SAVE = 4;
    public static final int START = 5;
    public static final int RETURNTOBASE = 6;
    
    public PrebattleMenu()
    {
        super(new String[]{"Select Units", "View Map", "Inventory", "Skills", "Save", "Start", "Return to Base"});
    }
}
