/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.dialoguescene;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class InfoEvent 
{
    private String name;
    private String fileName;
    private ArrayList<Weapon> weaponRewards;
    private ArrayList<Usable> itemRewards;
    
    public InfoEvent(BufferedReader in) throws IOException
    {
        name = in.readLine().substring(7);
        fileName = in.readLine();
        
        weaponRewards = new ArrayList<>();
        int numWeapons = Integer.parseInt(in.readLine().substring(12));
        for(int i = 0; i<numWeapons; i++)
            weaponRewards.add(Weapon.loadFromPrefab(in.readLine()));
        
        itemRewards = new ArrayList<>();
        int numItems = Integer.parseInt(in.readLine().substring(10));
        for(int i = 0; i<numItems; i++)
            itemRewards.add(Usable.loadFromPrefab(in.readLine()));
      
        in.readLine();
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) 
    {
        this.name = name;
    }

    /**
     * @return the fileName
     */
    public String getFileName() 
    {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    /**
     * @return the weaponRewards
     */
    public ArrayList<Weapon> getWeaponRewards() 
    {
        return weaponRewards;
    }

    /**
     * @param weaponRewards the weaponRewards to set
     */
    public void setWeaponRewards(ArrayList<Weapon> weaponRewards) 
    {
        this.weaponRewards = weaponRewards;
    }

    /**
     * @return the itemRewards
     */
    public ArrayList<Usable> getItemRewards() 
    {
        return itemRewards;
    }

    /**
     * @param itemRewards the itemRewards to set
     */
    public void setItemRewards(ArrayList<Usable> itemRewards) 
    {
        this.itemRewards = itemRewards;
    }
    
}
