/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.actor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import solenus.gridemblem3.item.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.party.PartyUnit;

/**
 * Any living creature, human or otherwise, playable or otherwise, that's on the grid.
 *
 * @author Chris
 */
public class Unit extends Actor
{
    PartyUnit pu;
    
    private boolean hasMoved;
    
    private int curHP;
    private int curMove;
    
    private int bonusHP;
    private int bonusStr;
    private int bonusMag;
    private int bonusSkill;
    private int bonusSpd;
    private int bonusLuck;
    private int bonusDef;
    private int bonusRes;
    private int bonusMove;
    
    private boolean aiActive;
    
    //<editor-fold desc="constructors">

    /**
     * Creates an empty unit.
     */
    public Unit()
    {
        super();
        
        //units can be passed through, as long as their allied.
        passable = true;
        
        pu = new PartyUnit();
    }

    /**
     * Creates a new unit from a text file
     * @param _pu The Party Unit which acts as the stats for this unit
     */
    public Unit(PartyUnit _pu) 
    {
        super(_pu.getName());
        pu = _pu;
        
        //units can be passed through, as long as their allied.
        passable = true;
        
        curHP = (int)pu.getHP();
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
            PartyUnit pu = new PartyUnit(in);
            ret = new Unit(pu);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Loading Unit prefab "+ name +" failed, file doesn't seem to exist.");
            System.exit(-1);
        }
        
        return ret;
    }
    
        
    /**
     * Writes the unit to a file.
     * @param bw The file writer.
     * @throws IOException 
     */
    public void save(BufferedWriter bw) throws IOException
    {
        pu.save(bw);
    }
    
    public final void loadPortrait()
    {
        pu.loadPortrait();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="inventory">
    
    /**
     * Equips a weapon, putting it at the front of its inventory
     * @param weapon the current location of the weapon in inventory
     */
    public void equipWeapon(int weapon)
    {
        pu.equipWeapon(weapon);
    }
    
    /**
     * Equips a weapon, putting it at the front of its inventory
     * @param w The weapon we're equipping.
     */
    public void equipWeapon(Weapon w)
    {
        pu.equipWeapon(w);
    }
    
    /**
     * Adds a weapon to the character's weapon inventory
     * 
     * @param w add to weapon inventory
     * @return if weapon inventory had room or not.
     */
    public final boolean addWeapon(Weapon w)
    {
        return pu.addWeapon(w);
    }
    
    /**
     * Ads an item to a character's inventory
     * @param u add to inventory
     * @return if it was successfully added (if false send to convoy)
     */
    public final boolean addItem(Usable u)
    {
        return pu.addItem(u);
    }
    
    
    /**
     * Removes an weapon from this unit's inventory.
     * @param w the weapon to be removed.
     * @return Weather this unit even HAD that weapon. 
     */
    public boolean removeWeapon(Weapon w)
    {
        return pu.removeWeapon(w);
    }

    /**
     * Removes an item from this unit's inventory.
     * @param u the item to be removed.
     * @return Weather this unit even HAD that item. 
     */
    public boolean removeItem(Usable u)
    {
        return pu.removeItem(u);
    }
    
       
    public boolean canEquipWeapon(Weapon w)
    {
        return(pu.canEquipWeapon(w));
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
        
        for(Weapon w : pu.getWeaponInventory())
        {
            if(canEquipWeapon(w) && w.getMinRange() <= dist && w.getMaxRange() >= dist)
                ret.add(w);
        }
        
        return ret;
    }
    
    /**
     * Uses a usable. This is still very much WIP.
     * @param use The usable to be used.
     * @return If it worked
     */
    public boolean useItem(Usable use)
    {
        use.use();
        if(use.getCurUses() <= 0)
        {
            try
            {
                return use.getOwner().removeItem(use);
            }
            catch(NullPointerException e)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Uses an item that is possibly in the convoy.
     * @param use The usable being used.
     * @param pd The save where the convoy is located.
     * @return If it worked
     */
    public boolean useItemFromConvoy(Usable use, PlayerData pd)
    {
        if(use.getOwner() == null)
        {
            use.use();
            return pd.removeItem(use);
        }
        else
            return this.useItem(use);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="skills">
    
    /**
     * Adds a skill to the skills list if the skills list is not full
     * @param s The skill to add.
     * @return Returns if it was added successfully.
     */
    public boolean addSkill(String s)
    {
        return pu.addSkill(s);
    }
    
    /**
     * Removes a Skill from the skills list.
     * @param s The skill to remove
     * @return Whether or not it worked.
     */
    public boolean removeSkill(String s)
    {
        return pu.removeSkill(s);
    }
    
    /**
     * Puts a skill in your skill bank, so you can add it back later.
     * @param s The skill to be put in the bank.
     * @return If it worked.
     */
    public boolean addSkillToReserve(String s)
    {
        return pu.addSkillToReserve(s);
    }
    
    /**
     * Removes a skill from your skill bank.
     * @param s The skill to remove
     * @return If it worked.
     */
    public boolean removeSkillFromReserve(String s)
    {
        return pu.removeSkillFromReserve(s);
    }
    
    /**
     * Moves a skill from the skill bank to the skills list.
     * @param s The skill to move
     * @return If it worked
     */
    public boolean moveSkillToReserve(String s)
    {
        return pu.moveSkillToReserve(s);
    }
    
    /**
     * Moves a skill from this unit's skills list to the reserve.
     * @param s The skill to move
     * @return If it worked.
     */
    public boolean moveSkillFromReserve(String s)
    {
        return pu.moveSkillFromReserve(s);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="stat mechanics">
    
    /**
     * Levels up the unit.
     */
    public void levelUp()
    {
        pu.levelUp();
    }
    
    /**
     * Makes the unit receive XP
     * @param xpReceived The XP the unit received.
     * @return If the unit leveled up
     */
    public boolean receiveXP(int xpReceived)
    {
        return(pu.receiveXP(xpReceived));
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
    
    public PartyUnit getPartyUnit()
    {
        return pu;
    }
    
    /**
     * @return name
     */
    public String getName()
    {
        return pu.getName();
    }
    
    public String getUnitClass()
    {
        return pu.getUnitClass();
    }
    
    /**
     * @return team number
     */
    public int getTeam()
    {
        return pu.getTeam();
    }
    
    public int getLevel()
    {
        return pu.getLevel();
    }
    
    public int getXP()
    {
        return pu.getXP();
    }
    
    /**
     * @return the hp
     */
    public double getHP() 
    {
        return pu.getHP();
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
        return pu.getStr();
    }

    /**
     * @return the mag
     */
    public double getMag() 
    {
        return pu.getMag();
    }

    /**
     * @return the spd
     */
    public double getSpd() 
    {
        return pu.getSpd();
    }

    /**
     * @return the skill
     */
    public double getSkill() 
    {
        return pu.getSpd();
    }


    /**
     * @return the def
     */
    public double getDef() 
    {
        return pu.getDef();
    }

    /**
     * @return the res
     */
    public double getRes()
    {
        return pu.getRes();
    }

    /**
     * @return the luck
     */
    public double getLuck() 
    {
        return pu.getLuck();
    }

    /**
     * @return the move
     */
    public int getMove()
    {
        return pu.getMove();
    }

    /**
     * @return the hpup
     */
    public double getHPup()
    {
        return pu.getHPup();
    }

    /**
     * @return the strup
     */
    public double getStrup() 
    {
        return pu.getStrup();
    }
    
    /**
     * @return the magup
     */
    public double getMagup()
    {
        return pu.getMagup();
    }

    /**
     * @return the spdup
     */
    public double getSpdup() 
    {
        return pu.getSpdup();
    }
    
    /**
     * @return the skillup
     */
    public double getSkillup()
    {
        return pu.getSkillup();
    }

    /**
     * @return the defup
     */
    public double getDefup() 
    {
        return pu.getDefup();
    }

    /**
     * @return the resup
     */
    public double getResup()
    {
        return pu.getResup();
    }

    /**
     * @return the luckup
     */
    public double getLuckup() 
    {
        return pu.getLuckup();
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
        return (int)(pu.getHP()+bonusHP);
    }

    /**
     * @return the totalStr
     */
    public int getTotalStr() 
    {
        return (int)(pu.getStr()+bonusStr);
    }

    /**
     * @return the totalMag
     */
    public int getTotalMag() 
    {
        return (int)(pu.getMag()+bonusMag);
    }

    /**
     * @return the totalSpd
     */
    public int getTotalSpd() 
    {
        return (int)(pu.getSpd()+bonusSpd);
    }

    /**
     * @return the totalSkill
     */
    public int getTotalSkill() 
    {
        return (int)pu.getSkill()+bonusSkill;
    }

    /**
     * @return the totalDef
     */
    public int getTotalDef() 
    {
        return (int)pu.getDef()+bonusDef;
    }

    /**
     * @return the totalRes
     */
    public int getTotalRes() 
    {
        return (int)pu.getRes()+bonusRes;
    }

    /**
     * @return the totalLuck
     */
    public int getTotalLuck() 
    {
        return (int)pu.getLuck()+bonusLuck;
    }

    /**
     * @return the totalMove
     */
    public int getTotalMove() 
    {
        return (int)pu.getMove()+bonusMove;
    }
    
    /**
     * @return hasMoved
     */
    public boolean getHasMoved()
    {
        return hasMoved;
    }
    
    /**
     * Signals that the unit has finished moving
     * @param b IF it has moved or not.
     */
    public void setHasMoved(boolean b)
    {
        hasMoved = b;
    }
    
    public int getTransportType()
    {
        return pu.getTransportType();
    }
    
    public int getArmorType()
    {
        return pu.getArmorType();
    }
    
    public ArrayList<Weapon> getWeaponInventory()
    {
        return pu.getWeaponInventory();
    }
        
    /**
     * Gets the equipped weapon. Returns null if no weapon is equipped 
     * @return The first item in weaponInventory. if it's an equpiable weapon.
     */
    public Weapon getEquppedWeapon()
    {
        return pu.getEquppedWeapon();
    }
    
    public ArrayList<Usable> getInventory()
    {
        return pu.getInventory();
    }
    
    public ArrayList<String> getSkills()
    {
        return pu.getSkills();
    }
    
    public ArrayList<String> getSkillsReserve()
    {
        return pu.getSkillsReserve();
    }
    
    /**
     * Returns the mastery level of a chosen weapon.
     * @param weaponType The type of weapon as defined by the Weapon class
     * @return The mastery in that weapon type
     */
    public int getWeaponMastery(int weaponType)
    {
        return pu.getWeaponMastery(weaponType);
    }
   
    /**
     * @return the portrait
     */
    public BufferedImage getPortrait() 
    {
        return pu.getPortrait();
    }
    
    public boolean getAIActive()
    {
        return aiActive;
    }
    
    public void setAIActive(boolean b)
    {
        aiActive = b;
    }

    //</editor-fold>


}
