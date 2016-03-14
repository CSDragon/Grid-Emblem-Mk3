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
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */
public class TerrainUI extends UI
{
    private BufferedImage ui;
    
    private float opacity;
    
    private MapCursor cursor;
    private Map map;
    
    private Terrain activeTerrain;
    
    public TerrainUI()
    {
        controlStateVisibility = new HashSet<>(Arrays.asList(1,2,5,15));
        
        try
        {
            //load sheet image
            ui = ImageIO.read(new File("assets/ui/TerrainUI.png"));
            
            //load info
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/TerrainUIAnimation.txt"));
            
            //get sprite dimensions
            width   = Integer.decode(in.readLine().substring(7));
            height  = Integer.decode(in.readLine().substring(8));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            xLoc = Integer.decode(in.readLine().substring(6));
            yLoc = Integer.decode(in.readLine().substring(6));

        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Terrain UI graphics failed to load. Problem, boss");
            System.out.println(e);
            System.exit(-1);
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
            activeTerrain = map.getTerrainAtPoint(cursor.getCoord());
        }

        return 1;
    }
    
    /**
     * renders the scene.
     * @param cs This UI's owner's control state
     */
    public void animate(int cs)
    {   
        //always check this
        if(active)
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
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        //always check this
        if(visible)
        {
            Composite ac = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            
            Rendering.renderAbsolute(ui, g, xLoc, yLoc, 0, 0, 0, 0);
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            g.setColor(Color.WHITE);
            Rendering.renderTextAbsolute(activeTerrain.getName(), g, xLoc+centerX, yLoc+30, 0, 0, 0, 0, 1);

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.setColor(Color.YELLOW);
            Rendering.renderTextAbsolute(Integer.toString(activeTerrain.getEvade()), g, xLoc+75, yLoc+83, 0, 0, 0, 0, 1);
            Rendering.renderTextAbsolute(Integer.toString(activeTerrain.getDef()), g, xLoc+140, yLoc+83, 0, 0, 0, 0, 1);
            Rendering.renderTextAbsolute(Integer.toString(activeTerrain.getRes()), g, xLoc+205, yLoc+83, 0, 0, 0, 0, 1);
            
            g.setComposite(ac);
        }
    }
    
    /**
     * Starts the UI
     * @param c Check which terrain this cursor is on.
     * @param m Check it on this map.
     */
    public void start(MapCursor c, Map m)
    {
        super.start();
        cursor = c;
        map = m;
    }
}
