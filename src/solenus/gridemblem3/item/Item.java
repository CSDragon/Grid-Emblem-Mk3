/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.item;

import solenus.gridemblem3.actor.Unit;

/**
 * Abstract class for Items
 *
 * @author Chris
 */
public abstract class Item 
{
    protected String name;
    protected int goldValue;
    protected int numUses;
    protected int curUses;
    
    private Unit owner; 
    
    
    public String getName()
    {
        return name;
    }
    
    public int getGoldValue()
    {
        return goldValue;
    }
    
    /**
     * Returns the max number of uses. -1 if infinite uses.
     * @return The max number of uses.
     */
    public int getNumUses() 
    {
        return numUses;
    }
    
    /**
     * Returns the number of uses left. -1 if infinite uses.
     * @return The number of uses left.
     */
    public int getCurUses()
    {
        return curUses;
    }

    
    
    /**
     * @return the owner. RETURNS NULL IF THERE"S NO OWNER. BE PREPARED FOR THIS.
     */
    public Unit getOwner() 
    {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Unit owner) 
    {
        this.owner = owner;
    }
}
