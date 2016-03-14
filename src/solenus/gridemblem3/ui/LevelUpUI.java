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
public class LevelUpUI extends UI
{
    private BufferedImage ui;
    
    public int controlState;
    public Unit levelingUpUnit;
    int frameCount;
    final int levelUpAnimation = 90;
    final int plusOneAnimation = 20;
    boolean[] didItGoUp;
    int diguIndex;
    
    public LevelUpUI()
    {
        xLoc = -200;
        yLoc = -100;
        
        try
        {
            //load sheet image
            ui = ImageIO.read(new File("assets/ui/LevelUpUISheet.png"));
            
            //load info
            BufferedReader in = new BufferedReader(new FileReader("assets/ui/LevelUpUIAnimation.txt"));
            
            //get sprite dimensions
            width   = Integer.decode(in.readLine().substring(7));
            height  = Integer.decode(in.readLine().substring(8));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Level Up UI graphics failed to load. Problem, hoss");
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
            /*
            STATES
                0) Offline
                1) Opening Animation
                2) +1 Animations
                3) Press A to close
                4) Closing
            */
            switch(controlState)
            {
                case 2:
                case 3:
                    if(im.getA() == 1 || im.getB() == 1)
                        controlState ++;
                    break;
            }
        }
    }
    
    /**
     * 
     * @return The state
     */
    public int runFrame()
    {
        if(active)
        {
            /*
            STATES
                0) Offline
                1) Opening Animation
                2) +1 Animations
                3) Press A to close
                4) Closing
            */
            switch(controlState)
            {
                case 1:
                    frameCount++;
                    if(frameCount == levelUpAnimation)
                        cst1to2();
                    break;
                    
                case 2:
                    //Functional first, flare later
                    /*
                    if(diguIndex < didItGoUp.length)
                    {
                        if(didItGoUp[diguIndex])
                        {
                            frameCount++;
                            if(frameCount == plusOneAnimation)
                            {
                                diguIndex++;
                            }
                        }
                    }
                    else
                    */
                    controlState = 3;
                    break;
                    
                case 4:
                    return 1;
            }
        }
        
        return -1;
    }
    
    /**
     * Draws to the graphics.
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            switch(controlState)
            {
                case 1:
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
                    g.setColor(Color.RED);
                    Rendering.renderTextAbsolute("LEVEL UP!", g, 0, 0, 0, 0, 1, 1 , 1);
                    break;
                case 2:
                    //TODO
                    break;
                case 3: 
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
                    g.setColor(Color.YELLOW);
                    
                    //Render UI
                    Rendering.renderAbsolute(ui, g, xLoc, yLoc, centerX, centerY, 1, 1);
                    
                    //Render Level
                    Rendering.renderTextAbsolute(Integer.toString(levelingUpUnit.getLevel()), g, xLoc + 260, yLoc + 42, centerX, centerY, 1, 1, 1);
                    
                    //Render Stats
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getHP()), g, xLoc + 113, yLoc + 100, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getStr()), g, xLoc + 113, yLoc + 181, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getMag()), g, xLoc + 113, yLoc + 261, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getSkill()), g, xLoc + 113, yLoc + 342, centerX, centerY, 1, 1, 1);

                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getSpd()), g, xLoc + 252, yLoc + 100, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getDef()), g, xLoc + 252, yLoc + 181, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getRes()), g, xLoc + 252, yLoc + 261, centerX, centerY, 1, 1, 1);
                    Rendering.renderTextAbsolute(Integer.toString((int)levelingUpUnit.getLuck()), g, xLoc + 252, yLoc + 342, centerX, centerY, 1, 1, 1);


                    
                    break;
            }       
                

        }
    }
    
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * sets up the level up
     */
    public void cst1to2()
    {
        controlState = 2;
        
        //record the before and after the level up values, and look for the differences.
        int[] before = new int[8];
        int[] after = new int[8];
        didItGoUp = new boolean[8];
        
        before[0] = (int)levelingUpUnit.getHP();
        before[1] = (int)levelingUpUnit.getStr();
        before[2] = (int)levelingUpUnit.getMag();
        before[3] = (int)levelingUpUnit.getSpd();
        before[4] = (int)levelingUpUnit.getSkill();
        before[5] = (int)levelingUpUnit.getDef();
        before[6] = (int)levelingUpUnit.getRes();
        before[7] = (int)levelingUpUnit.getLuck();
        
        //make the unit level up.
        levelingUpUnit.levelUp();
        
        after[0] = (int)levelingUpUnit.getHP();
        after[1] = (int)levelingUpUnit.getStr();
        after[2] = (int)levelingUpUnit.getMag();
        after[3] = (int)levelingUpUnit.getSpd();
        after[4] = (int)levelingUpUnit.getSkill();
        after[5] = (int)levelingUpUnit.getDef();
        after[6] = (int)levelingUpUnit.getRes();
        after[7] = (int)levelingUpUnit.getLuck();
        
        for(int i = 0; i<8; i++)
            didItGoUp[i] = (before[i] != after[i]);
        
        diguIndex = 0;
    }
    
    //</editor-fold>
    
    public void start(Unit lvl)
    {
        super.start();
        levelingUpUnit = lvl;
        controlState = 1;
        frameCount = 0;
    }
    
    public void end()
    {
        super.end();
        controlState = 0;
    }
    
}
