/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.item;

/**
 * 
 *
 * @author Chris
 */
public class Usable 
{
    private String name;
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
    
    
}
