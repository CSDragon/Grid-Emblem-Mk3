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
 * This class is for testing purposes only.
 * No class should extend this class in the end.
 * @author Chris
 */
public abstract class GenericMenu extends Menu
{
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
    
    private String[] actions;
    
    
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


        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "GenericMenu sprite failed to load. Why is something still using Generic Menu????");
            System.out.println(e);
            System.exit(-1);
        }

        top    = spriteSheet.getSubimage(0,        0, width, height);
        mid    = spriteSheet.getSubimage(0,   height, width, height);
        bot    = spriteSheet.getSubimage(0, 2*height, width, height);
        cursor = spriteSheet.getSubimage(0, 3*height, width, height);
        oneBox = spriteSheet.getSubimage(0, 4*height, width, height);

        actions = s;
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
                    Rendering.renderAbsolute(mid, g, 0, -height*i, centerX, height, 1, 1);
                Rendering.renderAbsolute(bot, g, 0, -height*(numCommands-1), centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, -height*cursorLoc, centerX, height, 1, 1);
            }
            
            else
            {
                Rendering.renderAbsolute(oneBox, g, 0, 0, centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, 0, centerX, height, 1, 1);
            }
            
            int textOffset = 11;
            
            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);
            
            //render text
            for(String s : actions)
            {
                Rendering.renderTextAbsolute(s, g, -10, textOffset, centerX, 0, 1, 1);
                textOffset -= height;
            }
           
        }
    }
}
