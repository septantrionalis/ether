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

package org.tdod.ether.taimpl.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.ta.spells.SpellDatabase;
import org.tdod.ether.taimpl.mobs.enums.MobSpellType;
import org.tdod.ether.taimpl.spells.enums.CureCondition;
import org.tdod.ether.taimpl.spells.enums.ManaEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect2;
import org.tdod.ether.taimpl.spells.enums.PoisonTarget;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.taimpl.spells.enums.SpellType;
import org.tdod.ether.taimpl.spells.enums.StatModifier;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for spell data.
 * @author rkinney
 */
public class DefaultSpellDatabase extends DataFileReader implements SpellDatabase {

   private static Log _log = LogFactory.getLog(DefaultSpellDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 3;
   private static final int STAT_SPLIT_SIZE = 15;

   private static HashMap<String, Spell> _spellMap = new HashMap<String, Spell>();
   private static ArrayList<Spell> _spellList = new ArrayList<Spell>();

   private static HashMap<MobSpellType, ArrayList<Spell>> _sorcerorMobSpells = new HashMap<MobSpellType, ArrayList<Spell>>();
   private static HashMap<MobSpellType, ArrayList<Spell>> _druidMobSpells = new HashMap<MobSpellType, ArrayList<Spell>>();
   private static HashMap<MobSpellType, ArrayList<Spell>> _necrolyteMobSpells = new HashMap<MobSpellType, ArrayList<Spell>>();

   /**
    * Creates a new DefaultSpellDatabase.
    */
   public DefaultSpellDatabase() {
   }

   /**
    * Gets a spell based on a string.
    * @param spell the spell in string format.
    * @return a spell.
    */
   public Spell getSpell(String spell) {
      return _spellMap.get(spell);
   }

   /**
    * Gets the list of spells for a particular class.
    * @param playerClass the class in question.
    * @return the list of spells for a particular class.
    */
   public ArrayList<Spell> getSpells(PlayerClass playerClass) {
      ArrayList<Spell> spellList = new ArrayList<Spell>();
      for (Spell spell : _spellList) {
         if (spell.getSpellClass().equals(playerClass)) {
            spellList.add(spell);
         }
      }

      Collections.sort(spellList);

      return spellList;
   }

   /**
    * Gets a the mob attack spell for a particular mob spell type.
    * @param mobSpellType the mob spell type.
    * @param index the index of the spell.
    * @return a spell.
    */
   public Spell getAttackSpellFor(MobSpellType mobSpellType, int index) {
      if (mobSpellType.equals(MobSpellType.SORCEROR)) {
         return _sorcerorMobSpells.get(mobSpellType).get(index);
      } else if (mobSpellType.equals(MobSpellType.DRUID)) {
         return _druidMobSpells.get(mobSpellType).get(index);
      } else if (mobSpellType.equals(MobSpellType.NECROLYTE)) {
         return _necrolyteMobSpells.get(mobSpellType).get(index);
      }

      return null;
   }

   /**
    * Initializes the data.
    * @throws InvalidFileException thrown if the spell file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.SPELL_DATA_FILE);
      _log.info("Loading spell file " + filename);
      readFile(filename);
      _log.info("Read in " + _spellMap.size() + " spells.");

      initializeMobSpells(MobSpellType.SORCEROR, _sorcerorMobSpells);
      initializeMobSpells(MobSpellType.DRUID, _druidMobSpells);
      initializeMobSpells(MobSpellType.NECROLYTE, _necrolyteMobSpells);
   }

   /**
    * Parses a single line of data.
    * @param line a single line of data.
    * @throws InvalidFileException thrown if the line is invalid.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Spell spell = new DefaultSpell();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      String[] statSplit = majorSplit[2].split(" ");
      if (statSplit.length != STAT_SPLIT_SIZE) {
         throw new InvalidFileException("Status section should contain " + STAT_SPLIT_SIZE
               + " numbers.  Got " + statSplit.length);
      }

      spell.setName(majorSplit[0]);
      spell.setMessage(majorSplit[1]);

      spell.setSpellType(SpellType.getSpellType(new Integer(statSplit[0])));
      spell.setMana(new Integer(statSplit[1]));
      spell.setMinSpellEffect(new Integer(statSplit[2]));
      spell.setMaxSpellEffect(new Integer(statSplit[3]));
      spell.setScalesWithLevel(new Integer(statSplit[4]));
      spell.setCost(new Integer(statSplit[5]));
      spell.setSpellTarget(SpellTarget.getSpellTarget(new Integer(statSplit[6])));
      spell.setArmorModifier(new Integer(statSplit[7]));
      spell.setCureCondition(CureCondition.getCureCondition(new Integer(statSplit[8])));
      spell.setPoisonTarget(PoisonTarget.getPoisonTarget(new Integer(statSplit[9])));
      spell.setManaEffect(ManaEffect.getManaEffect(new Integer(statSplit[10])));
      spell.setStatPenalty(StatModifier.getStatModifier(new Integer(statSplit[11])));
      spell.setStatBonus(StatModifier.getStatModifier(new Integer(statSplit[12])));
      spell.setMiscTargetEffect(MiscTargetEffect.getMiscTargetEffect(new Integer(statSplit[13])));
      spell.setMiscTargetEffect2(MiscTargetEffect2.getMiscTargetEffect2(new Integer(statSplit[14])));

      _spellList.add(spell);
      _spellMap.put(spell.getName(), spell);
   }

   /**
    * Initializes mobs spells.
    * @param mobSpellType the mob spell type.
    * @param map the map.
    */
   private void initializeMobSpells(MobSpellType mobSpellType, HashMap<MobSpellType, ArrayList<Spell>> map) {
      for (Spell spell : getSpells(mobSpellType.getPlayerClass())) {
         if (spell.getSpellType().equals(mobSpellType.getSpellType())) {
            ArrayList<Spell> list = map.get(mobSpellType);
            if (list == null) {
               list = new ArrayList<Spell>();
               map.put(mobSpellType, list);
            }
            list.add(spell);
         }
      }
   }
}
