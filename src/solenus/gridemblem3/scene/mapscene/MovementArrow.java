/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;

/**
 * Is the arrow when moving.
 * @author Chris
 */
public class MovementArrow 
{
    private ArrayList<Point> areaTraveled;
    private boolean active;
    private MapCursor c;
    private Unit movingUnit;
    private boolean repathFlag;
    private int weight;
    private Map map;
    private PathfindingReport pr;
    
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    
    private BufferedImage spriteSheet;
    
    //0-3 start lines
    //4-5 full lines, 6-9 corners
    //10-13 arrows
    private BufferedImage[] arrows;
    
    /**
     * Standard constructor
     */
    public MovementArrow()
    {
        areaTraveled = new ArrayList<>();
        active = false;
        
        //it's a square, so height and width are the same.
        height = GridEmblemMk3.GRIDSIZE;
        width = GridEmblemMk3.GRIDSIZE;
        centerX = GridEmblemMk3.HALFGRIDSIZE;
        centerY = GridEmblemMk3.HALFGRIDSIZE;
        
        try
        {
            //load sheet image
            spriteSheet = ImageIO.read(new File("assets/sprites/arrow_temp.png"));
            
            //load animations
            BufferedReader in = new BufferedReader(new FileReader("assets/sprites/arrow_tempAnimation.txt"));
            String read;
            
            //get sprite dimensions
            height = Integer.decode(in.readLine().substring(8));
            width = Integer.decode(in.readLine().substring(7));
            centerX = Integer.decode(in.readLine().substring(9));
            centerY = Integer.decode(in.readLine().substring(9));
            
            in.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "The movement arrow sprite failed to load");
            System.exit(-1);
        }
        
        arrows = new BufferedImage[14];
        
        //not the prettiest code, but it gets the job done
        arrows[ 0] = spriteSheet.getSubimage(      0,        0, width, height);
        arrows[ 1] = spriteSheet.getSubimage(  width,        0, width, height);
        arrows[ 2] = spriteSheet.getSubimage(2*width,        0, width, height);
        arrows[ 3] = spriteSheet.getSubimage(3*width,        0, width, height);
        arrows[ 4] = spriteSheet.getSubimage(      0,   height, width, height);
        arrows[ 5] = spriteSheet.getSubimage(  width,   height, width, height);
        arrows[ 6] = spriteSheet.getSubimage(      0, 2*height, width, height);
        arrows[ 7] = spriteSheet.getSubimage(  width, 2*height, width, height);
        arrows[ 8] = spriteSheet.getSubimage(2*width, 2*height, width, height);
        arrows[ 9] = spriteSheet.getSubimage(3*width, 2*height, width, height);
        arrows[10] = spriteSheet.getSubimage(      0, 3*height, width, height);
        arrows[11] = spriteSheet.getSubimage(  width, 3*height, width, height);
        arrows[12] = spriteSheet.getSubimage(2*width, 3*height, width, height);
        arrows[13] = spriteSheet.getSubimage(3*width, 3*height, width, height);
        

        
    }
    
    //<editor-fold desc="Scene-like methods">
    
    /**
     * progress the game state
     */
    public void runFrame()
    {
        if(active)
        {
            Point p = c.getCoord();
            
            addPoint(p);

        }
    }
    
    /**
     * draw the arrow
     * @param g graphics
     * @param ca the camera location.
     */
    public void draw(Graphics2D g, Camera ca)
    {
        if(active && areaTraveled.size() > 1)
        {
            //draw starting line
            drawImage(g, ca, arrows[startLine(areaTraveled.get(0), areaTraveled.get(1))], areaTraveled.get(0));
            
            //draw intermediate lines
            for(int i = 1; i<areaTraveled.size()-1; i++)
                drawImage(g, ca, arrows[middleLine(areaTraveled.get(i-1), areaTraveled.get(i), areaTraveled.get(i+1))], areaTraveled.get(i));
            
            //draw end arrow
            drawImage(g, ca, arrows[endLine(areaTraveled.get(areaTraveled.size()-1), areaTraveled.get(areaTraveled.size()-2))], areaTraveled.get(areaTraveled.size()-1));
            
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="control methods">
    
    /**
     * start arrowing
     * @param u the unit moving
     * @param pathRep the pathfinding report for the current unit
     * @param cu the mapCursor we need to watch
     * @param m the map it's happening on.
     */
    public void start(Unit u, PathfindingReport pathRep, MapCursor cu, Map m)
    {
        c = cu;
        active = true;
        areaTraveled.add(c.getCoord());
        movingUnit = u;
        pr = pathRep;
        map = m;
    }
    
    /**
     * use this when you've finished with an arrow
     */
    public void end()
    {
        active = false;
        areaTraveled.clear();
        movingUnit = null;
    }
    
    /**
     * Tries to add a point to the arrow
     * @param p the point to be added.
     */
    public void addPoint(Point p)
    {
        //First off, the behaviour is very different if p is a location we already traveled to. So let's find out where on the path it is. (-1 for new points)
        int locPath = areaTraveled.indexOf(p);

        //If p's not in a location we can even move to, do nothing and flag a repath for when we get back into move range.
        if(pr.getMovableLocations().indexOf(p) == -1)
            repathFlag = true;

        //If we're on the same spot, do nothing
        else if(locPath == areaTraveled.size()-1)
        {/*do nothing*/}

        //if we're on a new spot, add it
        else if(locPath == -1)
        {
            if(repathFlag)
                repath(p);
            else
            {
                //if Diagonal
                if(Math.abs(p.x-areaTraveled.get(areaTraveled.size()-1).x + p.y-areaTraveled.get(areaTraveled.size()-1).y) != 1)
                    addPoint(new Point(p.x, areaTraveled.get(areaTraveled.size()-1).y));

                areaTraveled.add(p);
                weight+= getWeightOfPoint(p);
            }


        }

        //if we're on an old point, move back to it. 
        else
        {
            areaTraveled.subList(locPath+1, areaTraveled.size()).clear();
            reweight();
        }

        if(weight > movingUnit.getMove())
        {
            repath();
        }
    }
    
    /**
     * Finds the movement requirement of moving onto point p.
     * @param p the point we're finding
     * @return the movement requirement
     */
    public int getWeightOfPoint(Point p)
    {
        return map.getTerrainAtPoint(p).getMoveCost(movingUnit.getTransportType());
    }
    
    /**
     * Create a new arrow to the last place traveled.
     */
    public void repath()
    {
        if(active)
        {
            Point dest = areaTraveled.get(areaTraveled.size()-1);
            areaTraveled = Pathfinding.repath(dest, movingUnit, pr.getDistanceMap());
        }
        reweight();
        
        repathFlag = false;
    }
    
    /**
     * Create a new arrow to point p
     * @param dest The destination of the arrow
     */
    public void repath(Point dest)
    {
        if(active)
        {
            areaTraveled = Pathfinding.repath(dest, movingUnit, pr.getDistanceMap());
        }
        //This is the one time we can set weight to the weightmap value, because it will be an optimized route.
        weight = pr.getDistanceTo(areaTraveled.get(areaTraveled.size()-1));
        
        repathFlag = false;
    }
    
    
    /**
     * sees how much movement the current arrow costs
     */
    public void reweight()
    {
        //this isn't as simple as looking it up on the weightmap from the pathfinding report, because the path might not be shortest distance (trap dodging)
        weight = 0;
        for(int i = 1; i<areaTraveled.size(); i++)
            weight+= getWeightOfPoint(areaTraveled.get(i));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="sprite functions">
      
    /**
     * finds the correct sprite for the start of a line
     * @param cur the point we're at
     * @param next the next point we're going
     * @return the index of the right sprite in Arrows[]
     */
    public int startLine(Point cur, Point next)
    {
        if(cur.x < next.x)
            return 0;
        else if(cur.y > next.y)
            return 1;
        else if(cur.x > next.x)
            return 2;
        else //if(cur.y < next.y)
            return 3;
        
    }
    
    /**
     * finds the correct sprite for connecting pieces of the line
     * @param prev the previous point in the line.
     * @param cur the point we're at
     * @param next the next point we're going
     * @return the index of the right sprite in Arrows[]
     */
    public int middleLine(Point prev, Point cur, Point next)
    {
        if(prev.x != cur.x && cur.x != next.x)
            return 4;
        else if(prev.y != cur.y && cur.y != next.y)
            return 5;
        
        else if((cur.y > prev.y && cur.x < next.x)||(cur.y > next.y && cur.x < prev.x))
            return 6;
        
        else if((cur.y < prev.y && cur.x < next.x)||(cur.y < next.y && cur.x < prev.x))
            return 7;
        
        else if((cur.y < prev.y && cur.x > next.x)||(cur.y < next.y && cur.x > prev.x))
            return 8;
        
        else //if((cur.y > prev.y && cur.x > next.x)||(cur.y > next.y && cur.x > prev.x))
            return 9;
    }
    
    /**
     * finds the correct sprite for ending arrow of the line.
     * @param prev the previous point in the line.
     * @param cur the point we're at
     * @return the index of the right sprite in Arrows[]
     */
    public int endLine(Point prev, Point cur)
    {
        if(cur.x > prev.x)
            return 10;
        else if(cur.y < prev.y)
            return 11;
        else if(cur.x < prev.x)
            return 12;
        else //if(cur.y > prev.y)
            return 13;
        
    }
    
    //</editor-fold>
    
    /**
     * Renders a piece of the line. Because it doesn't use sprites, we have to use this here.
     * @param g The graphics
     * @param ca The camera
     * @param i The image being rendered
     * @param p The point on the map the image is at.
     */
    private void drawImage(Graphics2D g, Camera ca, BufferedImage i, Point p)
    {
        Rendering.renderGrid(i, ca, g, p.x, p.y, centerX, centerY);
    }
    
    /**
     * gets the path the arrow travels
     * @return areaTravled
     */
    public ArrayList<Point> getPath()
    {
        return areaTraveled;
    }
    
    /**
     * A way to show and hide the arrow without resetting it
     * @param b ...
     */
    public void setVisible(boolean b)
    {
        active = b;
    }
}


