/****************************************************************************
 * Ether Code base, version 1.0                                             *
 *==========================================================================*
 * Copyright (C) 2011 by Ron Kinney                                         *
 * All rights reserved.                                                     *
 *                                                                          *
 * This program is free software; you can redistribute it and/or modify     *
 * it under the terms of the GNU General Public License as published        *
 * by the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                      *
 *                                                                          *
 * This program is distributed in the hope that it will be useful,          *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of           *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
 * GNU General Public License for more details.                             *
 *                                                                          *
 * Redistribution and use in source and binary forms, with or without       *
 * modification, are permitted provided that the following conditions are   *
 * met:                                                                     *
 *                                                                          *
 * * Redistributions of source code must retain this copyright notice,      *
 *   this list of conditions and the following disclaimer.                  *
 * * Redistributions in binary form must reproduce this copyright notice    *
 *   this list of conditions and the following disclaimer in the            *
 *   documentation and/or other materials provided with the distribution.   *
 *                                                                          *
 *==========================================================================*
 * Ron Kinney (ronkinney@gmail.com)                                         *
 * Ether Homepage:   http://tdod.org/ether                                  *
 ****************************************************************************/

package org.tdod.ether.taimpl.mobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.MobDatabase;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.taimpl.mobs.enums.AttackEffectEnum;
import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.taimpl.mobs.enums.MobSpellType;
import org.tdod.ether.taimpl.mobs.enums.SpecialAbilityEnum;
import org.tdod.ether.taimpl.mobs.enums.SubType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The implementation class of a MobDatabase.
 * @author Ron Kinney
 */
public class DefaultMobDatabase extends DataFileReader implements MobDatabase {

   private static Log _log = LogFactory.getLog(DefaultMobDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 8;
   private static final int STAT_SPLIT_SIZE = 26;
   private static HashMap<Integer, Mob> _mobsMap = new HashMap<Integer, Mob>();

   private static final ArrayList<Mob> _mobListCache = new ArrayList<Mob>();
   private static final HashMap<Terrain, ArrayList<Mob>> _mobsByTerrain = new HashMap<Terrain, ArrayList<Mob>>();

   /**
    * Creates a new DefaultMobDatabase.
    */
   public DefaultMobDatabase() {
   }

   //**********
   // Methods inherited from MobDatabase.
   //**********

   /**
    * Initializes the internal mob database.
    *
    * @throws InvalidFileException if the file could not be read.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_DATA_FILE);
      _log.info("Loading class data file " + filename);
      readFile(filename);
      _log.info("Read in " + _mobsMap.size() + " mobs.");
   }

   /**
    * Gets the complete list of mobs.
    *
    * @return the complete list of mobs.
    */
   public ArrayList<Mob> getMobList() {
      synchronized (_mobListCache) {
         if (_mobListCache.isEmpty()) {
            _log.info("_mobListCache is empty.  Populating... ");
            Collection<Mob> c = _mobsMap.values();
            Iterator<Mob> itr = c.iterator();
            while (itr.hasNext()) {
               _mobListCache.add(itr.next());
            }
         }
      }

      return _mobListCache;
   }

   /**
    * Gets a list of mobs in the specified terrain.
    *
    * @param terrain The specified terain.
    * @return a list of mobs in the specified terrain.
    */
   public ArrayList<Mob> getMobByTerrain(Terrain terrain) {
      synchronized (_mobsByTerrain) {
         if (_mobsByTerrain.isEmpty()) {
            _log.info("_mobsByTerrain is empty.  Populating... ");
            Collection<Mob> c = _mobsMap.values();
            Iterator<Mob> itr = c.iterator();
            while (itr.hasNext()) {
               Mob mob = itr.next();
               ArrayList<Mob> mobs = _mobsByTerrain.get(mob.getTerrain());
               if (mobs == null) {
                  mobs = new ArrayList<Mob>();
                  _mobsByTerrain.put(mob.getTerrain(), mobs);
               }

               mobs.add(mob);
            }
         }
      }

      return _mobsByTerrain.get(terrain);
   }

   /**
    * Gets a single mob by vnum.
    *
    * @param mobVnum The vnum.
    *
    * @return a single mob by vnum.
    */
   public Mob getMob(int mobVnum) {
      return _mobsMap.get(mobVnum);
   }

   //**********
   // Methods inherited from DataFileReader.
   //**********

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Mob mob = new DefaultMob();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      String[] statSplit = majorSplit[3].split(" ");
      if (statSplit.length != STAT_SPLIT_SIZE) {
         throw new InvalidFileException("Status section should contain "
               + STAT_SPLIT_SIZE + " numbers.  Got " + statSplit.length);
      }

      mob.setVnum(Integer.valueOf(majorSplit[0]));
      mob.setName(majorSplit[1]);
      mob.setDescription(majorSplit[2]);
      mob.setPlural(majorSplit[4]);
      mob.getGeneralAttack().setWeapon(majorSplit[5]);
      mob.getSpecialAttack().setSpecialAttackDescription(majorSplit[6]);
      mob.getSpecialAbility().setSpecialAbilityDescription(majorSplit[7]);

      mob.setCanTrack((statSplit[0].equals("1") ? false : true));
      mob.setCombatSkill(new Integer(statSplit[1]));
      mob.setTerrain(Terrain.getTerrain(new Integer(statSplit[2])));
      mob.setGold(new Integer(statSplit[3]));
      mob.setTreasure(new Integer(statSplit[4]));
      mob.setArmorRating(new Integer(statSplit[5]));
      mob.setSpecialAttackPercent(new Integer(statSplit[6]));
      mob.setHitDice(new Integer(statSplit[7]));
      mob.setRegeneration(new Integer(statSplit[8]));
      mob.getGeneralAttack().setMinDamage(new Integer(statSplit[9]));

      // If v9 == 0, then v10 is a weapon.  Otherwise, v9 is min damage and v10 is max damage.
      int v10 = new Integer(statSplit[10]);
      if (mob.getGeneralAttack().getMinDamage() == 0) {
         MobWeaponType mobWeaponType = MobWeaponType.getMobWeaponType(v10);
         mob.getGeneralAttack().setMobWeaponType(mobWeaponType);
         mob.getGeneralAttack().setMaxDamage(0);
      } else {
         mob.getGeneralAttack().setMaxDamage(v10);
      }

      mob.getSpecialAttack().setMinSpecialDamage(new Integer(statSplit[11]));
      mob.getSpecialAttack().setMaxSpecialDamage(new Integer(statSplit[12]));
      mob.getGeneralAttack().setAttackEffect(AttackEffectEnum.getAttackEffectEnum(new Integer(statSplit[13])));
      mob.getGeneralAttack().setMinAttackEffect(new Integer(statSplit[14]));
      mob.getGeneralAttack().setMaxAttackEffect(new Integer(statSplit[15]));
      mob.getSpecialAbility().setSpecialAbility(SpecialAbilityEnum.getSpecialAbilityEnum(new Integer(statSplit[16])));
      mob.getGeneralAttack().setNumAttacks(new Integer(statSplit[17]));
      mob.setLevel(new Integer(statSplit[18]));
      mob.setMorale(new Integer(statSplit[19]));
      mob.getMobSpells().setSpellSkill(new Integer(statSplit[20]));
      mob.getMobSpells().setMobSpellType(MobSpellType.getMobSpellType(new Integer(statSplit[21])));
      mob.getMobSpells().setMinSpell(new Integer(statSplit[22]));
      mob.getMobSpells().setMaxSpell(new Integer(statSplit[23]));
      mob.setGender(Gender.getGender(new Integer(statSplit[24])));
      mob.setSubType(SubType.getSubType(new Integer(statSplit[25])));

      // Quirky Java functionality.  If the description contains {0}, then any ' need to be replaced with ''.  This is
      // due to the MessageFormat.format() call getting screwed up.
      String description = mob.getDescription();
      StringBuffer fixedDescription = new StringBuffer();
      if (description.contains("{0}")) {
         for (int index = 0; index < description.length(); index++) {
            fixedDescription.append(description.charAt(index));
            if (description.charAt(index) == '\'') {
               fixedDescription.append('\'');
            }
         }
         mob.setDescription(fixedDescription.toString());            
      }

      _mobsMap.put(Integer.valueOf(mob.getVnum()), mob);
   }
}
