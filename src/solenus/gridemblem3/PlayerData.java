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
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;

/**
 * The data collection of your army. This should only be used for the HQ and for starting a mission. MapScene should NOT use Armies.
 * @author Chris
 */
public class PlayerData 
{
    private int mapNum;
    private boolean inBase;
    
    private ArrayList<Unit> unitList;
    private ArrayList<Weapon> weaponConvoy;
    private ArrayList<Usable> itemConvoy;
    
    public PlayerData()
    {
        unitList = new ArrayList<>();
        weaponConvoy = new ArrayList<>();
        itemConvoy = new ArrayList<>();
        
        mapNum = 1;
        inBase = true;
    }
    
    /**
     * Saves the file
     * @param saveNum The file number to save to 
     */
    public void saveFile(int saveNum)
    {
        File saves = new File("saves");
        saves.mkdir();
        
        File saveFile = new File("saves/"+saveNum+".sav");
        try
        {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            //Write the scenario information
            bw.write("Map Number: "+mapNum); bw.newLine();
            bw.write("In Base?: "+inBase); bw.newLine();
            bw.newLine();

            //Write the army
            bw.write("Army Size: "+unitList.size()); bw.newLine();
            bw.newLine();
            for(Unit u: unitList)
                u.save(bw);
            
            //Write the weapon convoy.
            bw.write("Num Weapons: "+weaponConvoy.size()); bw.newLine();
            bw.newLine();
            for(Weapon w: weaponConvoy)
                w.save(bw);
            
            //Write the item convoy
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
    
    /**
     * Loads the player's data from a file.
     * @param saveNum The file number we're loading the file from
     */
    public PlayerData(int saveNum)
    {
        this();
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("saves/"+saveNum+".sav"));
            
            //Get the map data
            mapNum = Integer.parseInt(in.readLine().substring(12));
            inBase = in.readLine().substring(10).equals("true");
            in.readLine();

            //Get the army data.
            int numUnits = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numUnits; i++)
                unitList.add(new Unit(in));
            
            //Get the weapon convoy data
            int numWeapons = Integer.parseInt(in.readLine().substring(13));
            in.readLine();
            
            for(int i = 0; i<numWeapons; i++)
                weaponConvoy.add(new Weapon(in));
        
            //Get the item convoy data.
            int numItems = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numItems; i++)
                itemConvoy.add(new Usable(in));
                
            
            in.close();
        }
        
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Unable to load this file. Something's wrong, and I don't know what. Sorry!");
        }
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
        File f = new File("saves");
        for(File fi: f.listFiles())
            System.out.println(fi.getName());
        
        /*
        PlayerData a = new PlayerData();
        Unit u = new Unit();
        u.addWeapon(new Weapon("Sheld",0,1,2,3,4,5,6,7,1,2));
        a.addUnit(u);
        a.addWeapon(new Weapon("Sord", 0, 1, 2, 3, 4, 5, 6, 7, 1, 2));
        a.saveFile(9);
        
        PlayerData b = PlayerData.loadFile(9);
        int i = 1;
        */
        
    }

    //<editor-fold desc="getters and setters">
    
    /**
     * @return the mapNum
     */
    public int getMapNum() 
    {
        return mapNum;
    }
    
    public void setMapNum(int mn)
    {
        mapNum = mn;
    }

    /**
     * @return the inBase
     */
    public boolean isInBase() 
    {
        return inBase;
    }
    
    public void setInBase(boolean ib)
    {
        inBase = ib;
    }
    
    //</editor-fold>
}
