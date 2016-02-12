/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

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


/**
 *
 * @author Chris
 */
public class WeaponSelectionMenu extends Menu
{
    private Unit selectedUnit;
    private Unit enemy;
    
    private ArrayList<Weapon> weaponList;
    private Weapon activeWeapon;
    
    private BufferedImage spriteSheet;
    private BufferedImage top;
    private BufferedImage mid;
    private BufferedImage bot;
    private BufferedImage cursor;
    private BufferedImage oneBox;
    
    private int height;
    private int width;
    private int centerX;
    private int centerY;
    
    /**
     * Standard constructor
     */
    public WeaponSelectionMenu()
    {
        try
        {
        //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/WeaponSelectSheet.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/WeaponSelectAnimation.txt"));
            
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Weapon Selection UI sprite failed to load. Did you change something?");
            System.out.println(e);
            System.exit(-1);
        }
     
        top    = spriteSheet.getSubimage(0,        0, width, height);
        mid    = spriteSheet.getSubimage(0,   height, width, height);
        bot    = spriteSheet.getSubimage(0, 2*height, width, height);
        cursor = spriteSheet.getSubimage(0, 3*height, width, height);
        oneBox = spriteSheet.getSubimage(0, 4*height, width, height);
            
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
                activeWeapon = weaponList.get(cursorLoc);
                resetTriggers();
                return 1;
            }
        }
        
        return NOTHING;
    }
    
    /**
     * Draw it TODO
     * @param g the graphics 
     */
    public void draw(Graphics2D g)
    {
        if(active)
        {
            if(numCommands > 1)
            {
                Rendering.renderAbsolute(top, g, 200, 0, width, height, 2, 1);
                for(int i = 1; i < numCommands-1; i++)
                    Rendering.renderAbsolute(mid, g, 200, -height*i, width, height, 2, 1);
                Rendering.renderAbsolute(bot, g, 200, -height*(numCommands-1), width, height, 2, 1);
                Rendering.renderAbsolute(cursor, g, 200, -height*cursorLoc, width, height, 2, 1);
            }
            
            else
            {
                Rendering.renderAbsolute(oneBox, g, 200, 0, width, height, 2, 1);
                Rendering.renderAbsolute(cursor, g, 200, 0, width, height, 2, 1);
            }
            
            int textOffset = 11;
            
            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);
            
            //render text
            for(Weapon w : weaponList)
            {
                Rendering.renderTextAbsolute(w.getName(), g, 190, textOffset, width, 0, 2, 1);
                textOffset -= height;
            }
            
            
        }
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
    }
    

    public Weapon getWeapon()
    {
        return activeWeapon;
    }
    
}
