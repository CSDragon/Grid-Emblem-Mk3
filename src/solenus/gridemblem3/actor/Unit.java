/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.actor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import solenus.gridemblem3.item.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
    
    
    private String unitClass;
    private int team;
    private boolean hasMoved;
    
    //Combat Stats
    int level;
    int xp;
    
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
    
    private int bonusHP;
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
    
    private int swordMastery;
    private int axeMastery;
    private int lanceMastery;
    private int bowMastery;
    private int daggerMastery;
    private int fireMastery;
    private int windMastery;
    private int lightningMastery;
    private int lightMastery;
    private int darkMastery;
    private int staffMastery;

    private ArrayList<Weapon> weaponInventory;
    private ArrayList<Usable> inventory;
    private ArrayList<String> skills;
    
    
    //<editor-fold desc="constructors">

    /**
     * Creates an empty unit.
     */
    public Unit()
    {
        super();
        
        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
    }

    //Test constructor
    public Unit(int t, int m, int mt)
    {
        super(null);
        
        hp = 10;
        curHP = 10;
        team = t;
        move = m;
        transportType = mt;
        
        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
        
        hasMoved = false;
        passable = true;
        
        xp = 80;
    }
    
    /**
     * Creates a new unit from a text file
     * @param in The text we're reading from.
     * @throws IOException 
     */
    public Unit(BufferedReader in) throws IOException
    {
        super(in.readLine().substring(6));
        unitClass = in.readLine().substring(7);
        team = Integer.parseInt(in.readLine().substring(6));
        level = Integer.parseInt(in.readLine().substring(7));
        xp = Integer.parseInt(in.readLine().substring(4));
        hp  = Double.parseDouble(in.readLine().substring(4));
        str = Double.parseDouble(in.readLine().substring(5));
        mag = Double.parseDouble(in.readLine().substring(5));
        spd = Double.parseDouble(in.readLine().substring(5));
        skill = Double.parseDouble(in.readLine().substring(7));
        def = Double.parseDouble(in.readLine().substring(5));
        res = Double.parseDouble(in.readLine().substring(5));
        luck = Double.parseDouble(in.readLine().substring(6));
        
        move = Integer.parseInt(in.readLine().substring(6));
        
        hpup  = Double.parseDouble(in.readLine().substring(6));
        strup = Double.parseDouble(in.readLine().substring(7));
        magup = Double.parseDouble(in.readLine().substring(7));
        spdup = Double.parseDouble(in.readLine().substring(7));
        skillup = Double.parseDouble(in.readLine().substring(9));
        defup = Double.parseDouble(in.readLine().substring(7));
        resup = Double.parseDouble(in.readLine().substring(7));
        luckup = Double.parseDouble(in.readLine().substring(8));
        
        transportType = Integer.parseInt(in.readLine().substring(11));
        armorType = Integer.parseInt(in.readLine().substring(12));
        
        swordMastery = Integer.parseInt(in.readLine().substring(15));
        axeMastery = Integer.parseInt(in.readLine().substring(13));
        lanceMastery = Integer.parseInt(in.readLine().substring(15));
        bowMastery = Integer.parseInt(in.readLine().substring(13));
        daggerMastery = Integer.parseInt(in.readLine().substring(16));
        fireMastery = Integer.parseInt(in.readLine().substring(14));
        windMastery = Integer.parseInt(in.readLine().substring(14));
        lightningMastery = Integer.parseInt(in.readLine().substring(19));
        lightMastery = Integer.parseInt(in.readLine().substring(15));
        darkMastery = Integer.parseInt(in.readLine().substring(14));
        staffMastery = Integer.parseInt(in.readLine().substring(15));


        
        in.readLine();

        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
        skills = new ArrayList<>();
        curHP = (int)hp;
        
        int numWeapons = Integer.parseInt(in.readLine().substring(13));
        in.readLine();
        
        for(int i = 0; i<numWeapons; i++)
            weaponInventory.add(new Weapon(in));
        
        int numItems = Integer.parseInt(in.readLine().substring(11));
        in.readLine();
        
        for(int i = 0; i<numItems; i++)
            inventory.add(new Usable(in));
        
        //Read in skills.
        in.readLine();
        int numSkills = Integer.parseInt(in.readLine().substring(12));
        
        for(int i = 0; i<numSkills; i++)
            skills.add(in.readLine());
        
        in.readLine();
    }
    
        
    /**
     * Writes the unit to a file.
     * @param bw The file writer.
     * @throws IOException 
     */
    public void save(BufferedWriter bw) throws IOException
    {
        bw.write("Name: "+name); bw.newLine();
        bw.write("Class: "+unitClass); bw.newLine();
        bw.write("Team: "+team); bw.newLine();
        
        bw.write("Level: "+level); bw.newLine();
        bw.write("XP: "+xp); bw.newLine();
        
        bw.write("HP: "+hp); bw.newLine();
        bw.write("STR: "+str); bw.newLine();
        bw.write("MAG: "+mag); bw.newLine();
        bw.write("SPD: "+spd); bw.newLine();
        bw.write("Skill: "+skill); bw.newLine();
        bw.write("DEF: "+def); bw.newLine();
        bw.write("RES: "+res); bw.newLine();
        bw.write("Luck: "+luck); bw.newLine();
        
        bw.write("Move: "+move); bw.newLine();
   
        bw.write("HPUP: "+hpup); bw.newLine();
        bw.write("STRUP: "+strup); bw.newLine();
        bw.write("MAGUP: "+magup); bw.newLine();
        bw.write("SPDUP: "+spdup); bw.newLine();
        bw.write("SkillUP: "+skillup); bw.newLine();
        bw.write("DEFUP: "+defup); bw.newLine();
        bw.write("RESUP: "+resup); bw.newLine();
        bw.write("LuckUP: "+luckup);  bw.newLine();
        
        bw.write("Move Type: "+transportType); bw.newLine();
        bw.write("Armor Type: "+armorType); bw.newLine();
        
        bw.write("Sword Mastery: "+swordMastery); bw.newLine();
        bw.write("Axe Mastery: "+axeMastery); bw.newLine();
        bw.write("Lance Mastery: "+lanceMastery); bw.newLine();
        bw.write("Bow Mastery: "+bowMastery); bw.newLine();
        bw.write("Dagger Mastery: "+daggerMastery); bw.newLine();
        bw.write("Fire Mastery: "+fireMastery); bw.newLine();
        bw.write("Wind Mastery: "+windMastery); bw.newLine();
        bw.write("Lightning Mastery: "+lightningMastery); bw.newLine();
        bw.write("Light Mastery: "+lightMastery); bw.newLine();
        bw.write("Dark Mastery: "+darkMastery); bw.newLine();
        bw.write("Staff Mastery: "+staffMastery); bw.newLine();
        
        bw.newLine();
        bw.write("Num Weapons: "+weaponInventory.size()); bw.newLine();
        bw.newLine();
        for(Weapon w: weaponInventory)
            w.save(bw);
        
        bw.write("Num Items: "+inventory.size()); bw.newLine();
        bw.newLine();
        for(Usable u: inventory)
            u.save(bw);
        
        bw.write("Skills"); bw.newLine();
        bw.write("Num Skills: "+skills.size()); bw.newLine();
        for(String s: skills)
        {
            bw.write(s); bw.newLine();
        }
        
        bw.newLine();
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
     * @param u add to inventory
     * @return if it was successfully added (if false send to convoy)
     */
    public boolean addItem(Usable u)
    {
        if(inventory.size() < INVENTORY_LIMIT)
        {
            inventory.add(u);
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
    
    /**
     * Levels up the unit.
     */
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
        xp -= 100;
    }
    
    /**
     * Makes the unit receive XP
     * @param xpReceived The XP the unit received.
     * @return If the unit leveled up
     */
    public boolean receiveXP(int xpReceived)
    {
        xp += xpReceived;
        return (xp >= 100);
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
        if(curHP > getTotalHP())
            curHP = getTotalHP();
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
    
    /**
     * Writes a Unit to a file
     * @param u The Unit to write to.
     */
    public static void writeToPrefab(Unit u)
    {
        File saveFile = new File("assets/prefabs/units/"+u.getName()+".txt");
        try
        {
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            u.save(bw);
            
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Unit Prefab Generation failed");
        }
    }
    
    /**
     * Loads a Unit from a prefab.
     * @param name The Unit prefab to load
     * @return The loaded Unit.
     */
    public static Unit loadFromPrefab(String name)
    {
        Unit ret = null;
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/prefabs/units/"+name+".txt"));
            ret = new Unit(in);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading Unit prefab "+ name +" failed, file doesn't seem to exist.");
            System.exit(-1);
        }
        
        return ret;
    }
    
    //<editor-fold desc="getters and setters">
    
    public int getLevel()
    {
        return level;
    }
    
    public int getXP()
    {
        return xp;
    }
    
    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }
    
    public String getUnitClass()
    {
        return unitClass;
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
    public double getHP() 
    {
        return hp;
    }
    
    public int getCurHP()
    {
        return curHP;
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
    public double getHPup()
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
     * @return the bonusHP
     */
    public int getBonusHP() 
    {
        return bonusHP;
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
     * @return the totalHP
     */
    public int getTotalHP() 
    {
        return (int)(hp+bonusHP);
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
    
    public ArrayList<Usable> getInventory()
    {
        return inventory;
    }
    
    /**
     * @return the swordMastery
     */
    public int getSwordMastery()
    {
        return swordMastery;
    }

    /**
     * @param swordMastery the swordMastery to set
     */
    public void setSwordMastery(int swordMastery) 
    {
        this.swordMastery = swordMastery;
    }

    /**
     * @return the axeMastery
     */
    public int getAxeMastery()
    {
        return axeMastery;
    }

    /**
     * @param axeMastery the axeMastery to set
     */
    public void setAxeMastery(int axeMastery) 
    {
        this.axeMastery = axeMastery;
    }

    /**
     * @return the lanceMastery
     */
    public int getLanceMastery()
    {
        return lanceMastery;
    }

    /**
     * @param lanceMastery the lanceMastery to set
     */
    public void setLanceMastery(int lanceMastery)
    {
        this.lanceMastery = lanceMastery;
    }

    /**
     * @return the bowMastery
     */
    public int getBowMastery()
    {
        return bowMastery;
    }

    /**
     * @param bowMastery the bowMastery to set
     */
    public void setBowMastery(int bowMastery)
    {
        this.bowMastery = bowMastery;
    }

    /**
     * @return the daggerMastery
     */
    public int getDaggerMastery()
    {
        return daggerMastery;
    }

    /**
     * @param daggerMastery the daggerMastery to set
     */
    public void setDaggerMastery(int daggerMastery) 
    {
        this.daggerMastery = daggerMastery;
    }

    /**
     * @return the fireMastery
     */
    public int getFireMastery()
    {
        return fireMastery;
    }

    /**
     * @param fireMastery the fireMastery to set
     */
    public void setFireMastery(int fireMastery)
    {
        this.fireMastery = fireMastery;
    }

    /**
     * @return the windMastery
     */
    public int getWindMastery()
    {
        return windMastery;
    }

    /**
     * @param windMastery the windMastery to set
     */
    public void setWindMastery(int windMastery)
    {
        this.windMastery = windMastery;
    }

    /**
     * @return the lightningMastery
     */
    public int getLightningMastery()
    {
        return lightningMastery;
    }

    /**
     * @param lightningMastery the lightningMastery to set
     */
    public void setLightningMastery(int lightningMastery)
    {
        this.lightningMastery = lightningMastery;
    }

    /**
     * @return the lightMastery
     */
    public int getLightMastery()
    {
        return lightMastery;
    }

    /**
     * @param lightMastery the lightMastery to set
     */
    public void setLightMastery(int lightMastery)
    {
        this.lightMastery = lightMastery;
    }

    /**
     * @return the darkMastery
     */
    public int getDarkMastery()
    {
        return darkMastery;
    }

    /**
     * @param darkMastery the darkMastery to set
     */
    public void setDarkMastery(int darkMastery)
    {
        this.darkMastery = darkMastery;
    }

    /**
     * @return the staffMastery
     */
    public int getStaffMastery()
    {
        return staffMastery;
    }

    /**
     * @param staffMastery the staffMastery to set
     */
    public void setStaffMastery(int staffMastery)
    {
        this.staffMastery = staffMastery;
    }
    
    //</editor-fold>



}
