/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.render;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class DialogSprite extends Sprite
{
    private ArrayList<BufferedImage> spritesLeft;
    
    //false = faceing to the right, true = facing to the left.
    private boolean flipped;
    
    /**
     * Standard constructor
     * @param n The name, and by extension, the filename of the sprite and animation file.
     */
    public DialogSprite(String n)
    {
        name = n;
        animationList = new TreeMap<>();
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/dialog/sprites/"+name+"Sheet.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/dialog/sprites/"+name+"Animation.txt"));
            
            //get sprite dimensions
            width = Integer.decode(in.readLine().substring(7));
            height = Integer.decode(in.readLine().substring(8));
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
            JOptionPane.showMessageDialog(null, name +" sprite failed to load. Did you change something?");
            System.exit(-1);
        }
        
        //Create the sprites from the sheet
        sprites = new ArrayList<>();
        for(int i = 0; i<rows; i++)
            for(int j = 0; j<cols; j++)
                sprites.add(spriteSheet.getSubimage(j*width, i*height, width, height));
        
        //create the left-facings sprites.
        spritesLeft = new ArrayList<>();
        for (BufferedImage rightSprite : sprites) 
            spritesLeft.add(createFlipped(rightSprite));
        
        
        if(!flipped)
            displaySprite = sprites.get(0);
        else
            displaySprite = spritesLeft.get(0);
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
        
        if(flipped)
            displaySprite = spritesLeft.get(activeAnimation.getStartY()*cols + activeAnimation.getStartX() + activeFrame);
        else
            displaySprite = sprites.get(activeAnimation.getStartY()*cols + activeAnimation.getStartX() + activeFrame);

    }
    
    /**
     * Creates a flipped version of a bufferedImage
     * Credit to StackOverflow User marco13
     * @param image The image to flip
     * @return The flipped image.
     */
    private static BufferedImage createFlipped(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    /**
     * Flips the sprite.
     */
    public void flip()
    {
        flipped = !flipped;
    }
    
    
    
    //<editor-fold desc="getters and setters">
    
    public boolean getFlipped()
    {
        return flipped;
    }
    
    public void setFlipped(boolean f)
    {
        flipped = f;
    }
    
    //</editor-fold>
}
