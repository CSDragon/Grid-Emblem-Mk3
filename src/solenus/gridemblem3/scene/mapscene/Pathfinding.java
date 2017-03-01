/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class Pathfinding 
{
    private static MapScene mapScene;
    private static Map map;
    
    /**
     * Finds the shortest route a Unit can move to any point on the map.
     * @param u The moving Unit
     * @return A hashmap of distances
     */
    public static HashMap<Point, Integer> mapShortestDistanceFromUnit(Unit u, boolean unconstrained)
    {
        HashMap<Point, Integer> ret = new HashMap<>();
        Queue<Point> queue = new LinkedList<>();
        
        queue.add(u.getCoord());
        ret.put(u.getCoord(), 0);
        
        while(queue.peek() != null)
        {
            Point cur = queue.poll();

            //test each adjacent tile
            Point[] cardinal = new Point[4];
            cardinal[0] = new Point(cur.x  , cur.y+1);
            cardinal[1] = new Point(cur.x+1, cur.y  );
            cardinal[2] = new Point(cur.x  , cur.y-1);
            cardinal[3] = new Point(cur.x-1, cur.y  );
            
            for(Point p : cardinal)
            {
                if(p.x >= 0 && p.x < map.getWidth() && p.y >= 0 && p.y < map.getHeight())
                {
                    int weight = map.getTerrainAtPoint(p).getMoveCost(u.getTransportType());
                    //If we can actually move through this space
                    if(Pathfinding.isPassthroughAllowed(u, p))
                    {
                        weight = ret.get(cur) + weight;
                        //If the map is empty or if this is a shorter path, update the hashmap
                        if(ret.get(p) == null || ret.get(p) > weight)
                        {
                            ret.put(p, weight);
                            queue.add(p);
                        }
                    }
                }
            }
        }
        
        return ret;
    }

    /**
     * Finds the list of available points to move.
     * @param u The unit whose range is being checked
     * @param distanceMap The map of how far the unit has to travel to reach this point.
     * @return the list of all movable locations
     */
    public static ArrayList<Point> listAllMovableLocations(Unit u, HashMap<Point, Integer> distanceMap)
    {
        ArrayList<Point> ret = new ArrayList<>();
        
        for(Point p : distanceMap.keySet())
        {
            if(distanceMap.get(p) <= u.getMove() && distanceMap.get(p) != -1)
                ret.add(p);
        }
        
        return ret;
    }
    
    /**
     * Finds the list of available points to move.
     * @param u The unit whose range is being checked
     * @return the list of all movable locations
     */
    public static ArrayList<Point> listAllMovableLocations(Unit u)
    {
        HashMap<Point, Integer> distanceMap = Pathfinding.mapShortestDistanceFromUnit(u, false);
        return listAllMovableLocations(u, distanceMap);
    }
    
    /**
     * Gets all the points a unit can attack 
     * @param u the attacking unit
     * @param moveRange the range it can move (which determines how far it can move before attacking)
     * @param staffFlag If we're checking a staff, this is true. Otherwise it's false.
     * @return the list of points the unit can attack
     */
    public static ArrayList<Point> listThreatRange(Unit u, ArrayList<Point> moveRange, boolean staffFlag)
    {
        ArrayList<Point> ret = new ArrayList<>();
        
        //Go down the list of weapons.
        for(Weapon w : u.getWeaponInventory())
        {      
            //if we can even EQUIP the weapon
            if(u.canEquipWeapon(w))
            {
                //If we're in staff mode, look for staves. If we're in attack mode look for anything but staves.
                if((w.getWeaponType() == Weapon.STAFF) == staffFlag)
                {
                    int min = w.getMinRange();
                    int max = w.getMaxRange();

                    //and get all the places you can hit from that range.
                    for (Point mr : moveRange) 
                    {
                        //"All movable locations" includes spaces occupied by allies and other objects you can pass through but not stop on.
                        //...Except that the point you're standing is also technically occupied, but we don't want to count that.
                        if(Pathfinding.isStoppingAllowed(u, mr))
                        {
                            for (int i = min; i<= max; i++) 
                            {
                                for (int j = 0; j<i; j++) 
                                {
                                    ret.add(new Point(mr.x-i+j, mr.y+j));
                                    ret.add(new Point(mr.x+j,   mr.y+i-j));
                                    ret.add(new Point(mr.x+i-j, mr.y-j));
                                    ret.add(new Point(mr.x-j,   mr.y-i+j));
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        //and finally, remove duplication
        HashSet h = new HashSet(ret);
        ret.clear();
        ret.addAll(h);
        
        return ret;
    }
    
    /**
     * Performs listThreatRange with the default movement range of the unit's whole range.
     * @param u the attacking unit
     * @param staffMode If we're actually looking for allies to heal, not enemies to attack.
     * @return the list of points the unit can attack
     */
    public static ArrayList<Point> listThreatRange(Unit u, boolean staffMode)
    {
        return Pathfinding.listThreatRange(u, listAllMovableLocations(u), staffMode);
    }
    
    /**
     * Gets the list of points a unit can attack while standing still
     * @param u The attacking unit.
     * @param p The point at which it is standing
     * @param staffMode If we're actually looking for allies to heal, not enemies to attack.
     * @return The list of points it can attack.
     */
    public static ArrayList<Point> listImmediateThreatRange(Unit u, Point p, boolean staffMode)
    {
        ArrayList points = new ArrayList<>();
        points.add(p);
        return Pathfinding.listThreatRange(u, points, staffMode);
    }
    
    /**
     * Finds and returns all attackable enemies and objects this unit can possibly attack.
     * @param selectedUnit the unit being checked
     * @param threatRange The points which this unit can attack into
     * @param staffMode True if we're actually looking for allies to heal, not enemies to attack.
     * @return The list of enemy units.
     */
    public static ArrayList<Unit> listAllAttackableObjects(Unit selectedUnit, ArrayList<Point> threatRange, boolean staffMode)
    {
        ArrayList<Unit> ret = new ArrayList();
        Unit check;
        
        for(Point checkpoint : threatRange)
        {
            check = mapScene.getUnitAtPoint(checkpoint);
            //TODO this check may not be optimal for non-team objects like destructable rocks.
            //Checks if it's an ally or an enemy. Add it if it's an enemy and we're attacking, or if it's an ally and we're healing.
            if(check != null && (selectedUnit.isAlly(check) == staffMode))
            ret.add(check);
        }
        
        return ret;
    }

    //Because of this method, any breakable rocks or stuff are gonna have to be Units.
    /**
     * Finds and returns all attackable enemies and objects in the area
     * @param selectedUnit the unit being checked
     * @param staffMode True if we're actually looking for allies to heal, not enemies to attack.
     * @return The list of enemy units.
     */
    public static ArrayList<Unit> listImmediateAttackableObjects(Unit selectedUnit, boolean staffMode)
    {
        ArrayList<Unit> ret = new ArrayList();
        ArrayList<Point> p = Pathfinding.listImmediateThreatRange(selectedUnit, selectedUnit.getCoord(), staffMode);
        Unit check;
        
        for(Point checkpoint : p)
        {
            check = mapScene.getUnitAtPoint(checkpoint);
            //TODO this check may not be optimal for non-team objects like destructable rocks.
            //Checks if it's an ally or an enemy. Add it if it's an enemy and we're attacking, or if it's an ally and we're healing.
            if(check != null && (selectedUnit.isAlly(check) == staffMode))
            ret.add(check);
        }
        
        return ret;
    }
    
    /**
     * Takes a list of points and a distance map, and returns an ordered list. Additionally removes points not in the list.
     * @param allSelectedPoints The list of points to be ordered 
     * @param map The distance map.
     * @return A copy of allSelectedPoints that's been ordered.
     */
    public static ArrayList<Point> sortLocationsByDistance(ArrayList<Point> allSelectedPoints, HashMap<Point, Integer> map)
    {
        ArrayList<Point> ret = new ArrayList<>();
        
        int min = -1;
        int max = -1;
        for(int i = 0; i< allSelectedPoints.size(); i++)
        {
            if(map.get(allSelectedPoints.get(i)) != null)
            {
                if(map.get(allSelectedPoints.get(i)) < min || min == -1)
                    min = map.get(allSelectedPoints.get(i));

                if(map.get(allSelectedPoints.get(i)) > max)
                    max = map.get(allSelectedPoints.get(i));
            }
        }
        
        //this is a horribly slow way to do this at larger sizes, but this will never be large, so it's ok to brute force.
        for(int i = min; i<= max; i++)
        {
            for(int j = 0; j<allSelectedPoints.size(); j++)
                if(map.get(allSelectedPoints.get(j))!= null && map.get(allSelectedPoints.get(j)) == i)
                    ret.add(allSelectedPoints.get(j));
        }
        
        return ret;
    }

    /**
     * Finds the fastest possible path to get a unit to a point
     * @param dest the place we're going
     * @param u the unit that's pathing
     * @param weightMap the map of distances from the unit's point
     * @return the path between the two points.
     */
    public static ArrayList<Point> repath(Point dest, Unit u, HashMap<Point, Integer> weightMap)
    {
        //set up return object
        ArrayList<Point> ret = new ArrayList<>();
        
        //set up stuff
        Point start = u.getCoord();
        
        //start MUST be the center of the weight map, or it doesn't even make sense.
        if(!weightMap.containsKey(start) || weightMap.get(start) != 0)
            return ret;
        
        //just check we can actually get to dest
        if(weightMap.containsKey(dest))
        {
            //add dest and let's get cracking
            ret.add(dest);
            Point top = dest;
            while (!top.equals(start))
            {
                //get the points in each direction
                Point n = new Point(top.x  , top.y-1);
                Point w = new Point(top.x-1, top.y  );
                Point s = new Point(top.x  , top.y+1);
                Point e = new Point(top.x+1, top.y  );
                
                //find the weights in those 4 points
                int nw = Integer.MAX_VALUE;
                int ww = Integer.MAX_VALUE;
                int sw = Integer.MAX_VALUE;
                int ew = Integer.MAX_VALUE;
                
                //make sure we can even reach those points
                if(weightMap.containsKey(n))
                    nw = weightMap.get(n);
                if(weightMap.containsKey(w))
                    ww = weightMap.get(w);
                if(weightMap.containsKey(s))
                    sw = weightMap.get(s);
                if(weightMap.containsKey(e))
                    ew = weightMap.get(e);
                
                
                //we want the point with the lowest remaining distance.
                //prioritise N,S,W,E. Because why not.
                //there's prolly a better way to do this, but whatever.
                //TODO: Randomize among equal directions.
                
                //is north the closest?
                if(nw <= ww && nw <= ew && nw <= sw)
                    top = n;
                else if(sw <= ew && sw <= ww) //north wasn't the closest, so we don't need to compare it.
                    top = s;
                else if(ww <= ew) //west wasn't the closest
                    top = w;
                else //gotta be east.
                    top = e;
                
                ret.add(top);
            }
            
            //it's actually backwards, so let's fix that.
            Collections.reverse(ret);
        }
        
        return ret;
    }
    
    /**
     * Determines if a unit is allowed to pass through a point, or if it is blocked.
     * @param u The unit moving
     * @param p The point it's moving through
     * @return If the unit is allowed to pass through (without stopping)
     */
    public static boolean isPassthroughAllowed(Unit u, Point p)
    {
                //If the unit can move on this terrain, AND
        return ((map.getTerrainAtPoint(p).getMoveCost(u.getTransportType()) != -1)&&
                //If the location is empty of Actors, or if the actor at that location is passable, AND
                (mapScene.getActorAtPoint(p) == null || mapScene.getActorAtPoint(p).isPassable())&&
                //If the location is empty of Units, or if the unit at that location is an ally
                (mapScene.getUnitAtPoint(p) == null || mapScene.getUnitAtPoint(p).isAlly(u)));
    }
    
    /**
     * Determines if a unit is allowed to stop at a point, or if it is blocked.
     * @param u The unit moving
     * @param p The point it's stopping at
     * @return If the unit is allowed stop there.
     */
    public static boolean isStoppingAllowed(Unit u, Point p)
    {
                //If the unit is able to move through this location, AND
        return ((map.getTerrainAtPoint(p).getMoveCost(u.getTransportType()) != -1)&&
                //If the location is empty of Actors (unless that actor is already itself)
                (mapScene.getActorAtPoint(p) == null) || mapScene.getUnitAtPoint(p) == u);
    }
    
    //<editor-fold desc="Getters and setters">
    
    /**
     * sets the map
     * @param m the gamemap
     * @param ms the map scene
     */
    public static void setMap(Map m, MapScene ms)
    {
        map = m;
        mapScene = ms;
    }
    
    //</editor-fold>
}
