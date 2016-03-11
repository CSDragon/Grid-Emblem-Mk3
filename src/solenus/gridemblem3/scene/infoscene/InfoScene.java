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
import solenus.gridemblem3.scene.DialogueScene;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InfoScene extends Scene
{
    private int mapNum;
    private ArrayList<InfoEvent> events;
    private InfoMenu infoMenu;
    private DialogueScene dialogueScene;
    
    private int activeEvent;
    
    public InfoScene()
    {
        super();
        
        dialogueScene = new DialogueScene(this);
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
                /*
                STATES
                    1) Info Menu
                */
                case 1:
                    infoMenu.respondControls(im);
                    break;
                    
                case 2:
                    dialogueScene.respondControls(im);
                    break;
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
                /*
                STATES
                    1) Info Menu
                */
                case 1:
                    switch(infoMenu.runFrame())
                    {
                        case InfoMenu.BACK:
                            return BACK;
                        
                        case InfoMenu.NOTHING:
                            break;
                        
                        default:
                            activeEvent = infoMenu.getCursorLoc();
                            cst1to2();
                            break;
                    }
                
                case 2:
                    switch(dialogueScene.runFrame())
                    {
                        case Scene.BACK:
                            cst2to1();
                            break;
                    }
            }
        }
        
        return NOTHING;
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
            infoMenu.animate();
            dialogueScene.animate();
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
            infoMenu.draw(g);
            dialogueScene.draw(g);
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
        
        infoMenu = new InfoMenu(events);
        infoMenu.start();
    }
    
    public void cst1to2()
    {
        controlState = 2;
        infoMenu.end();
        dialogueScene.start(events.get((activeEvent)).getFileName());
    }
    
    public void cst2to1()
    {
        controlState = 1;
        
        dialogueScene.end();
        infoMenu.resume();
    }
}
