/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.gameoverscene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class GameOverScene extends Scene
{
    //public static final int NOTHING = -1; already defined
    public static final int RESTART = 1;
    
    private boolean restartTrigger;
    private BufferedImage gameOverTemp;
    
    public GameOverScene(Scene parent)//TEST: make this take an extra arguement, the map id.
    {
        super(parent);
        
        try
        {
            gameOverTemp = ImageIO.read(new File("assets/ui/gameOver.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Game Over image failed to load.");
            System.out.println(e);
            System.exit(-1);
        }

    }
    
    
        // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input 
     */
    @Override
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            if(im.getA() == 1)
            {
                restartTrigger = true;
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * most scene subclasses must override this, and check if they are active.
     * @return The state of this scene that the parent scene needs to know.
     */
    @Override
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            if(restartTrigger)
                return RESTART;
        }
        
        return NOTHING;
    }
    
    /**
     * animates the scene's objects 1 frame
     * most scene subclasses must override this, and check if they are active.
     */
    @Override
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
    @Override
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rendering.renderAbsolute(gameOverTemp, g, 0, 0, 0, 0, Rendering.CENTER, Rendering.CENTER);
        }
    }

    public void end()
    {
        super.end();
        restartTrigger = false;
    }
    
    //</editor-fold>
        
}
