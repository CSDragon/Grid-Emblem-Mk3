/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.render.DialogSprite;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class DialogueScene extends Scene
{
    private BufferedImage blackout;
    private BufferedImage textbox;
    private BufferedImage background;
    
    private DialogSprite left;
    private DialogSprite right;
    private ArrayList<DialogSprite> speakers;
    private int numSpeakers;
    
    private ArrayList<String> commands;
    private ArrayList<Integer> leftTransitions;
    private ArrayList<Integer> rightTransitions;
    private ArrayList<String> leftScripts;
    private ArrayList<String> rightScripts;
    private ArrayList<String> neutralScripts;
    private ArrayList<String> leftAnimations;
    private ArrayList<String> rightAnimations;
    private int ltControl;
    private int rtControl;
    private int lsControl;
    private int rsControl;
    private int nsControl;
    private int laControl;
    private int raControl;
    
    private int curCommand;
    private String displayString;
    
    public DialogueScene(Scene parent)
    {
        super(parent);
        
        try
        {
            //load blackout and textbox images.
            blackout = ImageIO.read(new File("assets/dialog/art/blackout.png"));
            textbox = ImageIO.read(new File("assets/dialog/art/textbox.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Dialog images failed to load. Did you change something?");
            System.exit(-1);
        }
    }
        
        
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            /*
            STATES
                1) Neutral
                2) Get next line
                3) Animate
            */
            switch(controlState)
            {
                case 1:
                    if(im.getA() == 1)
                        controlState = 2;
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * most scene subclasses must override this, and check if they are active.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            /*
            STATES
                1) Neutral
                2) Get next line
                3) Entry Animation
                4) Text Animation
            */
            switch(controlState)
            {
                case 1:
                    break;
                case 2:
                    if(curCommand == commands.size()-1)
                        return 1;
                    nextLine();
                    break;
                case 3:
                    controlState = 2;
                    break;
                case 4:
                    controlState = 1;
                    break;
            }
        }
        
        return -1;
    }
    
    /**
     * animates the scene's objects 1 frame
     * most scene subclasses must override this, and check if they are active.
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
            left.animate();
            right.animate();
        }
    }
    
    /**
     * Draws the scene
     * @param g The graphics
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            if(background != null)
                Rendering.renderAbsolute(background, g, 0, 0, 640, 360, 1, 1);

            
            //test
            if(left != null)
                Rendering.renderAbsolute(left, g, -250, 89, 1, 1);
            if(right != null)
                Rendering.renderAbsolute(right, g, 250, 89, 1, 1);
            
            Rendering.renderAbsolute(blackout, g, 0, 0, 960, 540, 1, 1);
            Rendering.renderAbsolute(textbox , g, 0, 0, 640, 360, 1, 1);
            

            if(displayString != null)
                Rendering.renderTextAbsolute(displayString, g, -350, 200, 0, 0, 1, 1, 0);
        }
    }
            
    //</editor-fold>
    
    public void start(String dialog)
    {
        super.start();
        controlState = 2;
        
        commands = new ArrayList<>();
        leftTransitions = new ArrayList<>();
        rightTransitions = new ArrayList<>();
        leftScripts = new ArrayList<>();
        rightScripts = new ArrayList<>();
        neutralScripts = new ArrayList<>();
        leftAnimations = new ArrayList<>();
        rightAnimations = new ArrayList<>();
        ltControl = 0;
        rtControl = 0;
        lsControl = 0;
        rsControl = 0;
        nsControl = 0;
        laControl = 0;
        raControl = 0;

        curCommand = -1;
        displayString = null;
        
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/dialog/"+dialog+"Script.txt"));
            String read;
            
            numSpeakers = Integer.decode(in.readLine().substring(15));
            speakers = new ArrayList<>();
            for(int i = 0; i<numSpeakers; i++)
                speakers.add(new DialogSprite(in.readLine()));
            
            //dispose of the extra line
            in.readLine();
            
            //load in background.
            String bg = in.readLine().substring(12);
            if(!bg.equals("none"))
                background = ImageIO.read(new File("assets/dialog/art/backgrounds/testBackground.png"));
            
            //dispose of extra lines
            in.readLine();
            in.readLine();
            
            //Load initial speakers
            int l = Integer.decode(in.readLine().substring(6));
            if(l < 0)
                left = null;
            else
                left = speakers.get(l);
            
            int r = Integer.decode(in.readLine().substring(7));
            if(r < 0)
                right = null;
            else
                right = speakers.get(r);
            right.setFlipped(true);
            
            //dispose of extra lines
            in.readLine();
            in.readLine();
            
            //Load in dialog.
            String nextCommand;
            nextCommand = in.readLine();
            while(nextCommand != null)
            {
                commands.add(nextCommand.substring(0,2));
                switch(nextCommand.substring(0,2))
                {
                    case "LT":
                        leftTransitions.add(Integer.decode(nextCommand.substring(4)));
                        break;
                    case "RT":
                        rightTransitions.add(Integer.decode(nextCommand.substring(4)));
                        break;

                    case "LS":
                        leftScripts.add(nextCommand.substring(4));
                        break;
                    case "RS":
                        rightScripts.add(nextCommand.substring(4));
                        break;
                    case "NS":
                        neutralScripts.add(nextCommand.substring(4));
                        
                    case "LA":
                        leftAnimations.add(nextCommand.substring(4));
                        break;
                    case "RA":
                        rightAnimations.add(nextCommand.substring(4));
                        break;
                }
                
                nextCommand = in.readLine();
            }
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "A dialog failed to load. Did you change something?");
            e.printStackTrace(System.out);
        }
    }
    
    /**
     * Read the next line.
     */
    public void nextLine()
    {
        //and this is why curCommand starts at -1;
        curCommand++;
        switch(commands.get(curCommand))
        {
            case "LT":
                left = speakers.get(leftTransitions.get(ltControl));
                ltControl++;
                left.setFlipped(false);
                controlState = 3;
                break;
            case "RT":
                right = speakers.get(rightTransitions.get(rtControl));
                rtControl++;
                right.setFlipped(true);
                controlState = 3;
                break;
                
            case "LS":
                displayString = leftScripts.get(lsControl);
                lsControl++;
                controlState = 4;
                break;
            case "RS":
                displayString = rightScripts.get(rsControl);
                rsControl++;
                controlState = 4;
                break;
            case "NS":
                displayString = neutralScripts.get(nsControl);
                nsControl++;
                controlState = 4;
                break;
                
            case "LA":
                left.sendTrigger(leftAnimations.get(laControl));
                laControl++;
                break;
            case "RA":
                right.sendTrigger(rightAnimations.get(raControl));
                raControl++;
                break;
                
        }
    }
    
    
}
