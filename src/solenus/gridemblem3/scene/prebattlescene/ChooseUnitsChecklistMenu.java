/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.prebattlescene;

import java.util.ArrayList;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.scene.gamemap.Map;
import solenus.gridemblem3.ui.menu.GenericChecklistMenu;

/**
 *
 * @author Chris
 */
public class ChooseUnitsChecklistMenu extends GenericChecklistMenu
{
    private ArrayList<Unit> army;
    private ArrayList<Unit> mandatoryUnits;
    
    public ChooseUnitsChecklistMenu()
    {
        super();
    }
    
    //REMOVE LATER
    public void start()
    {
        super.start(new String[]{"asdf","jkl;"});
    }
    
    public void start(PlayerData pd, Map m)
    {
        ArrayList<String> names = new ArrayList<>();
        for(Unit u:army)
            names.add(u.getName());
    }
    
    
    
}
