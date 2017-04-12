/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Chris
 */
public class Team 
{
    private int teamNumber; 
    private String name;
    private boolean isHuman;
    private int playerNumber; //For multiplayer only. Tells which human should control this faction.
    private boolean[] alliances;
    
    /**
     * custom team constructor
     * @param teamNumber The team's ID number.
     * @param name The team's name
     * @param isHuman If the team is human-controlled or not
     * @param playerNumber If the team is human-controlled, which human controls it (for multiplayer)
     * @param alliances This team's relationship with the other teams. 1 for allied. 0 for enemy.
     */
    public Team(int teamNumber, String name, boolean isHuman, int playerNumber, boolean[] alliances)
    {
        this.teamNumber = teamNumber;
        this.name = name;
        this.isHuman = isHuman;
        this.playerNumber = playerNumber;
        this.alliances = alliances;
    }
    
    /**
     * Read team data from file.
     * @param in The file reader
     * @param teamNum The number of team we're on.
     * @param numberOfTeams The total number of teams
     * @throws IOException 
     */
    public Team(BufferedReader in, int teamNum, int numberOfTeams) throws IOException
    {
        teamNumber = teamNum;
        name = in.readLine().substring(6);
        isHuman = in.readLine().substring(6).equals("Human");
        if(isHuman)
            playerNumber = Integer.parseInt(in.readLine().substring(8));
        else
            playerNumber = -1;
        alliances = new boolean[numberOfTeams];
        String allies = in.readLine().substring(11);
        for(int i = 0; i< numberOfTeams; i++)
            alliances[i] = allies.charAt(i) == '1';
        
    }

    /**
     * @return the teamNumber
     */
    public int getTeamNumber() 
    {
        return teamNumber;
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @return the isHuman
     */
    public boolean getIsHuman() 
    {
        return isHuman;
    }

    /**
     * @return the playerNumber
     */
    public int getPlayerNumber() 
    {
        return playerNumber;
    }

    /**
     * @return the alliances
     */
    public boolean[] getAlliances() 
    {
        return alliances;
    }
    
    public boolean getAlliance(int teamNum)
    {
        if(teamNum < 0 || teamNum >= alliances.length)
            return false;
        return alliances[teamNum];
    }
}
