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
public class PlayerData 
{
    private int mapNum;
    private boolean inBase;
    private int gold;
    
    private ArrayList<Unit> unitList;
    private ArrayList<Weapon> weaponConvoy;
    private ArrayList<Usable> itemConvoy;
    
    private ArrayList<Boolean> eventsWatched;
    
    /**
     * Constructs an empty player data.
     */
    public PlayerData()
    {
        unitList = new ArrayList<>();
        weaponConvoy = new ArrayList<>();
        itemConvoy = new ArrayList<>();
        eventsWatched = new ArrayList<>();
        
        mapNum = 1;
        inBase = false;
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

            //Get how much gold you have
            gold = Integer.parseInt(in.readLine().substring(6));
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
                
            int numEvents = Integer.parseInt(in.readLine().substring(12));
            for(int i = 0; i<numEvents; i++)
                eventsWatched.add(Boolean.parseBoolean(in.readLine()));
            
            in.close();
        }
        
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Unable to load this file. Something's wrong, and I don't know what. Sorry!");
        }
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

            //Write Gold
            bw.write("Gold: "+gold); bw.newLine();
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
            
            bw.write("Num Events: "+eventsWatched.size()); bw.newLine();
            for(Boolean b: eventsWatched)
            {
                bw.write(b.toString()); bw.newLine();
            }
            
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Unable to save this file. Something's wrong, and I don't know what. Sorry!");
        }
    }
    
    /**
     * Creates the default units
     */
    public void newGame()
    {
        mapNum = 1;
        inBase = false;
        gold = 30000;
        
        unitList.add(Unit.loadFromPrefab("Garen"));
        unitList.add(Unit.loadFromPrefab("Lux"));
        unitList.add(Unit.loadFromPrefab("Quinn"));
        unitList.add(Unit.loadFromPrefab("Xin Zhao"));
        
        //TEST
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Steel Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Dagger"));
        
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Sword"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Lance"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Axe"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Bow"));
        weaponConvoy.add(Weapon.loadFromPrefab("Iron Dagger"));
        
        itemConvoy.add(Usable.loadFromPrefab("Vulnerary"));
        itemConvoy.add(Usable.loadFromPrefab("Vulnerary"));
        itemConvoy.add(Usable.loadFromPrefab("Vulnerary"));
        itemConvoy.add(Usable.loadFromPrefab("Vulnerary"));
        itemConvoy.add(Usable.loadFromPrefab("Vulnerary"));

        eventsWatched = new ArrayList<>();
        eventsWatched.add(Boolean.FALSE);
        eventsWatched.add(Boolean.FALSE);
        eventsWatched.add(Boolean.FALSE);
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
        w.setOwner(null);
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
        u.setOwner(null);
        return true;
    }
    
    /**
     * When a new map is loaded, send the number of events to here.
     * @param numEvents The number of events
     */
    public void setEvents(int numEvents)
    {
        eventsWatched = new ArrayList<>();
        for(int i = 0; i<numEvents; i++)
            eventsWatched.add(Boolean.FALSE);
    }
    
    /**
     * Tells your save file you watched an event.
     * @param numEvent 
     */
    public void watchedEvent(int numEvent)
    {
        eventsWatched.set(numEvent, Boolean.TRUE);
    }
    
    /**
     * Removes a weapon from the convoy
     * @param w The weapon to be removed.
     * @return If the weapon was even in the convoy to begin with.
     */
    public boolean removeWeapon(Weapon w)
    {
        return weaponConvoy.remove(w);
    }
    
    /**
     * Removes a usable item from the convoy
     * @param u The weapon to be removed.
     * @return If the usable item was even in the convoy to begin with.
     */
    public boolean removeItem(Usable u)
    {
        return itemConvoy.remove(u);
    }
    
    //TEST
    public static void main(String[] args) 
    {
       
        Unit u = new Unit(0,6,1);
        Unit.writeToPrefab(u);
        
        /*
        PlayerData a = new PlayerData();
        ArrayList<Unit> unitList = new ArrayList<>();
        unitList.add(new Unit(0, 6, 1));
        unitList.get(0).addWeapon(new Weapon("Tome", 100, 0, 0, Weapon.LIGHT, 19, 10, 2, 100, 0 ,0, 1, 2));
        
                
        unitList.add(new Unit(0, 6, 1));
        unitList.get(1).addWeapon(new Weapon("Tome"  , 100, 0, 0, Weapon.LIGHT, 19, 10, 20, 100, 0 ,0, 1, 2));
        unitList.get(1).addWeapon(new Weapon("Bow"   , 100, 0, 0, Weapon.BOW  , 19, 10,  2, 100, 0 ,0, 2, 2));
        unitList.get(1).addWeapon(new Weapon("Sword" , 100, 0, 0, Weapon.SWORD, 19, 10,  2, 100, 0 ,0, 1, 1));
        
        a.addUnit(unitList.get(0));
        a.addUnit(unitList.get(1));
        
        a.saveFile(1);
        
        File f = new File("saves");
        for(File fi: f.listFiles())
            System.out.println(fi.getName());
        
        
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
    
    /**
     * Gets the first unit in your army with this name. Because of this, no units can have duplicate names.
     * @param name The name of the unit we're searching for.
     * @return The unit with that name.
     */
    public Unit getUnit(String name)
    {
        for(Unit u: unitList)
        {
            if(u.getName().equals(name))
                return u;
        }
        return null;
    }
    
    /**
     * Returns every weapon you have.
     * @return An ArrayList of every weapon you have.
     */
    public ArrayList<Weapon> getAllWeapons()
    {
        ArrayList<Weapon> ret = new ArrayList<>();
        
        ret.addAll(weaponConvoy);
        
        for(Unit u: unitList)
            ret.addAll(u.getWeaponInventory());
        
        return ret;
    }
    
    /**
     * Returns every item you have.
     * @return An ArrayList of every item you have.
     */
    public ArrayList<Usable> getAllItems()
    {
        ArrayList<Usable> ret = new ArrayList<>();
        
        ret.addAll(itemConvoy);
        
        for(Unit u: unitList)
            ret.addAll(u.getInventory());
        
        return ret;
    }

    /**
     * Checks if you can buy an item
     * @param i The item we're checking the price of
     * @return Whether we can buy it or not.
     */
    public boolean canBuyItem(Item i)
    {
        return (gold >= i.getGoldValue());
    }
    
    /**
     * Buys an item. But does not add it to an invnetory. 
     * @param i The item to be purchased. 
     * @return Whether you can actually buy the item in the first place
     */
    public boolean buyItem(Item i)
    {
        if(canBuyItem(i))
        {
            gold -= i.getGoldValue();
            return true;
        }
        else
            return false;
    }
        
    /**
     * Gives a Weapon to a Unit. 
     * @param u The unit getting the weapon. u can be null, in which case send it directly to the convoy.
     * @param weapon The weapon the unit is getting
     * @return Returns true is the weapon was accepted. False if it was sent to convoy because of inventory limits.
     */
    public boolean giveWeapon(Unit u, Weapon weapon)
    {
        if(u != null && u.addWeapon(weapon))
            return true;
        else
        {
            weaponConvoy.add(weapon);
            return false;
        }
    }
    
    /**
     * Gives a Usable to a Unit. 
     * @param u The unit getting the item
     * @param item The item the unit is getting
     * @return Returns true is the item was accepted. False if it was sent to convoy because of inventory limits.
     */
    public boolean giveItem(Unit u, Usable item)
    {
        if(u != null && u.addItem(item))
            return true;
        else
        {
            itemConvoy.add(item);
            return false;
        }
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
    
    /**
     * @return the unitList
     */
    public ArrayList<Unit> getUnitList() 
    {
        return unitList;
    }

    /**
     * @return the weaponConvoy
     */
    public ArrayList<Weapon> getWeaponConvoy() 
    {
        return weaponConvoy;
    }

    /**
     * @return the itemConvoy
     */
    public ArrayList<Usable> getItemConvoy() 
    {
        return itemConvoy;
    }
    
    public ArrayList<Boolean> getEventsWatched()
    {
        return eventsWatched;
    }
    
    /**
     * Returns the amount of gold you have
     */
    public int getGold()
    {
        return gold;
    }
    
    //</editor-fold>

}
