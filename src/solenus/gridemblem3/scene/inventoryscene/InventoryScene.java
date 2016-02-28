/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InventoryScene extends Scene
{
    private PlayerData data;
    
    private CharacterSelectMenu csm;
    private UnitInventoryUI uiui;
    private ConvoyInventoryUI ciui;
    private ItemActionsMenu iom;
    
    private BufferedImage blackout;
    private BufferedImage background;
    
    public InventoryScene()
    {
        super();
        
        try
        {
            //load blackout and textbox images.
            blackout = ImageIO.read(new File("assets/ui/blackout.png"));
            background = ImageIO.read(new File("assets/ui/inventory/background.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Inventory border image failed to load. Did you change something?");
            System.exit(-1);
        }
    }
    
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            /*
            States:
                1) Character select menu
            */
            switch(controlState)
            {
                case 1:
                    csm.respondControls(im);
                    break;
            }
                
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            /*
            States:
                1) Character select menu
            */
            switch(controlState)
            {
                
                case 1:
                    switch(csm.runFrame())
                    {
                        
                    }
                    break;
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
            csm.animate();
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
            Rendering.renderAbsolute(blackout  , g, 0, 0, 960, 540, 1, 1);
            Rendering.renderAbsolute(background, g, 0, 0, 640, 360, 1, 1);
            csm.draw(g);
        }
    }

    //</editor-fold>

    public void start(PlayerData pd)
    {
        super.start();
        data = pd;
        
        controlState = 1;
        /*
        String[] names = new String[data.getUnitList().size()];
        for(int i = 0; i<data.getUnitList().size(); i++)
            names[i] = data.getUnitList().get(i).getName();
        */
        String[] names = new String[]{"A", "B", "B", "B", "B", "B", "B", "B", "B", "B"};
        csm = new CharacterSelectMenu(names);
        csm.start();
        
        
    }
    

}

