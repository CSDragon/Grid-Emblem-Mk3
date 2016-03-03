/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class InventoryScene extends Scene
{
    public static final int BACK = -1;
    public static final int NOTHING = 0;
    
    private PlayerData data;
    
    private CharacterSelectMenu csm;
    private UnitInventoryMenu uim;
    private ItemActionsMenu iam;
    private TabbedInventoryScene tis;
    
    Unit activeUnit;
    Item activeItem;
    
    private BufferedImage blackout;
    private BufferedImage background;
    
    public InventoryScene()
    {
        super();
        
        tis = new TabbedInventoryScene();
        uim = new UnitInventoryMenu();
        iam = new ItemActionsMenu();
        
        try
        {
            //load blackout and textbox images.
            blackout = ImageIO.read(new File("assets/ui/blackout.png"));
            background = ImageIO.read(new File("assets/ui/inventory/background.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Inventory border image failed to load. Did you change something?");
            System.exit(-1);
        }
    }
    
    // <editor-fold desc="Scene control methods">
        
    /**
     * Responds to controls.
     * @param im the input 
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            /*
            States:
                1) Character select menu
                2) Item management: Player
                3) Item management: ItemList
                4) Player item selected
                5) ItemList item selected.
                6) Player trade (Selecting an item from the convoy to trade with the player)
                7) Convoy trade (Selecting an item from the player to trade with the convoy
            */
            switch(getControlState())
            {
                case 1:
                    csm.respondControls(im);
                    break;
                case 2:
                    if(uim.getCursorLoc() < Unit.WEAPON_LIMIT)
                    {
                        
                        if(im.getLeft() == 1)
                        {
                            controlState = 3;
                            tis.controlStateChange(TabbedInventoryScene.MAXTAB-1);
                            break;
                        }
                        
                        if(im.getRight() == 1)
                        {
                            controlState = 3;
                            tis.controlStateChange(0);
                            break;
                        }
                        
                        
                    }
                    else
                    {
                        if(im.getLeft() == 1 || im.getRight() == 1)
                        {
                            controlState = 3;
                            tis.controlStateChange(TabbedInventoryScene.MAXTAB);
                            break;
                        }
                    }
                    
                    uim.respondControls(im);
                    break;
                case 3:
                    if(uim.getCursorLoc() < Unit.WEAPON_LIMIT)
                    {
                        if((im.getLeft() == 1 && tis.getControlState() == 0) || (im.getRight() == 1 && tis.getControlState() == TabbedInventoryScene.MAXTAB-1))
                        {
                            controlState = 2;
                            break;
                        }
                    }
                    else
                    {
                        if(im.getLeft() == 1 || im.getRight() == 1)
                        {
                            controlState = 2;
                            break;
                        }
                    }
                    
                    tis.respondControls(im);
                    break;
                    
                case 4:
                case 5:
                    iam.respondControls(im);
                    break;
                    
                case 6:
                    tis.respondControls(im);
                    break;
                case 7:
                    uim.respondControls(im);
                    break;
            }
                
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            /*
            States:
                1) Character select menu
                2) Item management: Player
                3) Item management: ItemList
                4) Player item selected
                5) ItemList item selected.
                6) Player trade (Selecting an item from the convoy to trade with the player)
                7) Convoy trade (Selecting an item from the player to trade with the convoy
            */
            switch(getControlState())
            {
                
                case 1:
                    int csmResult = csm.runFrame();
                    switch(csmResult)
                    {
                        case CharacterSelectMenu.BACK:
                            return BACK;
                            
                        case CharacterSelectMenu.NOTHING:
                            break;
                            
                        default:
                            cst1to2(csm.getUnitAt(csmResult));
                            break;
                    }
                    break;
                
                case 2:
                    int res2 = uim.runFrame();
                    switch(res2)
                    {
                        case UnitInventoryMenu.BACK:
                            cstXto1();
                            break;
                        case UnitInventoryMenu.NOTHING:
                            break;
                        
                        default:
                            activeItem = uim.getItemAt(res2);
                            if(activeItem != null)
                                cst2to4();
                            break;
                    }
                    break;
                    
                case 3:
                    int res3 = tis.runFrame();
                    switch(res3)
                    {
                        case TabbedInventoryScene.BACK:
                            cstXto1();
                            break;
                            
                        case UnitInventoryMenu.NOTHING:
                            break;
                        
                        default:
                            activeItem = tis.getItemAt(res3);
                            if(activeItem != null)
                                cst3to5();
                            break;
                    }
                    break;
                    
                case 4:
                    switch(iam.runFrame())
                    {
                        case ItemActionsMenu.BACK:
                            cst4to2();
                            break;
                            
                        case ItemActionsMenu.STORE:
                            if(activeItem instanceof Weapon)
                            {
                                activeUnit.removeWeapon((Weapon)activeItem);
                                data.addWeapon((Weapon)activeItem);
                                cst4to2();
                            }
                            else if(activeItem instanceof Usable)
                            {
                                activeUnit.removeItem((Usable)activeItem);
                                data.addItem((Usable)activeItem);
                                cst4to2();
                            }
                            break;
                            
                        case ItemActionsMenu.TRADE:
                            if(activeItem instanceof Weapon)
                            {
                                tis.controlStateChange(((Weapon)activeItem).getWeaponType());
                                cst4to6();
                                break;
                            }
                            else if(activeItem instanceof Usable)
                            {
                                tis.controlStateChange(TabbedInventoryScene.MAXTAB);
                                cst4to6();
                                break;
                            }
                            break;
                            
                        case ItemActionsMenu.EQUIP:
                            if(activeItem instanceof Weapon)
                                activeUnit.equipWeapon((Weapon)activeItem);
                            cst4to2();
                            break;
                            
                        case ItemActionsMenu.USE:
                            if(activeItem instanceof Usable)
                                activeUnit.useItem((Usable)activeItem);
                            cst4to2();
                            break;
                    }
                    break;
                case 5:
                    switch(iam.runFrame())
                    {
                        case ItemActionsMenu.TAKE:
                            if(activeItem instanceof Weapon)
                            {
                                if(activeUnit.addWeapon((Weapon)activeItem))
                                {
                                    cst5to2(true);
                                    break;
                                }
                            }
                            else if(activeItem instanceof Usable)
                            {
                                if(activeUnit.addItem((Usable)activeItem))
                                {
                                    cst5to2(false);
                                    break;
                                }
                            }
                            
                            //error?
                            cst5to3();
                            break;
                            
                        case ItemActionsMenu.TRADE:
                            if(activeItem instanceof Weapon)
                            {
                                uim.setMode(UnitInventoryMenu.WEAPONMODE);
                                cst5to7();
                                break;
                            }
                            else if(activeItem instanceof Usable)
                            {
                                uim.setMode(UnitInventoryMenu.USABLEMODE);
                                cst5to7();
                                break;
                            }
                            
                            //error?
                            cst5to3();
                            break;
                            
                        case ItemActionsMenu.USE:
                            if(activeItem instanceof Usable)
                                activeUnit.useItemFromConvoy((Usable)activeItem, data);
                            cst5to3();
                            break;
                        
                        case ItemActionsMenu.BACK:
                            cst5to3();
                            break;
                    }
                    break;
                
                case 6:
                    int res6 = tis.runFrame();
                    switch(res6)
                    {
                        case TabbedInventoryScene.NOTHING:
                            break;
                            
                        case TabbedInventoryScene.BACK:
                            cst6to2();
                            break;
                            
                        default:
                            Item item = tis.getItemAt(res6);
                            if(item instanceof Weapon)
                            {
                                activeUnit.removeWeapon((Weapon)activeItem);
                                data.removeWeapon((Weapon)item);
                                
                                activeUnit.addWeapon((Weapon)item);
                                data.addWeapon((Weapon)activeItem);
                                cst6to2();
                            }
                            
                            if(item instanceof Usable)
                            {
                                activeUnit.removeItem((Usable)activeItem);
                                data.removeItem((Usable)item);
                                
                                activeUnit.addItem((Usable)item);
                                data.addItem((Usable)activeItem);
                                cst6to2();
                            }
                            break;
                    }
                    break;
                    
                case 7:
                    int res7 = uim.runFrame();
                    switch(res7)
                    {
                        case UnitInventoryMenu.NOTHING:
                            break;
                            
                        case UnitInventoryMenu.BACK:
                            cst7to2();
                            break;
                            
                        default:
                            Item item = uim.getItemAt(res7);
                            if(item instanceof Weapon)
                            {
                                data.removeWeapon((Weapon)activeItem);
                                activeUnit.removeWeapon((Weapon)item);
                                
                                data.addWeapon((Weapon)item);
                                activeUnit.addWeapon((Weapon)activeItem);
                                cst7to2();
                            }
                            
                            if(item instanceof Usable)
                            {
                                data.removeItem((Usable)activeItem);
                                activeUnit.removeItem((Usable)item);
                                
                                data.addItem((Usable)item);
                                activeUnit.addItem((Usable)activeItem);
                                cst7to2();
                            }
                            break;
                    }
                    break;
            }
        }
        
        return NOTHING;
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
            csm.animate();
            uim.animate();
            tis.animate();
            iam.animate();
        }
    }
    
    /**
     * Draws the scene
     * @param g 
     */
    public void draw(Graphics2D g)
    {
        if(visible)
        {
            Rendering.renderAbsolute(blackout  , g, 0, 0, 960, 540, 1, 1);
            Rendering.renderAbsolute(background, g, 0, 0, 640, 360, 1, 1);
            csm.draw(g);
            uim.draw(g);
            tis.draw(g);
            iam.draw(g);
        }
    }

    //</editor-fold>

    public void start(PlayerData pd)
    {
        super.start();
        data = pd;
        
        controlState = 1;
        csm = new CharacterSelectMenu(data.getUnitList());
        csm.start();
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"

    public void cst1to2(Unit u)
    {
        controlState = 2;

        activeUnit = u;
        
        uim = new UnitInventoryMenu(activeUnit, -200, -100);
        uim.start();
        csm.end();
        tis.start(data.getAllWeapons(), data.getAllItems());
    }
    
    public void cst2to4()
    {
        controlState = 4;
        iam.start();
    }
    
    public void cst3to5()
    {
        controlState = 5;
        iam.start();
    }
    
    public void cst4to2()
    {
        controlState = 2;
        uim.refresh();
        iam.end();
    }
    
    public void cst4to6()
    {
        controlState = 6;
        iam.end();
    }
    
    public void cst5to2(boolean isWeapon)
    {
        controlState = 2;
        if(isWeapon)
            uim.setCursorLoc(activeUnit.getWeaponInventory().size()-1);
        else
            uim.setCursorLoc(Unit.WEAPON_LIMIT + activeUnit.getInventory().size()-1);
        
        uim.refresh();
        iam.end();
    }
    
    public void cst5to3()
    {
        controlState = 3;
        iam.end();
    }
    
    public void cst5to7()
    {
        controlState = 7;
        iam.end();
    }
    
    public void cst6to2()
    {
        controlState = 2;
        uim.setMode(UnitInventoryMenu.BOTHMODE);
        uim.refresh();;
    }
    
    public void cst7to2()
    {
        controlState = 2;
        uim.setMode(UnitInventoryMenu.BOTHMODE);
        uim.refresh();
        
    }
    
    public void cstXto1()
    {
        controlState = 1;
        uim.end();
        tis.end();
        csm.resume();
    }

    //</editor-fold>

}

