/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.render;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Chris
 */
public class Transition implements Comparable<Transition> 
{
    public Animation target;
    public String trigger;
    public ArrayList<String> options;
    
    
    
    public Transition(Animation ta, String tr, String o)
    {
        target = ta;
        trigger = tr;
        String[] arr = o.split(",");
        options = new ArrayList<String>(Arrays.asList(arr));
    }
    
    
    
    /**
     * Compares to another animation
     * @param other the one being compared to
     * @return if it's comparable
     */
    public int compareTo(Transition other)
    {
        return trigger.compareTo(other.getTrigger());
    }
    
    public Animation getTarget()
    {
        return target;
    }
    
    public String getTrigger()
    {
        return trigger;
    }
    
    public ArrayList<String> getOption()
    {
        return options;
    }
    
    public String toString()
    {
        return trigger + " triggers " + target.toString();
    }
}
