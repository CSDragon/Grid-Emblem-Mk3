/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Point;
import java.util.ArrayList;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class AI 
{
    private ArrayList<Unit> allUnits;
    private MapScene ms;
    private int allUnitsIndex;
    private int turn;
    private Unit activeUnit;
    private Point moveToPoint;
    private Unit gettingAttacked;
    private ArrayList<Point> movePath;
    private boolean hasMoved;
    private boolean hasPerformedAction;
    
    public static final int DONE = 0;
    public static final int UNITFOUND = 1;
    
    /**
     * Default constructor
     * @param units The list of units in the game.
     * @param _ms the mapscene, so we can check activity.
     */
    public AI(ArrayList<Unit> units, MapScene _ms)
    {
        allUnits = units;
        ms = _ms;
    }
    
    /**
     * Finds the next available unit to have act, and tells it to act, or tells MapScene we're done if we're done
     * @return If we're done or not. -1 is not done, 0 is done.
     */
    public int nextUnit()
    {
        //While we haven't gone out of bounds, and if we've yet to find a unit that is: on the right team, is active and hasn't moved, keep going.
        while(allUnitsIndex < allUnits.size() && !(allUnits.get(allUnitsIndex).getTeam() == turn && allUnits.get(allUnitsIndex).getAIActive() && !allUnits.get(allUnitsIndex).getHasMoved()))
            allUnitsIndex++;
            
        
        if(allUnitsIndex >= allUnits.size())
            return DONE;
        
        activeUnit = allUnits.get(allUnitsIndex);
        
        return UNITFOUND;
    }
    
    /**
     * Finds which units to activate.
     */
    public void activateUnits()
    {
        Unit u;
        for(int i = 0; i< allUnits.size(); i++)
        {
            u = allUnits.get(i);
            //if it's the right turn, and inactive, check if we need to activate it.
            if(u.getTeam() == ms.getTurn() && !u.getAIActive())
            {
                //AI ACTIVATION LOGIC:
                // * If there's any units it could possibly attack this turn, time to activate it.
                PathfindingReport pr = new PathfindingReport(u, true);
                if(pr.getAttackableUnits().size() > 0)
                    u.setAIActive(true);
            }
        }
    }
    
    /**
     * Tells the selected unit how to act.
     * If it can attack something, it will
     * If it can't, it moves to the closest unit.
     */
    public void decideAction()
    {
        //First, get a pathfinding report, and reset for this unit.
        PathfindingReport pr = new PathfindingReport(activeUnit, true);
        gettingAttacked = null;
        movePath = null;
        hasMoved = false;
        hasPerformedAction = false;
        
        //Then, get the list of all locations you can attack people from
        
        //First, get all the units. This will need to be changed once other factions are added, but for now, this is fine.
        //TODO: Add a team allianes chart, and have this get an arraylist for each team.
        ArrayList<Unit> attackableUnits = pr.getAttackableUnits();
        ArrayList<PreBattleReport> reports = new ArrayList<>();
        
        //for each unit you can attack
        for(Unit defender : attackableUnits)
        {
            //for each weapon you can attack with
            for(Weapon wep : activeUnit.getWeaponInventory())
            {
                //Get the list of points this weapon can attack that unit 
                ArrayList<Point> range = Pathfinding.listPointsInARange(wep.getMinRange(), wep.getMaxRange(), defender.getCoord());
                //and only keep the ones we can actually reach
                range.retainAll(pr.getMovableLocations());
                for(Point loc : range)
                {
                    reports.add(new PreBattleReport(activeUnit, wep, loc, defender,ms.getMap()));
                }
            }
        }
        
        PreBattleReport r = PreBattleReport.selectBestReport(reports);
        
        moveToPoint = r.getLocation();
        gettingAttacked = r.getDefender();
        
        /*System.out.println("I am at "+activeUnit.getCoord());
        for(int i = 0; i < attackableUnits.size(); i++)
        {
            PreBattleReport pbr = new PreBattleReport(activeUnit, activeUnit.getEquppedWeapon(), attackableUnits.get(i), ms.getMap());
            System.out.println("I can hit " + attackableUnits.get(i).getName() + " and expect a net of " + pbr.getExpectedNetDamage());
        }
        System.out.println(" ");
        */
        
       
        //HECK WITH THIS: TestAI
        //If it can move right it will.
        //Then, it attacks if able.
        
        /*
        moveToPoint = activeUnit.getCoord();
        Point right = new Point(1 + activeUnit.getCoord().x, 0 + activeUnit.getCoord().y);
        if(ms.getUnitAtPoint(right) == null)
            moveToPoint = right;
        else
            gettingAttacked = ms.getUnitAtPoint(right);
        */
        
        //creates a move path. It includes the location the unit is currently at, at position 0, so a length 1 path goes nowhere.
        movePath = Pathfinding.repath(moveToPoint, activeUnit, pr.getDistanceMap());
        
        //temp
        activeUnit.setHasMoved(true);
        
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
    
    public Unit getActiveUnit()
    {
        return activeUnit;
    }
    
    public Point getMoveToPoint()
    {
        return moveToPoint;
    }
    
    public Unit getGettingAttacked()
    {
        return gettingAttacked;
    }

    /**
     * @return the movePath
     */
    public ArrayList<Point> getMovePath()
    {
        return movePath;
    }

    /**
     * @return the hasMoved
     */
    public boolean getHasMoved() 
    {
        return hasMoved;
    }

    /**
     * @return the hasPerformedAction
     */
    public boolean getHasPerformedAction() 
    {
        return hasPerformedAction;
    }

    /**
     * @param hasMoved the hasMoved to set
     */
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }

    /**
     * @param hasPerformedAction the hasPerformedAction to set
     */
    public void setHasPerformedAction(boolean hasPerformedAction)
    {
        this.hasPerformedAction = hasPerformedAction;
    }
}
