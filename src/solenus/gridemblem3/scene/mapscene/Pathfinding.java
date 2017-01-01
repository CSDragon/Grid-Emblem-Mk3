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
    private static ArrayList<Integer> weightList;
    
    /**
     * Finds the list of available points to move, and their weights.
     * @param u The unit whose range is being checked
     * @return the list of all movable locations
     */
    public static ArrayList<Point> listAllMovableLocations(Unit u)
    {
        ArrayList<Point> p = new ArrayList<>();
        ArrayList<Integer> weight = new ArrayList<>();
        ArrayList<Point> alliedLocations = new ArrayList<>();
        
        int range = u.getMove();
        Point curLoc = new Point(u.getX(), u.getY());
        
        p.add(curLoc);
        weight.add(range);
        
        
        //This loop fills out the list of points.
        int i = 0;
        while(i<p.size())
        {
            //I know I coulda done if(checkPoint(...)) i--; but I wanted it to be clearer what was going on.
            
            //check the weast ponit
            boolean west = checkPoint(new Point(p.get(i).x-1, p.get(i).y), p, weight, i, u);
            if(west)
                i--;
            
            //check the north point.
            boolean north = checkPoint(new Point(p.get(i).x, p.get(i).y-1), p, weight, i, u);
            if(north)
                i--;
            
            //check the east point.
            boolean east = checkPoint(new Point(p.get(i).x+1, p.get(i).y), p, weight, i, u);
            if(east)
                i--;
            
            //check the south point.
            boolean south = checkPoint(new Point(p.get(i).x, p.get(i).y+1), p, weight, i, u);
            if(south)
                i--;
               
            i++;
        }
        
        weightList = weight;
        return p;        
    }
    
    
    /**
     * Looks at a point and figures out if it's possible to move from one point to another.
     * @param checkPoint The point we're checking
     * @param p the list of points
     * @param weight the weights corresponding to the list of points
     * @param i the index of p and weight that we're looking at.
     * @param moveType the way the unit we're checking moves
     * @return true if we deleted an older point that we found a faster route to. False in any other situation
     */
    private static boolean checkPoint(Point checkPoint, ArrayList<Point> p, ArrayList<Integer> weight, int i, Unit u)
    {
        //if not out of bounds
        if(map.getTerrainAtPoint(checkPoint) != null)
        {
            //getMovementCost isn't the fastest process, so better to write it down
            int checkPointWeight = map.getTerrainAtPoint(checkPoint).getMoveCost(u.getTransportType());

            //If the terrain can be passed, and we have enough movement left to even get there 
            if(checkPointWeight != -1 && checkPointWeight <= weight.get(i))
            {
                //make the point and check if it already exists.
                int index = p.indexOf(checkPoint);

                //if it doesn't exist yet, we've hit a new point. Add it to the lineup
                if(index == -1)
                {
                    //If the location is empty of Actors, or if the actor at that location is passable
                    if(mapScene.getActorAtPoint(checkPoint) == null || mapScene.getActorAtPoint(checkPoint).isPassable())
                    {
                        //If the location is empty of Units, or if the unit at that location is an ally
                        if(mapScene.getUnitAtPoint(checkPoint) == null || mapScene.getUnitAtPoint(checkPoint).isAlly(u))
                        {
                            p.add(checkPoint);
                            weight.add(weight.get(i)-checkPointWeight);
                        }
                    }
                }
                //else if, we've found a faster route to an existing point, remove the old pair and add the new one at the end so we see it again.
                else if(weight.get(index) < weight.get(i)-checkPointWeight)
                {
                    p.remove(index);
                    weight.remove(index);
                    p.add(checkPoint);
                    weight.add(weight.get(i)-checkPointWeight);
                    if(index < i)
                        return true;
                }
                //else, we found a point we've already traveled to, but in a slower way. Do nothing
            }
        }
            
        return false;
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
        ArrayList<Point> p = new ArrayList<>();
        
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
                        if(mapScene.getActorAtPoint(mr) == null || mapScene.getUnitAtPoint(mr) == u)
                        {
                            for (int i = min; i<= max; i++) 
                            {
                                for (int j = 0; j<i; j++) 
                                {
                                    p.add(new Point(mr.x-i+j, mr.y+j));
                                    p.add(new Point(mr.x+j,   mr.y+i-j));
                                    p.add(new Point(mr.x+i-j, mr.y-j));
                                    p.add(new Point(mr.x-j,   mr.y-i+j));
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        //and finally, remove duplication
        HashSet h = new HashSet(p);
        p.clear();
        p.addAll(h);
        
        return p;
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
     * Finds and returns all attackable enemies and objects this unit can possibly attack.
     * @param selectedUnit the unit being checked
     * @param staffMode True if we're actually looking for allies to heal, not enemies to attack.
     * @return The list of enemy units.
     */
    public static ArrayList<Unit> listAllAttackableObjects(Unit selectedUnit, boolean staffMode)
    {
        ArrayList<Unit> ret = new ArrayList();
        ArrayList<Point> p = Pathfinding.listThreatRange(selectedUnit, staffMode);
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
     * Finds the shortest route a Unit can move to any point on the map.
     * @param u The moving Unit
     * @return A hashmap of distances
     */
    public static HashMap<Point, Integer> mapShortestDistanceFromUnit(Unit u)
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
                    //If it's not impassable, if it's not occupied, or if it is occupied, if it's an ally, we can move to this space
                    if(weight != -1 && (mapScene.getUnitAtPoint(p) == null || u.isAlly(mapScene.getUnitAtPoint(p))))
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
     * Given a list of attackable objects, and a threat range, creates a list of all spaces from which calling unit can initiate an attack.
     * @param attackableUnits The units the calling unit can attack.
     * @param threatLocations The locations the calling unit threatens.
     * @return The list of locations the calling unit can attack something from.
     * PROBLEM: This should be called before weapon select
     */
    public static ArrayList<Point> listAttackLocations(ArrayList<Unit> attackableUnits, ArrayList<Point> threatLocations)
    {
        ArrayList<Point> ret = new ArrayList();
        
        
        
        return ret;
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
