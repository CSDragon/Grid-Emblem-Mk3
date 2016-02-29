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
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InventoryScene extends Scene
{
    public static final int BACK = -1;
    public static final int NOTHING = 0;
    
    private PlayerData data;
    
    private CharacterSelectMenu csm;
    private UnitInventoryMenu uim;
    private ConvoyInventoryUI ciui;
    private ItemActionsMenu iom;
    private TabbedInventoryScene tis;
    
    private BufferedImage blackout;
    private BufferedImage background;
    
    public InventoryScene()
    {
        super();
        
        tis = new TabbedInventoryScene();
        uim = new UnitInventoryMenu();
        
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
                2) Item management: Player
                3) Item management: ItemList
            */
            switch(controlState)
            {
                case 1:
                    csm.respondControls(im);
                    break;
                case 2:
                    uim.respondControls(im);
                    break;
                case 3:
                    tis.respondControls(im);
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
                2) Item management: Player
                3) Item management: ItemList
            */
            switch(controlState)
            {
                
                case 1:
                    int csmResult = csm.runFrame();
                    switch(csmResult)
                    {
                        case CharacterSelectMenu.BACK:
                            return BACK;
                            
                        case CharacterSelectMenu.NOTHING:
                            break;
                            
                        default:
                            cst1to2(csm.getUnitAt(csmResult));
                            break;
                    }
                    break;
                
                case 2:
                    switch(uim.runFrame())
                    {
                        case UnitInventoryMenu.BACK:
                            cstXto1();
                            break;
                    }
                    break;
                    
                case 3:
                    switch(tis.runFrame())
                    {
                        case TabbedInventoryScene.BACK:
                            cstXto1();
                            break;
                    }
                    break;
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
            csm.animate();
            uim.animate();
            tis.animate();
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
            uim.draw(g);
            tis.draw(g);
        }
    }

    //</editor-fold>

    public void start(PlayerData pd)
    {
        super.start();
        data = pd;
        
        controlState = 1;
        csm = new CharacterSelectMenu(data.getUnitList());
        csm.start();
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"

    public void cst1to2(Unit u)
    {
        controlState = 2;

        uim = new UnitInventoryMenu(u, -200, -100);
        uim.start();
        csm.end();
        tis.start(data.getAllWeapons(), data.getAllItems());
    }
    
    public void cstXto1()
    {
        controlState = 1;
        uim.end();
        tis.end();
        csm.resume();
    }

    //</editor-fold>

}

