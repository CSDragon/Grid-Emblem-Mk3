/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
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
    private static ArrayList<Point> moveList;
    private static ArrayList<Integer> weightList;
    
    /**
     * Finds the list of available points to move, and their weights.
     * @param u The unit whose range is being checked
     */
    public static void findAllMovableLocations(Unit u)
    {
        ArrayList<Point> p = new ArrayList<>();
        ArrayList<Integer> weight = new ArrayList<>();
        
        int range = u.getMove();
        Point curLoc = new Point(u.getX(), u.getY());
        
        p.add(curLoc);
        weight.add(range);
        
        
        //This loop fills out the list of points.
        int i = 0;
        while(i<p.size())
        {
            //I know I coulda done if(checkPoint()) i--; but I wanted it to be clearer what was going on.
            
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
        
        moveList = p;
        weightList = weight;
        
    }
    
    
    /**
     * Looks at a point and figures out if it's possible to move from one point to another.
     * @param checkPoint The point we're checking
     * @param p the list of points
     * @param weight the weights corrisponding to the list of points
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
                    if(mapScene.getActorAtPoint(checkPoint) == null || mapScene.getActorAtPoint(checkPoint).isPassable())
                    {
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
     * @return the list of points the unit can attack
     */
    public static ArrayList<Point> getAllAttackLocations(Unit u, ArrayList<Point> moveRange)
    {
        ArrayList<Point> p = new ArrayList<>();
        
        Weapon w = u.getEquppedWeapon();
        
        //if we even have a weapon
        if(w != null)
        {
            int min = w.getMinRange();
            int max = w.getMaxRange();

            //and get all the places you can hit from that range.
            for (Point mr : moveRange) 
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
        
        //and finally, remove duplication
        HashSet h = new HashSet(p);
        p.clear();
        p.addAll(h);
        
        return p;
    }
    
    /**
     * Performs getAllAttackLocations with the default movement range of the unit's whole range.
     * @param u the attacking unit
     * @return the list of points the unit can attack
     */
    public static ArrayList<Point> getAllAttackLocations(Unit u)
    {
        findAllMovableLocations(u);
        return getAllAttackLocations(u, moveList);
    }
    
    /**
     * Gets the list of points a unit can attack while standing still
     * @param u The attacking unit.
     * @param p The point at which it is standing
     * @return The list of points it can attack.
     */
    public static ArrayList<Point> getAllAttackLocations(Unit u, Point p)
    {
        ArrayList points = new ArrayList<>();
        points.add(p);
        return getAllAttackLocations(u, points);
    }
    

    
    /**
     * Finds the fastest possible path to get a unit to a point
     * @param dest the place we're going
     * @param u the unit that's pathing
     * @return the path between the two points.
     */
    public static ArrayList<Point> repath(Point dest, Unit u)
    {
        findAllMovableLocations(u);
        return(repath(moveList, weightList, dest, u));
    }
        
    /**
     * Finds the fastest possible path to get a unit to a point
     * @param mL the move list. This should always be this.moveList, but this allows for more applications of the function
     * @param wL the weight list. This should always be this.weightList, but this allows for more applications of the function
     * @param dest the place we're going
     * @param u the unit that's pathing
     * @return the path between the two points.
     */
    public static ArrayList<Point> repath(ArrayList<Point> mL, ArrayList<Integer> wL, Point dest, Unit u)
    {
        ArrayList<Point> ret = new ArrayList();
        
        //where in the list of all points we can go is 
        int index = moveList.indexOf(dest);
        
        //it's not in the list...we can't get there.
        if(index == -1)
            return null;
        
        ret.add(dest);
        
        int i = 0;
        while(i < u.getMove())//there is actually a maximum this time.
        {
            int w = moveList.indexOf(new Point(ret.get(i).x-1, ret.get(i).y  ));
            int n = moveList.indexOf(new Point(ret.get(i).x  , ret.get(i).y-1));
            int e = moveList.indexOf(new Point(ret.get(i).x+1, ret.get(i).y  ));
            int s = moveList.indexOf(new Point(ret.get(i).x  , ret.get(i).y+1));
            
            int ww = 0;
            int nw = 0;
            int ew = 0;
            int sw = 0;
            
            //make sure each one actually exists
            if(w != -1)
                ww = weightList.get(w);
            if(n != -1)
                nw = weightList.get(n);
            if(e != -1)
                ew = weightList.get(e);
            if(s != -1)
                sw = weightList.get(s);
            
            if(nw >= ww && nw >= ew && nw >= sw)
                ret.add(moveList.get(n));
            else if(sw >= ew && sw >= ww) //nw wasn't the largest, so we don't need to compare it.
                ret.add(moveList.get(s));
            else if(ww >= ew) //sw wasn't the largest
                ret.add(moveList.get(w));
            else
                ret.add(moveList.get(e));
            
            if(ret.get(ret.size() -1).equals(u.getCoord()))
            {
                Collections.reverse(ret);
                return ret;
            }
            
            i++;
            
        }
        
        //if we made it this far, something went wrong. But return ret for debug purposes.
        return ret;
    }
    
    
    //Because of this thread, any breakable rocks or stuff are gonna have to be Units.
    /**
     * Finds and returns all attackable enemies and objects in the 
     * @param selectedUnit the unit being checked
     * @return The list of enemy units.
     */
    public static ArrayList<Unit> getAttackableObjects(Unit selectedUnit)
    {
        ArrayList<Unit> ret = new ArrayList();
        
        ArrayList<Point> p = Pathfinding.getAllAttackLocations(selectedUnit, selectedUnit.getCoord());
        
        Unit check;
        
        for(Point checkpoint : p)
        {
            check = mapScene.getUnitAtPoint(checkpoint);
            //TODO this check may not be optimal for non-team objects like destructable rocks.
            if(check != null && !selectedUnit.isAlly(check))
            ret.add(check);
        }
        
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
    
    public static ArrayList<Point> getMoveList()
    {
        return moveList;
    }
    
    public static ArrayList<Integer> getWeightList()
    {
        return weightList;
    }
    
    //</editor-fold>
}
