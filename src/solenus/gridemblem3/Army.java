/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;

/**
 * The data collection of your army. This should only be used for the HQ and for starting a mission. MapScene should NOT use Armies.
 * @author Chris
 */
public class Army 
{
    private ArrayList<Unit> unitList;
    private ArrayList<Weapon> weaponConvoy;
    private ArrayList<Usable> itemConvoy;
    
    public Army()
    {
        unitList = new ArrayList<>();
        weaponConvoy = new ArrayList<>();
        itemConvoy = new ArrayList<>();
    }
    
    public void saveArmy()
    {
        File graphics = new File("test.txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(graphics);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("Army Size: "+unitList.size()); bw.newLine();
            bw.newLine();
            for(Unit u: unitList)
                u.save(bw);
            
            bw.write("Num Weapons: "+weaponConvoy.size()); bw.newLine();
            bw.newLine();
            for(Weapon w: weaponConvoy)
                w.save(bw);
            
            bw.write("Num Items: "+itemConvoy.size()); bw.newLine();
            bw.newLine();
            for(Usable u: itemConvoy)
                u.save(bw);
            
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Unable to save this file. Something's wrong, and I don't know what. Sorry!");
        }
    }
    
    public static Army loadArmy()
    {
        Army a = new Army();
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("test.txt"));
            
            int numUnits = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numUnits; i++)
                a.addUnit(new Unit(in));
            
            int numWeapons = Integer.parseInt(in.readLine().substring(13));
            in.readLine();
            
            for(int i = 0; i<numWeapons; i++)
                a.addWeapon(new Weapon(in));
            
            int numItems = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numItems; i++)
                a.addItem(new Usable(in));
                
            
            in.close();
        }
        
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Unable to load this file. Something's wrong, and I don't know what. Sorry!");
        }
        
        return a;
        
    }
    
    /**
     * Adds a unit to the army.
     * @param u The unit to be added
     * @return Duplication check. Returns false if the unit already existed.
     */
    public boolean addUnit(Unit u)
    {
        if(unitList.contains(u))
            return false;
        unitList.add(u);
        return true;
    }
    
    /**
     * Adds an weapon to the convoy.
     * @param w The weapon to be added
     * @return Duplication check. Returns false if the item already existed.
     */
    public boolean addWeapon(Weapon w)
    {
        if(weaponConvoy.contains(w))
            return false;
        weaponConvoy.add(w);
        return true;
    }
    
    /**
     * Adds an item to the convoy.
     * @param u The item to be added
     * @return Duplication check. Returns false if the item already existed.
     */
    public boolean addItem(Usable u)
    {
        if(itemConvoy.contains(u))
            return false;
        itemConvoy.add(u);
        return true;
    }
    
    //TEST
    public static void main(String[] args) 
    {
        
        Army a = new Army();
        Unit u = new Unit();
        u.addWeapon(new Weapon("Sheld",0,1,2,3,4,5,6,7,1,2));
        a.addUnit(u);
        a.addWeapon(new Weapon("Sord", 0, 1, 2, 3, 4, 5, 6, 7, 1, 2));
   //     a.saveArmy();
        
        Army b = Army.loadArmy();
        int i = 1;
    }
}
