/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import java.awt.Graphics2D;
import java.util.ArrayList;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.item.Item;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;
import solenus.gridemblem3.scene.Scene;

/**
 *
 * @author Chris
 */
public class TabbedInventoryScene extends Scene
{
    public static final int BACK = -2;
    public static final int NOTHING = -1;
    public static final int MAXTAB = 11;
    
    private ArrayList<Weapon> weaponInventory;
    private ArrayList<Usable> usableInventory;
    
    private ArrayList<Item> swords;
    private ArrayList<Item> lances;
    private ArrayList<Item> axes;
    private ArrayList<Item> bows;
    private ArrayList<Item> daggers;
    private ArrayList<Item> lights;
    private ArrayList<Item> fires;
    private ArrayList<Item> winds;
    private ArrayList<Item> lightnings;
    private ArrayList<Item> darks;
    private ArrayList<Item> staves;
    private ArrayList<Item> usables;
    
    private InventoryScrollingMenu swordMenu;
    private InventoryScrollingMenu lanceMenu;
    private InventoryScrollingMenu axeMenu;
    private InventoryScrollingMenu bowMenu;
    private InventoryScrollingMenu daggerMenu;
    private InventoryScrollingMenu lightMenu;
    private InventoryScrollingMenu fireMenu;
    private InventoryScrollingMenu windMenu;
    private InventoryScrollingMenu lightningMenu;
    private InventoryScrollingMenu darkMenu;
    private InventoryScrollingMenu staffMenu;
    private InventoryScrollingMenu usablesMenu;
    
    private InventoryScrollingMenu activeMenu;
    
    public TabbedInventoryScene()
    {
        super();
    }
    
    public void start(ArrayList<Weapon> weapons, ArrayList<Usable> items)
    {
        super.start();
        
        weaponInventory = weapons;
        usableInventory = items;

        swords = new ArrayList<>();
        lances = new ArrayList<>(); 
        axes   = new ArrayList<>();
        bows = new ArrayList<>();
        daggers = new ArrayList<>();
        lights = new ArrayList<>();
        fires = new ArrayList<>();
        winds = new ArrayList<>();
        lightnings = new ArrayList<>();
        darks = new ArrayList<>();
        staves = new ArrayList<>();
        usables = new ArrayList<>();
        
        for (Weapon item : weaponInventory) 
        {
            switch (item.getWeaponType()) 
            {
                case Weapon.SWORD:
                    swords.add(item);
                    break;
                case Weapon.LANCE:
                    lances.add(item);
                    break;
                case Weapon.AXE:
                    axes.add(item);
                    break;
                case Weapon.BOW:
                    bows.add(item);
                    break;
                case Weapon.DAGGER:
                    daggers.add(item);
                    break;
                case Weapon.LIGHT:
                    lights.add(item);
                    break;
                case Weapon.FIRE:
                    fires.add(item);
                    break;
                case Weapon.WIND:
                    winds.add(item);
                    break;
                case Weapon.LIGHTNING:
                    lightnings.add(item);
                    break;
                case Weapon.DARK:
                    darks.add(item);
                    break;
                case Weapon.STAFF:
                    staves.add(item);
                    break;
            }
        }
        
        usables.addAll(usableInventory);
        
        
        swordMenu = new InventoryScrollingMenu(swords, 10, 100, -100);
        lanceMenu = new InventoryScrollingMenu(lances, 10, 100, -100);
        axeMenu = new InventoryScrollingMenu(axes, 10, 100, -100);
        bowMenu = new InventoryScrollingMenu(bows, 10, 100, -100);
        daggerMenu = new InventoryScrollingMenu(daggers, 10, 100, -100);
        lightMenu = new InventoryScrollingMenu(lights, 10, 100, -100);
        fireMenu = new InventoryScrollingMenu(fires, 10, 100, -100);
        windMenu = new InventoryScrollingMenu(winds, 10, 100, -100);
        lightningMenu = new InventoryScrollingMenu(lightnings, 10, 100, -100);
        darkMenu = new InventoryScrollingMenu(darks, 10, 100, -100);
        staffMenu = new InventoryScrollingMenu(staves, 10, 100, -100);
        usablesMenu = new InventoryScrollingMenu(usables, 10, 100, -100);
        
        activeMenu = swordMenu;
        activeMenu.start();
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
            if(im.getLeft() == 1)
                controlStateChange(getControlState()-1);
            if(im.getRight() == 1)
                controlStateChange(getControlState()+1);
            
            activeMenu.respondControls(im);
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
            int amResult = activeMenu.runFrame();
            switch(amResult)
            {
                case InventoryScrollingMenu.BACK:
                    return BACK;
                    
                case InventoryScrollingMenu.NOTHING:
                    return NOTHING;
                    
                default:
                    return amResult;
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
            activeMenu.animate();
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
            activeMenu.draw(g);
        }
    }

    //</editor-fold>
    
    /**
     * Hides the menu no-longer being used, and shows the new one. 
     * @param newCS the new controlState
     */
    public void controlStateChange(int newCS)
    {
        controlState = newCS;
        
        if(getControlState() < 0)
            controlState = 11;
        if(getControlState() > 11)
            controlState = 0;

        activeMenu.end();
        
        switch(getControlState())
        {
            case 0:
                activeMenu = swordMenu;
                break;
            case 1:
                activeMenu = lanceMenu;
                break;
            case 2:
                activeMenu = axeMenu;
                break;
            case 3:
                activeMenu = bowMenu;
                break;
            case 4:
                activeMenu = daggerMenu;
                break;
            case 5:
                activeMenu = lightMenu;
                break;
            case 6:
                activeMenu = fireMenu;
                break;
            case 7:
                activeMenu = windMenu;
                break;
            case 8:
                activeMenu = lightningMenu;
                break;
            case 9:
                activeMenu = darkMenu;
                break;
            case 10:
                activeMenu = staffMenu;
                break;
            case 11:
                activeMenu = usablesMenu;
                break;
        }
        
        activeMenu.start();
    }
    
    
    public Item getItemAt(int loc)
    {
        return activeMenu.getItem(loc);
    }
}
