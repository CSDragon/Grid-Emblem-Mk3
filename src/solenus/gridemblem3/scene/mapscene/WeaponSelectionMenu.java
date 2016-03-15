/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.ui.menu.GenericMenu;


/**
 *
 * @author Chris
 */
public class WeaponSelectionMenu extends GenericMenu
{
    private Unit selectedUnit;
    private Unit enemy;
    
    private ArrayList<Weapon> weaponList;
    private Weapon activeWeapon;
    
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
                activeWeapon = weaponList.get(cursorLoc);
                resetTriggers();
                return 1;
            }
        }
        
        return NOTHING;
    }
    

    public void start(Unit u, Unit e)
    {
        super.start();
        selectedUnit = u;
        enemy = e;
        checkWeapons();
    }
    
    /**
     * Finds what weapons the unit can use to fight.
     */
    public void checkWeapons()
    {
        weaponList = selectedUnit.listEquipableWeaponsVsTarget(enemy);
        numCommands = weaponList.size();
        actions = new String[numCommands];
        for(int i = 0; i<numCommands; i++)
            actions[i] = weaponList.get(i).getName();
    }
    

    public Weapon getWeapon()
    {
        return activeWeapon;
    }
    
}
