/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.shopscene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.ui.menu.scrollingMenu.GenericScrollingMenu;

/**
 *
 * @author Chris
 */
public class ShopMenu extends GenericScrollingMenu
{
    private ArrayList<String> weapons;
    private ArrayList<String> usables;
    
    public ShopMenu(int shopNum)
    {
        super(8);
        
        weapons = new ArrayList<>();
        usables = new ArrayList<>();
        
        try
        {
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/shop/"+shopNum+".txt"));

            //Read in the weapons
            int weapNum = Integer.decode(in.readLine().substring(9));
            for(int i = 0; i<weapNum; i++)
                weapons.add(in.readLine());
            in.readLine();
            
            //read in the usables.
            int itemNum = Integer.decode(in.readLine().substring(7));
            for(int i = 0; i<itemNum; i++)
                usables.add(in.readLine());
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Could not load shop data. What did you do?");
            System.out.println(e);
            System.exit(-1);
        }
        
        numCommands = weapons.size()+usables.size();
        ArrayList<String> total = new ArrayList<>();
        total.addAll(weapons);
        total.addAll(usables);
        actions = total.toArray(new String[total.size()]);
        
        if(numChoicesVisible > numCommands)
            numChoicesVisible = numCommands;
    }
    
    /**
     * Gets an item from the shop.
     * @return The item to be made
     */
    public Item createItem()
    {
        Item i;
        if(cursorLoc < weapons.size())
            i = Weapon.loadFromPrefab(weapons.get(cursorLoc));
        else
            i = Usable.loadFromPrefab(usables.get(cursorLoc - weapons.size()));
            
        return i;
    }
}
