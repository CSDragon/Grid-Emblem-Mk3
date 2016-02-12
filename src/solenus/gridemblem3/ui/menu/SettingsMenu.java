/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.ui.menu;

/**
 *
 * @author Chris
 */
public class SettingsMenu extends GenericMenu
{
    public static final int KEYBINDS = 0;
    public static final int GRAPHICS = 1;
    public static final int AUDIO    = 2;
    
    public SettingsMenu()
    {
        super(new String[]{"Keybinds", "Graphics", "Audio"});
    }
}
