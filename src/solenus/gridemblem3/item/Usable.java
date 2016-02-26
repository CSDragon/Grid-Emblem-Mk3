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
 * 
 *
 * @author Chris
 */
public class Usable extends Item
{
    private int numUses;
    private int curUses;
    private ArrayList<String> effects;
    
    public Usable()
    {
        effects = new ArrayList<>();
    }
    
    /**
     * 
     * @param n Name of the item
     * @param value The worth of the item
     * @param u Number of uses the item contains
     * @param e The name of the effect to be looked up
     */
    public Usable(String n, int value, int u, ArrayList<String> e)
    {
        this();
        name = n;
        goldValue = value;
        numUses = u;
        curUses = u;
        effects = e;
    }    
    
    /**
     * Creates a new usable from a text file
     * @param in The text we're reading from.
     * @throws IOException 
     */
    public Usable(BufferedReader in) throws IOException
    {
        this();
        name = in.readLine().substring(6);
        goldValue = Integer.parseInt(in.readLine().substring(7));
        numUses = Integer.parseInt(in.readLine().substring(16));
        curUses = Integer.parseInt(in.readLine().substring(16));
        int numEffects = Integer.parseInt(in.readLine().substring(13));
        for(int i = 0; i<numEffects; i++)
        {
            effects.add(in.readLine());
        }
        in.readLine();
    }
    
    /**
     * Saves the Usable to a file.
     * @param bw The file writer.
     * @throws IOException 
     */
    public void save(BufferedWriter bw) throws IOException
    {
        bw.write("Name: "+name); bw.newLine();
        bw.write("Value: "+goldValue); bw.newLine();
        bw.write("Number of Uses: "+numUses); bw.newLine();
        bw.write("Remaining Uses: "+curUses); bw.newLine();
        bw.write("Num Effects: "+ effects.size()); bw.newLine();
        for(String s: effects)
        {
            bw.write(s); bw.newLine();
        }
        
        bw.newLine();
    }
    
        /**
     * Writes a usable to a file
     * @param u The usable to write to.
     */
    public static void writeToPrefab(Usable u)
    {
        File saveFile = new File("assets/prefabs/usables/"+u.getName()+".txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            u.save(bw);
            
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Usable Prefab Generation failed");
        }
    }
    
    /**
     * Loads a usable from a prefab.
     * @param name The usable prefab to load
     * @return The loaded usable.
     */
    public static Usable loadFromPrefab(String name)
    {
        Usable ret = null;
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/prefabs/usables/"+name+".txt"));
            ret = new Usable(in);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading item prefab "+ name +" failed, file doesn't seem to exist.");
            System.exit(-1);
        }
        
        return ret;
    }
    
    /**
     * Uses an item, does not check if it uses it up
     * @return the effect name the item will have
     */
    public ArrayList<String> use()
    {
        curUses --;
        return effects;
    }

    //<editor-fold desc="Getters and Setters">
    
    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @return the numUses
     */
    public int getNumUses() 
    {
        return numUses;
    }

    /**
     * @return the effect
     */
    public ArrayList<String> getEffects() 
    {
        return effects;
    }
    
    //</editor-fold>
    
}
