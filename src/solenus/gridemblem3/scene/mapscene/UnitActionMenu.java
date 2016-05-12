/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.util.ArrayList;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class UnitActionMenu extends GenericMenu
{
    public static final int ATTACK = 0;
    public static final int STAFF = 1;
    public static final int ITEM = 2;
    //Put any new commands ABOVE WAIT, and update the value of WAIT acordingly. WAIT should always be the last number.
    public static final int WAIT = 3;
    
    private Unit selectedUnit;
    private ArrayList<Unit> attackableUnits;
    private ArrayList<Unit> staffableUnits;
    private ArrayList<Integer> availCommands;
    private boolean attackFlag;
    private boolean staffFlag;
    private boolean itemFlag;
    
    
    /**
     * The Generic Constructor. If it calls this one, it must manually update actions and numCommands.
     */
    public UnitActionMenu()
    {
        super();
    }

    
    /**
     * Makes the menu respond to game logic
     * @return the exit status
     */
    public int runFrame()
    {
        if(active)
        {
            if(upTrigger)
                cursorLoc--;
            if(downTrigger)
                cursorLoc++;
            if(cursorLoc < 0)
                cursorLoc = numCommands-1;
            if(cursorLoc >= numCommands)
                cursorLoc = 0;

            //If B, exit the unit action box
            if(bTrigger)
            {
                resetTriggers();
                return BACK;
            }
            
            //If A,select which enemy to attack
            if(aTrigger)
            {
                resetTriggers();
                return availCommands.get(cursorLoc);
            }
        }
        
        return NOTHING;
    }
    
    /**
     * Draw it
     * @param g the graphics 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            for(int i = 0; i < numCommands; i++)
                Rendering.renderAbsolute(box, g, xLoc, yLoc + height*i, centerX, centerY, 1, 1);
            Rendering.renderAbsolute(cursor, g, xLoc, yLoc + height*cursorLoc, centerX, centerY, 1, 1);
            
            int textOffset = 19;
            
            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);
            
            //render text
            if(attackFlag)
            {
                Rendering.renderTextAbsolute("Attack", g, xLoc + 10, yLoc + textOffset, centerX, centerY, 1, 1, 0);
                textOffset += height;
            }
            
            if(staffFlag)
            {
                Rendering.renderTextAbsolute("Staff", g, xLoc + 10, yLoc + textOffset, centerX, centerY, 1, 1, 0);
                textOffset += height;
            }
            
            if(itemFlag)
            {
                Rendering.renderTextAbsolute("Item", g, xLoc + 10, yLoc + textOffset, centerX, centerY, 1, 1, 0);
                textOffset += height;
            }
            
                Rendering.renderTextAbsolute("Wait", g, xLoc + 10, yLoc + textOffset, centerX, centerY, 1, 1, 0);
            
            
        }
    }
    
        
    /**
     * Checks the the commands a unit can do TODO
     */
    public void checkCommands()
    {
        availCommands = new ArrayList();
        attackFlag = false;
        staffFlag = false;
        itemFlag = false;
        
        attackableUnits = Pathfinding.getAttackableObjects(selectedUnit, false);
        attackFlag = (attackableUnits.size() > 0);
        
        if(attackFlag)
            availCommands.add(ATTACK);

        staffableUnits = Pathfinding.getAttackableObjects(selectedUnit, true);
            staffFlag = (staffableUnits.size() > 0);

        if(staffFlag)
            availCommands.add(STAFF);
        
        itemFlag = (selectedUnit.getInventory().size() > 0);
        if(itemFlag)
            availCommands.add(ITEM);
        
        //OTHER COMMANDS, TODO
        
        
        //you ALWAYS can wait, waitFlag is always true.
        //if (waitFlag == true)
        availCommands.add(WAIT);
        
        numCommands = availCommands.size();
    }
    
    
    
    /**
     * get the UnitActionMenu ready to work on a unit.
     * @param u 
     */
    public void start(Unit u)
    {
        super.start();
        selectedUnit = u;
        attackableUnits = null;
        availCommands = null;
        checkCommands();
    }
    
    
    //<editor-fold desc="Getters and setters">
    
    
    public ArrayList<Unit> getAttackableUnits()
    {
        return attackableUnits;
    }
    
    public ArrayList<Unit> getStaffableUnits()
    {
        return staffableUnits;
    }
    
    //</editor-fold>
}
