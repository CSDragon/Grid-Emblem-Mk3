/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.dialoguescene;

import solenus.gridemblem3.scene.dialoguescene.InfoEvent;
import java.util.ArrayList;
import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class InfoMenu extends GenericMenu
{
    private ArrayList<InfoEvent> events;
    
    public InfoMenu(ArrayList<InfoEvent> ie, ArrayList<Boolean> seenEvent)
    {
        super(eventListNameArray(ie));
        ie = events;
    }
    
    public static String[] eventListNameArray(ArrayList<InfoEvent> ie)
    {
        String[] ret = new String[ie.size()];
        
        for(int i = 0; i<ie.size(); i++)
            ret[i] = ie.get(i).getName();
        
        return ret;
    }
}
