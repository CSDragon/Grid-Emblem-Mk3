/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class PreBattleReport 
{
    private Map map;
    
    private boolean canAttackerAttack; //theoretically useless
    private int attackerHit;
    private int attackerDamage;
    private int attackerCrit;
    private int attackerAttacks;
    private int attackerAdvantage;
    
    private boolean canDefenderAttack;
    private int defenderHit;
    private int defenderDamage;
    private int defenderCrit;
    private int defenderAttacks;
    private int defenderAdvantage;
    
    private double expectedNetDamage;
    
    
    /**
     * Standard constructor
     * @param attacker The attacking unit
     * @param attackerWeapon The weapon the attacking unit is examining to attack with.
     * @param defender The defending unit
     * @param m The map
     */
    public PreBattleReport(Unit attacker, Weapon attackerWeapon, Unit defender, Map m)
    {
        canAttackerAttack = CombatMechanics.canAttack(attacker, attackerWeapon, defender);
        attackerAdvantage = CombatMechanics.weaponAdvantage(attackerWeapon, defender.getEquppedWeapon());
        attackerHit = CombatMechanics.hitChance(attacker, attackerWeapon, defender, attackerAdvantage, m);
        attackerDamage = CombatMechanics.determineDamage(attacker, attackerWeapon, defender, attackerAdvantage);
        attackerCrit = CombatMechanics.critChance(attacker, attackerWeapon, defender);
        if(canAttackerAttack)
            attackerAttacks = CombatMechanics.numAttacksDisplay(attacker, attackerWeapon, defender, defender.getEquppedWeapon()); 
        //else = 0 by initialization.
        
        canDefenderAttack = CombatMechanics.canAttack(defender, defender.getEquppedWeapon(), attacker);
        defenderAdvantage = CombatMechanics.weaponAdvantage(defender.getEquppedWeapon(), attackerWeapon);
        defenderHit = CombatMechanics.hitChance(defender, defender.getEquppedWeapon(), attacker, defenderAdvantage, m);
        defenderDamage = CombatMechanics.determineDamage(defender, defender.getEquppedWeapon(), attacker, defenderAdvantage);
        defenderCrit = CombatMechanics.critChance(defender, defender.getEquppedWeapon(), attacker);
        if(canDefenderAttack)
            defenderAttacks = CombatMechanics.numAttacksDisplay(defender, defender.getEquppedWeapon(), attacker, attackerWeapon);
        
        double attackerExpectedDamage = (attackerHit/100.0 * attackerDamage * (1+3*(attackerCrit/100.0))) * attackerAttacks;
        double defenderExpectedDamage = (defenderHit/100.0 * defenderDamage * (1+3*(defenderCrit/100.0))) * defenderAttacks;
        
        expectedNetDamage = attackerExpectedDamage - defenderExpectedDamage;
    }
    
    public static PreBattleReport selectBestReport(ArrayList<PreBattleReport> reports)
    {
        if(reports == null || reports.isEmpty())
            return null;
        
        int bestIndex = 0;
        double bestValue = reports.get(0).getExpectedNetDamage();
        for(int i = 1; i< reports.size(); i++)
        {
            if(reports.get(i).getExpectedNetDamage() > bestValue)
            {
                bestIndex = i;
                bestValue = reports.get(i).getExpectedNetDamage();
            }
        }
        
        return reports.get(bestIndex);
    }
    
    //<editor-fold desc="Getters and Setters">

    /**
     * @return the canAttackerAttack
     */
    public boolean isCanAttackerAttack() 
    {
        return canAttackerAttack;
    }

    /**
     * @return the attackerHit
     */
    public int getAttackerHit() 
    {
        return attackerHit;
    }

    /**
     * @return the attackerDamage
     */
    public int getAttackerDamage() 
    {
        return attackerDamage;
    }

    /**
     * @return the attackerCrit
     */
    public int getAttackerCrit() 
    {
        return attackerCrit;
    }

    /**
     * @return the attackerDouble
     */
    public int getAttackerAttacks() 
    {
        return attackerAttacks;
    }
    
    /**
     * @return the attackerAdvantage
     */
    public int getAttackerAdvantage() 
    {
        return attackerAdvantage;
    }


    /**
     * @return the canDefenderAttack
     */
    public boolean isCanDefenderAttack() 
    {
        return canDefenderAttack;
    }

    /**
     * @return the defenderHit
     */
    public int getDefenderHit() 
    {
        return defenderHit;
    }

    /**
     * @return the defenderDamage
     */
    public int getDefenderDamage() 
    {
        return defenderDamage;
    }

    /**
     * @return the defenderCrit
     */
    public int getDefenderCrit() 
    {
        return defenderCrit;
    }

    /**
     * @return the defenderDouble
     */
    public int getDefenderAttacks() 
    {
        return defenderAttacks;
    }

    /**
     * @return the defenderAdvantage
     */
    public int getDefenderAdvantage() 
    {
        return defenderAdvantage;
    }
    
    /**
     * @return the expectedNetDamage
     */
    public double getExpectedNetDamage() 
    {
        return expectedNetDamage;
    }
    
    //</editor-fold>
    
}
