/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */
public class StaffActionUI extends UI
{

    private int controlState;
    private Unit healer;
    private Unit healed;
    private int xDir;
    private int yDir;
    private FightHealthBarUI healedHealthBar;
    private int healerXP;
    private int frameCount;
    
    private boolean healedBarDone;
    private int animationLength = 16;
    private int noGraphicDamagePoint = 8;
    private int damagePoint;
   
    
    
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
            */
            switch(controlState)
            {
                case 0:
                    loadRecorces();
                    break;
                case 1:
                    cleanup();
                    return 1;
                case 3:
                    combat();
                    break;
                case 4:
                    watchAnimation();
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
            if(frameCount > noGraphicDamagePoint)
                healer.moveVisually(healer.getX() + xDir*(.05*(double)(frameCount-animationLength)), healer.getY() + yDir*(.05*(frameCount-animationLength)));
            else
                healer.moveVisually(healer.getX() - xDir*.05*frameCount, healer.getY() - yDir*.05*frameCount);

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
            healedHealthBar.draw(g);
        }
    }
    
        
    //</editor-fold>

    /**
     * Start up a fight
     * @param a The healer
     * @param d The healed
     * @param mode Full animation (true) or no animation (false).
     */
    public void start(Unit a, Unit d)
    {
        super.start();
        controlState = 0;
        healer = a;
        healed = d;
        
        //Clean the flags.
        int x = healer.getX() - healed.getX();
        if(x > 0)
            x = 1;
        else
            x = -1;
        healedHealthBar = new FightHealthBarUI(healed.getTotalHP(), healed.getCurHP(), -x);
    }
    
    public void loadRecorces()
    {
        controlState = 3;
        xDir = healer.getX() - healed.getX();
        yDir = healer.getY() - healed.getY();
        if(xDir > 1)
            xDir = 1;
        if(xDir < -1)
            xDir = -1;
        if(yDir > 1)
            yDir = 1;
        if(yDir < -1)
            yDir = -1;
            
    }
    
    /**
     * Cleans up, and awards XP TODO
     */
    public void cleanup()
    {
        //Just in case they're not back where they belong.
        healed.moveInstantly(healed.getCoord());
        
        //Award XP
        if(healer.getTeam() == 0)
        {
            int xp = 10;
            healerXP = xpMath(healer, healed, xp);
        }
    }
    
    /**
     * Determines how much XP a unit should get
     * @param a The attacking unit
     * @param b The defending unit
     * @param xp The base XP for the action
     * @return The amount of XP Unit a gets
     */
    public int xpMath(Unit a, Unit b, int xp)
    {
        float ret = xp;
        
        //if A is underleveled give it 20% more xp per level it was weaker than b.
        if(a.getLevel() < b.getLevel())
            ret = ret + (b.getLevel() - a.getLevel())*xp*.2f;
        return (int)ret;
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * Performs the combat and preps the animation
     */
    public void combat()
    {
        attack(healer,healed);
        
        
        
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
            healedBarDone = healedHealthBar.runFrame();
        }
        
        if(frameCount < animationLength)
            frameCount++;
        if(frameCount >= animationLength && healedBarDone)
            controlState = 1;
    }
    
    //</editor-fold>
    
    
    /**
     * Simulates 1 attack, from a to b
     * @param a The unit attacking. Not necessarily attacker
     * @param b The unit defending. Not necessarily defender
     */
    public void attack(Unit a, Unit b)
    {
        String staffType = healer.getEquppedWeapon().getSpecialEffects().get(0);

        int healingDone = 0;


        if(staffType.substring(0,7).equals("RodHeal"))
        {
            healingDone = Integer.parseInt(staffType.substring(7));
            healingDone += a.getMag()*0.5;
        }

        //Take Damage
        b.heal(healingDone);

        healedHealthBar.heal(healingDone);

        a.getEquppedWeapon().dull();
    }
    
    //<editor-fold desc="getters and setters">

    /**
     * @return the healer
     */
    public Unit getHealer() 
    {
        return healer;
    }

    /**
     * @return the healed
     */
    public Unit getHealed() 
    {
        return healed;
    }

    /**
     * @return the healedHealthBar
     */
    public FightHealthBarUI getHealedHealthBar() 
    {
        return healedHealthBar;
    }

    /**
     * @return the healerXP
     */
    public int getHealerXP() 
    {
        return healerXP;
    }

    /**
     * @return the frameCount
     */
    public int getFrameCount() 
    {
        return frameCount;
    }

    /**
     * @return the healedBarDone
     */
    public boolean isHealedBarDone() 
    {
        return healedBarDone;
    }

    /**
     * @return the animationLength
     */
    public int getAnimationLength() 
    {
        return animationLength;
    }

    /**
     * @return the noGraphicDamagePoint
     */
    public int getNoGraphicDamagePoint() 
    {
        return noGraphicDamagePoint;
    }

    /**
     * @return the damagePoint
     */
    public int getDamagePoint() 
    {
        return damagePoint;
    }
    
    //</editor-fold>
}
