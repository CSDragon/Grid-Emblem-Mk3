/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.gamemap.Camera;

/**
 *
 * @author Chris
 */
public class Rendering 
{
 
    /**
     * Renders an image at a location on the map (x,y)
     * @param image the image to be rendered
     * @param c the camera
     * @param g the graphics
     * @param x the x point on the map to render at 
     * @param y the y point on the map to render at
     * @param centerX the x center of the image to offset x
     * @param centerY the y center of the image to offset y
     */
    public static void renderGrid(BufferedImage image, Camera c, Graphics2D g, double x, double y, int centerX, int centerY)
    {
        g.drawImage(image, (int)Math.round((x - c.getX())*GridEmblemMk3.GRIDSIZE) - centerX + GridEmblemMk3.HALFWIDTH, (int)Math.round(GridEmblemMk3.GRIDSIZE*(y - c.getY())) - centerY + GridEmblemMk3.HALFHEIGHT, null);
    }
    
    /**
     * Renders a sprite at tile (x,y)
     * @param sprite the sprite to render
     * @param c the camera
     * @param g the graphics
     * @param x the xloc on the map
     * @param y the yloc on the map
     */
    public static void renderGrid(Sprite sprite, Camera c, Graphics2D g, double x, double y)
    {
        renderGrid(sprite.getDisplaySprite(), c, g, x, y, sprite.getCenterX(), sprite.getCenterY());
    }
    
    /*  orientations:
        0 = up/left
        1 = center
        2 = down/right
    
        adjustments:
        0 none
        1 center x/y
        2 width/height
    */
    
    /**
     * Renders an image at a point on the screen, not the gamemap
     * @param image The image to be rendered
     * @param g the graphics
     * @param x the xloc on the screen
     * @param y the yloc on the screen
     * @param xAdjust the amount to move x based on the sprite's size and orientation
     * @param yAdjust the amount to move y based on the sprite's size and orientation.
     * @param xOrientation the orientation which it should be rendered, left, center or right.
     * @param yOrientation the orientation which it should be rendered, up, center or down.
     */
    public static void renderAbsolute(BufferedImage image, Graphics2D g, int x, int y, int xAdjust, int yAdjust, int xOrientation, int yOrientation)
    {
        //change the orientation
        if(xOrientation == 1)
            x = GridEmblemMk3.HALFWIDTH + x;
        else if(xOrientation == 2)
            x = GridEmblemMk3.WIDTH - x;

        if(yOrientation == 1)
            y = GridEmblemMk3.HALFHEIGHT + y;
        else if (yOrientation == 2)
            y = GridEmblemMk3.HEIGHT - y;

        g.drawImage(image, x - xAdjust, y - yAdjust, null);
    }
    
    public static void renderAbsolute(Sprite sprite, Graphics2D g, int x, int y, int xOrientation, int yOrientation)
    {
        renderAbsolute(sprite.getDisplaySprite(), g, x, y, sprite.getCenterX(), sprite.getCenterY(), xOrientation, yOrientation);
    }
    
    
    /**
     * Renders text at a point on the screen, not the gamemap
     * @param s The string to be rendered
     * @param g the graphics
     * @param x the xloc on the screen
     * @param y the yloc on the screen
     * @param xAdjust the amount to move x based on the sprite's size and orientation
     * @param yAdjust the amount to move y based on the sprite's size and orientation.
     * @param xOrientation the orientation which it should be rendered, left, center or right.
     * @param yOrientation the orientation which it should be rendered, up, center or down.
     * @param textOrientation the orientation which line of text renders. Left, center or right.
     */
    public static void renderTextAbsolute(String s, Graphics2D g, int x, int y, int xAdjust, int yAdjust, int xOrientation, int yOrientation, int textOrientation)
    {
        //change the orientation
        if(xOrientation == 1)
            x = GridEmblemMk3.HALFWIDTH - xAdjust + x;
        else if(xOrientation == 2)
            x = GridEmblemMk3.WIDTH - xAdjust - x;

        if(yOrientation == 1)
            y = GridEmblemMk3.HALFHEIGHT - yAdjust + y;
        else if (yOrientation == 2)
            y = GridEmblemMk3.HEIGHT - yAdjust - y;
        
        if(textOrientation == 1)
            x -= (g.getFontMetrics().stringWidth(s))/2;
        else if(textOrientation == 2)
            x -= (g.getFontMetrics().stringWidth(s));
        
        g.drawString(s, x, y);
    }
    
    /**
     * 
     * @param g The graphics
     * @param x The x location to render at.
     * @param y The y location to render at.
     * @param width The width
     * @param height The height
     * @param c The camera.
     * @param xAdjust Adjustment due to orientation
     * @param yAdjust Adjustment due to orientation
     * @param xOrientation the orientation which it should be rendered, left, center or right.
     * @param yOrientation the orientation which it should be rendered, up, center or down.
     */
    public static void renderRectAbsolute(Graphics2D g, int x, int y, int width, int height, Color c, int xAdjust, int yAdjust, int xOrientation, int yOrientation)
    {
        //change the orientation
        if(xOrientation == 1)
            x = GridEmblemMk3.HALFWIDTH - xAdjust + x;
        else if(xOrientation == 2)
            x = GridEmblemMk3.WIDTH - xAdjust - x;

        if(yOrientation == 1)
            y = GridEmblemMk3.HALFHEIGHT - yAdjust + y;
        else if (yOrientation == 2)
            y = GridEmblemMk3.HEIGHT - yAdjust - y;
        
        g.setColor(c);
        g.fillRect(x, y, width, height);
    }
    
    
}
