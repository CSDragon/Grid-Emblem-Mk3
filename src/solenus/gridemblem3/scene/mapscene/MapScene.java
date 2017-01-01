/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.scene.mapscene;


import solenus.gridemblem3.scene.prebattlescene.PreBattleScene;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import solenus.gridemblem3.GridEmblemMk3;
import solenus.gridemblem3.InputManager;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.actor.*;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.render.Rendering;
import solenus.gridemblem3.scene.Scene;
import solenus.gridemblem3.scene.mapscene.unitinspectscene.UnitInspectScene;
import solenus.gridemblem3.ui.XPBarUI;


/**
 *
 * @author Chris
 */
public class MapScene extends Scene
{
    public static final int RETURNTOBASE = 2;
    public static final int VICTORY = 3;
    
    //the game map
    private Map map;
    private MapCursor cursor;
    private Camera camera;
    private ArrayList<Actor> actorList;
    private ArrayList<Unit> unitList;
    private ArrayList<Unit> dieingUnits;
    
    
    //game control
    private int numFactions;
    private int turn;
    private Unit selectedUnit;
    private int lastUnitIndexSearched;
    private ArrayList<Unit> attackableUnits;
    private int attackableUnitsIndex;
    private AI ai;
    boolean fightGraphicsMode;
    private int animationFrames;
    private PlayerData playerArmy;
    

    //UI Elements
    private MovementArrow mvArrow;
    private int movingIndex;
    private ArrayList<Point> movingLine;
    private UnitActionMenu unitActionMenu;
    private WeaponSelectionMenu weaponSelect;
    private ItemSelectionMenu itemSelect;
    private FightUI fightUI;
    private StaffActionUI staffUI;
    private FightHealthBarUI singleHealthBar;
    private SystemActionMenu systemAction;
    private Grid grid;
    private UnitCircles unitCircles;
    private XPBarUI xp;
    private PreBattleScene preBattleScene;
    private TerrainUI terrainUI;
    private UnitHoverUI unitHoverUI;
    private UnitInspectScene unitInspectScene;
    
    //range UI
    private boolean drawAllyMoveRange;
    private ArrayList<Point> allyRangeMap;
    private ArrayList<Unit> enemyRangeList;
    private ArrayList<Point> selectedEnemyRangeMap;
    private boolean drawAllEnemyRanges;
    private ArrayList<Point> allEnemyRangeMap;
    
    private ArrayList<Point> playerStartingLocations;

    
    public MapScene(Scene parent)//TEST: make this take an extra arguement, the map id.
    {
        super(parent);
        
        //initialize UI
        mvArrow = new MovementArrow();
        unitActionMenu = new UnitActionMenu();
        weaponSelect = new WeaponSelectionMenu();
        itemSelect = new ItemSelectionMenu();
        fightUI = new FightUI();
        staffUI = new StaffActionUI();
        systemAction = new SystemActionMenu();
        xp = new XPBarUI();
        preBattleScene = new PreBattleScene(this);
        terrainUI = new TerrainUI();
        unitHoverUI = new UnitHoverUI();
    }
    
    // <editor-fold desc="Scene control methods">
    
        
    /**
     * Responds to controls.
     * most Scene subclasses must override this, and check if they are active.
     * @param im the input manager
     */
    public void respondControls(InputManager im)
    {
        //always check this
        if(active)
        {
            /*  
            STATES:
                0)  Startup
                1)  Cursor. Moving the cursor around. 
                2)  Unit Move. Once a unit has been selected, cursor movement.
                3)  Unit Action Menu. Once a unit has been moved, controling the UnitAction Menu.
                4)  System Action Menu. When you select nothing, controlling the SystemAction Menu.
                5)  Moving Unit mode. When a unit is curently moving, move the unit and wait for the animation to finish.
                6)  Select enemy to fight
                7)  Select a weapon to fight with. 
                8)  Battle
                9)  End turn
                10) Someone else's turn
                11) Player turn Start
                12) Unit death
                13) Exp
                14) Prebattle Menu
                15) View Map
                16) Unit Inspect Scene
                17) Select an ally to heal
                18) Select a staff to use 
                19) Heal "combat"
                20) Healing item usage
            */

            //cursor mode
            switch(getControlState())
            {
                case 1:
                    //R: Open the unit inspection.
                    if(im.getR() == 1)
                    {
                        cstXto16();
                        break;
                    }
                    //A: On a friendly unit, enter move mode, on anything else enter system action box.
                    if(im.getA() == 1)
                    {
                        Unit found = getUnitAtPoint(cursor.getX(), cursor.getY());

                        //nothing found, open up the system action box
                        if(found == null)
                            cst1to4();
                        else if(found.getTeam() == 1)
                            addEnemyRange(found);
                        else if(found.getTeam() == 0)
                        {
                            if(!found.getHasMoved())
                                cst1to2(found);
                            else
                                cst1to4();
                        }
                    }
                    //X: Toggles enemy ranges on/off.
                    if(im.getX() == 1)
                        toggleEnemyRanges();
                    //Y: Clears manually set enemy ranges. 
                    if(im.getY() == 1)
                        clearRanges();
                    //L: Move cursor instantly to next unmoved unit.
                    if(im.getL()%20 == 1)
                        moveToNextUnmovedUnit();
                    //Check that nothing has changed. If the move mode was changed, we don't want to move anymore.
                    if (getControlState() == 1)
                        cursor.respondControls(im);
                    break;

                case 2:
                    //R: Open the unit inspection.
                    if(im.getR() == 1)
                    {
                        cstXto16();
                        break;
                    }
                    //A: When over an available location, move the unit to that location and open the action box
                    if(im.getA() == 1)
                        if(allyRangeMap.contains(cursor.getCoord()) && Pathfinding.isStoppingAllowed(selectedUnit, cursor.getCoord()))
                            cst2to5();

                    //B: Return to state 1.
                    if(im.getB() == 1)
                        cst2to1();

                    //X: Toggles enemy ranges on/off.
                    if(im.getX() == 1)
                        toggleEnemyRanges();
                    //Y: Clears manually set enemy ranges.
                    if(im.getY() == 1)
                        clearRanges();


                    //Check that nothing has changed. If the move mode was changed, we don't want to move anymore.
                    if (getControlState() == 2)
                        cursor.respondControls(im);
                    break;

                case 3:
                    unitActionMenu.respondControls(im);
                    break;

                case 4:
                    systemAction.respondControls(im);
                    break;

                case 6:
                    //R: Open the unit inspection.
                    if(im.getR() == 1)
                    {
                        cstXto16();
                        break;
                    }
                    if(im.getUp() == 1 || im.getRight() == 1)
                        attackableUnitsIndex--;
                    if(im.getDown() == 1 || im.getLeft() == 1)
                        attackableUnitsIndex++;

                    if(attackableUnitsIndex < 0)
                        attackableUnitsIndex = attackableUnits.size()-1;
                    if(attackableUnitsIndex >= attackableUnits.size())
                        attackableUnitsIndex = 0;

                    if(im.getA() == 1)
                        cst6to7();
                    if(im.getB() == 1)
                        cst6to3();

                    break;

                case 7:
                    weaponSelect.respondControls(im);
                    break;
                case 13:
                    xp.respondControls(im);
                    break;
                case 14:
                    preBattleScene.respondControls(im);
                    break;
                case 15:
                    cursor.respondControls(im);
                    //R: Open the unit inspection.
                    if(im.getR() == 1)
                    {
                        cstXto16();
                        break;
                    }
                    //A: If pressed on a starting location that contains a unit, you mave move the unit.
                    if(im.getA() == 1)
                    {
                        if(playerStartingLocations.contains(cursor.getCoord()))
                        {
                            if (selectedUnit == null)
                                selectedUnit = getUnitAtPoint(cursor.getCoord());
                            else
                            {
                                Unit tempU = getUnitAtPoint(cursor.getCoord());
                                Point temp = selectedUnit.getCoord();
                                selectedUnit.moveInstantly(cursor.getCoord());
                                if(tempU != null)
                                    tempU.moveInstantly(temp);
                                selectedUnit = null;
                            }
                        }
                    }
                    if(im.getB() == 1)
                    {
                        if(selectedUnit == null)
                            cst15to14();
                        else
                            selectedUnit = null;
                    }
                    break;
                    
                case 16:
                    unitInspectScene.respondControls(im);
                    break;
                    
                case 17:
                    //R: Open the unit inspection.
                    if(im.getR() == 1)
                    {
                        cstXto16();
                        break;
                    }
                    if(im.getUp() == 1 || im.getRight() == 1)
                        attackableUnitsIndex--;
                    if(im.getDown() == 1 || im.getLeft() == 1)
                        attackableUnitsIndex++;

                    if(attackableUnitsIndex < 0)
                        attackableUnitsIndex = attackableUnits.size()-1;
                    if(attackableUnitsIndex >= attackableUnits.size())
                        attackableUnitsIndex = 0;

                    if(im.getA() == 1)
                        cst17to18();
                    if(im.getB() == 1)
                        cst6to3();

                    break;
                  
                //Select Staff
                case 18:
                    weaponSelect.respondControls(im);
                    break;
                
                //Select Item
                case 21:
                    itemSelect.respondControls(im);
                    break;
                    
            }
        }
    }
    
    /**
     * advances the scene's gamestate 1 frame.
     * most scene subclasses must override this, and check if they are active.
     * @return The state of this scene that the parent scene needs to know.
     */
    public int runFrame()
    {   
        //always check this
        if(active)
        {
            switch(controlState)
            {
                /*  
                STATES:
                    0)  Startup.
                    1)  Cursor. Moving the cursor around. 
                    2)  Unit Move. Once a unit has been selected, cursor movement.
                    3)  Unit Action Menu. Once a unit has been moved, controling the UnitAction Menu.
                    4)  System Action Menu. When you select nothing, controlling the SystemAction Menu.
                    5)  Moving Unit mode. When a unit is curently moving, move the unit and wait for the animation to finish.
                    6)  Select enemy to fight
                    7)  Select a weapon to fight with. 
                    8)  Battle
                    9)  End turn
                    10) Someone else's turn
                    11) Player turn Start
                    12) Unit death
                    13) Exp
                    14) Prebattle Menu
                    15) View Map
                    16) Unit Inspect Scene.
                    17) Select an ally to heal
                    18) Select a staff to use
                    19) Heal "combat"
                    20) Healing item usage

                */
                
                /*  Cursor Mode
                    Active Objects: Cursor
                    Camera Follows: Cursor
                */
                case 1:
                    cursor.runFrame();
                    camera.moveToRenderable(cursor, map);
                    if(checkWinCondition())
                        return VICTORY;
                    break;
                
                /*  Unit Movement Mode
                    Active Objects: Cursor
                    Camera Follows: Cursor
                */
                case 2:
                    mvArrow.runFrame();
                    cursor.runFrame();
                    camera.moveToRenderable(cursor, map);
                    break;
                
                /*  Unit Action Menu
                    Active Objects: UnitActionMenu
                    Camera Follows: Cursor
                */
                case 3:
                    cursor.moveToDest();
                    camera.moveToRenderable(cursor, map);
                    switch(unitActionMenu.runFrame())
                    {
                        case UnitActionMenu.BACK:
                            cst3to2();
                            break;
                            
                        case UnitActionMenu.ATTACK:
                            cst3to6();
                            break;
                            
                        case UnitActionMenu.STAFF:
                            cst3to17();
                            break;
                            
                        case UnitActionMenu.ITEM:
                            cst3to21();
                            break;
                        
                        case UnitActionMenu.WAIT:
                            cst3to1();
                            break;
                            
                    }
                    break;
                    
                /*  System Action Menu
                    Active Objects: SystemActionMenu
                    Camera Follows: Cursor
                */    
                case 4:
                    cursor.moveToDest();
                    camera.moveToRenderable(cursor, map);
                        switch(systemAction.runFrame())
                        {
                            case SystemActionMenu.BACK:
                                cst4to1();
                                break;
                                
                            case SystemActionMenu.ENDTURN:
                                cst4to9();
                                break;
                            
                        }
                    break;
                
                /*  Movement animation
                    Active Objects: selectedUnit
                    Camera Follows: selectedUnit
                */
                case 5:
                    //tell the unit to move to the next place on the line. This would have been unit.runFrame() if not for cst5toX
                    cursor.moveToDest();
                    if(!selectedUnit.isMoving())
                    {
                        if(movingIndex < movingLine.size())
                        {
                            selectedUnit.setDest(movingLine.get(movingIndex));
                            movingIndex++;
                        }
                        else
                            cst5toX();
                    }
                    selectedUnit.moveToDest();
                    camera.moveToRenderable(selectedUnit, map);

                    break;
                
                /*  Enemy Selection
                    Active Objects: Cursor
                    Camera Follows: Cursor
                */
                case 6:
                    cursor.moveInstantly(attackableUnits.get(attackableUnitsIndex).getCoord());
                    camera.moveToRenderable(cursor, map);
                    break;
                    
                /*  Weapon Selection
                    Active Objects: WeaponSelctUI
                    Camera Follows: SelectedUnit
                */
                case 7:
                    camera.moveToRenderable(selectedUnit, map);
                    switch(weaponSelect.runFrame())
                    {
                        //If B pressed, return to enemy select
                        case WeaponSelectionMenu.BACK:
                            cst7to6();
                            break;
                        case 1:
                            cst7to8();
                            break;
                    }
                    break;
                    
                /*  Battle
                    Active Objects: fightUI
                    Camera Follows: none
                */
                case 8:
                    int result = fightUI.runFrame(); //gotta store result so resolveBattle can be passed it regardless of which case it came from.
                    switch(result)
                    {
                        case 0:
                            cst8toX();
                            break;
                        case 1:
                        case 2:
                        case 3:
                            resolveBattle(result);
                            cst8to12();
                            break;
                    }
                    break;
                    
                /*  End Turn
                    Active Object: None
                    Camera Follows: None
                */
                case 9:
                    cst9toX();
                    break;
                    
                /*  Staring the AI turn by checking if any units need to become active
                    Active Object: AI
                    Camera Follows: Cursor
                */    
                case 10:
                    camera.moveToRenderable(cursor, map);
                    ai.activateUnits();
                    cst10to22();
                    break;
                    
                /*  Player Turn Start
                    Active Object: None
                    Camera Follows: None
                */
                case 11:
                    cst11to1();
                    break;
                
                /*  Dieing Unit
                    Active Object: None
                    Camera Follows: None
                */
                case 12:
                    if(animationFrames != 0)
                        animationFrames --;
                    else
                        cst12toX();
                    break;
                
                /*  XP Bar
                    Active Object: XPBarUI
                    Camera Follows: None
                */
                case 13:
                    switch(xp.runFrame())
                    {
                        case 1:
                            cst13to1();
                            break;
                    }
                    break;
                
                /*  Pre-battle Menu
                    Active Object: preBattleScene
                    Camera Follows: cursor
                */
                case 14:
                    camera.moveToRenderable(cursor, map);
                    switch(preBattleScene.runFrame())
                    {
                        case PreBattleScene.VIEWMAP:
                            cst14to15();
                            break;
                        case PreBattleScene.START:
                            cst14to1();
                            break;
                        case PreBattleScene.UPDATEUNITS:
                            setStartingUnits();
                            break;
                        case PreBattleScene.RETURNTOBASE:
                            return RETURNTOBASE;
                    }
                    break;
                    
                /*  View Map
                    Active Object: cursor
                    Camera Follows: cursor
                */
                case 15:
                    cursor.runFrame();
                    camera.moveToRenderable(cursor, map);
                    break;
                    
                /*  Unit Inspect Scene
                    Active Object: Unit Inspect Scene
                    Camera Follows: cursor
                */
                case 16:
                    switch(unitInspectScene.runFrame())
                    {
                        case Scene.BACK:
                            cst16toX();
                    }
                    camera.moveToRenderable(cursor, map);
                    break;
                    
                /*  Ally to heal Selection
                    Active Objects: Cursor
                    Camera Follows: Cursor
                */
                case 17:
                    cursor.moveInstantly(attackableUnits.get(attackableUnitsIndex).getCoord());
                    camera.moveToRenderable(cursor, map);
                    break;
                    
                /*  Staff Selection
                    Active Objects: WeaponSelctUI
                    Camera Follows: SelectedUnit
                */
                case 18:
                    camera.moveToRenderable(selectedUnit, map);
                    switch(weaponSelect.runFrame())
                    {
                        //If B pressed, return to enemy select
                        case WeaponSelectionMenu.BACK:
                            cst18to17();
                            break;
                        case 1:
                            cst18to19();
                            break;
                    }
                    break;
                    
                /*  Healing
                    Active Objects: staffUI
                    Camera Follows: selectedUnit
                */
                case 19:
                    camera.moveToRenderable(selectedUnit, map);
                    switch(staffUI.runFrame())
                    {
                        case 1:
                            cst19to13();
                            break;
                    }
                    break;
                    
                /* Using a healing item
                    Active Objects: singleHealthBar
                    Camera Follows: selectedUnit
                */
                case 20:
                {
                    camera.moveToRenderable(selectedUnit, map);
                    if(singleHealthBar.runFrame())
                        cst20to1();
                }
                
                /*  item Selection
                    Active Objects: WeaponSelctUI
                    Camera Follows: SelectedUnit
                */
                case 21:
                    camera.moveToRenderable(selectedUnit, map);
                    switch(itemSelect.runFrame())
                    {
                        //If B pressed, return to unit action menu
                        case ItemSelectionMenu.BACK:
                            cst21to3();
                            break;
                        case 1:
                            cst21to20();
                            break;
                    }
                    break;
                    
                /*  AI Unit selection
                    Active Objects: AI, selectedUnit
                    Camera Follows: selectedUnit
                */
                case 22:
                    switch(ai.nextUnit())
                    {
                        case AI.UNITFOUND:
                            selectedUnit = ai.getActiveUnit();
                            controlState = 23;
                            break;
                        case AI.DONE:
                            cst10to9();
                            break;
                    }
                    break;
                    
                /* AI Unit Movement
                    Active Objects: AI, selectedUnit
                    Camera Follows: selectedUnit
                */
                case 23:
                    ai.decideAction();
                    controlState = 22;
                    break;    
            }  
            
            terrainUI.runFrame();
            unitHoverUI.runFrame();
        }
        return NOTHING;
    }
    
    /**
     * renders the scene.
     * most scene subclasses must override this, and check if they are visible.
     */
    public void animate()
    {   
        //always check this
        if(active)
        {
            //animate actors
            for (Actor al : actorList) 
                al.animate();
            
            //animate units
            for (Unit ul : unitList) 
                ul.animate();
            
            //animate cursor
            cursor.animate();
            
            //UI
            fightUI.animate();
            staffUI.animate();
            if(singleHealthBar != null)
                singleHealthBar.animate();
            preBattleScene.animate();
            terrainUI.animate(controlState);
            unitHoverUI.animate(controlState);
            unitInspectScene.animate();
        }
    }

    
    
    
    /**
     * Paints the scene
     * @param g2 graphics.
     */
    public void draw(Graphics2D g2)
    {
        if(visible)
        {
            Rendering.renderGrid(map.getMapImage(), camera, g2, 0, 0, (int)(GridEmblemMk3.GRIDSIZE*2.5), (int)(GridEmblemMk3.GRIDSIZE*2.5));

            grid.draw(g2, camera);

            drawRanges(g2);

            unitCircles.draw(camera, g2);
            
            for (Actor al : actorList)
                al.renderCam(g2, camera);

            for (Unit ul : unitList)
                ul.renderCam(g2, camera);
            
            //Draw the dieing units in fade out.
            if(!dieingUnits.isEmpty())
            {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (animationFrames/30.0f)));
                for (Unit dead : dieingUnits)
                    dead.renderCam(g2, camera);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }

            cursor.renderCam(g2, camera);

            //and the movement arrow
            mvArrow.draw(g2, camera);

            //draw UI
            unitActionMenu.draw(g2);
            weaponSelect.draw(g2);
            itemSelect.draw(g2);
            systemAction.draw(g2);
            fightUI.draw(g2);
            staffUI.draw(g2);
            if(singleHealthBar != null)
                singleHealthBar.draw(g2);
            xp.draw(g2);
            preBattleScene.draw(g2);
            terrainUI.draw(g2);
            unitHoverUI.draw(g2);
            unitInspectScene.draw(g2);
        }
        
    }
    
    /**
     * resizes the scene to current app size
     */
    public void resize()
    {
        super.resize();
    }
    
    /**
     * Starts the MapScene.
     * @param pd The save data.
     */
    public void start(PlayerData pd)
    {
        super.start();
        
        //clear up all the previous map's data
        turn = 0;
        selectedUnit = null;
        
        //Load up the main grid objects
        playerArmy = pd;
        fightGraphicsMode = false;
        map = new Map(playerArmy);
        Pathfinding.setMap(map, this);
        
        //Control State
        cst0to14();

        cursor = new MapCursor(map);
        camera = new Camera(cursor.getX(), cursor.getY());
        grid = new Grid(map.getWidth(), map.getHeight());

        //Unit Lists
        actorList = new ArrayList<>();
        unitList = new ArrayList<>();
        dieingUnits = new ArrayList<>();
        
        unitCircles = new UnitCircles(unitList);
        
        ai = new AI(unitList, this);
        
        //range objects
        enemyRangeList = new ArrayList<>();
        allyRangeMap = new ArrayList<>();
        selectedEnemyRangeMap = new ArrayList<>();
        allEnemyRangeMap = new ArrayList<>();
        playerStartingLocations = map.getStartingPlayerLocations();

        
        //Add starting units
        setStartingUnits();
        
        if(!unitList.isEmpty())
        {
            cursor.moveInstantly(unitList.get(0).getCoord());
            camera.moveInstantly(cursor.getXCur(), cursor.getYCur());
        }
        getAllEnemyRanges();
        
        //UI
        terrainUI.start(cursor, map);
        unitHoverUI.start(cursor, this);
        unitInspectScene = new UnitInspectScene(this);
        
        //TEST
        numFactions = 2;
    }
    
    
    //</editor-fold>
    
    //<editor-fold desc="Range methods">

    
    /**
     * Tells the game to render range indicators on the tiles you can move. If U is null, display nothing
     * @param u 
     */
    public void getAllyMoveRange(Unit u)
    {
        allyRangeMap.clear();
        if(u != null)
        {
            PathfindingReport pr = new PathfindingReport(u, false);
            allyRangeMap.addAll(pr.getMovableLocations());
        }
    }
    
    /**
     * Tells the game to render range indicators on the tiles this unit can affect. If U is null, display nothing
     * @param u The unit whose range is being displayed.
     */
    public void getAllyRange(Unit u)
    {
        allyRangeMap.clear();
        if(u != null)
        {
            PathfindingReport pr = new PathfindingReport(u, false);
            allyRangeMap.addAll(pr.getThreatRange());
        }
    }
    
    /**
     * Adds a unit to the range display. If the enemy is already there, remove it.
     * @param u The unit whose range is being displayed 
     */
    public void addEnemyRange(Unit u)
    {
        //since this is the only way 
        if(enemyRangeList.contains(u))
            removeEnemyRange(u);
        else
        {
            //add to list
            enemyRangeList.add(u);

            //add threat points to threat map
            PathfindingReport pr = new PathfindingReport(u, false);
            selectedEnemyRangeMap.addAll(pr.getThreatRange());

            //Remove duplicates
            HashSet h = new HashSet(selectedEnemyRangeMap);
            selectedEnemyRangeMap.clear();
            selectedEnemyRangeMap.addAll(h);
        }
    }
    
    /**
     * Removes a unit from the range display
     * @param u The unit whose range is being displayed 
     */
    public void removeEnemyRange(Unit u)
    {
        //remove from list
        enemyRangeList.remove(u);
        
        //recreate the threat map.
        selectedEnemyRangeMap.clear();
        for (Unit enemy : enemyRangeList)
        {
            PathfindingReport pr = new PathfindingReport(u, false);
            selectedEnemyRangeMap.addAll(pr.getThreatRange());
        }

    }
    
    /**
     * finds all the enemies threat ranges.
     */
    public void getAllEnemyRanges()
    {
        for (Unit u : unitList)
        {
            if(u.getTeam() == 1)
            {
                PathfindingReport pr = new PathfindingReport(u, false);
                allEnemyRangeMap.addAll(pr.getThreatRange());
            }
        }
        
        //Remove duplicates
        HashSet h = new HashSet(allEnemyRangeMap);
        allEnemyRangeMap.clear();
        allEnemyRangeMap.addAll(h);
    }
    
    /**
     * toggles the all enemy range.
     */
    public void toggleEnemyRanges()
    {
        drawAllEnemyRanges = !drawAllEnemyRanges;
    }
    
    /**
     * Clears the list of enemies whose range is being drawn. 
     */
    public void clearRanges()
    {
        enemyRangeList.clear();
        selectedEnemyRangeMap.clear();
    }
    
    
    //Drawing methods.
    
    /**
     * Draws in the ally, enemy and all enemy range indicators
     * @param g Graphics
     */
    public void drawRanges(Graphics2D g)
    {
        //allenemy
        if(drawAllEnemyRanges)
        {
            g.setColor(new Color(255,0,0,64));
            for(Point p : allEnemyRangeMap)
                drawRangeSquare(g, p);
        }
        //enemy
        else
        {
            g.setColor(new Color(225,0,0,64));
            for(Point p : selectedEnemyRangeMap)
                drawRangeSquare(g, p);
        }
        
        //ally
        if(drawAllyMoveRange)
        {
            
            g.setColor(new Color(63,63,225,127));
            for(Point p : allyRangeMap)
                drawRangeSquare(g, p);
        }
        
        //player locations
        if(controlState == 14 || controlState == 15)
        {
            Color normal = new Color(63,63,225,127);
            Color selected = new Color(63,255,63);
            g.setColor(normal);
            for(Point p : playerStartingLocations)
            {
                if(this.getUnitAtPoint(p) == null || this.getUnitAtPoint(p) != selectedUnit)
                    drawRangeSquare(g, p);
                else
                {
                    g.setColor(selected);
                    drawRangeSquare(g, p);
                    g.setColor(normal);
                }
            }
        }
    }
    
    /**
     * Draws a square on the tile at point p
     * @param g the graphics
     * @param p the point
     */
    public void drawRangeSquare(Graphics2D g, Point p)
    {
        if(!(p.x >= map.getWidth() || p.x < 0 || p.y >= map.getHeight() || p.y < 0))
        g.fillRect((int)Math.round(GridEmblemMk3.GRIDSIZE*(p.x - camera.getX()) - GridEmblemMk3.HALFGRIDSIZE + GridEmblemMk3.HALFWIDTH), 
                   (int)Math.round(GridEmblemMk3.GRIDSIZE*(p.y - camera.getY()) - GridEmblemMk3.HALFGRIDSIZE + GridEmblemMk3.HALFHEIGHT), 
                   GridEmblemMk3.GRIDSIZE, GridEmblemMk3.GRIDSIZE);
    }
    
    
    
    //</editor-fold>
    
    //<editor-fold desc="Unit Selection methods">
    
    
    /**
     * Finds the unit on tile (x,y).
     * @param x the x loc
     * @param y the y loc
     * @return the unit at that (x,y) location.
     */
    public Unit getUnitAtPoint(int x, int y)
    {
        //static search in O(n) time. The list will always be unsorted, and it's a small list, so this is fine.
        for (Unit u : unitList) 
        {
            if(u.getX() == x && u.getY() == y)
                return u;
        }
        
        //nothing was found
        return null;
    }
    
    

    /**
     * Finds the unit at point p
     * @param p the point where we're looking
     * @return  the unit at point p.
     */
    public Unit getUnitAtPoint(Point p)
    {
        return getUnitAtPoint(p.x,p.y);
    }
    
    /**
     * Finds the actor on tile (x,y).
     * @param x the x loc
     * @param y the y loc
     * @return the actor at that (x,y) location.
     */
    public Actor getActorAtPoint(int x, int y)
    {
        //static search in O(n) time. The list will always be unsorted, and it's a small list, so this is fine.
        for (Actor a : unitList) 
        {
            if(a.getX() == x && a.getY() == y)
                return a;
        }
        
        //nothing was found
        return null;
    }
    
    
    /**
     * Finds the unit at point p
     * @param p the point where we're looking
     * @return  the unit at point p.
     */
    public Actor getActorAtPoint(Point p)
    {
        return getActorAtPoint(p.x,p.y);
    }
    
    /**
     * moves cursor to next unit
     * This is possibly the least elegant code I've ever written. But it works
     */
    public void moveToNextUnmovedUnit()
    {
        //If we're currently on an unmoved allied unit, use that one as the base
        if(getUnitAtPoint(cursor.getCoord()) != null && (unitList.get(lastUnitIndexSearched).getTeam() == 0 && unitList.get(lastUnitIndexSearched).getHasMoved() == false))
        {
            lastUnitIndexSearched = unitList.indexOf(getUnitAtPoint(cursor.getCoord()));
        }
        
        //If units died, this might have become small enough that we're starting out of bounds.
        if (lastUnitIndexSearched >= unitList.size())
            lastUnitIndexSearched = 0;
        int back = lastUnitIndexSearched;
        
        //this loop system is so convaluted, but it works. And I think it makes sense. Maybe a do-while would have been better.
        Unit found = null;
        lastUnitIndexSearched++;
        if(lastUnitIndexSearched == unitList.size())
                lastUnitIndexSearched = 0;
        
        while (lastUnitIndexSearched != back && found == null)
        {
            if(unitList.get(lastUnitIndexSearched).getTeam() == 0 && unitList.get(lastUnitIndexSearched).getHasMoved() == false)
            {
                found = unitList.get(lastUnitIndexSearched);
                break;
            }
            
            lastUnitIndexSearched++;
            if(lastUnitIndexSearched == unitList.size())
                lastUnitIndexSearched = 0;
        }
        if(found != null)
            cursor.moveInstantly(found.getX(), found.getY());
    }
    
    //</editor-fold>

    /**
     * Starts a new turn
     */
    public void startTurn()
    {
        for(Unit u : unitList)
        {
            if(u.getTeam() == turn)
                u.beginTurn();
        }
    }
    
    /**
     * Ends the curent turn
     */
    public void endTurn()
    {
        turn++;
        turn %= numFactions;
    }
    
    /**
     * Removes dead units from the board after a fight.
     * @param whoDied Who died. 1 = attacker died. 2 = defender died, 3 = both died.
     */
    public void resolveBattle(int whoDied)
    {
        if(whoDied !=2)
        {
            unitList.remove(fightUI.getAttacker());
            actorList.remove(fightUI.getAttacker());
            dieingUnits.add(fightUI.getAttacker());
            
            //if team == 0, remove from your party.
        }

        if(whoDied != 1)
        {
            unitList.remove(fightUI.getDefender());
            actorList.remove(fightUI.getDefender());
            dieingUnits.add(fightUI.getDefender());
            
            //if team == 0, remove from your party.
        }
    }
    
    /**
     * Redoes the starting units based on the selected units.
     */
    public void setStartingUnits()
    {
        ArrayList<Point> manP = map.getMandatoryPlayerUnitLocations();
        ArrayList<Point> choP = map.getStartingPlayerLocations();
        ArrayList<Unit> sel = preBattleScene.getSelectedUnits();
        
        unitList.clear();
        
        for(int i = 0; i< sel.size(); i++)
        {
            if(i < manP.size())
                sel.get(i).moveInstantly(manP.get(i));
            else
                sel.get(i).moveInstantly(choP.get(i-manP.size()));
        }
        
        unitList.addAll(sel);
        unitList.addAll(map.getStartingUnits());
    }
    
    /**
     * Checks if you've won.
     * @return 
     */
    public boolean checkWinCondition()
    {
       switch(map.getWinCondition())
       {
           case Map.WINCON_ROUTE:
               int enemiesLeft = 0;
               for(Unit u: unitList)
               {
                   if(u.getTeam() == 1)
                     enemiesLeft++;
               }
               if(enemiesLeft == 0)
                   return true;
               break;
       }
       return false;
    }
    
    //<editor-fold desc="controlState Methods">
    //Methods who's primary function is to transition the control state from one state to another.
    //"cst = controlState transition"

    /**
     * Starts the prebattle menu
     */
    public void cst0to14()
    {
        controlState = 14;
        preBattleScene.start(playerArmy, map);
    }
    
    /**
     * enters move mode with unit U
     * @param u the unit that will move
     */
    public void cst1to2(Unit u)
    {
        getAllyMoveRange(u);
        drawAllyMoveRange = true;
        controlState = 2;
        selectedUnit = u;
        PathfindingReport pr = new PathfindingReport(u, false);
        mvArrow.start(u, pr, cursor, map);
        cursor.getSprite().sendTrigger("activate");
    }
        
    /**
     * Opens the system action box
     */
    public void cst1to4()
    {
        controlState = 4;
        cursor.setVisible(false);
        systemAction.start();
        
    }
    
    /**
     * Returns from move mode to cursor mode
     */
    public void cst2to1()
    {
        cursor.setVisible(true);
        cursor.moveInstantly(selectedUnit.getX(), selectedUnit.getY());
        cursor.getSprite().sendTrigger("deactivate");
        drawAllyMoveRange = true;
        getAllyMoveRange(null);
        controlState = 1;
        selectedUnit = null;
        mvArrow.end();
    }
    
    /**
     * goes to the mode that actually moves a unit.
     */
    public void cst2to5()
    {
        drawAllyMoveRange = false;
        controlState = 5;
        movingIndex = 0;
        
        movingLine = mvArrow.getPath();
        
        mvArrow.setVisible(false);
    }
    
    /**
     * transitions from the unit action box back to cursor mode
     */
    public void cst3to1()
    {
        cursor.getSprite().sendTrigger("deactivate");
        drawAllyMoveRange = true;
        getAllyMoveRange(null);
        controlState = 1;
        selectedUnit.setHasMoved(true);
        selectedUnit = null;
        mvArrow.end();
        unitActionMenu.end();
    }
    
    /**
     * Transitions from the unit action box back to move mode
     */
    public void cst3to2()
    {
        mvArrow.setVisible(true);
        drawAllyMoveRange = true;
        selectedUnit.moveInstantly(movingLine.get(0));
        unitActionMenu.end();
        
        controlState = 2;
    }
    
    /**
     * Transitions from the unit action box to selecting an item.
     */
    public void cst3to6()
    {
        controlState = 6;
        attackableUnits = unitActionMenu.getAttackableUnits();
        attackableUnitsIndex = attackableUnits.size()-1;
        unitActionMenu.setVisible(false);
    }
    
    public void cst3to17()
    {
        controlState = 17;
        attackableUnits = unitActionMenu.getStaffableUnits();
        attackableUnitsIndex = attackableUnits.size()-1;
        unitActionMenu.setVisible(false);
    }
    
    public void cst3to21()
    {
        controlState = 21;
        itemSelect.start(selectedUnit);
        unitActionMenu.setVisible(false);
    }
    
    /**
     * Leaves the system action box and returns to the cursor
     */
    public void cst4to1()
    {
        controlState = 1;
        cursor.setVisible(true);
        systemAction.end();
    }
    
    /**
     * Ends the turn from the system action box
     */
    public void cst4to9()
    {
        controlState = 9;
        cursor.setVisible(false);
        systemAction.end();
        endTurn();
    }
    
    /**
     * leaves movingUnitMode to go to a number of different options.
     */
    public void cst5toX()
    {
        //if it's you're turn, and you're in a place w
        if(turn == 0)
        {
            controlState = 3;
            selectedUnit.getSprite().sendTrigger("idle");
            unitActionMenu.start(selectedUnit);
        }
    }
    
    /**
     * Goes to weapon select.
     */
    public void cst6to7()
    {
        controlState = 7;
        weaponSelect.start(selectedUnit, attackableUnits.get(attackableUnitsIndex));
    }

    
    /**
     * Going back from attack weapon select mode to unit action box
     */
    public void cst6to3()
    {
        controlState = 3;
        unitActionMenu.setVisible(true);
        cursor.moveInstantly(selectedUnit.getCoord());

    }
    
    /**
     * Going from weapon select back to target select
     */
    public void cst7to6()
    {
        controlState = 6;
        weaponSelect.end();
    }
    
    /**
     * Going from weapon select to a fight
     */
    public void cst7to8()
    {
        controlState = 8;
        cursor.setVisible(false);
        selectedUnit.equipWeapon(weaponSelect.getWeapon());
        weaponSelect.end();
        fightUI.start(selectedUnit,  attackableUnits.get(attackableUnitsIndex), fightGraphicsMode, map);
        mvArrow.end();
    }
    
    /**
     * The unit has finished attacking. Go back to move mode.
     */
    public void cst8toX()
    {
        switch(fightUI.isXPAwarded())
        {
            case 1:
                xp.start(fightUI.getAttacker(), fightUI.getAttackerXP());
                controlState = 13;
                break;
                
            case 2:
                xp.start(fightUI.getDefender(), fightUI.getDefenderXP());
                controlState = 13;
                break;
            
            case 0:
                controlState = 1;
                cursor.setVisible(true);
                cursor.moveInstantly(selectedUnit.getCoord());
                cursor.getSprite().sendTrigger("deactivate");
                selectedUnit.setHasMoved(true);
                fightUI.end();
                break;
        }
        
    }
    
    public void cst8to12()
    {
        //if player character among the dead
        //Send trigger for player death dialog Dialog
        
        controlState = 12;
        animationFrames = 30;
        
        //play sound of unit dieing
        
        
    }
    
    /**
     * Moves from the end of turn phase to someone else's turn
     * TODO: This doesn't work with other AI turns, as AI works right now.
     */
    public void cst9toX()
    {
        //if it's now the player's turn
        if(turn == 0)
            controlState = 11;
        
        //if it's now someone else's turn;
        else
        {
            controlState = 10;
            ai.start(turn);
        }
        
        startTurn();

    }
    
    /**
     * Ends the current AI turn and returns to the next turn function.
     */
    public void cst10to9()
    {
        controlState = 9;
        endTurn();
    }
    
    public void cst10to22()
    {
        controlState= 22;
    }
    
    /**
     * Starts a new player turn
     */
    public void cst11to1()
    {
        controlState = 1;
        cursor.setVisible(active);
    }
    
    /**
     * Removes the dieing units from DieingUnits, removing them from the game. And then goes back to 8's transition.
     */
    public void cst12toX()
    {
        dieingUnits.clear();
        cst8toX();
    }
    
    public void cst13to1()
    {
        controlState = 1;
        cursor.setVisible(true);
        cursor.moveInstantly(selectedUnit.getCoord());
        cursor.getSprite().sendTrigger("deactivate");
        selectedUnit.setHasMoved(true);
        fightUI.end();
        staffUI.end();
        xp.end();
    }
    
    /**
     * Temporary CST. Starts the battle from the prebattle menu.
     */
    public void cst14to1()
    {
        controlState = 1;
        cursor.setVisible(true);
        preBattleScene.end();
        
    }
    
    /**
     * Switches from the pre-battle menu to View Map mode
     */
    public void cst14to15()
    {
        controlState = 15;
        cursor.setVisible(true);
        preBattleScene.stop();
    }
    
    /**
     * Switches from the view map mode to the pre-battle menu
     */
    public void cst15to14()
    {
        controlState = 14;
        cursor.setVisible(false);
        preBattleScene.resume();
    }
    
    public void cstXto16()
    {
        if(getUnitAtPoint(cursor.getCoord()) != null)
        {
            unitInspectScene.start(controlState, getUnitAtPoint(cursor.getCoord()));
            controlState = 16;
        }
    }
    
    public void cst16toX()
    {
        controlState = unitInspectScene.getParentControlState();
        unitInspectScene.end();
    }
    
    /**
     * Goes to staff select.
     */
    public void cst17to18()
    {
        controlState = 18;
        weaponSelect.start(selectedUnit, attackableUnits.get(attackableUnitsIndex));
    }

    
    /**
     * Going from staff select back to target select
     */
    public void cst18to17()
    {
        controlState = 17;
        weaponSelect.end();
    }
    

    /**
     * Going from staff select to doing the healing.
     */
    public void cst18to19()
    {
        controlState = 19;
        cursor.setVisible(false);
        selectedUnit.equipWeapon(weaponSelect.getWeapon());
        weaponSelect.end();
        staffUI.start(selectedUnit, attackableUnits.get(attackableUnitsIndex));
        mvArrow.end();
    }
    
    /**
     * The unit has finished healing. award XP
     */
    public void cst19to13()
    {
        controlState = 13;
        xp.start(staffUI.getHealer(), staffUI.getHealerXP());
    }
    
    
    
    public void cst20to1()
    {
        controlState = 1;
        itemSelect.end();
        selectedUnit.setHasMoved(true);
        singleHealthBar = null;
        mvArrow.end();

    }
    
    public void cst21to20()
    {
        controlState = 20;
        singleHealthBar = new FightHealthBarUI(selectedUnit.getTotalHP(), selectedUnit.getCurHP(), 0);
        
        Usable u = selectedUnit.getInventory().get(itemSelect.getCursorLoc());
        
        String itemType = u.use().get(0);

        if (u.getCurUses() <= 0)
            selectedUnit.getInventory().remove(u);
        
        int healingDone = 0;


        if(itemType.substring(0,4).equals("Heal"))
        {
            healingDone = Integer.parseInt(itemType.substring(4));
        }

        //Take Damage
        selectedUnit.heal(healingDone);

        singleHealthBar.heal(healingDone);
    }
    
    public void cst21to3()
    {
        controlState = 3;
        itemSelect.end();
    }
    
    public int getTurn()
    {
        return turn;
    }
    
    //</editor-fold>
}
