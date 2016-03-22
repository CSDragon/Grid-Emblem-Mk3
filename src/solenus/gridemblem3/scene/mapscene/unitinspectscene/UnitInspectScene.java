/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene.unitinspectscene;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;
import static solenus.gridemblem3.scene.Scene.NOTHING;

/**
 *
 * @author Chris
 */
public class UnitInspectScene extends Scene
{
    
    private BufferedImage UI;
    private Unit inspectedUnit;
    
    private boolean bPressed;
    
    private int parentControlState;
    private int centerY;
    
    public UnitInspectScene(Scene parent)
    {
        this.parent = parent;
        
        try
        {
            UI = ImageIO.read(new File("assets/ui/UnitInspectUI.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Unit Inspect UI failed to load. Did you change something?");
            System.exit(-1);
        }
        
        width = 320;
        height = 720;
        centerY = 360;
    }
    
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            bPressed = (im.getB() == 1);
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            if(bPressed)
                return BACK;
        }
        
        return NOTHING;
    }
    
    /**
     * animates the scene's objects 1 frame
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
            if(xLoc < 0)
                xLoc += 30;
            if(xLoc > 0)
                xLoc = 0;
        }
    }
    
    /**
     * Draws the scene
     * @param g 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rendering.renderAbsolute(UI, g, xLoc, 0, width, centerY, 2, 1);
            Rendering.renderAbsolute(inspectedUnit.getPortrait(), g, xLoc-10, 46, width, centerY, 2, 1);
            
            Font small = new Font(Font.SANS_SERIF, Font.BOLD, 12);
            Font medium = new Font(Font.SANS_SERIF, Font.BOLD, 16);
            Font large = new Font(Font.SANS_SERIF, Font.BOLD, 20);
            
            //Name
            g.setFont(large);
            Rendering.renderTextAbsolute(inspectedUnit.getName(), g, xLoc-66, 32, width, centerY, 2, 1, 1);
            
            //HP
            Rendering.renderTextAbsolute(inspectedUnit.getCurHP()+"", g, xLoc-146, 32, width, centerY, 2, 1, 1);
            Rendering.renderTextAbsolute(((int)inspectedUnit.getHP())+"", g, xLoc-183, 32, width, centerY, 2, 1, 1);

            //Level/XP
            g.setFont(small);
            Rendering.renderTextAbsolute(inspectedUnit.getLevel()+"", g, xLoc-276, 20, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute(inspectedUnit.getXP()+"", g, xLoc-254, 38, width, centerY, 2, 1, 2);
            
            //Move
            Rendering.renderTextAbsolute(inspectedUnit.getMove()+"", g, xLoc-294, 36, width, centerY, 2, 1, 1);

            //stats
            g.setFont(medium);
            Rendering.renderTextAbsolute((int)inspectedUnit.getStr()+"", g, xLoc-89, 366, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getMag()+"", g, xLoc-89, 384, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getSkill()+"", g, xLoc-89, 402, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getSpd()+"", g, xLoc-89, 420, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getLuck()+"", g, xLoc-89, 438, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getDef()+"", g, xLoc-89, 456, width, centerY, 2, 1, 2);
            Rendering.renderTextAbsolute((int)inspectedUnit.getRes()+"", g, xLoc-89, 474, width, centerY, 2, 1, 2);

            //Skills
            g.setFont(medium);
            for(int i = 0; i < inspectedUnit.getSkills().size(); i++)
                Rendering.renderTextAbsolute(inspectedUnit.getSkills().get(i), g, xLoc-129, 369 + 25*i, width, centerY, 2, 1, 0);
            
            //Weapons
            g.setFont(small);
            for(int i = 0; i < inspectedUnit.getWeaponInventory().size(); i++)
            {
                Weapon w = inspectedUnit.getWeaponInventory().get(i);
                Rendering.renderTextAbsolute(w.getName(), g, xLoc-30, 517+ 18*i, width, centerY, 2, 1, 0);
                Rendering.renderTextAbsolute(w.getDmg()+"", g, xLoc-154, 517+ 18*i, width, centerY, 2, 1, 1);
                Rendering.renderTextAbsolute(w.getHit()+"", g, xLoc-179, 517+ 18*i, width, centerY, 2, 1, 1);
                Rendering.renderTextAbsolute(w.getCrit()+"", g, xLoc-211, 517+ 18*i, width, centerY, 2, 1, 1);
                if(w.getMinRange() == w.getMaxRange())
                    Rendering.renderTextAbsolute(w.getMinRange()+"", g, xLoc-250, 517+ 18*i, width, centerY, 2, 1, 2);
                else
                    Rendering.renderTextAbsolute(w.getMinRange()+"-"+w.getMaxRange(), g, xLoc-256, 517+ 18*i, width, centerY, 2, 1, 2);
                Rendering.renderTextAbsolute(w.getCurUses()+"/"+w.getNumUses(), g, xLoc-304, 517+ 18*i, width, centerY, 2, 1, 2);
            }
            
            //Items
            for(int i = 0; i < inspectedUnit.getInventory().size(); i++)
            {
                Usable u = inspectedUnit.getInventory().get(i);
                Rendering.renderTextAbsolute(u.getName(), g, xLoc-30, 629+ 18*i, width, centerY, 2, 1, 0);
                Rendering.renderTextAbsolute(u.getCurUses()+"/"+u.getNumUses(), g, xLoc-302, 629+ 18*i, width, centerY, 2, 1, 2);
            }

        }
    }

    //</editor-fold>
    
    
    public void start(int parentControlState, Unit inspectedUnit)
    {
        super.start();
        this.inspectedUnit = inspectedUnit;
        
        bPressed = false;
        xLoc = -width;
        
        this.parentControlState = parentControlState;
    }
    
    
    //<editor-fold desc="Getters and Setters">
    
    /**
     * @return the parentControlState
     */
    public int getParentControlState() 
    {
        return parentControlState;
    }
    
    //</editor-fold>
}
