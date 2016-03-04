/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.gamemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.actor.Unit;
import solenus.gridemblem3.render.Rendering;

/**
 *
 * @author Chris
 */
public class UnitCircles 
{
    
    private ArrayList<Unit> unitList;
    private BufferedImage playerCircle;
    private BufferedImage enemyCircle;
    private BufferedImage allyCircle;
    private BufferedImage otherCircle;

    private boolean visible;
    
    public UnitCircles(ArrayList<Unit> unitList)
    {
        this.unitList = unitList;
        visible = true;
        
        try
        {
            playerCircle = ImageIO.read(new File("assets/ui/UnitCirclePlayer.png"));
            enemyCircle = ImageIO.read(new File("assets/ui/UnitCircleEnemy.png"));
            allyCircle = ImageIO.read(new File("assets/ui/UnitCircleAlly.png"));
            otherCircle = ImageIO.read(new File("assets/ui/UnitCircleOther.png"));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Unit circles failed to load.");
            System.out.println(e);
            System.exit(-1);
        }
    }
    
    public void draw(Camera c, Graphics2D g)
    {
        for(Unit u: unitList)
        {
            switch(u.getTeam())
            {
                case 0:
                    Rendering.renderGrid(playerCircle, c, g, u.getXCur(), u.getYCur(), GridEmblemMk3.HALFGRIDSIZE, GridEmblemMk3.HALFGRIDSIZE);
                    break;
                case 1:
                    Rendering.renderGrid(enemyCircle , c, g, u.getXCur(), u.getYCur(), GridEmblemMk3.HALFGRIDSIZE, GridEmblemMk3.HALFGRIDSIZE);
                    break;
                case 2:
                    Rendering.renderGrid(allyCircle  , c, g, u.getXCur(), u.getYCur(), GridEmblemMk3.HALFGRIDSIZE, GridEmblemMk3.HALFGRIDSIZE);
                    break;
                case 3:
                    Rendering.renderGrid(otherCircle , c, g, u.getXCur(), u.getYCur(), GridEmblemMk3.HALFGRIDSIZE, GridEmblemMk3.HALFGRIDSIZE);
                    break;

            }
        }
    }
    
    
    
}
