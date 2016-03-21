/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene.unitinspectscene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;
import static solenus.gridemblem3.scene.Scene.NOTHING;

/**
 *
 * @author Chris
 */
public class UnitInspectScene extends Scene
{
    
    private BufferedImage UI;
    
    private boolean bPressed;
    
    private int parentControlState;
    private int centerY;
    private int xPos;
    
    public UnitInspectScene(Scene parent)
    {
        this.parent = parent;
        
        try{ UI = ImageIO.read(new File("assets/ui/UnitInspectUI.png"));}
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Unit Inspect UI failed to load. Did you change something?");
            System.exit(-1);
        }
        
        width = 320;
        height = 720;
        centerY = 360;
        
        xPos = 0;

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
            bPressed = (im.getB() == 1);
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
            if(bPressed)
                return BACK;
        }
        
        return NOTHING;
    }
    
    /**
     * animates the scene's objects 1 frame
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
            Rendering.renderAbsolute(UI, g, xLoc, 0, width, centerY, 2, 1);
        }
    }

    //</editor-fold>
    
    
    public void start(int parentControlState)
    {
        super.start();
        
        bPressed = false;
        
        this.parentControlState = parentControlState;
    }
    
    
    //<editor-fold desc="Getters and Setters">
    
    /**
     * @return the parentControlState
     */
    public int getParentControlState() 
    {
        return parentControlState;
    }
    
    //</editor-fold>
}
