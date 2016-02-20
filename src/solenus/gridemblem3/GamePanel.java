/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import solenus.gridemblem3.scene.SceneManager;

/**
 *
 * @author Chris
 */
public class GamePanel extends JPanel 
{
    private SceneManager sceneControl;
    
    public GamePanel(SceneManager sm)
    {
        sceneControl = sm;
        //boot it up
        setLayout(null);
        setSize(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT);
        setLocation(0, 0);
        setPreferredSize(new Dimension(GridEmblemMk3.WIDTH, GridEmblemMk3.HEIGHT));
        setVisible(true);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        sceneControl.draw(g2);
    }
    
}
