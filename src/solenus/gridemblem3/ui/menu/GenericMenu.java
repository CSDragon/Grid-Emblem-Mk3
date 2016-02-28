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
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.render.Rendering;

/**
 * This class is for testing purposes only.
 * No class should extend this class in the end.
 * @author Chris
 */
public abstract class GenericMenu extends Menu
{
    protected BufferedImage spriteSheet;
    protected BufferedImage cursor;
    protected BufferedImage box;
    
    protected int xLoc;
    protected int yLoc;
    protected int height;
    protected int width;
    protected int centerX;
    protected int centerY;
    
    protected String[] actions;
    
    
    public GenericMenu(String[] s)
    {
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/GenericMenuSheet.png"));

            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/GenericMenuAnimation.txt"));


            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "GenericMenu sprite failed to load. Why is something still using Generic Menu????");
            System.out.println(e);
            System.exit(-1);
        }

        box    = spriteSheet.getSubimage(0, 0, width, height);
        cursor = spriteSheet.getSubimage(0, height, width, height);

        actions = s;
        numCommands = actions.length;
    }
    
    /**
     * 
     * @param s The menu options
     * @param x The x distance AWAY FROM THE CENTER, the menu should be rendered at.
     * @param y The y distance AWAY FROM THE CENTER, the menu should be rendered at.
     */
    public GenericMenu(String[] s, int x, int y)
    {
        this(s);
        
        xLoc = x;
        yLoc = y;
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
            for(String s : actions)
            {
                Rendering.renderTextAbsolute(s, g, xLoc + 10, yLoc + textOffset, centerX, centerY, 1, 1, 0);
                textOffset += height;
            }
           
        }
    }
}
