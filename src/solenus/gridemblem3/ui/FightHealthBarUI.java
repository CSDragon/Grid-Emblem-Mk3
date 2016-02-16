/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class FightHealthBarUI extends UI
{
    private int maxHP;
    private int lastHP;
    private int curHP;
    private int controlState;
    private int framesLeft;
    private int location;
    
    private BufferedImage spriteSheet;
    private BufferedImage ui;
    private BufferedImage emptyPip;
    private BufferedImage fullPip;
    
    private int uiWidth;
    private int uiHeight;
    private int pipWidth;
    private int pipHeight;
    
    /**
     * 
     * @param max The max hp the Unit has
     * @param cur The current HP the unit is at.
     * @param loc Where the heath bar shows up. -1 = left, 0 = center, 1 = right.
     */
    public FightHealthBarUI(int max, int cur, int loc)
    {
        super();
        
        maxHP = max;
        lastHP = cur;
        curHP = cur;
        visible = true;
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/FightHealthBarUISheet.png"));
        
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/FightHealthBarUIAnimation.txt"));
            
            //get sprite dimensions
            uiWidth   = Integer.decode(in.readLine().substring(7));
            uiHeight  = Integer.decode(in.readLine().substring(8));
            pipWidth  = Integer.decode(in.readLine().substring(7));
            pipHeight = Integer.decode(in.readLine().substring(8));
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Health bar UI graphics failed to load. Problem, hoss");
            System.out.println(e);
            System.exit(-1);
        }
        
        ui = spriteSheet.getSubimage(0, 0, uiWidth, uiHeight);
        emptyPip = spriteSheet.getSubimage(0, uiHeight, pipWidth, pipHeight);
        fullPip = spriteSheet.getSubimage(0, uiHeight+pipHeight, pipWidth, pipHeight);
        
        controlState = 0;
        
        location = loc*50;
    }
    
    /**
     * Progresses the element forward 1 frame.
     * @return The resultant state.
     */
    public int runFrame()
    {
        if(active)
        {
            /*
            STATES:
                1) Idle
                2) Animating
            */
            if(controlState == 2)
            {
                framesLeft--;
                if(framesLeft == 0)
                    return 1;
            }
        }
        
        return -1;
    }
    
    /**
     * renders the scene.
     */
    public void renderFrame()
    {   
        //always check this
        if(active)
        {
        }
    }
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        //always check this
        if(visible)
        {
            Rendering.renderAbsolute(ui, g, 0, -200, 0, 0, 1, 1);
            
            //render empty pips
            for(int i = curHP; i<maxHP; i++)
            {
                //render the top bar
                if(i<30)
                   Rendering.renderAbsolute(emptyPip, g, -30 -(pipWidth+1)*i, -205, 0, 0, 1, 1);
                else
                    Rendering.renderAbsolute(emptyPip, g, -30 -(pipWidth+1)*(i-30), -215, 0, 0, 1, 1);
            }
            
            //render full pips
            for(int i = 0; i<curHP; i++)
            {
                //render the top bar
                if(i<30)
                   Rendering.renderAbsolute(fullPip, g, -30 -(pipWidth+1)*i, -205, 0, 0, 1, 1);
                else
                    Rendering.renderAbsolute(fullPip, g, -30 -(pipWidth+1)*(i-30), -215, 0, 0, 1, 1);
            }
        }
    }
    
    public void start()
    {
        
    }

    
}
