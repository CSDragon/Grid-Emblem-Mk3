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
public class MainMenu extends GenericMenu 
{
    public static final int NEWGAME = 0;
    public static final int CONTINUE = 1;
    public static final int SETTINGS = 2;
    public static final int EXIT = 3;
    
    public MainMenu()
    {
        super(new String[]{"New Game", "Continue", "Settings", "Exit"});
    }

    
    
}
