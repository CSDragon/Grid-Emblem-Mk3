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
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class SystemActionMenu extends Menu
{
    public static final int UNITSTATS = 0;
    public static final int OPTIONS = 1;
    public static final int SAVE = 2;
    public static final int ENDTURN = 3;
    
    private String[] actions;
    
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
    
    public SystemActionMenu()
    {
        try
        {
        //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/SystemActionUISheet.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/SystemActionUIAnimation.txt"));
            
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "System Action UI sprite failed to load. Did you change something?");
            System.out.println(e);
            System.exit(-1);
        }
     
        top    = spriteSheet.getSubimage(0,        0, width, height);
        mid    = spriteSheet.getSubimage(0,   height, width, height);
        bot    = spriteSheet.getSubimage(0, 2*height, width, height);
        cursor = spriteSheet.getSubimage(0, 3*height, width, height);
        oneBox = spriteSheet.getSubimage(0, 4*height, width, height);
        
        actions = new String[]{"Units", "Options", "Save", "End Turn"};
        numCommands = actions.length;

            
    }
    

    
    
    /**
     * Draw it
     * @param g the graphics 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            if(numCommands > 1)
            {
                Rendering.renderAbsolute(top, g, 0, 0, centerX, height, 1, 1);
                for(int i = 1; i < numCommands-1; i++)
                    Rendering.renderAbsolute(mid, g, 0, height*i, centerX, height, 1, 1);
                Rendering.renderAbsolute(bot, g, 0, height*(numCommands-1), centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, height*cursorLoc, centerX, height, 1, 1);
            }
            
            else
            {
                Rendering.renderAbsolute(oneBox, g, 0, 0, centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, 0, centerX, height, 1, 1);
            }
            
            int textOffset = -11;
            
            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);
            
            //render text
            for(String s : actions)
            {
                Rendering.renderTextAbsolute(s, g, 10, textOffset, centerX, 0, 1, 1, 0);
                textOffset += height;
            }
           
        }
    }
    
    
}
