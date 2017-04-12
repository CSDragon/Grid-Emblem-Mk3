/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class CombatMechanics 
{
    /**
     * Determines if the attacking unit can even hit the defending unit
     * @param a The unit attacking. Not necessarily attacker
     * @param attackingWeapon The weapon the attacking unit is using.
     * @param b The unit defending. Not necessarily defender
     * @return If an attack can be made.
     */
    public static boolean canAttack(Unit a, Weapon attackingWeapon, Unit b)
    {
        int dist = a.distanceTo(b);
        return (attackingWeapon.getMinRange() <= dist && attackingWeapon.getMaxRange() >= dist);
    }
    
    /**
     * Gets the chance a unit can hit its target
     * @param a The attacking Unit
     * @param attackingWeapon The weapon the attacking unit is using.
     * @param b The defending Unit
     * @param advantage The weapon triangle modifier.
     * @param m The map it takes place on.
     * @return The hit chance.
     */
    public static int hitChance(Unit a, Weapon attackingWeapon, Unit b, int advantage, Map m)
    {
        //Determine Hit Rate, Evasion, and Weapon Advantage.
        int hitRate = a.getTotalSkill()*2 + a.getTotalLuck() + attackingWeapon.getHit();
        int evade = b.getTotalSpd()*2 + b.getTotalLuck() + m.getTerrainAtPoint(b.getCoord()).getEvade();
        // +/- 10 if advantaged or disadvantaged.
        int hit = hitRate + advantage*10 - evade;
        
        //sanity check.
        if(hit < 0)
            hit = 0;
        if(hit > 100)
            hit = 100;
        
        return hit;
    }
    
    /**
     * Determines the crit chance
     * @param a The unit dealing damage (not necessarily attacker)
     * @param attackingWeapon The weapon the attacking unit is using.
     * @param b The unit taking damage (not necessarily defender)
     * @return The chance of a crit
     */
    public static int critChance(Unit a, Weapon attackingWeapon, Unit b)
    {
        //Determine crit chance
        int critRate = attackingWeapon.getCrit() + a.getTotalSkill()/2;
        int avoid = b.getTotalLuck();
        
        //sanity check
        int crit = critRate-avoid;
        if(crit < 0)
            crit = 0;
        if(crit > 100)
            crit = 100;
        
        return crit;
    }
    
    /**
     * Determines the amount of damage
     * @param a The unit dealing damage (not necessarily attacker)
     * @param attackingWeapon The weapon the attacking unit is using.
     * @param b The unit taking damage (not necessarily defender)
     * @param advantage The weapon triangle modifier.
     * @return The amount of damage taken
     */
    public static int determineDamage(Unit a, Weapon attackingWeapon, Unit b, int advantage)
    {
        //weapon damage, +/- 1 damage if advantaged/disadvantaged
        int attack = attackingWeapon.getDmg() + advantage;
        switch(attackingWeapon.getStrOrMag())
        {
            case 0:
                attack += a.getTotalStr();
                break;
            case 1:
                attack += a.getTotalMag();
                break;
            // case other? What else do we need? Half/half?
        }
        
        int defense = 0;
        //I need to have a better way of doing this...TODO
        switch(attackingWeapon.getWeaponType())
        {
            case 0:
            case 1:
            case 2:
            case 3:
                defense = (int)b.getTotalDef();
                break;
            case 4:
            case 5:
            case 6:
                defense = (int)b.getTotalRes();
                break;
        }
        
        if(defense > attack)
            return 0;
        else
            return attack - defense;
    }
    
    /**
     * Determines the weapon triangle advantage of two weapons. TODO
     * @param a The attacker's weapon
     * @param b The defender's weapon
     * @return The bonus the attacker gets;
     */
    public static int weaponAdvantage(Weapon a, Weapon b)
    {
        return Weapon.WEAPONADVANTAGE[a.getWeaponType()][b.getWeaponType()];
    }

    /**
     * Figures out how many attacks the attacker can make. This is for DISPLAY PURPOSES ONLY
     * No seriously, the way Fire Emblem works, this number is meaningless outside the battle preview panel.
     * TODO: Other ways of getting more attacks (brave weapons)
     * @param a The attacking Unit
     * @param attackingWeapon The weapon the attacking unit is using.
     * @param b The defending Unit
     * @param defendingWeapon The weapon the defending unit is using.
     * @return 
     */
    public static int numAttacksDisplay(Unit a, Weapon attackingWeapon, Unit b, Weapon defendingWeapon)
    {
        int numAttacks = 1;
        if(a.getSpd() - b.getSpd() + weaponAdvantage(attackingWeapon, defendingWeapon) >= 4)
            numAttacks *= 2;
        
        return numAttacks;
    }
}
