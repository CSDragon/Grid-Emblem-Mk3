/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.render;

import java.util.TreeMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class Sprite 
{
    protected String name;
    protected BufferedImage spriteSheet;
    protected BufferedImage displaySprite;
    protected ArrayList<BufferedImage> sprites;
    
    protected int height;
    protected int width;
    protected int centerX;
    protected int centerY;
    protected int rows;
    protected int cols;
    
    protected Animation activeAnimation;
    protected int activeFrame;
    protected int frameCount;
    
    protected TreeMap<String, Animation> animationList;
    
    
    /**
     * Standard constructor
     * @param n The name, and by extension, the filename of the sprite and animation file.
     */
    public Sprite(String n)
    {
        name = n;
        animationList = new TreeMap<>();
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/sprites/"+name+"Sheet.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/sprites/"+name+"Animation.txt"));
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            rows = Integer.decode(in.readLine().substring(6));
            cols = Integer.decode(in.readLine().substring(6));

            
            //discard extra line
            in.readLine();
            
            
            //get number of animations
            int numAnimations = Integer.decode(in.readLine().substring(12));
            
            for(int i = 0; i<numAnimations; i++)
            {
                String[] arr = in.readLine().split(",");    

                animationList.put(arr[0], new Animation(arr[0],Integer.decode(arr[1]),Integer.decode(arr[2]),Integer.decode(arr[3]), Integer.decode(arr[4])));
            }
            //dispose extra line
            in.readLine();
            
            
            //load triggers
            int numTransitions = Integer.decode(in.readLine().substring(13));
            
            for(int i = 0; i < numTransitions; i++)
            {
                String[] arr = in.readLine().split(",",4); 
                if(arr[0].equals("any"))
                    for(Map.Entry<String,Animation> a : animationList.entrySet())
                    {
                        a.getValue().addTransition(arr[2], new Transition(animationList.get(arr[1]),arr[2],arr[3]));
                    }
                       
                else
                    animationList.get(arr[0]).addTransition(arr[2], new Transition(animationList.get(arr[1]),arr[2],arr[3]));
            }
            //dispose extra line
            in.readLine();
            
            
            //get default animation
            activeAnimation = animationList.get(in.readLine().substring(9));
            
            in.close();
        }
        
        catch(Exception e)
        {
            try
            {
                //load sheet image
                spriteSheet = ImageIO.read(new File("assets/sprites/genericSheet.png"));

                //load animations
                BufferedReader in = new BufferedReader(new FileReader("assets/sprites/genericAnimation.txt"));

                //get sprite dimensions
                height = Integer.decode(in.readLine().substring(8));
                width = Integer.decode(in.readLine().substring(7));
                centerX = Integer.decode(in.readLine().substring(9));
                centerY = Integer.decode(in.readLine().substring(9));
                rows = Integer.decode(in.readLine().substring(6));
                cols = Integer.decode(in.readLine().substring(6));


                //discard extra line
                in.readLine();


                //get number of animations
                int numAnimations = Integer.decode(in.readLine().substring(12));

                for(int i = 0; i<numAnimations; i++)
                {
                    String[] arr = in.readLine().split(",");    

                    animationList.put(arr[0], new Animation(arr[0],Integer.decode(arr[1]),Integer.decode(arr[2]),Integer.decode(arr[3]), Integer.decode(arr[4])));
                }
                //dispose extra line
                in.readLine();


                //load triggers
                int numTransitions = Integer.decode(in.readLine().substring(13));

                for(int i = 0; i < numTransitions; i++)
                {
                    String[] arr = in.readLine().split(",",4); 
                    if(arr[0].equals("any"))
                        for(Map.Entry<String,Animation> a : animationList.entrySet())
                        {
                            a.getValue().addTransition(arr[2], new Transition(animationList.get(arr[1]),arr[2],arr[3]));
                        }

                    else
                        animationList.get(arr[0]).addTransition(arr[2], new Transition(animationList.get(arr[1]),arr[2],arr[3]));
                }
                //dispose extra line
                in.readLine();


                //get default animation
                activeAnimation = animationList.get(in.readLine().substring(9));

                in.close();
            }
            catch(Exception e1)
            {
                JOptionPane.showMessageDialog(null, name +" sprite failed to load. Did you change something?");
                System.exit(-1);
            }
        }
        
        sprites = new ArrayList<>();
        for(int i = 0; i<rows; i++)
            for(int j = 0; j<cols; j++)
                sprites.add(spriteSheet.getSubimage(j*width, i*height, width, height));
        displaySprite = sprites.get(0);
    }

    /**
     * "Default" constructor for child classes. If this is ever called to directly make a sprite, we have a problem.
     */
    public Sprite()
    {
    }
    
    /**
     * Sends a trigger to the animation, telling it to move to a different animation.
     * @param trigger The name of the trigger specified in the animation file.
     */
    public void sendTrigger(String trigger)
    {
        Transition t = activeAnimation.getTransition(trigger);
        
        if(t != null)
        {
            if(!t.getOption().contains("keepFrame"))
            {
                activeFrame = 0;
                frameCount = 0;
            }

            activeAnimation = t.getTarget();
        }
    }
    
    /**
     * Advance the sprite's animation 1 frame.
     */
    public void animate()
    {
        frameCount++;
        if(frameCount%activeAnimation.getSpeed() == 0)
            activeFrame++;
        if(activeFrame%activeAnimation.getNumFrames() == 0)
            activeFrame = 0;
        
        displaySprite = sprites.get(activeAnimation.getStartY()*cols + activeAnimation.getStartX() + activeFrame);
    }
    
    
    //<editor-fold desc="getters and setters">
    
    public BufferedImage getDisplaySprite()
    {
        return displaySprite;
    }
    
    public int getCenterX()
    {
        return centerX;
    }
    
    public int getCenterY()
    {
        return centerY;
    }
    
    //</editor-fold>
}
