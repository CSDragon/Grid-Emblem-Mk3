/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.render;

import java.util.TreeMap;

/**
 *
 * @author Chris
 */
public class Animation implements Comparable<Animation> 
{
    private int startX;
    private int startY;
    private int numFrames;
    private int speed;
    
    private String name;
    
    private TreeMap<String, Transition> transitionMap;
    
    
    /**
     * Standard constructor
     * @param x Sprite Sheet Animation Start Coord
     * @param y Sprite Sheet Animation Start Coord
     * @param num Number of frames in the animation
     * @param n Animation Name
     * @param s Number of game frames this animation should take per frame of animation.
     */
    public Animation(String n, int x, int y, int num, int s)
    {
        startX = x;
        startY = y;
        numFrames = num;
        name = n;
        speed = s;
        
        transitionMap = new TreeMap<String, Transition>();
    }
    
    /**
     * Adds a new Transition to the animation
     * @param t 
     */
    public void addTransition(String trigger, Transition t)
    {
        transitionMap.put(trigger, t);
    }
    
    
    public Transition getTransition(String trigger)
    {
        return transitionMap.get(trigger);
    }
    
    /**
     * Compares to another animation
     * @param other the one being compared to
     * @return if it's comparable
     */
    public int compareTo(Animation other)
    {
        return name.compareTo(other.getName());
    }
    

    /**
     * @return the startX
     */
    public int getStartX() 
    {
        return startX;
    }

    /**
     * @return the startY
     */
    public int getStartY() 
    {
        return startY;
    }

    /**
     * @return the numFrames
     */
    public int getNumFrames() 
    {
        return numFrames;
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * @return The number of game frames each animation frame lasts
     */
    public int getSpeed()
    {
        return speed;
    }
    
    public String toString()
    {
        return name;
    }
}
