/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.gamemap;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.Point;

/**
 *
 * @author Chris
 */
public class Map 
{
    private int height;
    private int width;
    private int cursorStartX;
    private int cursorStartY;
    private String name;
    private int idNum;
    
    private BufferedImage mapImage;
    
    private ArrayList<Terrain> terrainList;
    
    public Map(int id)
    {
        idNum = id;
    
        try
        {
            mapImage = ImageIO.read(new File("assets/levels/"+id+"/map.png"));
            BufferedReader in = new BufferedReader(new FileReader("assets/levels/"+id+"/mapdata.txt"));
            String read;
            
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            cursorStartX = Integer.decode(in.readLine().substring(16));
            cursorStartY = Integer.decode(in.readLine().substring(16));
            
            //discard extra lines
            in.readLine();
            in.readLine();
            
            //read in terrain
            terrainList = new ArrayList();
            for(int i = 0; i<width*height;i++)
                terrainList.add(new Terrain(in.readLine()));
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Critical Error:\nMap no map found for Stage "+id+". I have no clue how, but ya bork'd ");
        }
        
        
    }

    
    
    
    
    //getters and setters
    
    /**
     * @return the height
     */
    public int getHeight() 
    {
        return height;
    }

    /**
     * @return the width
     */
    public int getWidth() 
    {
        return width;
    }
    
    /**
     * @return the starting location of the cursor on X
     */
    public int getCursorStartX()
    {
        return cursorStartX;
    }    
    
    /**
     * @return the starting location of the cursor on Y
     */
    public int getCursorStartY()
    {
        return cursorStartY;
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @return the idNum
     */
    public int getIdNum() 
    {
        return idNum;
    }

    /**
     * @return the mapImage
     */
    public BufferedImage getMapImage() 
    {
        return mapImage;
    }

    /**
     * Gets the terrain at point (x,y)
     * @param x x loc
     * @param y y loc
     * @return The terrain at x,y
     */
    public Terrain getTerrainAt(int x, int y)
    {
        try
        {
            return terrainList.get(y*width + x);
        }
        //indexOutOfBounds
        catch(Exception e)
        {
            return null;
        }
    }
    
    /**
     * gets the terrain at point p.
     * @param p the point on the map to look
     * @return the terrain at that point
     */
    public Terrain getTerrainAtPoint(Point p)
    {
        return getTerrainAt(p.x, p.y);
    }
}
