/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class SystemActionMenu extends GenericMenu
{
    public static final int UNITSTATS = 0;
    public static final int OPTIONS = 1;
    public static final int SAVE = 2;
    public static final int ENDTURN = 3;
    
    public SystemActionMenu()
    {
        super(new String[]{"Units", "Options", "Save", "End Turn"});
    }
    
    
}
