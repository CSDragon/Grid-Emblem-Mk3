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
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class Sprite 
{
    private String name;
    private BufferedImage spriteSheet;
    private BufferedImage displaySprite;
    
    private int height;
    private int width;
    private int centerX;
    private int centerY;
    
    private Animation activeAnimation;
    private int activeFrame;
    private int frameCount;
    
    private TreeMap<String, Animation> animationList;
    
    
    /**
     * Standard constructor
     * @param n The name, and by extension, the filename of the sprite and animation file.
     */
    public Sprite(String n)
    {
        name = n;
        
        animationList = new TreeMap<String, Animation>();
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/sprites/"+name+"Sheet.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/sprites/"+name+"Animation.txt"));
            String read;
            
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            
            //discard extra line
            in.readLine();
            
            
            //get number of animations
            read = in.readLine();
            int numAnimations = Integer.decode(read.substring(12));
            
            for(int i = 0; i<numAnimations; i++)
            {
                read = in.readLine();
                String[] arr = read.split(",");    

                animationList.put(arr[0], new Animation(arr[0],Integer.decode(arr[1]),Integer.decode(arr[2]),Integer.decode(arr[3]), Integer.decode(arr[4])));
            }
            //dispose extra line
            in.readLine();
            
            
            //load triggers
            read = in.readLine();
            
            int numTransitions = Integer.decode(read.substring(13));
            
            for(int i = 0; i < numTransitions; i++)
            {
                read = in.readLine();
                String[] arr = read.split(",",4); 
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
            JOptionPane.showMessageDialog(null, name +" sprite failed to load. Did you change something?");
            System.exit(-1);
        }
        
        displaySprite = spriteSheet.getSubimage(0, 0, width, height);
    }
    
    
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
    
    
    public void animate()
    {
        frameCount++;
        if(frameCount%activeAnimation.getSpeed() == 0)
            activeFrame++;
        if(activeFrame%activeAnimation.getNumFrames() == 0)
            activeFrame = 0;
        
        
        displaySprite = spriteSheet.getSubimage(activeAnimation.getStartX()*width + activeFrame*width, activeAnimation.getStartY()*height, width, height);
    }
    
    
    //getters and setters
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
    
}
