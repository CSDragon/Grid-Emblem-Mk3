/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class XPBarUI extends UI
{
    private BufferedImage ui;
    
    private Unit receivingXP;
    
    private int startingXP;
    private int endingXP;
    private int cur;
    
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private int xpZeroX;
    private int xpZeroY;
    private int pixelsPerXP;
    private int barHeight;
    
    private int controlState;
    
    private int preWait = 5;
    private int preCounter;
    private int postWait = 20;
    private int postCounter;
    
    private boolean fastMode;
    
    private LevelUpUI lvl;
    
    public XPBarUI()
    {
        lvl = new LevelUpUI();
        
        try
        {
            //load sheet image
            ui = ImageIO.read(new File("assets/ui/XPBarUISheet.png"));
            
            //load info
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/XPBarUIAnimation.txt"));
            
            //get sprite dimensions
            width   = Integer.decode(in.readLine().substring(7));
            height  = Integer.decode(in.readLine().substring(8));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            xpZeroX  = Integer.decode(in.readLine().substring(11));
            xpZeroY = Integer.decode(in.readLine().substring(11));
            pixelsPerXP = Integer.decode(in.readLine().substring(15));
            barHeight = Integer.decode(in.readLine().substring(12));
            
            in.close();
            
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "XP bar UI graphics failed to load. Problem, hoss");
            System.out.println(e);
            System.exit(-1);
        }
    }
    
    /**
     * Responds to controls.
     * @param im the input manager
     */
    public void respondControls(InputManager im)
    {
        if(active)
        {
            fastMode = (im.getA() > 0);
            lvl.respondControls(im);
        }
    }
    
    /**
     * Progresses the gamestate of this object forward one frame
     * @return If it's done or not
     */
    public int runFrame()
    {
        if(active)
        {
            /*
            STATES
                0) Inactive
                1) Filling the bar
                2) Level Up
            */
            switch(controlState)
            {
                case 1:
                    if(preCounter < preWait)
                        preCounter++;
                    else if (cur < endingXP)
                    {
                        cur++;
                        if(receivingXP.receiveXP(1))
                            cst1to2();
                    }
                    else if (postCounter < postWait)
                        postCounter++;
                    else
                        controlState = 0;
                    return -1;
                    
                case 2:
                    if(lvl.runFrame() == 1)
                        cst2to1();
                    return -1;
            }
        }
        
        return 1;
    }
    
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rendering.renderAbsolute(ui, g, 0, 0, centerX, 0, 1, 1);
            Rendering.renderRectAbsolute(g, xpZeroX, xpZeroY, (cur%100 * pixelsPerXP), barHeight, Color.yellow, centerX, 0, 1, 1);
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            g.setColor(Color.white);
            Rendering.renderTextAbsolute("EXP", g, 40, centerY+6, centerX, 0, 1, 1, 1);
            Rendering.renderTextAbsolute(Integer.toString(cur%100), g, width-40, centerY+6, centerX, 0, 1, 1, 1);

            lvl.draw(g);
        }
    }
    
    public void start(Unit r, int high)
    {
        super.start();
        receivingXP = r;
        startingXP = r.getXP();
        endingXP = startingXP + high;
        cur = r.getXP();
        preCounter = 0;
        postCounter = 0;
        
        controlState = 1;
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"

    /**
     * Start the level up UI
     */
    public void cst1to2()
    {
        controlState = 2;
        lvl.start(receivingXP);
    }
    
    /**
     * We're done with the Level Up UI
     */
    public void cst2to1()
    {
        controlState = 1;
        lvl.end();
    }
    
    //</editor-fold>
}
