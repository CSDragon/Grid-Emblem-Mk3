/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.util.ArrayList;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class InventoryScrollingMenu extends GenericScrollingMenu 
{
    private ArrayList<Item> items;
    
    /**
     * Creates a scrolling menu for a convoy or shop inventory
     * @param items The items in stock.
     * @param mcv The number of commands the scrolling menu should show at any given time.
     * @param x The x location of this object relative TO THE CENTER OF THE WINDOW
     * @param y The Y location of this object relative TO THE CENTER OF THE WINDOW
     */
    public InventoryScrollingMenu(ArrayList<Item> items, int mcv, int x, int y)
    {
        super(itemsToNames(items), mcv, x ,y);
        
        this.items = items;
    }
    
    /**
     * Returns items.get(itemNum)
     * @param itemNum The index in items to look at.
     * @return the item stored at that index.
     */
    public Item getItem(int itemNum)
    {
        return items.get(itemNum);
    }
    
    /**
     * Takes the list of items and returns a GenericMenu-friendly string array.
     * @param items The list of items
     * @return The array of strings.
     */
    public static String[] itemsToNames(ArrayList<Item> items)
    {
        String[] ret = new String[items.size()];
        for(int i = 0; i<items.size(); i++)
            ret[i] = items.get(i).getName();
        
        return ret;
    }
}
