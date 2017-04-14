/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */

public class FightUI extends UI
{
    public static final int NOBODYDIED = 0;
    public static final int ATTACKERDIED = 1;
    public static final int DEFENDERDIED = 2;
    public static final int BOTHDIED = 3;
    
    public static final int ATTACKMISS = 0;
    public static final int ATTACKHIT = 1;
    public static final int ATTACKCRIT = 2;
    
    private boolean fastForwardFlag;
    private boolean graphics;
    
    private Unit attacker;
    private Unit defender;
    
    private int controlState;
    private int nextState;
    private int numAttacks;
    
    private BufferedImage attackerSprite;
    private BufferedImage defenderSprite;
    
    private int attackerHitFrames;
    private int attackerMissFrames;
    private int attackerCritFrames;
    private int attackerHitDamagePoint;
    private int attackerMissDamagePoint;
    private int attackerCritDamagePoint;
    
    private int defenderHitFrames;
    private int defenderMissFrames;
    private int defenderCritFrames;
    private int defenderHitDamagePoint;
    private int defenderMissDamagePoint;
    private int defenderCritDamagePoint;
    
    private int animationLength;
    private int damagePoint;
    private int frameCount;
    private boolean attackerBarDone;
    private boolean defenderBarDone;
    
    private int noGraphicFrames = 16;
    private int noGraphicDamagePoint = 8;
    
    //Control flags
    private int attackPhase; //0 = nothing yet, 1 = attacker, 2 = defender, 3 = speed
    private boolean attackerTurn;
    private boolean defenderTurn;
    private boolean attackerDied;
    private boolean defenderDied;
    private int attackerXP;
    private int defenderXP;
    private int returnValue;
    
    
    
    private double xDir;
    private double yDir;
    
    private int attackResult; //0 = miss, 1 = hit, 2 = crit
    private int attackDamage;

    private Map map;
    
    //UI elements
    private FightHealthBarUI attackerHealthBar;
    private FightHealthBarUI defenderHealthBar;
    
    public FightUI()
    {
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
                2) Determine who should attack next, and set up for it.
                3) Attack
                4) Animate
                5) Death check
                6) Unit Death
            */
            switch(controlState)
            {
                case 0:
                    loadRecorces();
                    break;
                case 1:
                    cleanup();
                    return returnValue;
                    
                case 2:
                    startAttack();
                    break;
                case 3:
                    combat();
                    break;
                case 4:
                    watchAnimation();
                    break;
                case 5:
                    deathCheck();
                    break;
                case 6:
                    killUnit();
                    break;
            }
        }
        
        return -1;
    }
    
    /**
     * renders the scene.
     */
    public void animate()
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
                    if(frameCount > noGraphicDamagePoint)
                        attacker.moveVisually(attacker.getX() + xDir*(.05*(double)(frameCount-noGraphicFrames)), attacker.getY() + yDir*(.05*(frameCount-noGraphicFrames)));
                    else
                        attacker.moveVisually(attacker.getX() - xDir*.05*frameCount, attacker.getY() - yDir*.05*frameCount);
                }
                
                if(defenderTurn)
                {
                    if(frameCount > noGraphicDamagePoint)
                        defender.moveVisually(defender.getX() - xDir*(.05*(frameCount-noGraphicFrames)), defender.getY() - yDir*(.05*(frameCount-noGraphicFrames)));
                    else
                        defender.moveVisually(defender.getX() + xDir*.05*frameCount, defender.getY() + yDir*.05*frameCount);
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
                attackerHealthBar.draw(g);
                defenderHealthBar.draw(g);
            }
        }
    }
    
    //</editor-fold>

    /**
     * Start up a fight
     * @param a The attacker
     * @param d The defender
     * @param mode Full animation (true) or no animation (false).
     * @param m The terrain map.
     */
    public void start(Unit a, Unit d, boolean mode, Map m)
    {
        super.start();
        map = m;
        controlState = 0;
        attacker = a;
        defender = d;
        graphics = mode;
        
        //Clean the flags.
        numAttacks = 0;
        
        attackPhase = 0;
        attackerTurn = false;
        defenderTurn = false;
        attackerDied = false;
        defenderDied = false;
        attackerXP = 0;
        defenderXP = 0;
        returnValue = NOBODYDIED;
        
        
        int x = attacker.getX() - defender.getX();
        if(x > 0)
            x = 1;
        else
            x = -1;
        attackerHealthBar = new FightHealthBarUI(attacker.getTotalHP(), attacker.getCurHP(),  x);
        defenderHealthBar = new FightHealthBarUI(defender.getTotalHP(), defender.getCurHP(), -x);
    }
    
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
    
    /**
     * Cleans up, and awards XP TODO
     */
    public void cleanup()
    {
        //Just in case they're not back where they belong.
        if(!graphics)
        {
            attacker.moveInstantly(attacker.getCoord());
            defender.moveInstantly(defender.getCoord());
        }
        
        //Award XP
        if(attacker.getTeam() == 0)
        {
            int xp = 10;
            if(defenderDied)
                xp+=20;
            attackerXP = xpMath(attacker, defender, xp);
        }
        
        if(defender.getTeam() == 0)
        {
            int xp = 10;
            if(attackerDied)
                xp+=20;
            defenderXP = xpMath(defender, attacker, xp);
        }
    }
    
    /**
     * Determines how much XP a unit should get
     * @param a The attacking unit
     * @param b The defnding unit
     * @param xp The base XP for the action
     * @return The amount of XP Unit a gets
     */
    public int xpMath(Unit a, Unit b, int xp)
    {
        float ret = xp;
        
        //If A is overleveled (more than 2 higher), reduce by 10% for each level too hight.
        if(a.getLevel() > b.getLevel()+2)
        {
            ret = ret - (a.getLevel() - b.getLevel() - 2)*xp*0.1f;
            if(ret < 1)
                ret = 1;
        }
        //if A is underleveled give it 20% more xp per level it was weaker than b.
        else if(a.getLevel() < b.getLevel())
            ret = ret + (b.getLevel() - a.getLevel())*xp*.2f;
        return (int)ret;
    }
    
    /**
     * Decides who gets xp. This model is a bit limited, as only one unit can get XP in a fight, but only player units should get XP,
     * there should be no XP in multiplayer, and player units should not attack other player units.
     * @return Weather attacker, defender or neither get XP.
     */
    public int isXPAwarded()
    {
        if(attackerXP > 0 && attacker.getTeam() == 0 && (returnValue != 1 && returnValue != 3))
            return 1;
        if(defenderXP > 0 && defender.getTeam() == 0 && (returnValue != 2 && returnValue != 3))
            return 2;
        
        return 0;
    }

    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * Starts up the attack.
     */
    public void startAttack()
    {
        //Nobody has attacked yet. The attacker goes first
        if(attackPhase == 0)
        {
            attackPhase = 1;
            attackerTurn = true;
            numAttacks = attacker.numAttacks();
            controlState = 3;
        }
        
        //Now it's the defender's turn
        else if(attackPhase == 1)
        {
            attackPhase = 2;
            attackerTurn = false;
            defenderTurn = true;
            numAttacks = defender.numAttacks();
            controlState = 3;
        }
        
        //Now the speed round
        else if(attackPhase == 2)
        {
            attackPhase = 3;
            attackerTurn = false;
            defenderTurn = false;
            
            int speedDef = attacker.getTotalSpd() - defender.getTotalSpd();
            
            //the attacker is fast enough. FIGHT
            if (speedDef >= 4)
            {
                attackerTurn = true;
                numAttacks = attacker.numAttacks();
                controlState = 3;
            }
            
            //the defender is fast enough. FIGHT
            else if(speedDef <= -4)
            {
                defenderTurn = true;
                numAttacks = defender.numAttacks();
                controlState = 3;
            }
            
            //they were the same-ish speed. No more fighting.
            else
                controlState = 1;
        }
        
        //We can't get to attack phase 4
        else
            controlState = 1;
    }
    
    /**
     * Performs the combat and preps the animation
     */
    public void combat()
    {
        numAttacks--;
        
        //Temp holding spots.
        Unit a = (attackerTurn) ? attacker : defender;
        Unit b = (attackerTurn) ? defender : attacker;
        
        //Perform the attack
        if(CombatMechanics.canAttack(a, a.getEquppedWeapon(), b))
        {
            attack(a,b);
        }
        
        //Set how long the animation is gonna last
        if(!graphics)
            animationLength = noGraphicFrames;
        else
        {
            if(attackerTurn)
                switch(attackResult)
                {
                    case ATTACKMISS:
                        animationLength = attackerMissFrames;
                        damagePoint = attackerMissDamagePoint;
                        break;
                    case ATTACKHIT:
                        animationLength = attackerHitFrames;
                        damagePoint = attackerHitDamagePoint;
                        break;
                    case ATTACKCRIT:
                        animationLength = attackerCritFrames;
                        damagePoint = attackerCritDamagePoint;
                        break;
                }
            else
                switch(attackResult)
                {
                    case ATTACKMISS:
                        animationLength = defenderMissFrames;
                        damagePoint = defenderMissDamagePoint;
                        break;
                    case ATTACKHIT:
                        animationLength = defenderHitFrames;
                        damagePoint = defenderHitDamagePoint;
                        break;
                    case ATTACKCRIT:
                        animationLength = defenderCritFrames;
                        damagePoint = defenderCritDamagePoint;
                        break;
                }
            
        }
        frameCount = 0;
        
        //and move on to the animation.
        controlState = 4;
    }
    
    /**
     * Makes crud wait for the animation to play out.
     */
    public void watchAnimation()
    {
        //Decrement the number of remaining frames. And when we're out of remaining frames move on.
        if(frameCount > damagePoint)
        {
            attackerBarDone = attackerHealthBar.runFrame();
            defenderBarDone = defenderHealthBar.runFrame();
        }
        
        if(frameCount < animationLength)
            frameCount++;
        if(frameCount >= animationLength && attackerBarDone && defenderBarDone)
            controlState = 5;
    }
    
    /**
     * Checks if anything died.
     */
    public void deathCheck()
    {
        attackerDied = attacker.isDead();
        defenderDied = defender.isDead();
        
        //if either have died, cut the battle short.
        if(attackerDied || defenderDied)
            controlState = 6;
        
        //If we have no more attacks, move on to the next phase
        else if(numAttacks == 0)
            controlState = 2;
        //If we do have attacks, go back to fighting.
        else
            controlState = 3;
    }
    
    public void killUnit()
    {
        if(attackerDied && defenderDied)
            returnValue = BOTHDIED;
        else if(attackerDied)
            returnValue = ATTACKERDIED;
        else if(defenderDied)
            returnValue = DEFENDERDIED;
        else
            returnValue = NOBODYDIED;
        
        controlState = 1;
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="Combat Mechanics">
    

    /**
     * Simulates 1 attack, from a to b
     * @param a The unit attacking. Not necessarily attacker
     * @param b The unit defending. Not necessarily defender
     */
    public void attack(Unit a, Unit b)
    {
        int weaponAdvantage = CombatMechanics.weaponAdvantage(a.getEquppedWeapon(), b.getEquppedWeapon());
        int d100; //Just makes things a bit easier to parse.

        //Do we hit?
        d100 = (int)Math.ceil(Math.random()*100);
        boolean hit = d100 < (CombatMechanics.hitChance(a, a.getEquppedWeapon(), b, weaponAdvantage, map));
        
        if(hit)
        {
            //Get the RNG
            d100 = (int)Math.ceil(Math.random()*100);
            
            //Deterimine raw damage
            attackDamage = CombatMechanics.determineDamage(a, a.getEquppedWeapon(), b, weaponAdvantage);
            
            //(Insert Damage Resist abilities)
            
            //Do we crit?
            boolean crit = d100 < CombatMechanics.critChance(a, a.getEquppedWeapon(), b);

            if(crit)
            {
                attackDamage = attackDamage*3;
                attackResult = ATTACKCRIT; 
            }
            else
                attackResult = ATTACKHIT;
            
            //Take Damage
            b.takeDamage(attackDamage);
            
            if(attackerTurn)
                defenderHealthBar.damage(attackDamage);
            else
                attackerHealthBar.damage(attackDamage);
            
            a.getEquppedWeapon().dull();
        }
        
        //we missed
        else
            attackResult = 0;
        
    }
    
    //</editor-fold>
    
    //<editor-fold desc="getters and setters">
    
    public Unit getAttacker()
    {
        return attacker;
    }
    
    public Unit getDefender()
    {
        return defender;
    }
    
    public int getAttackerXP()
    {
        return attackerXP;
    }
    
    public int getDefenderXP()
    {
        return defenderXP;
    }
    
    public int getWhoDied()
    {
        return returnValue;
    }
    
    //</editor-fold>
}
