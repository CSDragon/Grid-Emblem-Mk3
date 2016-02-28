/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu.scrollingMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public abstract class GenericScrollingMenu extends GenericMenu 
{
    protected int numChoicesVisible;
    protected int camY;
    protected int topY;
    
    public GenericScrollingMenu(String[] choices, int ncv)
    {
        super(choices);
        numChoicesVisible = ncv;
    }
    
    public GenericScrollingMenu(String[] choices, int ncv, int x, int y)
    {
        super(choices, x, y);
        numChoicesVisible = ncv;
        if(numChoicesVisible > choices.length)
            numChoicesVisible = choices.length;
    }
    
    /**
     * Responds to controls
     * @param in the input manager
     */
    public void respondControls(InputManager in)
    {
        if(active)
        {
            upTrigger   = (in.getUp()   %10 == 1);
            downTrigger = (in.getDown() %10 == 1);
            aTrigger    = (in.getA() == 1);
            bTrigger    = (in.getB() == 1);
        }
    }
    
    /**
     * progresses the game
     * @return the exit status
     */
    public int runFrame()
    {
        if(active)
        {
            if(upTrigger)
                cursorLoc--;
            if(downTrigger)
                cursorLoc++;
            
            if(cursorLoc == topY+numChoicesVisible-1 && topY != numCommands - numChoicesVisible)
                topY++;
            if(cursorLoc == topY && topY != 0)
                topY--;
            
            if(cursorLoc < 0)
            {
                cursorLoc = numCommands-1;
                topY = numCommands - numChoicesVisible; //No -1s for TopY because (nuCommands-1) - (numChoicesVisible-1) cancle out.
                camY = (topY) * height;
            }
            if(cursorLoc >= numCommands)
            {
                cursorLoc = 0;
                topY = 0;
                camY = 0;
            }

            //If B, exit the unit action box
            if(bTrigger)
            {
                resetTriggers();
                return BACK;
            }
            
            //If A,select which enemy to attack
            if(aTrigger)
            {
                resetTriggers();
                return cursorLoc;
            }
        }
        
        return NOTHING;
    }
    
    public void animate()
    {
        if(active)
        {
            /*
                It only scrolls if there are options left.
                    If it's in position 0 or 1, up scrolls up.
                    If it's in position n-1 or n-2, 
            */
            
            
            if(camY - topY*height > 3)
                camY -= 4;
            else if(camY - topY*height < -3)
                camY += 4;
            else
                camY = topY*height;
        }
    }
    
    /**
     * Draw it
     * @param g the graphics 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rectangle gr = g.getClipBounds();
            g.setClip(GridEmblemMk3.HALFWIDTH + xLoc - centerX,GridEmblemMk3.HALFHEIGHT + yLoc - height, width, height*numChoicesVisible);
            
            for(int i = 0; i < numCommands; i++)
                Rendering.renderAbsolute(box, g, 0, height*i - camY, centerX, centerY, 1, 1);
            Rendering.renderAbsolute(cursor, g, 0, height*cursorLoc - camY, centerX, centerY, 1, 1);

            int textOffset = 19;

            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);

            //render text
            for(String s : actions)
            {
                Rendering.renderTextAbsolute(s, g, 10, textOffset - camY, centerX, centerY, 1, 1, 0);
                textOffset += height;
            }
            
            g.setClip(gr);
        }
    }
}
