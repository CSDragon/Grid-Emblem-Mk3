/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.gamemap;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.actor.Unit;

/**
 *
 * @author Chris
 */
public class Terrain 
{
    private String name;
    
    private boolean impassable;
    private boolean landPassable;
    private boolean mountPassable;
    
    private int landCost;
    private int mountCost;
    private int airCost;
        
    private int evade;
    private int def;
    private int res;
    
    
    
    private String specialEffects;
    
    /**
     * Standard constructor. Loads in all the data for that type of terrain.
     * @param n the name of the terrain tile
     */
    public Terrain(String n)
    {
        name = n;
        
        try
        {
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/terrain/"+name+".txt"));
            String read;
            
            //get terrain stats
            impassable = (in.readLine().substring(12)).equals("true");
            landPassable = (in.readLine().substring(15)).equals("true");
            mountPassable = (in.readLine().substring(18)).equals("true");
            landCost = Integer.decode(in.readLine().substring(20));
            mountCost = Integer.decode(in.readLine().substring(23));
            airCost = Integer.decode(in.readLine().substring(19));
            evade = Integer.decode(in.readLine().substring(7));
            def = Integer.decode(in.readLine().substring(9));
            res = Integer.decode(in.readLine().substring(12));
            specialEffects = in.readLine().substring(17);
            
            in.close();
            
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, name +" terrain data failed to load. Did you change something?");
            System.exit(-1);
        }
    }
    
    
    public Terrain()
    {
        this("Grasslands");
    }
    
    /**
     * If this tile has special effects, run them.
     */
    public void processSpecialEffects()
    {
        //TODO
    }
    
    /* transport values
       ON_FOOT = 1;
       MOUNTED = 2;
       FLYING = 3;
       FLYING_MOUNT = 4;
    */
    
    /**
     * returns the move cost for a specific movement type
     * @param moveType the movement type of the moving unit.
     * @return returns the move cost of that movement type, or -1 if it's impassable
     */
    public int getMoveCost(int moveType)
    {
        if(impassable)
            return -1;
        
        if(moveType == Unit.ON_FOOT && landPassable)
            return landCost;
        else if(moveType == Unit.MOUNTED && mountPassable)
            return mountCost;
        else if(moveType == Unit.FLYING || moveType == Unit.FLYING_MOUNT)
            return airCost;
        
        else 
            return -1;
            
    }
    
    
    
    public String getName()
    {
        return name;
    }

    /**
     * @return the evade
     */
    public int getEvade() 
    {
        return evade;
    }

    /**
     * @return the def
     */
    public int getDef() 
    {
        return def;
    }

    /**
     * @return the res
     */
    public int getRes()
    {
        return res;
    }
    
}
