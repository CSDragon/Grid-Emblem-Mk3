/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.gamemap.Map;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */

public class FightUI extends UI
{
    private boolean fastForwardFlag;
    private boolean graphics;
    
    private Unit attacker;
    private Unit defender;
    
    private int controlState;
    private int remainingFrames;
    
    private BufferedImage attackerSprite;
    private BufferedImage defenderSprite;

    private Map map;
    
    public FightUI(Map m)
    {
        map = m;
    }
    
    
    // <editor-fold desc="Control methods">
    
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            fastForwardFlag = (im.getA() > 0 || im.getB() > 0);
                
        }
    }
    
    /**
     * Progresses the fight
     * @return the result
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            /*
            States:
                0) Startup (Load resorces)
                1) Determine all damage
                2) Animations
                3) End
            */
            switch(controlState)
            {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    return 0;
            }
        }
        
        return -1;
    }
    
    /**
     * renders the scene.
     */
    public void renderFrame()
    {   
        //always check this
        if(active)
        {
        }
    }
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        //always check this
        if(visible)
        {
        }
    }
    
    /**
     * Start up a fight
     * @param a The attacker
     * @param d The defender
     */
    public void start(Unit a, Unit d)
    {
        super.start();
        attacker = a;
        defender = d;
        remainingFrames = 1;
    }
    

    //</editor-fold>
    
    //<editor-fold desc="Combat Mechanics">
    
    /**
     * Simulates 1 attack, from a to b
     * @param a The unit attacking. Not necessarily attacker
     * @param b The unit defending. Not necessarily defender
     * @return if the attack hit or not
     */
    public boolean attack(Unit a, Unit b)
    {
        //Determine Hit Rate, Evasion, and Weapon Advantage.
        int hitRate = a.getTotalSkill()*2 + a.getTotalLuck() + a.getEquppedWeapon().getHit();
        int evade = b.getTotalSpd()*2 + b.getTotalLuck() + map.getTerrainAtPoint(b.getCoord()).getEvade();
        int weaponAdvantage = weaponAdvantageAccuracy(a.getEquppedWeapon(), b.getEquppedWeapon());
        
        //Get the RNG
        int d100 = (int)Math.ceil(Math.random()*100);
        
        //Do we hit?
        boolean hit = d100 < (hitRate + weaponAdvantage - evade);
        
        if(hit)
        {
            //Deterimine raw damage
            int damage = determineDamage(a, b);
            
            //(Insert Damage Resist abilities)
            
            //Take Damage
            b.takeDamage(damage);
        }
        
        return hit;
    }
    
    
    /**
     * Determines the amount of damage
     * @param a The unit dealing damage (not necessarily attacker)
     * @param b The unit taking damage (not necessarily defender)
     * @return The amount of damage taken
     */
    public static int determineDamage(Unit a, Unit b)
    {
        int attack = a.getEquppedWeapon().getDmg();
        switch(a.getEquppedWeapon().getStrOrMag())
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
        switch(a.getEquppedWeapon().getWeaponType())
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
        
        return attack - defense;
    }
    
    /**
     * Determines the weapon triangle advantage of two weapons. TODO
     * @param a The attacker's weapon
     * @param b The defender's weapon
     * @return The bonus accuracy the attacker gets;
     */
    public static int weaponAdvantageAccuracy(Weapon a, Weapon b)
    {
        return 0;
    }
    
    /**
     * Determines the weapon triangle advantage of two weapons. TODO
     * @param a The attacker's weapon
     * @param b The defender's weapon
     * @return The bonus accuracy the defender gets.
     */
    public static int weaponAdvantageDamage(Weapon a, Weapon b)
    {
        return 0;
    }
    
    //</editor-fold>
    
    
}
