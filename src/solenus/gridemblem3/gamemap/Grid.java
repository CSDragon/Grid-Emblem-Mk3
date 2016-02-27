/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.gamemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class Grid 
{
    private BufferedImage grid;
    private BufferedImage singleGrid;

    private boolean visible;
    
    public Grid(int w, int h)
    {
        visible = true; 
        
        try
        {
            singleGrid = ImageIO.read(new File("assets/ui/grid.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Grid sprite failed to load.");
            System.out.println(e);
            System.exit(-1);
        }
        
        int width = w * GridEmblemMk3.GRIDSIZE;
        int height = h * GridEmblemMk3.GRIDSIZE;
        
        grid = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = grid.createGraphics();
        
        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++)
                g2.drawImage(singleGrid, i*GridEmblemMk3.GRIDSIZE, j*GridEmblemMk3.GRIDSIZE, null);
        
        g2.dispose();
    }
    
    public void draw(Graphics2D g, Camera c)
    {
        if(visible)
            Rendering.renderGrid(grid, c, g, 0, 0, GridEmblemMk3.HALFGRIDSIZE, GridEmblemMk3.HALFGRIDSIZE);
    }
    
    

    /**
     * @return the visible
     */
    public boolean getVisible() 
    {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) 
    {
        this.visible = visible;
    }
}
