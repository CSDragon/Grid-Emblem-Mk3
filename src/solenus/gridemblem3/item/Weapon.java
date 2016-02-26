/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Weapons are objects of this class
 *
 * @author Chris
 */
public class Weapon extends Item
{
    //Weapon types
    public static final int SWORD = 0;
    public static final int LANCE = 1;
    public static final int AXE = 2;
    public static final int BOW = 3;
    public static final int DAGGER = 4;
    public static final int FIRE = 5;
    public static final int WIND = 6;
    public static final int LIGHTNING = 7;
    public static final int LIGHT = 8;
    public static final int DARK = 9;
    public static final int STAFF = 10;
    
    public static final int E = 0;
    public static final int D = 10;
    public static final int C = 2*D;
    public static final int B = 2*C;
    public static final int A = 2*B;
    public static final int S = 2*A;
      
    private int weaponType;
    private int strOrMag;
    private char masteryRequirement;
    private int dmg;
    private int maxUses;
    private int curUses;
    private int hit;
    private int weight;
    private int crit;
    private int minRange;
    private int maxRange;
    private ArrayList<String> specialEffects;
    
    public Weapon(String n, int value, int type, int som, char mr, int use, int d, int h, int w, int c, int minR, int maxR)
    {
        name = n;
        goldValue = value;
        weaponType = type;
        strOrMag = som;
        masteryRequirement = mr;
        maxUses = use;
        curUses = use;
        dmg = d;
        hit = h;
        weight = w;
        crit = c;
        minRange = minR;
        maxRange = maxR;
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
        masteryRequirement = in.readLine().charAt(21);
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
        bw.write("Strength or Magic: "+strOrMag); bw.newLine();
        bw.write("Mastery Requirement: "+masteryRequirement); bw.newLine();
        bw.write("Damage: "+dmg); bw.newLine();
        bw.write("Max Uses: "+maxUses); bw.newLine();
        bw.write("Cur Uses: "+curUses); bw.newLine();
        bw.write("Hit: "+hit); bw.newLine();
        bw.write("Weight: "+weight); bw.newLine();
        bw.write("Crit: "+crit); bw.newLine();
        bw.write("Min Range: "+minRange); bw.newLine();
        bw.write("Max Range: "+maxRange); bw.newLine();
        bw.write("Num Effects: "+specialEffects.size()); bw.newLine();
        for(int i = 0; i<specialEffects.size(); i++)
        {
            bw.write(specialEffects.get(0)); bw.newLine();
        }
        bw.newLine();
    }
    
    /**
     * Writes a weapon to a file
     * @param w The weapon to write to.
     */
    public static void writeToPrefab(Weapon w)
    {
        File saveFile = new File("assets/prefabs/weapons/"+w.getName()+".txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            w.save(bw);
            
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Weapon Prefab Generation failed");
        }
    }
    
    /**
     * Loads a weapon from a prefab.
     * @param name The weapon prefab to load
     * @return The loaded weapon.
     */
    public static Weapon loadFromPrefab(String name)
    {
        Weapon ret = null;
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/prefabs/weapons/"+name+".txt"));
            ret = new Weapon(in);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading weapon prefab "+ name +" failed, file doesn't seem to exist.");
            System.exit(-1);
        }
        
        return ret;
    }
        
    /**
     * Dulls the weapon
     */
    public void dull()
    {
        if(curUses == 1)
            name = name + " (broken)";
        
        if(curUses > 0)
            curUses -= 1;
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
