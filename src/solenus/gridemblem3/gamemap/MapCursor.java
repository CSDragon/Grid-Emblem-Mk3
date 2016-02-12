/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.gamemap;


import solenus.gridemblem3.InputManager;

/**
 *
 * @author Chris
 */
public class MapCursor extends GridRenderable
{
    
    private int cursorMoveTimer;
    
    private int cuUp;
    private int cuDown;
    private int cuLeft;
    private int cuRight;
    private boolean fast;
    
    private Map map;
    
    private int frameSkip;
    private final int slowFS = 6;
    private final double slowSpeed = 1.0/slowFS;
    private final int fastFS = 3;
    private final double fastSpeed = 1.0/fastFS;
    
    
    
    /**
     * standard constructor
     * @param m the game map
     */
    public MapCursor(Map m)
    {
        super("cursor");
        
        x = m.getCursorStartX();
        xCur = m.getCursorStartX();
        y = m.getCursorStartY();
        yCur = m.getCursorStartY();
        
        map = m;
    }
    
    /**
     * respond to controls
     * @param im the input manager
     */
    public void respondControls(InputManager im)
    {
        if(im.getUp() == 0 && im.getDown() == 0 && im.getLeft() == 0 && im.getRight() == 0)
        {
            cursorMoveTimer = 0;
            cuUp = 0;
            cuDown = 0;
            cuLeft = 0;
            cuRight = 0;
        }
        
        else
            cursorMoveTimer++;
        
        cuUp = im.getUp();
        cuDown = im.getDown();
        cuLeft = im.getLeft();
        cuRight = im.getRight();
            
        fast = im.getB() > 0;
    }
    
    
    /**
     * Runs the frame
     */
    public void runFrame()
    {
        boolean secondMove = ((cursorMoveTimer >= frameSkip && cursorMoveTimer < 2*frameSkip-1) && !fast);
        if(!isMoving() && !secondMove)
        {
            if(cuLeft > 0)
                x--;
            
            if(cuRight > 0)
                x++;
            
            if(cuUp > 0)
                y--;

            if(cuDown > 0)
                y++;
            
            
            if(x < 0 )
                x=0;
            if(x > map.getWidth()-1)
                x = map.getWidth()-1;
            if(y < 0)
                y = 0;
            if(y > map.getHeight()-1)
                y = map.getHeight()-1;
            
            if(fast)
            {
                frameSkip = fastFS;
                speed = fastSpeed;
            }
            else
            {
                frameSkip = slowFS;
                speed = slowSpeed;
            }
            
            
        }
        
        moveToDest();
         
    }
    
    
    

}


