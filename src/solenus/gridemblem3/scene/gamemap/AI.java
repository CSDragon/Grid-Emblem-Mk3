/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.gamemap;

import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;


/**
 *
 * @author Chris
 */
public class AI 
{
    private ArrayList<Unit> allUnits;
    private int allUnitsIndex;
    private int turn;
    
    /**
     * Default constructor
     * @param units The list of units in the game.
     */
    public AI(ArrayList<Unit> units)
    {
        allUnits = units;
    }
    
    
    /**
     * Finds the next available unit to have act, and tells it to act, or tells MapScene we're done if we're done
     * @return If we're done or not. -1 is not done, 0 is done.
     */
    public int runFrame()
    {
        //While we haven't gone out of bounds, and if we've yet to find a unit that is both on the right team and hasn't moved.
        while(allUnitsIndex < allUnits.size() && !(allUnits.get(allUnitsIndex).getTeam() == turn && !allUnits.get(allUnitsIndex).getHasMoved()))
            allUnitsIndex++;
            
        
        if(allUnitsIndex >= allUnits.size())
            return 0;
        
        act(allUnits.get(allUnitsIndex));
        
        return -1;
    }
    
    /**
     * Tells the unit how to act. TODO
     * @param u The unit to act
     */
    public void act(Unit u)
    {
        u.endMovement();
    }
    
    /**
     * Starts the AI
     * @param t Who's turn it is
     */
    public void start(int t)
    {
        turn = t;
        allUnitsIndex = 0;
    }
}
