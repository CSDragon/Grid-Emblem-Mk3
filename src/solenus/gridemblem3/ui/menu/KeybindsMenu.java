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
public class KeybindsMenu extends GenericMenu
{
    public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int A = 4;
	public static final int B = 5;
	public static final int X = 6;
	public static final int Y = 7;
	public static final int L = 8;
	public static final int R = 9;
	public static final int CONFIRM = 10;
    
    public KeybindsMenu()
    {
        super(new String[]{"Up", "Down", "Left", "Right", "A", "B", "X", "Y", "L", "R", "Confirm"});
    }
    
}
