/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import solenus.gridemblem3.actor.Unit;

/**
 *  This class is a template for the future. It has no actual place in the current build.
 * Let's redo pathfinding:
	When a unit is selected (for varius reasons), a pathfinding report is generated.
	The following things happen. 
		1) The shortest distance map is created.
		2) All movable locations are taken from the map.
		3) All threat locations are taken from the movable locations.
		4) All units it can attack are listed
		5) All locations you can attack from are listed.
		6) This can then be modified to add other things
 * @author Chris
 */
public class PathfindingReport 
{
    private Unit unitMapped;
    private boolean unlimited;
    private HashMap<Point, Integer> distanceMap;
    private ArrayList<Point> movableLocations;
    private ArrayList<Point> stoppableLocations;
    private ArrayList<Point> threatRange;
    private ArrayList<Point> staffRange;
    private ArrayList<Unit> attackableUnits;
    private ArrayList<Unit> staffableUnits;
     
    /**
     * 
     * @param u
     * @param unconstrained True if you are allowing this to check the entire map, or false if just the movement range.
     */
    public PathfindingReport(Unit u, boolean unconstrained)
    {
        unitMapped = u;
        unlimited = unconstrained;
        distanceMap = Pathfinding.mapShortestDistanceFromUnit(unitMapped, unlimited);
        movableLocations = Pathfinding.listAllMovableLocations(unitMapped, distanceMap);
        stoppableLocations = Pathfinding.listAllStoppableLocations(unitMapped, movableLocations);
        threatRange = Pathfinding.listThreatRange(unitMapped, stoppableLocations, false);
        staffRange = Pathfinding.listThreatRange(unitMapped, stoppableLocations, true);
        attackableUnits = Pathfinding.listAllAttackableObjects(unitMapped, threatRange, false);
        staffableUnits = Pathfinding.listAllAttackableObjects(unitMapped, staffRange, true);
    }

    /**
     * Updates the pathfinding report to the latest data
     */
    public void update()
    {
        distanceMap = Pathfinding.mapShortestDistanceFromUnit(unitMapped, unlimited);
        movableLocations = Pathfinding.listAllMovableLocations(unitMapped, distanceMap);
        threatRange = Pathfinding.listThreatRange(unitMapped, stoppableLocations, false);
        staffRange = Pathfinding.listThreatRange(unitMapped, stoppableLocations, false);
        attackableUnits = Pathfinding.listAllAttackableObjects(unitMapped, threatRange, false);
        staffableUnits = Pathfinding.listAllAttackableObjects(unitMapped, staffRange, true);
    }
    
    //<editor-fold desc="getters and setters">
    
    /**
     * Finds the distance unit has to travel to reach point p, by looking it up on the weight table.
     * @param p The location this unit would have to travel to
     * @return the distance
     */
    public int getDistanceTo(Point p)
    {
        return distanceMap.get(p);
    }
    
    /**
     * @return the unitMapped
     */
    public Unit getUnitMapped() 
    {
        return unitMapped;
    }
    
    /**
     * @return unlimited
     */
    public boolean isUnlimited()
    {
        return unlimited;
    }
    /**
     * @return the distanceMap
     */
    public HashMap<Point, Integer> getDistanceMap() 
    {
        return distanceMap;
    }
    
    /**
     * @return the movableLocations
     */
    public ArrayList<Point> getMovableLocations() 
    {
        return movableLocations;
    }
    
    public ArrayList<Point> getStoppableLocations()
    {
        return stoppableLocations;
    }

    /**
     * @return the locations this unit can attack
     */
    public ArrayList<Point> getThreatRange() 
    {
        return threatRange;
    }
    
    /**
     * @return the locations this unit can use a staff on
     */
    public ArrayList<Point> getStaffRange() 
    {
        return staffRange;
    }

    /**
     * @return the attackableUnits
     */
    public ArrayList<Unit> getAttackableUnits() 
    {
        return attackableUnits;
    }

    /**
     * @return the staffableUnits
     */
    public ArrayList<Unit> getStaffableUnits() 
    {
        return staffableUnits;
    }
   
    //</editor-fold>
}
