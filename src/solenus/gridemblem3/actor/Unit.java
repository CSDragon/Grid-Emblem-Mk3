/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.actor;

import solenus.gridemblem3.item.*;
import java.util.ArrayList;

/**
 * Any living creature, human or otherwise, playable or otherwise, that's on the grid.
 *
 * @author Chris
 */
public class Unit extends Actor
{
    //inventory limits
    public static final int WEAPON_LIMIT = 5;
    public static final int INVENTORY_LIMIT = 5;
    
    //transport values
    public static final int ON_FOOT = 1;
    public static final int MOUNTED = 2;
    public static final int FLYING = 3;
    public static final int FLYING_MOUNT = 4;
    
    //Armor type values
    public static final int LIGHT = 1;
    public static final int MEDIUM = 2;
    public static final int HEAVY = 3;
    
    //Team values
    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int ALLY = 2;
    public static final int OTHER = 3;
    
    
    
    private String name;
    private String unitClass;
    private int team;
    private boolean hasMoved;
    
    //Combat Stats
    int level;
    
    private double hp;
    private double str;
    private double mag;
    private double spd;
    private double skill;
    private double def;
    private double res;
    private double luck;
    private int move;
    
    private int curHP;
    private int curMove;
    
    private int transportType;
    private int armorType;
    
    private int bonusHp;
    private int bonusStr;
    private int bonusMag;
    private int bonusSpd;
    private int bonusSkill;
    private int bonusDef;
    private int bonusRes;
    private int bonusLuck;
    private int bonusMove;
    
    //Level up stat boosts.
    private double hpup;
    private double strup;
    private double magup;
    private double spdup;
    private double skillup;
    private double defup;
    private double resup;
    private double luckup;

    private ArrayList<Weapon> weaponInventory;
    private ArrayList<Item> inventory;
    
    
    //<editor-fold desc="constructors">

    //defaultConstructor
    public Unit()
    {
        super("generic");
        
        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
    }

    //Test constructor
    public Unit(int t, int m, int mt)
    {
        super("generic");
        
        hp = 10;
        curHP = 10;
        team = t;
        move = m;
        transportType = mt;
        
        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
        
        hasMoved = false;
        passable = true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="inventory">
    
    /**
     * Equips a weapon, putting it at the front of its inventory
     * @param weapon the current location of the weapon in inventory
     */
    public void equipWeapon(int weapon)
    {
        Weapon temp = weaponInventory.get(weapon);
        weaponInventory.remove(weapon);
        weaponInventory.add(0, temp);
    }
    
    /**
     * Equips a weapon, putting it at the front of its inventory
     * @param w The weapon we're equipping.
     */
    public void equipWeapon(Weapon w)
    {
        //if it already exists in your inventory, remove it for a sec so we can add it to the front.
        if(weaponInventory.contains(w))
        {
            weaponInventory.remove(w);
        }
        
        weaponInventory.add(0, w);
    }
    
    /**
     * Adds a weapon to the character's weapon inventory
     * 
     * @param w add to weapon inventory
     * @return if weapon inventory had room or not.
     */
    public boolean addWeapon(Weapon w)
    {
        if(weaponInventory.size() < WEAPON_LIMIT)
        {
            weaponInventory.add(w);
            return true;
        }
        
        return false;
    }
    
    /**
     * Ads an item to a character's inventory
     * @param w add to inventory
     * @return if it was successfully added (if false send to convoy)
     */
    public boolean addItem(Item w)
    {
        if(inventory.size() < INVENTORY_LIMIT)
        {
            inventory.add(w);
            return true;
        }
        
        return false;
    }
    
       
    public boolean isEquipable(Weapon w)
    {
        //TODO
        return true;
    }
    
    /**
     * Finds all the weapons that can actually hit stuff.
     * @param enemy The dude you're fighting.
     * @return the list of weapons that can hit.
     */
    public ArrayList<Weapon> listEquipableWeaponsVsTarget(Unit enemy)
    {
        ArrayList<Weapon> ret = new ArrayList();
        int dist = distanceTo(enemy);
        
        for(Weapon w : weaponInventory)
        {
            if(isEquipable(w) && w.getMinRange() <= dist && w.getMaxRange() >= dist)
                ret.add(w);
        }
        
        return ret;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="stat mechanics">
    
    //level up
    public void levelUp()
    {
        hp += hpup;
        str += strup;
        mag += magup;
        spd += spdup;
        skill += skillup;
        def += defup;
        res += resup;
        luck += luckup;
        
        level++;
    }
    
    /**
     * The unit takes damage. Does not check if the unit dies
     * @param damage the damage dealt
     */
    public void takeDamage(int damage)
    {
        curHP -= damage;
        if (curHP < 0)
            curHP = 0;
    }
    
    /**
     * Heals the unit, up to max hp.
     * @param amt the ammount healed
     */
    public void heal(int amt)
    {
        curHP += amt;
        if(curHP > getTotalHp())
            curHP = getTotalHp();
    }

    //</editor-fold>
    
    /**
     * Start a new turn. So far it's just hasMoved, but there will be more.
     */
    public void beginTurn()
    {
        hasMoved = false;
    }
    
    /**
     * Signals that the unit has finished moving
     */
    public void endMovement()
    {
        hasMoved = true;
    }
    
    /**
     * sets the destination to point(x,y)
     * @param _x the x coord where we want to go
     * @param _y the y coord where we want to go
     */
    public void setDest(int _x, int _y)
    {
        //animation transitions
        if(_x < x)
            sprite.sendTrigger("moveLeft");
        else if(_y < y)
            sprite.sendTrigger("moveUp");
        else if (_x > x)
            sprite.sendTrigger("moveRight");
        else if (_y > y)
            sprite.sendTrigger("moveDown");
        
        super.setDest(_x, _y);
    }
    
    /**
     * Checks if this unit and another unit are friends or foes
     * @param other
     * @return if they are allies or enemies
     */
    public boolean isAlly(Unit other)
    {
        if(team == ENEMY)
            return other.team == ENEMY;
        else
            return (other.getTeam() != ENEMY);
    }
    
    public boolean isDead()
    {
        return (curHP <= 0);
    }
    
    /**
     * Using brave weapon? Has adept? Calcluate it here. TODO
     * @return The number of attacks this unit does this round.
     */
    public int numAttacks()
    {
        return 1;
    }

    
    //<editor-fold desc="getters and setters">
    
    public int getLevel()
    {
        return level;
    }
    
    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @return team number
     */
    public int getTeam()
    {
        return team;
    }
    
    /**
     * @return the hp
     */
    public double getHp() 
    {
        return hp;
    }

    /**
     * @return the str
     */
    public double getStr() 
    {
        return str;
    }

    /**
     * @return the mag
     */
    public double getMag() 
    {
        return mag;
    }

    /**
     * @return the spd
     */
    public double getSpd() 
    {
        return spd;
    }

    /**
     * @return the skill
     */
    public double getSkill() 
    {
        return skill;
    }


    /**
     * @return the def
     */
    public double getDef() 
    {
        return def;
    }

    /**
     * @return the res
     */
    public double getRes()
    {
        return res;
    }

    /**
     * @return the luck
     */
    public double getLuck() 
    {
        return luck;
    }

    /**
     * @return the move
     */
    public int getMove()
    {
        return move;
    }

    /**
     * @return the hpup
     */
    public double getHpup()
    {
        return hpup;
    }

    /**
     * @return the strup
     */
    public double getStrup() 
    {
        return strup;
    }
    
    /**
     * @return the magup
     */
    public double getMagup()
    {
        return magup;
    }

    /**
     * @return the spdup
     */
    public double getSpdup() 
    {
        return spdup;
    }
    
    /**
     * @return the skillup
     */
    public double getSkillup()
    {
        return skillup;
    }

    /**
     * @return the defup
     */
    public double getDefup() 
    {
        return defup;
    }

    /**
     * @return the resup
     */
    public double getResup()
    {
        return resup;
    }

    /**
     * @return the luckup
     */
    public double getLuckup() 
    {
        return luckup;
    }

    /**
     * @return the bonusHp
     */
    public int getBonusHp() 
    {
        return bonusHp;
    }

    /**
     * @return the bonusStr
     */
    public int getBonusStr() 
    {
        return bonusStr;
    }

    /**
     * @return the bonusMag
     */
    public int getBonusMag() 
    {
        return bonusMag;
    }

    /**
     * @return the bonusSpd
     */
    public int getBonusSpd() 
    {
        return bonusSpd;
    }

    /**
     * @return the bonusSkill
     */
    public int getBonusSkill() 
    {
        return bonusSkill;
    }

    /**
     * @return the bonusDef
     */
    public int getBonusDef() 
    {
        return bonusDef;
    }

    /**
     * @return the bonusRes
     */
    public int getBonusRes() 
    {
        return bonusRes;
    }

    /**
     * @return the bonusLuck
     */
    public int getBonusLuck() 
    {
        return bonusLuck;
    }

    /**
     * @return the bonusMove
     */
    public int getBonusMove() 
    {
        return bonusMove;
    }
    
    
    /**
     * @return the totalHp
     */
    public int getTotalHp() 
    {
        return (int)(hp+bonusHp);
    }

    /**
     * @return the totalStr
     */
    public int getTotalStr() 
    {
        return (int)(str+bonusStr);
    }

    /**
     * @return the totalMag
     */
    public int getTotalMag() 
    {
        return (int)(mag+bonusMag);
    }

    /**
     * @return the totalSpd
     */
    public int getTotalSpd() 
    {
        return (int)(spd+bonusSpd);
    }

    /**
     * @return the totalSkill
     */
    public int getTotalSkill() 
    {
        return (int)skill+bonusSkill;
    }

    /**
     * @return the totalDef
     */
    public int getTotalDef() 
    {
        return (int)def+bonusDef;
    }

    /**
     * @return the totalRes
     */
    public int getTotalRes() 
    {
        return (int)res+bonusRes;
    }

    /**
     * @return the totalLuck
     */
    public int getTotalLuck() 
    {
        return (int)luck+bonusLuck;
    }

    /**
     * @return the totalMove
     */
    public int getTotalMove() 
    {
        return (int)move+bonusMove;
    }
    
    /**
     * @return hasMoved
     */
    public boolean getHasMoved()
    {
        return hasMoved;
    }
    
    /**
     * Gets the equipped weapon. Returns null if no weapon is equipped 
     * @return The first item in weaponInventory. if it's an equpiable weapon.
     */
    public Weapon getEquppedWeapon()
    {
        if(weaponInventory.isEmpty() || !isEquipable(weaponInventory.get(0)))
            return null;
        else
            return weaponInventory.get(0);
    }
    
    public int getTransportType()
    {
        return transportType;
    }
    
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
    
    
    //</editor-fold>


}
