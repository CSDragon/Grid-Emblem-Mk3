/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.ui.UI;

/**
 *
 * @author Chris
 */
public class FightHealthBarUI extends UI
{
    public static final int FRAMESPERTIC = 6;
    
    private int maxHP;
    private int curHP;
    private int tempHP;
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
        curHP = cur;
        tempHP = cur;
        start();
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/ui/FightHealthBarUISheet.png"));
        
            //load info
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
        if(loc < 0)
            location -= uiWidth;
        
        if(loc == 0)
            location -= uiWidth/2;
    }
    
    /**
     * Progresses the element forward 1 frame.
     * @return The resultant state.
     */
    public boolean runFrame()
    {
        if(active)
        {
            /*
            STATES:
                1) Idle
                2) Animating Damage
                3) Animating Healing
            */
            switch(controlState)
            {
                case 2:
                    if(framesLeft <= 0)
                        controlState = 1;
                    else if(framesLeft % FRAMESPERTIC == 0)
                        tempHP--;
                    framesLeft--;
                    
                    return false;
                
                case 3:
                    if (framesLeft == 0)
                        controlState = 1;
                    else if(framesLeft % FRAMESPERTIC == 0)
                        tempHP++;
                    framesLeft--;
                    
                    return false;
            }
        }
        
        return true;
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
            Rendering.renderAbsolute(ui, g, location, 200, 0, 0, 1, 1);
            
            //render empty pips
            for(int i = tempHP; i<maxHP; i++)
            {
                //render the top bar
                if(i<30)
                   Rendering.renderAbsolute(emptyPip, g, location + 31 +(pipWidth+1)*i, 205, 0, 0, 1, 1);
                else
                    Rendering.renderAbsolute(emptyPip, g, location + 31 +(pipWidth+1)*(i-30), 215, 0, 0, 1, 1);
            }
            
            //render full pips
            for(int i = 0; i<tempHP; i++)
            {
                //render the top bar
                if(i<30)
                   Rendering.renderAbsolute(fullPip, g, location + 31 +(pipWidth+1)*i, 205, 0, 0, 1, 1);
                else
                    Rendering.renderAbsolute(fullPip, g, location + 31 +(pipWidth+1)*(i-30), 215, 0, 0, 1, 1);
            }
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g.setColor(Color.white);
            Rendering.renderTextAbsolute(Integer.toString(tempHP), g, location + 14, 218, 0, 0, 1, 1, 1);
        }
    }
    
    /**
     * Deal damage
     * @param dmg The damage taken
     */
    public void damage(int dmg)
    {
        tempHP = curHP;
        curHP -= dmg;
        if(curHP < 0)
            curHP = 0;
        
        framesLeft = (tempHP - curHP)*FRAMESPERTIC;
        
        controlState = 2;
    }
    
    /**
     * Get healed.
     * @param heal The damage healed
     */
    public void heal(int heal)
    {
        tempHP = curHP;
        curHP += heal;
        if(curHP > maxHP)
            curHP = maxHP;
        
        framesLeft = (curHP - tempHP)*FRAMESPERTIC;
        
        controlState = 3;
    }
    
}
