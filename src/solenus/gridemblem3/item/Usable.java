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
 * 
 *
 * @author Chris
 */
public class Usable extends Item
{
    private int numUses;
    private String effect;
    
    /**
     * 
     * @param n Name of the item
     * @param u Number of uses the item contains
     * @param e The name of the effect to be looked up
     */
    public Usable(String n, int u, String e)
    {
        name = n;
        numUses = u;
        effect = e;
    }
    
    /**
     * Uses an item, does not check if it uses it up
     * @return the effect name the item will have
     */
    public String use()
    {
        numUses --;
        return effect;
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
        bw.write("Effect: "+effect); bw.newLine();
        bw.newLine();
    }
    
    /**
     * Creates a new usable from a text file
     * @param in The text we're reading from.
     * @throws IOException 
     */
    public Usable(BufferedReader in) throws IOException
    {
        name = in.readLine().substring(6);
        goldValue = Integer.parseInt(in.readLine().substring(7));
        numUses = Integer.parseInt(in.readLine().substring(16));
        effect = in.readLine().substring(7);
        in.readLine();
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
    public String getEffect() 
    {
        return effect;
    }
    
    //</editor-fold>
    
}
