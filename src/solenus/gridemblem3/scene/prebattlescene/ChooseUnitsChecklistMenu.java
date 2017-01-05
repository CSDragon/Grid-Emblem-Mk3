/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.prebattlescene;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.party.PartyUnit;
import solenus.gridemblem3.scene.mapscene.Map;
import solenus.gridemblem3.ui.menu.GenericChecklistMenu;

/**
 *
 * @author Chris
 */
public class ChooseUnitsChecklistMenu extends GenericChecklistMenu
{
    private ArrayList<Unit> army;
    private ArrayList<Unit> mandatoryUnits;
    
    private int numSelected;
    private int maxSelected;
    
    public ChooseUnitsChecklistMenu()
    {
        super();
    }
    
    /**
     * progresses the game
     * @return the exit status
     */
    public int runFrame()
    {
        if(active)
        {
            if(upTrigger)
                cursorLoc--;
            if(downTrigger)
                cursorLoc++;
            if(cursorLoc < 0)
                cursorLoc = numCommands;
            if(cursorLoc > numCommands)
                cursorLoc = 0;

            //If B, exit the unit action box
            if(bTrigger)
            {
                resetTriggers();
                return BACK;
            }
            
            //If A,select which enemy to attack
            if(aTrigger)
            {
                if(cursorLoc == numCommands)
                {
                    resetTriggers();
                    return CONFIRM;
                }
                
                if(cursorLoc >= mandatoryUnits.size())
                {
                    if(selected[cursorLoc])
                    {
                        selected[cursorLoc] = false;
                        numSelected--;
                    }
                    else if(numSelected < maxSelected)
                    {
                        selected[cursorLoc] = true;
                        numSelected++;
                    }
                }
                
            }
        }
        
        return NOTHING;
    }
    
    /**
     * Starts up the checklist
     * @param pd The savedata, where the army is stored.
     * @param m The mapdata, where the mandatory units are stored.
     */
    public void start(PlayerData pd, Map m)
    {
        ArrayList<PartyUnit> pus = pd.getUnitList();
        ArrayList<String> manNames = m.getMandatoryPlayerUnits();
        army = new ArrayList<>();
        mandatoryUnits = new ArrayList<>();
        
        for(PartyUnit pu : pus)
        {
            Unit u = new Unit(pu);
            army.add(u);
            for(String name : manNames)
            {
                if (pu.getName().equals(name))
                    mandatoryUnits.add(u);
            }
        }

        super.start(army.size());
        
        maxSelected = m.getStartingPlayerLocations().size() + mandatoryUnits.size();
        if(maxSelected > numCommands)
            maxSelected = numCommands;
        numSelected = maxSelected;

        
        for(int i = 0; i<maxSelected; i++)
            selected[i] = true;
        
        reOrderArmy();
        
        //super.start initializes mandatory
        try
        {
            for(Unit man : mandatoryUnits)
                mandatory[army.indexOf(man)] = true;
        }
        //If this ever gives a -1, that means 
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "A unit did not exist in your army that this level deems mandetory. This is a game-breaking problem, and you'll have to contact me directly if you run into it.");
            System.exit(-1);
        }
    }

    /**
     * Resumes the checklist from where it left off
     */
    public void resume()
    {
        super.resume();
        reOrderArmy();
    }
    
    /**
     * Changes the order of the army, so the mandatories are first, the selecteds are next, and the unselecteds after
     */
    public void reOrderArmy()
    {
        ArrayList<Unit> newOrder = new ArrayList<>();
        
        for(Unit u:mandatoryUnits)
            newOrder.add(u);
        
        for(int i = 0; i< army.size(); i++)
        {
            if(selected[i] && !newOrder.contains(army.get(i)))
                newOrder.add(army.get(i));
        }
        
        for(int i = 0; i< army.size(); i++)
        {
            if(!selected[i] && !newOrder.contains(army.get(i)))
                newOrder.add(army.get(i));
        }
        
        army.clear();
        army.addAll(newOrder);
        
        for(int i = 0; i < numSelected; i++)
            selected[i] = true;
        for(int i = numSelected; i < numCommands; i++)
            selected[i] = false;
        
        redoActions();
    }
    
    /**
     * Updates the checklist's options
     */
    public void redoActions()
    {
        numCommands = army.size();
        actions = new String[numCommands];
        for(int i = 0; i< numCommands; i++)
            actions[i] = army.get(i).getName();
    }
    
    /**
     * Get the selected units.
     * @return The selected units
     */
    public ArrayList<Unit> getSelectedUnits()
    {
        ArrayList<Unit> ret = new ArrayList<>();
        
        for(int i = 0; i < numCommands; i++)
            if(selected[i] || mandatory[i])
                ret.add(army.get(i));
        
        return ret;
    }
}
