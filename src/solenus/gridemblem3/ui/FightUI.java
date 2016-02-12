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
    
    int remainingFrames;
    
    private BufferedImage attackerSprite;
    private BufferedImage defenderSprite;


    
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
            //DUMMY TODO
            defender.takeDamage(determineDamage(attacker, defender));
            attacker.takeDamage(determineDamage(defender, attacker));
            remainingFrames--;
            
            if(remainingFrames == 0)
                return 0;
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
    
    
    //</editor-fold>
    
    
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
                a.getStr();
                break;
            case 1:
                a.getMag();
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
                defense = (int)b.getDef();
                break;
            case 4:
            case 5:
            case 6:
                defense = (int)b.getRes();
                break;
        }
        
        return attack - defense;
        
    }
    
    
}
