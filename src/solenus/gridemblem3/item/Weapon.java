/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Weapons are objects of this class
 *
 * @author Chris
 */
public class Weapon extends Item
{
    //Weapon types
    private static final int SWORD = 0;
    private static final int AXE = 1;
    private static final int LANCE = 2;
    private static final int BOW = 3;
    private static final int LIGHT = 4;
    private static final int ELEMENTAL = 5;
    private static final int DARK = 6;
      
    private int weaponType;
    private int strOrMag;
    private int mastery;
    private int dmg;
    private int maxUses;
    private int curUses;
    private int hit;
    private int weight;
    private int crit;
    private int minRange;
    private int maxRange;
    
    public Weapon(String n, int type, int som, int m, int use, int d, int h, int w, int c, int minR, int maxR)
    {
        name = n;
        weaponType = type;
        strOrMag = som;
        mastery = m;
        maxUses = use;
        curUses = use;
        dmg = d;
        hit = h;
        weight = w;
        crit = c;
        minRange = minR;
        maxRange = maxR;
    }
    
    public void dull()
    {
        if(curUses == 1)
            name = name + " (broken)";
        
        if(curUses > 0)
            curUses -= 1;
    }
    

    /**
     * Writes a weapon to a file.
     * @param bw The file writer
     * @throws IOException 
     */
    public void save(BufferedWriter bw) throws IOException
    {
        bw.write("Name: "+name); bw.newLine();
        bw.write("Value: "+goldValue); bw.newLine();
        bw.write("Weapon Type: "+weaponType); bw.newLine();
        bw.write("Strenght or Magic: "+strOrMag); bw.newLine();
        bw.write("Mastery: "+mastery); bw.newLine();
        bw.write("Damage: "+dmg); bw.newLine();
        bw.write("Max Uses: "+maxUses); bw.newLine();
        bw.write("Cur Uses: "+curUses); bw.newLine();
        bw.write("Hit: "+hit); bw.newLine();
        bw.write("Weight: "+weight); bw.newLine();
        bw.write("Crit: "+crit); bw.newLine();
        bw.write("Min Range: "+minRange); bw.newLine();
        bw.write("Max Range: "+maxRange); bw.newLine();
        bw.newLine();
    }
    
    /**
     * Creates a new weapon from a text file
     * @param in The text we're reading from.
     * @throws IOException 
     */
    public Weapon(BufferedReader in) throws IOException
    {
        name = in.readLine().substring(6);
        goldValue = Integer.parseInt(in.readLine().substring(7));
        weaponType = Integer.parseInt(in.readLine().substring(13));
        strOrMag = Integer.parseInt(in.readLine().substring(19));
        mastery = Integer.parseInt(in.readLine().substring(9));
        dmg = Integer.parseInt(in.readLine().substring(8));
        maxUses = Integer.parseInt(in.readLine().substring(10));
        curUses = Integer.parseInt(in.readLine().substring(10));
        hit = Integer.parseInt(in.readLine().substring(5));
        weight = Integer.parseInt(in.readLine().substring(8));
        crit = Integer.parseInt(in.readLine().substring(6));
        minRange = Integer.parseInt(in.readLine().substring(11));
        maxRange = Integer.parseInt(in.readLine().substring(11));
        in.readLine();
    }

    
    //<editor-fold desc="Getters and Setters">
    
    /**
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the weaponType
     */
    public int getWeaponType()
    {
        return weaponType;
    }
    
    /**
     * @return strOrMag which will be 0 for str, 1 for mag, or something else for ???? (mixed damage?)
     */
    public int getStrOrMag()
    {
        return strOrMag;
    }

    /**
     * @return the mastery
     */
    public int getMastery() 
    {
        return mastery;
    }

    /**
     * @return the dmg
     */
    public int getDmg() 
    {
        if (curUses > 0)
            return dmg;
        else
            return dmg/2;
    }

    /**
     * @return the hit
     */
    public int getHit() 
    {
        return hit;
    }

    /**
     * @return the weight
     */
    public int getWeight()
    {
        return weight;
    }

    /**
     * @return the crit
     */
    public int getCrit() 
    {
        return crit;
    }

    /**
     * @return the minRange
     */
    public int getMinRange() 
    {
        return minRange;
    }

    /**
     * @return the maxRange
     */
    public int getMaxRange() 
    {
        return maxRange;
    }
    
    //</editor-fold>
}
