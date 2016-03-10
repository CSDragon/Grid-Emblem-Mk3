/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.infoscene;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InfoScene extends Scene
{
    private int mapNum;
    private ArrayList<InfoEvent> events;

    public InfoScene()
    {
        super();
    }
    
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            switch(controlState)
            {
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * most scene subclasses must override this, and check if they are active.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            switch(controlState)
            {
            }
        }
        
        return -1;
    }
    
    /**
     * animates the scene's objects 1 frame
     * most scene subclasses must override this, and check if they are active.
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
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
        }
    }

    //</editor-fold>
    
    /**
     * Starts the scene up.
     * @param mapNum the map we're loading the events for.
     */
    public void start(int mapNum)
    {
        super.start();
        controlState = 1;
        
        this.mapNum = mapNum;
        events = new ArrayList<>();
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/levels/"+mapNum+"/events.txt"));
            
            int numEvents = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numEvents; i++)
                events.add(new InfoEvent(in));
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading Events for map "+ mapNum +"didn't work.");
            System.exit(-1);
        }
        
        int i = 0;
    }
}
