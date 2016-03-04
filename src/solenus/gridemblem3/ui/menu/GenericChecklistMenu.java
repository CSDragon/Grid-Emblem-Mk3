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
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class GenericChecklistMenu extends ChecklistMenu<String>
{
    protected BufferedImage spriteSheet;
    protected BufferedImage cursor;
    protected BufferedImage box;
    protected BufferedImage selectedBox;
    protected BufferedImage confirmBox;
    
    public GenericChecklistMenu()
    {
        super();
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/GenericMultiSelectMenuSheet.png"));

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
            JOptionPane.showMessageDialog(null, "MultiSelectGenericMenu sprite failed to load.");
            System.out.println(e);
            System.exit(-1);
        }

        box         = spriteSheet.getSubimage(0,        0, width, height);
        selectedBox = spriteSheet.getSubimage(0,   height, width, height);
        cursor      = spriteSheet.getSubimage(0, 2*height, width, height); 
        confirmBox  = spriteSheet.getSubimage(0, 3*height, width, height);
    }
    
    public GenericChecklistMenu(int x, int y)
    {
        this();
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
            {
                if(selected[i])
                    Rendering.renderAbsolute(selectedBox, g, xLoc, yLoc + height*i, centerX, centerY, 1, 1);
                else
                    Rendering.renderAbsolute(box, g, xLoc, yLoc + height*i, centerX, centerY, 1, 1);

            }
            Rendering.renderAbsolute(confirmBox, g, xLoc, yLoc + height*numCommands, centerX, centerY, 1, 1);
            
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
    
    public void start(String[] s)
    {
        super.start(s.length);
        actions = s;
    }
    
    public void start(ArrayList<String> s)
    {
        super.start(s.size());
        actions = new String[numCommands];
        actions = (String[])s.toArray();
    }
}
