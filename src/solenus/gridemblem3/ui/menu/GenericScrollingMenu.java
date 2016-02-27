/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class GenericScrollingMenu extends GenericMenu 
{
    private int numChoicesVisible;
    
    public GenericScrollingMenu(String[] choices, int numChoicesVisible)
    {
        super(choices);
        this.numChoicesVisible = numChoicesVisible;
    }
    
    /**
     * Draw it
     * @param g the graphics 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            if(numCommands > 1)
            {
                Rendering.renderAbsolute(top, g, 0, 0, centerX, height, 1, 1);
                for(int i = 1; i < numCommands-1; i++)
                    Rendering.renderAbsolute(mid, g, 0, height*i, centerX, height, 1, 1);
                Rendering.renderAbsolute(bot, g, 0, height*(numCommands-1), centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, height*cursorLoc, centerX, height, 1, 1);
            }

            else
            {
                Rendering.renderAbsolute(oneBox, g, 0, 0, centerX, height, 1, 1);
                Rendering.renderAbsolute(cursor, g, 0, 0, centerX, height, 1, 1);
            }

            int textOffset = -11;

            //set font
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.BLACK);

            //render text
            for(String s : actions)
            {
                Rendering.renderTextAbsolute(s, g, 10, textOffset, centerX, 0, 1, 1, 0);
                textOffset += height;
            }

        }
       
    }
}
