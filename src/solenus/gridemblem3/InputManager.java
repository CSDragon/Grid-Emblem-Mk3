/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Chris
 */
public class InputManager implements KeyListener
{
    private int up;
    private int down;
    private int left;
    private int right;
    private int a;
    private int b;
    private int x;
    private int y;
    private int l;
    private int r;
    private int start;
    
    private boolean upSwitch;
    private boolean downSwitch;
    private boolean leftSwitch;
    private boolean rightSwitch;
    private boolean aSwitch;
    private boolean bSwitch;
    private boolean xSwitch;
    private boolean ySwitch;
    private boolean lSwitch;
    private boolean rSwitch;
    private boolean startSwitch;
    
    private int upKey = KeyEvent.VK_W;
    private int downKey = KeyEvent.VK_S;
    private int leftKey = KeyEvent.VK_A;
    private int rightKey = KeyEvent.VK_D;
    private int aKey = KeyEvent.VK_J;
    private int bKey = KeyEvent.VK_K;
    private int xKey = KeyEvent.VK_I;
    private int yKey = KeyEvent.VK_L;
    private int lKey = KeyEvent.VK_E;
    private int rKey = KeyEvent.VK_U;
    private int startKey = KeyEvent.VK_ENTER;
    
    /**
     * keyReleased key event
     * @param e key event
     */
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == upKey)
            upSwitch = false;
        
        if(e.getKeyCode() == downKey)
            downSwitch = false;
        
        if(e.getKeyCode() == leftKey)
            leftSwitch = false;
        
        if(e.getKeyCode() == rightKey)
            rightSwitch = false;
        
        if(e.getKeyCode() == aKey)
            aSwitch = false;
        
        if(e.getKeyCode() == bKey)
            bSwitch = false;
        
        if(e.getKeyCode() == xKey)
            xSwitch = false;
                
        if(e.getKeyCode() == yKey)
            ySwitch = false;
        
        if(e.getKeyCode() == lKey)
            lSwitch = false;
                
        if(e.getKeyCode() == rKey)
            rSwitch = false;
                
        if(e.getKeyCode() == startKey)
            startSwitch = false;
    }
    
    
    /**
     * keyPressed key event 
     * @param e key event
     */
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == upKey)
            upSwitch = true;
        
        if(e.getKeyCode() == downKey)
            downSwitch = true;
        
        if(e.getKeyCode() == leftKey)
            leftSwitch = true;
        
        if(e.getKeyCode() == rightKey)
            rightSwitch = true;
        
        if(e.getKeyCode() == aKey)
            aSwitch = true;
        
        if(e.getKeyCode() == bKey)
            bSwitch = true;
        
        if(e.getKeyCode() == xKey)
            xSwitch = true;
        
        if(e.getKeyCode() == yKey)
            ySwitch = true;
        
        if(e.getKeyCode() == lKey)
            lSwitch = true;
        
        if(e.getKeyCode() == rKey)
            rSwitch = true;
        
        if(e.getKeyCode() == startKey)
            startSwitch = true;
        
        //Remove Later
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(1);
        
    }
    
    /**
     * keyTyped key event 
     * @param e key event
     */
    public void keyTyped(KeyEvent e)
    {

    }
    
    
    /**
     * iterates the keys if they're being pressed
     */
    public void gameStep()
    {
        
        if(upSwitch)
            up++;
        else
            up = 0;
        
        if(downSwitch)
            down++;
        else
            down = 0;
        
        if(leftSwitch)
            left++;
        else
            left = 0;
        
        if(rightSwitch)
            right++;
        else
            right = 0;
        
        if(aSwitch)
            a++;
        else
            a = 0;
        
        if(bSwitch)
            b++;
        else
            b = 0;
        
        if(xSwitch)
            x++;
        else
            x = 0;
        
        if(ySwitch)
            y++;
        else
            y = 0;
        
        if(lSwitch)
            l++;
        else
            l = 0;
        
        if(rSwitch)
            r++;
        else
            r = 0;
        
        if(startSwitch)
            start++;
        else
            start = 0;
        
    }

    
    
    
    /**
     * @return the up
     */
    public int getUp() 
    {
        return up;
    }

    /**
     * @return the down
     */
    public int getDown() 
    {
        return down;
    }

    /**
     * @return the left
     */
    public int getLeft() 
    {
        return left;
    }

    /**
     * @return the right
     */
    public int getRight() 
    {
        return right;
    }

    /**
     * @return the a
     */
    public int getA() 
    {
        return a;
    }

    /**
     * @return the b
     */
    public int getB() 
    {
        return b;
    }

    /**
     * @return the x
     */
    public int getX() 
    {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() 
    {
        return y;
    }
    
    /**
     * @return the l
     */
    public int getL() 
    {
        return l;
    }

    /**
     * @return the r
     */
    public int getR() 
    {
        return r;
    }

    /**
     * @return the start
     */
    public int getStart() 
    {
        return start;
    }
    
    
    
    
}
