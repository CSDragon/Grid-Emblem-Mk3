/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.gridemblem3.party;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import solenus.gridemblem3.PlayerData;
import solenus.gridemblem3.item.Usable;
import solenus.gridemblem3.item.Weapon;

/**
 *
 * @author Chris
 */
public class PartyUnit 
{
    //inventory limits
    public static final int WEAPON_LIMIT = 5;
    public static final int INVENTORY_LIMIT = 5;
    public static final int SKILL_LIMIT = 5;
    
    //transport values
    public static final int ON_FOOT = 1;
    public static final int MOUNTED = 2;
    public static final int FLYING = 3;
    public static final int FLYING_MOUNT = 4;
    
    //Armor type values
    public static final int ROBES = 0;
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
    
    //Progression Stats
    int level;
    int xp;
    
    //Combat Stats
    private double hp;
    private double str;
    private double mag;
    private double skill;
    private double spd;
    private double luck;
    private double def;
    private double res;
    private int move;

    
    //Level up stat boosts.
    private double hpup;
    private double strup;
    private double magup;
    private double skillup;
    private double spdup;
    private double luckup;
    private double defup;
    private double resup;
    
    private int transportType;
    private int armorType;
    
    private int[] weaponMastery;
    
    private ArrayList<Weapon> weaponInventory;
    private ArrayList<Usable> inventory;
    private ArrayList<String> skills;
    private ArrayList<String> skillsReserve;
    
    private BufferedImage portrait;

    
    //<editor-fold desc="constructors">

    /**
     * Creates an empty unit.
     */
    public PartyUnit()
    {
        //units can be passed through, as long as their allied.
        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
        skills = new ArrayList<>();
        skillsReserve = new ArrayList<>();
        
        loadPortrait();
    }
    
        /**
     * Creates a new unit from a text file
     * @param in The text we're reading from.
     * @throws IOException 
     */
    public PartyUnit(BufferedReader in) throws IOException
    {
        name = in.readLine().substring(6);
        unitClass = in.readLine().substring(7);
        team = Integer.parseInt(in.readLine().substring(6));
        level = Integer.parseInt(in.readLine().substring(7));
        xp = Integer.parseInt(in.readLine().substring(4));
        hp  = Double.parseDouble(in.readLine().substring(4));
        str = Double.parseDouble(in.readLine().substring(5));
        mag = Double.parseDouble(in.readLine().substring(5));
        skill = Double.parseDouble(in.readLine().substring(7));
        spd = Double.parseDouble(in.readLine().substring(5));
        luck = Double.parseDouble(in.readLine().substring(6));
        def = Double.parseDouble(in.readLine().substring(5));
        res = Double.parseDouble(in.readLine().substring(5));
        
        move = Integer.parseInt(in.readLine().substring(6));
        
        hpup  = Double.parseDouble(in.readLine().substring(6));
        strup = Double.parseDouble(in.readLine().substring(7));
        magup = Double.parseDouble(in.readLine().substring(7));
        skillup = Double.parseDouble(in.readLine().substring(9));
        spdup = Double.parseDouble(in.readLine().substring(7));
        luckup = Double.parseDouble(in.readLine().substring(8));
        defup = Double.parseDouble(in.readLine().substring(7));
        resup = Double.parseDouble(in.readLine().substring(7));
        
        transportType = Integer.parseInt(in.readLine().substring(11));
        armorType = Integer.parseInt(in.readLine().substring(12));
        
        weaponMastery = new int[12];
        
        weaponMastery[Weapon.SWORD] = Integer.parseInt(in.readLine().substring(15));
        weaponMastery[Weapon.AXE] = Integer.parseInt(in.readLine().substring(13));
        weaponMastery[Weapon.LANCE] = Integer.parseInt(in.readLine().substring(15));
        weaponMastery[Weapon.BOW]= Integer.parseInt(in.readLine().substring(13));
        weaponMastery[Weapon.DAGGER]= Integer.parseInt(in.readLine().substring(16));
        weaponMastery[Weapon.FIRE]= Integer.parseInt(in.readLine().substring(14));
        weaponMastery[Weapon.WIND]= Integer.parseInt(in.readLine().substring(14));
        weaponMastery[Weapon.LIGHTNING] = Integer.parseInt(in.readLine().substring(19));
        weaponMastery[Weapon.LIGHT] = Integer.parseInt(in.readLine().substring(15));
        weaponMastery[Weapon.DARK] = Integer.parseInt(in.readLine().substring(14));
        weaponMastery[Weapon.STAFF]= Integer.parseInt(in.readLine().substring(15));
        weaponMastery[Weapon.NATURAL]= Integer.parseInt(in.readLine().substring(17));


        
        in.readLine();

        weaponInventory = new ArrayList<>();
        inventory = new ArrayList<>();
        skills = new ArrayList<>();
        skillsReserve = new ArrayList<>();
        
        int numWeapons = Integer.parseInt(in.readLine().substring(13));
        in.readLine();
        
        for(int i = 0; i<numWeapons; i++)
            addWeapon(new Weapon(in));
        
        int numItems = Integer.parseInt(in.readLine().substring(11));
        in.readLine();
        
        for(int i = 0; i<numItems; i++)
            addItem(new Usable(in));
        
        //Read in skills.
        in.readLine();
        int numSkills = Integer.parseInt(in.readLine().substring(12));
        for(int i = 0; i<numSkills; i++)
            skills.add(in.readLine());
        in.readLine();
        
        int numSkillsReserve = Integer.parseInt(in.readLine().substring(17));
        for(int i = 0; i<numSkillsReserve; i++)
            skillsReserve.add(in.readLine());
        in.readLine();
        
        loadPortrait();
    }
    
    /**
     * Loads a Unit from a prefab.
     * @param name The Unit prefab to load
     * @return The loaded Unit.
     */
    public static PartyUnit loadFromPrefab(String name)
    {
        PartyUnit ret = null;
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("assets/prefabs/units/"+name+".txt"));

            ret = new PartyUnit(in);
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
        bw.write("Name: "+name); bw.newLine();
        bw.write("Class: "+unitClass); bw.newLine();
        bw.write("Team: "+team); bw.newLine();
        
        bw.write("Level: "+level); bw.newLine();
        bw.write("XP: "+xp); bw.newLine();
        
        bw.write("HP: "+hp); bw.newLine();
        bw.write("STR: "+str); bw.newLine();
        bw.write("MAG: "+mag); bw.newLine();
        bw.write("Skill: "+skill); bw.newLine();
        bw.write("SPD: "+spd); bw.newLine();
        bw.write("Luck: "+luck); bw.newLine();
        bw.write("DEF: "+def); bw.newLine();
        bw.write("RES: "+res); bw.newLine();
        
        bw.write("Move: "+move); bw.newLine();
   
        bw.write("HPUP: "+hpup); bw.newLine();
        bw.write("STRUP: "+strup); bw.newLine();
        bw.write("MAGUP: "+magup); bw.newLine();
        bw.write("SkillUP: "+skillup); bw.newLine();
        bw.write("SPDUP: "+spdup); bw.newLine();
        bw.write("LuckUP: "+luckup);  bw.newLine();
        bw.write("DEFUP: "+defup); bw.newLine();
        bw.write("RESUP: "+resup); bw.newLine();
        
        bw.write("Move Type: "+transportType); bw.newLine();
        bw.write("Armor Type: "+armorType); bw.newLine();
        
        bw.write("Sword Mastery: "+weaponMastery[Weapon.SWORD]); bw.newLine();
        bw.write("Axe Mastery: "+weaponMastery[Weapon.AXE]); bw.newLine();
        bw.write("Lance Mastery: "+weaponMastery[Weapon.LANCE]); bw.newLine();
        bw.write("Bow Mastery: "+weaponMastery[Weapon.BOW]); bw.newLine();
        bw.write("Dagger Mastery: "+weaponMastery[Weapon.DAGGER]); bw.newLine();
        bw.write("Fire Mastery: "+weaponMastery[Weapon.FIRE]); bw.newLine();
        bw.write("Wind Mastery: "+weaponMastery[Weapon.WIND]); bw.newLine();
        bw.write("Lightning Mastery: "+weaponMastery[Weapon.LIGHTNING]); bw.newLine();
        bw.write("Light Mastery: "+weaponMastery[Weapon.LIGHT]); bw.newLine();
        bw.write("Dark Mastery: "+weaponMastery[Weapon.DARK]); bw.newLine();
        bw.write("Staff Mastery: "+weaponMastery[Weapon.STAFF]); bw.newLine();
        bw.write("Natural Mastery: "+weaponMastery[Weapon.NATURAL]); bw.newLine();
        
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
        
        bw.write("Num Skills Bank: "+skillsReserve.size()); bw.newLine();
        for(String s: skillsReserve)
        {
            bw.write(s); bw.newLine();
        }
        bw.newLine();
    }    
    
    /**
     * Writes a Unit to a file
     * @param u The Unit to write to.
     */
    public static void writeToPrefab(PartyUnit u)
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
    
    
    public final void loadPortrait()
    {
        if(name == null)
        {
            try
            {
                portrait = ImageIO.read(new File("assets/portraits/Generic.png"));
            }
            catch(Exception e)
            {
                e.printStackTrace(System.out);
                JOptionPane.showMessageDialog(null, "Loading generic portrait failed, file doesn't seem to exist.");
                System.exit(-1);
            }
        }
        
        else
        {
            try
            {
                portrait = ImageIO.read(new File("assets/portraits/"+ name +".png"));
            }
            catch(Exception e)
            {
                try
                {
                    portrait = ImageIO.read(new File("assets/portraits/Generic.png"));
                }
                catch(Exception e1)
                {
                    e1.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Loading generic portrait failed, file doesn't seem to exist.");
                    System.exit(-1);
                }
            }
        }
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
        
        w.setOwner(this);
        weaponInventory.add(0, w);
    }
    
    /**
     * Adds a weapon to the character's weapon inventory
     * 
     * @param w add to weapon inventory
     * @return if weapon inventory had room or not.
     */
    public final boolean addWeapon(Weapon w)
    {
        if(weaponInventory.size() < PartyUnit.WEAPON_LIMIT)
        {
            if(w.getOwner() != null)
                w.getOwner().removeWeapon(w);
            weaponInventory.add(w);
            w.setOwner(this);
            return true;
        }
        
        return false;
    }
    
    /**
     * Ads an item to a character's inventory
     * @param u add to inventory
     * @return if it was successfully added (if false send to convoy)
     */
    public final boolean addItem(Usable u)
    {
        if(inventory.size() < PartyUnit.INVENTORY_LIMIT)
        {
            if(u.getOwner() != null)
                u.getOwner().removeItem(u);
            u.setOwner(this);
            inventory.add(u);
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Removes an weapon from this unit's inventory.
     * @param w the weapon to be removed.
     * @return Weather this unit even HAD that weapon. 
     */
    public boolean removeWeapon(Weapon w)
    {
        if(weaponInventory.remove(w))
        {
            w.setOwner(null);
            return true;
        }
        return false;
    }

    /**
     * Removes an item from this unit's inventory.
     * @param u the item to be removed.
     * @return Weather this unit even HAD that item. 
     */
    public boolean removeItem(Usable u)
    {
        if(inventory.remove(u))
        {
            u.setOwner(null);
            return true;
        }
        return false;
    }
    
    public boolean canEquipWeapon(Weapon w)
    {
        return( w.getMasteryRequirementAsInt() < weaponMastery[w.getWeaponType()]);
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
        if(skills.size() < PartyUnit.SKILL_LIMIT)
            return(skills.add(s));
        
        return false;
    }
    
    /**
     * Removes a Skill from the skills list.
     * @param s The skill to remove
     * @return Whether or not it worked.
     */
    public boolean removeSkill(String s)
    {
        return skills.remove(s);
    }
    
    /**
     * Puts a skill in your skill bank, so you can add it back later.
     * @param s The skill to be put in the bank.
     * @return If it worked.
     */
    public boolean addSkillToReserve(String s)
    {
        return skillsReserve.add(s);
    }
    
    /**
     * Removes a skill from your skill bank.
     * @param s The skill to remove
     * @return If it worked.
     */
    public boolean removeSkillFromReserve(String s)
    {
        return skillsReserve.remove(s);
    }
    
    /**
     * Moves a skill from the skill bank to the skills list.
     * @param s The skill to move
     * @return If it worked
     */
    public boolean moveSkillToReserve(String s)
    {
        if(removeSkill(s))
            return addSkillToReserve(s);
        return false;
    }
    
    /**
     * Moves a skill from this unit's skills list to the reserve.
     * @param s The skill to move
     * @return If it worked.
     */
    public boolean moveSkillFromReserve(String s)
    {
        if(skillsReserve.contains(s))
        {
            if(addSkill(s))
                return removeSkillFromReserve(s);
        }
        return false;
    }
    
    /**
     * Checks if this unit and another unit are friends or foes
     * @param other
     * @return if they are allies or enemies
     */
    public boolean isAlly(PartyUnit other)
    {
        if(team == PartyUnit.ENEMY)
            return other.team == PartyUnit.ENEMY;
        else
            return (other.getTeam() != PartyUnit.ENEMY);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Getters and Setters">
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String s)
    {
        name = s;
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
    
    public int getLevel()
    {
        return level;
    }
    
    public int getXP()
    {
        return xp;
    }
        
    /**
     * @return the hp
     */
    public double getHP() 
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

    
    public int getTransportType()
    {
        return transportType;
    }
    
    public int getArmorType()
    {
        return armorType;
    }
    
    /**
     * Returns the mastery level of a chosen weapon.
     * @param weaponType The type of weapon as defined by the Weapon class
     * @return The mastery in that weapon type
     */
    public int getWeaponMastery(int weaponType)
    {
        return weaponMastery[weaponType];
    }
    
    public ArrayList<Weapon> getWeaponInventory()
    {
        return weaponInventory;
    }
        
    /**
     * Gets the equipped weapon. Returns null if no weapon is equipped 
     * @return The first item in weaponInventory. if it's an equpiable weapon.
     */
    public Weapon getEquppedWeapon()
    {
        if(weaponInventory.isEmpty() || !canEquipWeapon(weaponInventory.get(0)))
            return null;
        else
            return weaponInventory.get(0);
    }
    
    public ArrayList<Usable> getInventory()
    {
        return inventory;
    }
    
    public ArrayList<String> getSkills()
    {
        return skills;
    }
    
    public ArrayList<String> getSkillsReserve()
    {
        return skillsReserve;
    }
       
    /**
     * @return the portrait
     */
    public BufferedImage getPortrait() 
    {
        return portrait;
    }
    
    //</editor-fold>
    
}
