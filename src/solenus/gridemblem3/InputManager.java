/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import javax.swing.JOptionPane;
import solenus.gridemblem3.scene.topmenuscene.KeybindsMenu;

/**
 *
 * @author Chris
 */
public class InputManager implements KeyListener
{
    private boolean rebindMode;
    private int rebindNum;
    private int lastKey;
    
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
        if(rebindMode)
        {
            lastKey = e.getKeyCode();
        }
        else
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
    
    public static final int NOTHING = -1;
    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    
    public int rebindKey(int keyNum)
    {
        if(!rebindMode)
            lastKey = 0;
        rebindMode = true;
        rebindNum = keyNum;
        
        if(lastKey != 0)
        {
            if(lastKey == KeyEvent.VK_ESCAPE || lastKey == KeyEvent.VK_ENTER || lastKey == upKey || lastKey == downKey
            || lastKey == leftKey || lastKey == rightKey || lastKey == aKey || lastKey == bKey || lastKey == xKey
            || lastKey == yKey|| lastKey == lKey || lastKey == rKey)
            {
                rebindMode = false;
                return FAILURE;
            }
            switch(keyNum)
            {
                case KeybindsMenu.UP:
                    upKey = lastKey;
                    break;
                case KeybindsMenu.DOWN:
                    downKey = lastKey;
                    break;
                case KeybindsMenu.LEFT:
                    leftKey = lastKey;
                    break;
                case KeybindsMenu.RIGHT:
                    rightKey = lastKey;
                    break;
                case KeybindsMenu.A:
                    aKey = lastKey;
                    break;
                case KeybindsMenu.B:
                    bKey = lastKey;
                    break;
                case KeybindsMenu.X:
                    xKey = lastKey;
                    break;
                case KeybindsMenu.Y:
                    yKey = lastKey;
                    break;
                case KeybindsMenu.L:
                    lKey = lastKey;
                    break;
                case KeybindsMenu.R:
                    rKey = lastKey;
                    break;
            }
            rebindMode = false;
            saveKeybinds();
            return SUCCESS;
        }
        
        return NOTHING;
    }
    
    
    /**
     * If there are any keybind settings, load them in.
     */
    public void checkKeybindSettings()
    {
        File keybinds = new File("settings/keybinds.txt");
        if(!keybinds.exists())
        {
            try
            {
                File settings = new File("settings");
                settings.mkdir();
                keybinds.createNewFile();
                FileOutputStream fos = new FileOutputStream(keybinds);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                
                bw.write(String.valueOf(KeyEvent.VK_W));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_S));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_A));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_D));
                bw.newLine();
                
                bw.write(String.valueOf(KeyEvent.VK_J));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_K));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_I));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_L));
                bw.newLine();
                
                bw.write(String.valueOf(KeyEvent.VK_E));
                bw.newLine();
                bw.write(String.valueOf(KeyEvent.VK_U));
                bw.newLine();
                
                bw.close();
                
            }
            catch(Exception e)
            {
                System.out.println(e);
                JOptionPane.showMessageDialog(null,"Unable to save keybind settings by writing a file. If you're seeing this you have bigger problems than the game not working.");
                System.exit(-1);
            }
        }
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("settings/keybinds.txt"));
            
            upKey  = Integer.decode(in.readLine());
            downKey = Integer.decode(in.readLine());
            leftKey  = Integer.decode(in.readLine());
            rightKey  = Integer.decode(in.readLine());
            
            aKey  = Integer.decode(in.readLine());
            bKey  = Integer.decode(in.readLine());
            xKey  = Integer.decode(in.readLine());
            yKey  = Integer.decode(in.readLine());
            
            lKey  = Integer.decode(in.readLine());
            rKey  = Integer.decode(in.readLine());
            
            in.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Unable to load your keybind settings. Idk what you did, but you messed it up somehow...");
            System.exit(-1);
        }
    }
    
    
    /**
     * Saves the keybinds
     */
    public void saveKeybinds()
    {
        File keybinds = new File("settings/keybinds.txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(keybinds);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(String.valueOf(upKey));
            bw.newLine();
            bw.write(String.valueOf(downKey));
            bw.newLine();
            bw.write(String.valueOf(leftKey));
            bw.newLine();
            bw.write(String.valueOf(rightKey));
            bw.newLine();
            
            bw.write(String.valueOf(aKey));
            bw.newLine();
            bw.write(String.valueOf(bKey));
            bw.newLine();
            bw.write(String.valueOf(xKey));
            bw.newLine();
            bw.write(String.valueOf(yKey));
            bw.newLine();
            
            bw.write(String.valueOf(lKey));
            bw.newLine();
            bw.write(String.valueOf(rKey));
            bw.newLine();


            bw.close();

        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Unable to save keybind settings by writing a file. If you're seeing this you have bigger problems than the game not working.");
            System.exit(-1);
        }
        
    }

    
    //<editor-fold desc="Getters and setters">
    
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
    
    //</editor-fold>
    
    
}
