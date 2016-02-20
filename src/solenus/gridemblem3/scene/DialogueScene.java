/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class DialogueScene extends Scene
{
    private BufferedImage blackout;
    private BufferedImage textbox;
    private BufferedImage background;
    
    public DialogueScene(Scene parent)
    {
        super(parent);
        
        try
        {
            //load blackout and textbox images.
            blackout = ImageIO.read(new File("assets/dialog/art/blackout.png"));
            textbox = ImageIO.read(new File("assets/dialog/art/textbox.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Dialog images failed to load. Did you change something?");
            System.exit(-1);
        }
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
            //always do this
            if(targetScene != null)
                targetScene.respondControls(im);
            
            
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
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rendering.renderAbsolute(blackout, g, 0, 0, 960, 540, 1, 1);
            Rendering.renderAbsolute(textbox , g, 0, 0, 640, 360, 1, 1);
        }
    }
            
    //</editor-fold>
    
    
}
