/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
    private HashMap<Point, Integer> distanceMap;
    private ArrayList<Point> movableLocations;
    private ArrayList<Unit> attackableUnits;
    private ArrayList<Unit> staffableUnits;
    
     
    public PathfindingReport(Unit u)
    {
        unitMapped = u;
        distanceMap = Pathfinding.mapShortestDistanceFromUnit(unitMapped);
        movableLocations = Pathfinding.listAllMovableLocations(unitMapped);
        attackableUnits = Pathfinding.listAllAttackableObjects(unitMapped, true);
        staffableUnits = Pathfinding.listAllAttackableObjects(unitMapped, false);
    }

    /**
     * Updates the pathfinding report to the latest data
     */
    public void update()
    {
        distanceMap = Pathfinding.mapShortestDistanceFromUnit(unitMapped);
        movableLocations = Pathfinding.listAllMovableLocations(unitMapped);
        attackableUnits = Pathfinding.listAllAttackableObjects(unitMapped, true);
        staffableUnits = Pathfinding.listAllAttackableObjects(unitMapped, false);
    }
    
    //<editor-fold desc="getters and setters">
    
    public int getDistanceAt(Point p)
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
