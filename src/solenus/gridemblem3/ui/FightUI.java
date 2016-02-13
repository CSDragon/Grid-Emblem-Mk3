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
    
    private int attackerHitFrames;
    private int attackerMissFrames;
    private int attackerCritSpecialFrames;
    
    private int defenderHitFrames;
    private int defenderMissFrames;
    private int defenderCritSpecialFrames;
    
    private int noGraphicFrames = 16;
    private int halfNoGraphicFrames = 8;
    
    private boolean attackerTurn;
    private boolean defenderTurn;
    
    private double xDir;
    private double yDir;

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
                1) End
                2) First attack.
            */
            switch(controlState)
            {
                case 0:
                    loadRecorces();
                    break;
                case 1:
                    cleanup();
                    return 0;
                case 2:
                    cst2to3();
                    break;
                case 3:
                    break;
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
            if(graphics)
            {
                
            }
            
            else
            {
                if(attackerTurn)
                {
                    if(remainingFrames > halfNoGraphicFrames)
                        attacker.moveVisually(attacker.getX() + xDir*(.05*(double)(remainingFrames-noGraphicFrames)), attacker.getY() + yDir*(.05*(remainingFrames-noGraphicFrames)));
                    else
                        attacker.moveVisually(attacker.getX() - xDir*.05*remainingFrames, attacker.getY() - yDir*.05*remainingFrames);
                }
                
                if(defenderTurn)
                {
                    if(remainingFrames > halfNoGraphicFrames)
                        defender.moveVisually(defender.getX() - xDir*(.05*(remainingFrames-noGraphicFrames)), defender.getY() - yDir*(.05*(remainingFrames-noGraphicFrames)));
                    else
                        defender.moveVisually(defender.getX() + xDir*.05*remainingFrames, defender.getY() + yDir*.05*remainingFrames);
                }
            }
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
            if(graphics)
            {
                
            }
            else
            {
                
            }
        }
    }
    
    /**
     * Start up a fight
     * @param a The attacker
     * @param d The defender
     * @param mode Full animation (true) or no animation (false).
     */
    public void start(Unit a, Unit d, boolean mode)
    {
        super.start();
        attacker = a;
        defender = d;
        remainingFrames = 1;
        graphics = mode;
    }
    

    //</editor-fold>
    
    public void loadRecorces()
    {
        controlState = 2;
        //If fight with graphics
        if(graphics)
        {
            //TODO
        }
        
        //Otw, no graphics.
        else
        {
            xDir = attacker.getX() - defender.getX();
            yDir = attacker.getY() - defender.getY();
            if(xDir > 1)
                xDir = 1;
            if(xDir < -1)
                xDir = -1;
            if(yDir > 1)
                yDir = 1;
            if(yDir < -1)
                yDir = -1;
            
        }
    }
    
    public void cleanup()
    {
        //Just in case they're not back where they belong.
        if(!graphics)
        {
            attacker.moveInstantly(attacker.getCoord());
            defender.moveInstantly(defender.getCoord());
        }
    }
    
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    public void cst2to3()
    {
        controlState = 1;//I know.
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="Combat Mechanics">
    
    /**
     * Simulates a battle between attacker and defender.
     */
    public void combat()
    {
        attack(attacker, defender);
        if(attacker.isDead() || defender.isDead())
        {
            return;
        }
        
        attack(defender, attacker);
        if(attacker.isDead() || defender.isDead())
        {
            return;
        }
        
        if(attacker.getTotalSpd() >= defender.getTotalSpd() + 4)
        {
            attack(attacker, defender);
            if(attacker.isDead() || defender.isDead())
            {
                return;
            }
        }
        
        else if(defender.getTotalSpd() >= attacker.getTotalSpd()+ 4)
        {
            attack(defender, attacker);
            if(attacker.isDead() || defender.isDead())
            {
                return;
            }
        }
        
        //Other stuff might happen, so don't remove those returns.
        
        
    }
    
    
    /**
     * Determines if a can even hit b
     * @param a The unit attacking. Not necessarily attacker
     * @param b The unit defending. Not necessarily defender
     * @return If an attack can be made.
     */
    public boolean canAttack(Unit a, Unit b)
    {
        //TODO Automatic weapon selection if at the wrong range.
        int dist = a.distanceTo(b);
        if(a.getEquppedWeapon().getMinRange() <= dist && a.getEquppedWeapon().getMaxRange() >= dist)
            return true;
        else
            return false;
    }
    
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
            
            a.getEquppedWeapon().dull();
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
