/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.topmenuscene;

import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
public class AudioMenu extends GenericMenu
{
    public static final int MASTER = 0;
    public static final int SFX = 1;
    public static final int MUSIC = 2;
    
    public AudioMenu()
    {
        super(new String[]{"Master","SFX","Music"});
        
    }
}
