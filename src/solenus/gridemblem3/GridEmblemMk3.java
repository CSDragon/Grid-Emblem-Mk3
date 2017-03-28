/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import solenus.gridemblem3.scene.SceneManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class GridEmblemMk3 
{
    //This means, on slow hardware, if it's somehow taking too long to process the game,
    //the max ammount of gameframes the game can process without rendering is 5. After that it MUST render. Giving a theoretic minimum of 12fps.
    //though, if it's having trouble processing the gamestate good gracious it's gonna have trouble rendering.
    private static final int MAX_FRAMESKIP = 5; 
    private static final double TIMEBETWEENFRAMES = 1000000000/60.0;
    
    private static JFrame gameFrame;
    private static GamePanel gamePanel;
    private static SceneManager sceneControl;
    private static InputManager im;
    private static MotionPanel mp;
    
    //Global render values
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    public static int HALFWIDTH = 640;
    public static int HALFHEIGHT = 360;
    public static int GRIDSIZE = 80;
    public static int HALFGRIDSIZE = 40;
    
    //Window types
    public static boolean windowState = false;
    public static final int WINDOWED = 1;
    public static final int BORDERLESS = 2;
    
    //test data
    public static long framesLost = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        checkGraphicSettings();
        
        gameFrame = new JFrame("Grid Emblem");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setUndecorated(windowState);
        gameFrame.setResizable(false);
        
        mp = new MotionPanel(gameFrame, windowState);
        gameFrame.add(mp);
        
        sceneControl = new SceneManager();
        sceneControl.start();
        
        gamePanel = new GamePanel(sceneControl);
        gameFrame.add(gamePanel);
        
        gameFrame.pack();
        gameFrame.setVisible(true);
        
        im = new InputManager();
        im.checkKeybindSettings();
        
        gameFrame.addKeyListener(im);
        gameFrame.setLocationRelativeTo(null);
        
        runGame();
		
    }
    
    

    
    /**
     * Runs the game
     */
    public static void runGame()
    {
        //Set up
        long nextGameTick = System.nanoTime();
        int loops = 0;
        
        
        //Game Loop
        while(true) 
        {
            
            
            //If it's been 16.6666 ms since the last game tick happened, advance the game
            if(System.nanoTime() > nextGameTick)
            {
                //Input
                im.gameStep();
                sceneControl.respondControls(im);

                //Game Logic
                sceneControl.runFrame();
                
                //Run Animations
                sceneControl.animate();
                
                //set the next frame
                nextGameTick += TIMEBETWEENFRAMES;
                
                //Graphics: If we still have time before the next frame, draw the graphics, with a minimum of 12fps
                if(System.nanoTime() < nextGameTick || loops >= MAX_FRAMESKIP)
                {
                    gameFrame.repaint();
                    loops = 0;
                }
                else
                {
                    loops++;
                } 
                
                
            }
            
        }
    }
    
    
    /**
     * runs the gamestate 1 frame
     */
    public static void gameLocic()
    {
        im.gameStep();
        //sceneControl.getActiveScene().respondControls(im);
        //sceneControl.getActiveScene().runFrame();
        
    }
    
    
    
    /**
     * fetches the SceneManager
     * @return the scene manager
     */
    public static SceneManager getSceneMangager()
    {
        return sceneControl;
    }
    
    /**
     * If there are any graphic settings, load them in.
     */
    public static void checkGraphicSettings()
    {
        File graphics = new File("settings/graphics.txt");
        if(!graphics.exists())
        {
            try
            {
                File settings = new File("settings");
                settings.mkdir();
                graphics.createNewFile();
                FileOutputStream fos = new FileOutputStream(graphics);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                
                bw.write("Width: 1280");
                bw.newLine();
                
                bw.write("Height: 720");
                bw.newLine();
                
                bw.write("Windowed");
                bw.newLine();
                
                bw.close();
                
            }
            catch(Exception e)
            {
                System.out.println(e);
                JOptionPane.showMessageDialog(null,"Unable to save graphics settings by writing a file. If you're seeing this you have bigger problems than the game not working.");
                System.exit(-1);
            }
        }
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("settings/graphics.txt"));
            
            WIDTH  = Integer.decode(in.readLine().substring(7));
            HEIGHT = Integer.decode(in.readLine().substring(8));
            HALFWIDTH = WIDTH/2;
            HALFHEIGHT = HEIGHT/2;
            windowState = in.readLine().equals("Borderless");
            
            in.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Unable to load your graphics settings. Idk what you did, but you messed it up somehow...");
            System.exit(-1);
        }
    }
    
    /**
     * Changes the resolution
     * @param w the new width
     * @param h the new height
     */
    public static void setResolution(int w, int h)
    {
        WIDTH = w;
        HEIGHT = h;
        HALFWIDTH = w/2;
        HALFHEIGHT = h/2;
        
        sceneControl.resize();
        
        mp.setPreferredSize(new Dimension(w,h));

        gameFrame.pack();
        gameFrame.setSize(w,h);
        gameFrame.setLocationRelativeTo(null);
        
        saveGraphicsSettings();
        
    }
    
    /**
     * Changes between borderless mode and regular
     * @param b The border type we want
     */
    public static void setBorderless(boolean b)
    {
        gameFrame.dispose();
        gameFrame.setTitle(gameFrame.getGraphicsConfiguration().getDevice().getIDstring());
        gameFrame.setUndecorated(b);
        windowState = b;
        
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        
        mp.setActive(b);
        
        saveGraphicsSettings();
    }
    
    public static void saveGraphicsSettings()
    {
        File graphics = new File("settings/graphics.txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(graphics);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("Width: "+ WIDTH);
            bw.newLine();

            bw.write("Height: "+HEIGHT);
            bw.newLine();

            if(windowState)
                bw.write("Borderless");
            else
                bw.write("Windowed");

            bw.newLine();

            bw.close();

        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Unable to save graphics settings by writing a file. If you're seeing this you have bigger problems than the game not working.");
            System.exit(-1);
        }
        
    }
    
    public static void saveKeybinds()
    {
        im.saveKeybinds();
    }
    

    
}
