/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.party;

/**
 *
 * @author Chris
 */
public class PartyUnit 
{
    //inventory limits
    public static final int WEAPON_LIMIT = 5;
    public static final int INVENTORY_LIMIT = 5;
    public static final int SKILL_LIMIT = 5;
    
    //transport values
    public static final int ON_FOOT = 1;
    public static final int MOUNTED = 2;
    public static final int FLYING = 3;
    public static final int FLYING_MOUNT = 4;
    
    //Armor type values
    public static final int ROBES = 0;
    public static final int LIGHT = 1;
    public static final int MEDIUM = 2;
    public static final int HEAVY = 3;
    
    //Team values
    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int ALLY = 2;
    public static final int OTHER = 3;
}
