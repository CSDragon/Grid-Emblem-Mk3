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
public class HQMenu extends GenericMenu
{
    //shorthands
    public static final int INVENTORY = 0;
    public static final int SHOP = 1;
    public static final int SKILLS = 2;
    public static final int SUPPORT = 3;
    public static final int EVENTS = 4;
    public static final int SAVE = 5;
    public static final int END = 6;
    
    /**
     * Undo Generic Menu later.
     */
    public HQMenu()
    {
        super(new String[]{"Inventory", "Shop", "Skills", "Support", "Events", "Save", "End"});
    }
}
