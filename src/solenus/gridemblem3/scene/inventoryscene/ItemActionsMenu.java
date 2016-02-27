/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.inventoryscene;

import solenus.gridemblem3.ui.menu.GenericMenu;

/**
 *
 * @author Chris
 */
class ItemActionsMenu extends GenericMenu
{
    public ItemActionsMenu()
    {
        super(new String[]{"Take", "Store", "Trade", "Equip", "Use"});
    }
}
