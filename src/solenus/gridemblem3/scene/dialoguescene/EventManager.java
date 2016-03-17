/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.dialoguescene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.scene.SceneManager;

/**
 *
 * @author Chris
 */
public class EventManager 
{
    private int mapNum;
    private boolean preHQEvent;
    private boolean midEvent;
    private boolean postBattleEvent;
    
    private int numInfoEvents;
    private ArrayList<InfoEvent> infoEvents;
    
    public EventManager(PlayerData pd)
    {
        mapNum = pd.getMapNum();
        infoEvents = new ArrayList<>();
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/levels/"+ mapNum +"/events.txt"));
            
            preHQEvent = Boolean.parseBoolean(in.readLine().substring(18));
            midEvent = Boolean.parseBoolean(in.readLine().substring(15));
            postBattleEvent = Boolean.parseBoolean(in.readLine().substring(17));
            in.readLine();
            
            numInfoEvents = Integer.parseInt(in.readLine().substring(11));
            in.readLine();
            
            for(int i = 0; i<numInfoEvents; i++)
                infoEvents.add(new InfoEvent(in));
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading Events for map "+ mapNum +"didn't work.");
            System.exit(-1);
        }
        
        pd.setupEvents(numInfoEvents);
    }
    
    /**
     * Grabs the filename for the event SceneManager needs for the current state.
     * @param controlState The controlstate of the scene manager
     * @return The filename of the dialogue we need.
     */
    public String getGameFlowEvent(int controlState)
    {
        switch(controlState)
        {
            case SceneManager.PREHQSKIT:
                return (mapNum+"-1");
            
            case SceneManager.MIDDLESKIT:
                return (mapNum+"-2");
                
            case SceneManager.POSTMAPSKIT:
                return (mapNum+"-3");

        }
        
       return "test"; 
    }

    /**
     * @return the preHQEvent
     */
    public boolean getPreHQEvent() 
    {
        return preHQEvent;
    }

    /**
     * @return the midEvent
     */
    public boolean getMidEvent() 
    {
        return midEvent;
    }

    /**
     * @return the postBattleEvent
     */
    public boolean getPostBattleEvent() 
    {
        return postBattleEvent;
    }

    /**
     * @return the numInfoEvents
     */
    public int getNumInfoEvents() 
    {
        return numInfoEvents;
    }

    /**
     * @return the infoEvents
     */
    public ArrayList<InfoEvent> getInfoEvents() 
    {
        return infoEvents;
    }
}
