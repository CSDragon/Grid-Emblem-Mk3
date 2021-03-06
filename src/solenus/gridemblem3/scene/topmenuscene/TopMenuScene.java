/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.topmenuscene;

import java.awt.Graphics2D;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.ui.menu.LoadMenu;


/**
 *
 * @author Chris
 */
public class TopMenuScene extends Scene
{
    //Return values
    public static final int NEWGAME = 1;
    public static final int CONTINUE = 2;
    
    //Menus
    private MainMenu main;
    private SettingsMenu settings;
    private KeybindsMenu keybinds;
    private GraphicsMenu graphics;
    private AudioMenu audio;
    private LoadMenu load; 
    
    private int fileNum;
    
    
    public TopMenuScene(Scene parent)
    {
        //seup
        super(parent);
        
        //Initialize menus
        main     = new MainMenu();
        settings = new SettingsMenu();
        keybinds = new KeybindsMenu();
        graphics = new GraphicsMenu();
        audio    = new AudioMenu();
        load     = new LoadMenu(); 
        
        controlState = 0;
        main.start();
    }
    
    
        //</editor-fold>
    
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
            STATES:
                0 = Main Menu
                1 = New Game
                2 = Continue
                3 = Settings
                4 = Keybinds
                5 = Graphics
                6 = Audio
            */
            switch(getControlState())
            {
                //Main Menu
                case 0:
                    main.respondControls(im);
                    break;
                case 2:
                    load.respondControls(im);
                    break;
                case 3:
                    settings.respondControls(im);
                    break;
                case 4:
                    keybinds.respondControls(im);
                    break;
                case 5:
                    graphics.respondControls(im);
                    break;
                case 6:
                    audio.respondControls(im);
                    break;
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
            STATES:
                0 = Main Menu
                1 = New Game
                2 = Continue
                3 = Settings
                4 = Keybinds
                5 = Graphics
                6 = Audio
            */
            
            switch(getControlState())
            {
                //Main Menu
                case 0:
                    switch(main.runFrame())
                    {
                        case MainMenu.NEWGAME:
                            return 1;
                        case MainMenu.CONTINUE:
                            cst0to2();
                            break;
                        case MainMenu.SETTINGS:
                            cst0to3();
                            break;
                        case MainMenu.EXIT:
                            System.exit(1);
                            break;
                    }
                    break;
                    
                //New Game
                case 1:
                    break;
                    
                //Continue
                case 2:
                    int loadFrame = load.runFrame();
                    switch(loadFrame)
                    {
                        case LoadMenu.BACK:
                            cst2to0();
                            break;
                        case LoadMenu.NOTHING:
                            break;
                        default:
                            fileNum = loadFrame+1;
                            return CONTINUE;
                    }
                    break;
                
                //Settings
                case 3:
                    switch(settings.runFrame())
                    {
                        case SettingsMenu.KEYBINDS:
                            cst3to4();
                            break;
                        case SettingsMenu.GRAPHICS:
                            cst3to5();
                            break;
                        case SettingsMenu.AUDIO:
                            cst3to6();
                            break;
                        case SettingsMenu.BACK:
                            cst3to0();
                            break;
                    }
                    break;
                
                //Keybinds
                case 4:
                    switch(keybinds.runFrame())
                    {
                        case KeybindsMenu.CONFIRM:
                        case KeybindsMenu.BACK:
                            cst4to3();
                            break;
                    }
                    break;
                
                //Graphics
                case 5:
                    switch(graphics.runFrame())
                    {
                        case GraphicsMenu.BACK:
                            cst5to3();
                            break;
                    }
                    break;
                
                //Audio
                case 6:
                    switch(audio.runFrame())
                    {
                        case AudioMenu.BACK:
                            cst6to3();
                            break;
                    }
                    break;
            }
            
            
        }
        
        return NOTHING;
    }
    
    /**
     * renders the scene.
     * most scene subclasses must override this, and check if they are visible.
     */
    public void animate()
    {   
        //always check this
        if(visible)
        {
        }
    }
    
    /**
     * Paints the scene
     * @param g2 graphics.
     */
    public void draw(Graphics2D g2)
    {
        if(visible)
        {
            //draw UI
            main.draw(g2);
            settings.draw(g2);
            keybinds.draw(g2);
            graphics.draw(g2);
            audio.draw(g2);
            load.draw(g2);
        }
    }
    
    /**
     * resizes the scene to the current app size
     */
    public void resize()
    {
        super.resize();
    }
    
    /**
     * Starts this scene.
     */
    public void start()
    {
        super.start();
        
        main.start();
    }
    
    /**
     * Ends this scene.
     */
    public void end()
    {
        super.end();
        
        main.end();
        settings.end();
        keybinds.end();
        graphics.end();
        audio.end();
        load.end();
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"
    
    /**
     * Goes from the main menu to the load menu.
     */
    public void cst0to2()
    {
        controlState = 2;
        main.end();
        load.start();
    }
    
    /**
     * Goes from the main menu to the settings menu
     */
    public void cst0to3()
    {
        controlState = 3;
        main.end();
        settings.start();
    }
    
    /**
     * Goes from the load menu to the main menu
     */
    public void cst2to0()
    {
        controlState = 0;
        load.end();
        main.start();
    }
    
    
    
    /**
     * Goes back to the main menu from settings
     */
    public void cst3to0()
    {
        controlState = 0;
        settings.end();
        main.start();
        
    }
    
    /**
     * Goes from settings to keybinds
     */
    public void cst3to4()
    {
        controlState = 4;
        settings.end();
        keybinds.start();
    }
    
    /**
     * Goes from settings to graphics
     */
    public void cst3to5()
    {
        controlState = 5;
        settings.end();
        graphics.start();
    }
    
    /**
     * Goes from settings to audio
     */
    public void cst3to6()
    {
        controlState = 6;
        settings.end();
        audio.start();
    }
    
    /**
     * Goes from keybinds back to settings
     */
    public void cst4to3()
    {
        controlState = 3;
        keybinds.end();
        settings.start();
    }
    
    /**
     * goes from graphics back to settings
     */
    public void cst5to3()
    {
        controlState = 3;
        graphics.end();
        settings.start();
    }
    
    /**
     * goes from audio back to settings
     */
    public void cst6to3()
    {
        controlState = 3;
        audio.end();
        settings.start();
    }
    
    //</editor-fold>
    
    /**
     * Returns the number of the file we need to be loading.
     * @return 
     */
    public int getFileNum()
    {
        return fileNum;
    }

    
}
