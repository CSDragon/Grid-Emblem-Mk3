/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */
public class UnitHoverUI extends UI
{
    private BufferedImage ui;
    private float opacity;
    
    private MapCursor cursor;
    private MapScene parent;

    private Unit activeUnit;
    
    public UnitHoverUI()
    {
        controlStateVisibility = new HashSet<>(Arrays.asList(1,2,15));
        
        try
        {
            //load sheet image
            ui = ImageIO.read(new File("assets/ui/UnitHoverUI.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Unit Hover UI graphics failed to load. Problem, boss");
            System.out.println(e);
            System.exit(-1);
        }
        
        //get sprite dimensions
        width   = 250;
        height  = 100;
        centerX = 125;
        centerY = 50;
        xLoc = 10;
        yLoc = 10;
    }
    
    /**
     * renders the scene.
     * @param cs This UI's owner's control state
     */
    public void animate(int cs)
    {   
        //always check this
        if(active && activeUnit != null)
        {
            if(controlStateVisibility.contains(cs))
            {
                if(opacity != 1f)
                    opacity += 0.25f;
                if(opacity > 1f)
                    opacity = 1f;
            }
            else
            {
                if(opacity != 0f)
                    opacity -= 0.5f;
                if(opacity < 0f)
                    opacity = 0f;
            }
            
                
        }
    }
    
    /**
     * Runs the frame
     * @return That it worked. Which it always does. But this has to return an int because that's what runframe does.
     */
    public int runFrame()
    {
        if(active)
        {
            activeUnit = parent.getUnitAtPoint(cursor.getCoord());
        }

        return 1;
    }
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        //always check this
        if(visible && activeUnit != null)
        {
            Composite ac = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            
            Rendering.renderAbsolute(ui, g, xLoc, yLoc, width, 0, 2, 0);
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            g.setColor(Color.BLACK);
            Rendering.renderTextAbsolute(activeUnit.getName(), g, xLoc+80, yLoc+30, 0, 0, 2, 0, 1);

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);
            Rendering.renderTextAbsolute("Lv: " + activeUnit.getLevel(), g, xLoc+142, yLoc+58, 0, 0, 2, 0, 0);
            Rendering.renderTextAbsolute("HP: " + activeUnit.getCurHP() + " / " + (int)activeUnit.getHP(), g, xLoc+8, yLoc+58, 0, 0, 2, 0, 2);
            Rendering.renderTextAbsolute(activeUnit.getEquppedWeapon().getName(), g, xLoc+122, yLoc+86, 0, 0, 2, 0, 0);
            
            g.setComposite(ac);
        }
    }
    
    /**
     * Starts the UI
     * @param c The cursor.
     * @param m The mapscene which has the list of units.
     */
    public void start(MapCursor c, MapScene m)
    {
        super.start();
        cursor = c;
        parent = m;
    }
}
