/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.party.PartyUnit;

/**
 *
 * @author Chris
 */
public class Map 
{
    public static final int WINCON_ROUTE = 0;
    public static final int WINCON_PRIORITYTARGETS = 1;
    public static final int WINCON_CAPTURE = 2;
    
    private int height;
    private int width;
    private boolean skipSetup;
    private String name;
    private int idNum;
    
    private int winCondition;
    
    private BufferedImage mapImage;
    
    private ArrayList<Terrain> terrainList;
    
    //Places the player can put their units.
    private ArrayList<Point> startingPlayerLocations;
    
    //Units that start on the map
    private ArrayList<Unit> startingUnits;
    private ArrayList<Point> startingUnitLocations;
    
    //Reinforcements.
    private ArrayList<Unit> reinforcements;
    private ArrayList<Point> reinforcementLocations;
    private ArrayList<Integer> reinforcementTurnTimings;
    
    private ArrayList<String> mandatoryPlayerUnits;
    private ArrayList<Point> mandatoryPlayerUnitLocations;
    
    public Map()
    {
        terrainList = new ArrayList<>();
        startingPlayerLocations = new ArrayList<>();
        startingUnits = new ArrayList<>();
        startingUnitLocations = new ArrayList<>();
        reinforcements = new ArrayList<>();
        reinforcementLocations = new ArrayList<>();
        reinforcementTurnTimings = new ArrayList<>();
        mandatoryPlayerUnits = new ArrayList<>();
        mandatoryPlayerUnitLocations = new ArrayList<>();
    }
    
    public Map(PlayerData pd)
    {
        this();
        idNum = pd.getMapNum();
    
        try
        {
            mapImage = ImageIO.read(new File("assets/levels/"+idNum+"/map.png"));
            BufferedReader in = new BufferedReader(new FileReader("assets/levels/"+idNum+"/mapdata.map"));
            
            
            //get basic data
            name = in.readLine().substring(6);
            width = Integer.decode(in.readLine().substring(7));
            height = Integer.decode(in.readLine().substring(8));
            skipSetup = in.readLine().substring(12).equals("true");
            winCondition = Integer.decode(in.readLine().substring(15));

            
            //discard extra lines
            in.readLine();
            in.readLine();
            
            //read in terrain
            terrainList = new ArrayList();
            for(int i = 0; i<height;i++)
            {
                ArrayList<String> terrains = parseComma(in.readLine());
                for(String t: terrains)
                    terrainList.add(new Terrain(t));
            }
            in.readLine();
            
            //Read in player army starting locations
            in.readLine();
            int numPoints = Integer.parseInt(in.readLine().substring(30));
            for(int i = 0; i<numPoints; i++)
                startingPlayerLocations.add(loadPoint(parseComma(in.readLine())));
            in.readLine();
            
            //Read in starting Units
            in.readLine();
            int numUnits = Integer.parseInt(in.readLine().substring(26));
            in.readLine();
            for(int i = 0; i<numUnits; i++)
            {
                PartyUnit pu = new PartyUnit(in);
                startingUnits.add(new Unit(pu));
            }
            
            //Read in the staring unit locations.
            in.readLine();
            for(int i = 0; i<numUnits; i++)
                startingUnitLocations.add(loadPoint(parseComma(in.readLine())));
            in.readLine();
            
            //Reinforcements
            in.readLine();
            int numReinforcements = Integer.parseInt(in.readLine().substring(26));
            in.readLine();
            
            for(int i = 0; i< numReinforcements; i++)
            {
                PartyUnit pu = new PartyUnit(in);
                reinforcements.add(new Unit(pu));
            }
            
            //Reinforcement Locations
            in.readLine();
            for(int i = 0; i< numReinforcements; i++)
                reinforcementLocations.add(loadPoint(parseComma(in.readLine())));
            in.readLine();
            
            //Reinforcement Timings
            in.readLine();
            for(int i = 0; i< numReinforcements; i++)
                reinforcementTurnTimings.add(Integer.parseInt(in.readLine().substring(5)));
            in.readLine();

            //sets the mandatory player units.
            in.readLine();
            int numManUnits = Integer.parseInt(in.readLine().substring(34));
            
            ArrayList<String> manNames = new ArrayList<>();
            
            for(int i = 0; i<numManUnits; i++)
                manNames.add(in.readLine());
            in.readLine();
            
            in.readLine();
            for(int i = 0; i<numManUnits; i++)
                mandatoryPlayerUnitLocations.add(loadPoint(parseComma(in.readLine())));
            in.readLine();
            
                        
            
            //Set the starting units's locations
            for(int i = 0; i< numUnits; i++)
                startingUnits.get(i).moveInstantly(startingUnitLocations.get(i));
            
            //Set the reinforcements's locations.
            for(int i = 0; i< numReinforcements; i++)
                reinforcements.get(i).moveInstantly(reinforcementLocations.get(i));
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Critical Error:\nMap no map found for Stage "+idNum+". I have no clue how, but ya bork'd ");
        }
    }

    /**
     * Saves a map for map creation purposes.
     * @param mapNum The map we're saving to. This should not be used for temp saves because of this.
     */
    public void saveMap(int mapNum)
    {
        File saveFile = new File("assets/levels/"+mapNum+"/mapData.map");
        try
        {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            //save dimensions
            bw.write("Width: "+ width); bw.newLine();
            bw.write("Height: "+ height); bw.newLine();
            bw.write("Skip Setup: "+skipSetup); bw.newLine();
            bw.newLine();
            
            //save terrain
            bw.write("Terrain:"); bw.newLine();
            for(int i = 0; i < height; i++)
            {
                String s = "";
                s+= terrainList.get(i*width).getName();
                for(int j = 1; j< width; j++)
                    s+= ", "+terrainList.get(i*width + j).getName();
                bw.write(s); bw.newLine();
            }
            bw.newLine();
            
            //write starting locations
            bw.write("Player Army Starting Locations (Not including mandatory units)"); bw.newLine();
            bw.write("Number of Starting Locations: "+startingPlayerLocations.size()); bw.newLine();
            for (Point startingPoint : startingPlayerLocations) 
                writePoint(startingPoint, bw);
            bw.newLine();
            
            //write starting units
            bw.write("Units staring on the map"); bw.newLine();
            bw.write("Number of Starting Units: "+startingUnits.size()); bw.newLine();
            bw.newLine();
            
            for(Unit u: startingUnits)
                u.save(bw);
            
            bw.write("Unit Starting Locations"); bw.newLine();
            for(Point sel : startingUnitLocations)
                writePoint(sel, bw);
            bw.newLine();
            
            bw.write("Reinforcements"); bw.newLine();
            bw.write("Number of Reinforcements: "+reinforcements.size()); bw.newLine();
            bw.newLine();
            
            for(Unit re : reinforcements)
                re.save(bw);
            
            bw.write("Reinforcement Locations"); bw.newLine();
            for(Point reLoc: reinforcementLocations)
                writePoint(reLoc, bw);
            bw.newLine();
            
            bw.write("Reinforcement Timings"); bw.newLine();
            for(Integer i: reinforcementTurnTimings)
            {
                bw.write("Turn "+Integer.toString(i)); bw.newLine();
            }
            bw.newLine();
            
            bw.write("Mandatory Player Units"); bw.newLine();
            bw.write("Number of Mandatory Player Units: "+mandatoryPlayerUnits.size()); bw.newLine();
            bw.newLine();
            for(String name: mandatoryPlayerUnits)
            {
                bw.write(name); bw.newLine();
            }
            bw.newLine();
            
            bw.write("Mandatory Player Units Locations"); bw.newLine();
            for(Point manLoc: mandatoryPlayerUnitLocations)
                writePoint(manLoc, bw);
            bw.newLine();
            
            
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Could not save map data. Someting's wrong");
        }
    }

    //<editor-fold desc="Internal IO helpers">
    
    public static ArrayList<String> parseComma(String s)
    {
        List<String> parsedList = Arrays.asList(s.split(", "));
        return new ArrayList<>(parsedList);
    }
    
    /**
     * Writes a point to file. 
     * @param p The point to write
     * @param bw The file writer doing the writing.
     * @throws IOException
     */
    public static void writePoint(Point p, BufferedWriter bw) throws IOException
    {
        bw.write(p.x + ", "+p.y); 
        bw.newLine();
    }
    
    public static Point loadPoint(ArrayList<String> al)
    {
        //If somehow we didn't get the right number of arguments.
        if(al.size() != 2)
            return null;
        
        return new Point(Integer.parseInt(al.get(0)), Integer.parseInt(al.get(1)));
    }
    
    //</editor-fold>
    
    
    /**
     * Gets the terrain at point (x,y)
     * @param x x loc
     * @param y y loc
     * @return The terrain at x,y
     */
    public Terrain getTerrainAt(int x, int y)
    {
        try
        {
            return terrainList.get(y*width + x);
        }
        //indexOutOfBounds
        catch(Exception e)
        {
            return null;
        }
    }
    
    /**
     * gets the terrain at point p.
     * @param p the point on the map to look
     * @return the terrain at that point
     */
    public Terrain getTerrainAtPoint(Point p)
    {
        return getTerrainAt(p.x, p.y);
    }
    
    //<editor-fold desc="getters and setters">
    
    /**
     * @return the height
     */
    public int getHeight() 
    {
        return height;
    }

    /**
     * @return the width
     */
    public int getWidth() 
    {
        return width;
    }
    
    public boolean getSkipSetup()
    {
        return skipSetup;
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @return the idNum
     */
    public int getIdNum() 
    {
        return idNum;
    }

    /**
     * @return the mapImage
     */
    public BufferedImage getMapImage() 
    {
        return mapImage;
    }
    
    public ArrayList<Point> getStartingPlayerLocations()
    {
        return startingPlayerLocations;
    }
    
    public void setStartingPoints(ArrayList<Point> sp)
    {
        startingPlayerLocations = sp;
    }
    
    /**
     * @return the startingUnits
     */
    public ArrayList<Unit> getStartingUnits() 
    {
        return startingUnits;
    }

    /**
     * @param se the startingUnits to set
     */
    public void setStartingUnits(ArrayList<Unit> se) 
    {
        startingUnits = se;
    }
    
    /**
     * @return the startingUnitLocations
     */
    public ArrayList<Point> getStartingUnitLocations() 
    {
        return startingUnitLocations;
    }

    /**
     * @param startingUnitLocations the startingUnitLocations to set
     */
    public void setStartingUnitLocations(ArrayList<Point> startingUnitLocations) 
    {
        this.startingUnitLocations = startingUnitLocations;
    }

    /**
     * @return the mandatoryPlayerUnits
     */
    public ArrayList<String> getMandatoryPlayerUnits() 
    {
        return mandatoryPlayerUnits;
    }

    /**
     * @return the mandatoryPlayerUnitLocations
     */
    public ArrayList<Point> getMandatoryPlayerUnitLocations() 
    {
        return mandatoryPlayerUnitLocations;
    }

    /**
     * @param mandatoryPlayerUnitLocations the mandatoryPlayerUnitLocations to set
     */
    public void setMandatoryPlayerUnitLocations(ArrayList<Point> mandatoryPlayerUnitLocations) 
    {
        this.mandatoryPlayerUnitLocations = mandatoryPlayerUnitLocations;
    }

    /**
     * @return the winCondition
     */
    public int getWinCondition() 
    {
        return winCondition;
    }
    
//</editor-fold>

}
