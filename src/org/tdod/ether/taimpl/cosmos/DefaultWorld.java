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

package org.tdod.ether.taimpl.cosmos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Area;
import org.tdod.ether.ta.cosmos.BarrierDatabase;
import org.tdod.ether.ta.cosmos.CommandTriggerDatabase;
import org.tdod.ether.ta.cosmos.EmoteDatabase;
import org.tdod.ether.ta.cosmos.HelpAssisant;
import org.tdod.ether.ta.cosmos.NpcDatabase;
import org.tdod.ether.ta.cosmos.RoomDatabase;
import org.tdod.ether.ta.cosmos.RoomDescDatabase;
import org.tdod.ether.ta.cosmos.TeleporterDatabase;
import org.tdod.ether.ta.cosmos.TrapDatabase;
import org.tdod.ether.ta.cosmos.TreasureDatabase;
import org.tdod.ether.ta.cosmos.World;
import org.tdod.ether.ta.items.armor.ArmorDatabase;
import org.tdod.ether.ta.items.equipment.EquipmentDatabase;
import org.tdod.ether.ta.items.weapons.WeaponDatabase;
import org.tdod.ether.ta.manager.PlayerFacade;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.MobDatabase;
import org.tdod.ether.ta.mobs.MobWeaponDatabase;
import org.tdod.ether.ta.spells.SpellDatabase;
import org.tdod.ether.taimpl.items.armor.DefaultArmorDatabase;
import org.tdod.ether.taimpl.items.equipment.DefaultEquipmentDatabase;
import org.tdod.ether.taimpl.items.weapons.DefaultWeaponDatabase;
import org.tdod.ether.taimpl.mobs.DefaultMobDatabase;
import org.tdod.ether.taimpl.mobs.DefaultMobWeaponDatabase;
import org.tdod.ether.taimpl.spells.DefaultSpellDatabase;
import org.tdod.ether.util.InvalidFileException;

/**
 * This is the default implementation class for the world.
 *
 * @author Ron Kinney
 */
public class DefaultWorld implements World {

   private static Log _log = LogFactory.getLog(DefaultWorld.class);

   private HelpAssisant           _helpAssistant = new DefaultHelpAssistant();
   private RoomDatabase           _roomDatabase = new DefaultRoomDatabase();
   private RoomDescDatabase       _roomDescDatabase = new DefaultRoomDescDatabase();
   private WeaponDatabase         _weaponDatabase = new DefaultWeaponDatabase();
   private ArmorDatabase          _armorDatabase = new DefaultArmorDatabase();
   private MobDatabase            _mobDatabase = new DefaultMobDatabase();
   private SpellDatabase          _spellDatabase = new DefaultSpellDatabase();
   private EquipmentDatabase      _equipmentDatabase = new DefaultEquipmentDatabase();
   private MobWeaponDatabase      _mobWeaponDatabase = new DefaultMobWeaponDatabase();
   private NpcDatabase            _npcDatabase = new DefaultNpcDatabase();
   private EmoteDatabase          _emoteDatabase = new DefaultEmoteDatabase();
   private BarrierDatabase        _barrierDatabase = new DefaultBarrierDatabase();
   private TrapDatabase           _trapDatabase = new DefaultTrapDatabase();
   private TeleporterDatabase     _teleporterDatabase = new DefaultTeleporterDatabase();
   private TreasureDatabase       _treasureDatabase = new DefaultTreasureDatabase();
   private CommandTriggerDatabase _commandTriggerDatabase = new DefaultCommandTriggerDatabase();

   /**
    * Creates a new DefaultWorld.
    */
   public DefaultWorld() {
   }

   /**
    * Initializes the world.
    *
    * @throws InvalidFileException if any of the data files are invalid.
    */
   public void bigBang() throws InvalidFileException {
      _log.info("***INITIALIZING INTERNAL DATABASES***");
      _helpAssistant.initialize();
      _weaponDatabase.initialize();
      _armorDatabase.initialize();
      _mobWeaponDatabase.initialize();
      _mobDatabase.initialize();
      _spellDatabase.initialize();
      _equipmentDatabase.initialize();
      _npcDatabase.initialize();
      _emoteDatabase.initialize();
      _barrierDatabase.initialize();
      _trapDatabase.initialize();
      _teleporterDatabase.initialize();
      _treasureDatabase.initialize();
      _commandTriggerDatabase.initialize();
      _roomDescDatabase.initialize();
      _roomDatabase.initialize();      // Insure that room is initialized last since it relies on items and other things!
      PlayerFacade.initialize();
      _log.info("***COMPLETED INITIALIZATION OF INTERNAL DATABASES***");
      WorldManager.initializeLairs();
      WorldManager.populateRandomMobs();
      WorldManager.populateRandomItems();
   }

   /**
    * Gets the help data.
    *
    * @return the help data.
    */
   public HelpAssisant getHelpAssistant() {
      return _helpAssistant;
   }

   /**
    * Gets the area.
    *
    * @return the area.
    */
   public Area getArea() {
      return _roomDatabase.getArea();
   }

   /**
    * Gets the room description database.
    *
    * @return the room description database.
    */
   public RoomDescDatabase getRoomDescDatabase() {
      return _roomDescDatabase;
   }

   /**
    * Gets the weapon database.
    *
    * @return the weapon database.
    */
   public WeaponDatabase getWeaponDatabase() {
      return _weaponDatabase;
   }

   /**
    * Gets the armor database.
    *
    * @return the armor database.
    */
   public ArmorDatabase getArmorDatabase() {
      return _armorDatabase;
   }

   /**
    * Gets the mob database.
    *
    * @return the mob database.
    */
   public MobDatabase getMobDatabase() {
      return _mobDatabase;
   }

   /**
    * Gets the spell database.
    *
    * @return the spell database.
    */
   public SpellDatabase getSpellDatabase() {
      return _spellDatabase;
   }

   /**
    * Gets the equipment database.
    *
    * @return the equipment database.
    */
   public EquipmentDatabase getEquipmentDatabase() {
      return _equipmentDatabase;
   }

   /**
    * Gets the mob weapon database.
    *
    * @return the mob weapon database.
    */
   public MobWeaponDatabase getMobWeaponDatabase() {
      return _mobWeaponDatabase;
   }

   /**
    * Gets the npc database.
    *
    * @return the npc database.
    */
   public NpcDatabase getNpcDatabase() {
      return _npcDatabase;
   }

   /**
    * Gets the emote database.
    *
    * @return the emote database.
    */
   public EmoteDatabase getEmoteDatabase() {
      return _emoteDatabase;
   }

   /**
    * Gets the barrier database.
    *
    * @return the barrier database.
    */
   public BarrierDatabase getBarrierDatabase() {
      return _barrierDatabase;
   }

   /**
    * Gets the trap database.
    *
    * @return the trap database.
    */
   public TrapDatabase getTrapDatabase() {
      return _trapDatabase;
   }

   /**
    * Gets the teleporter database.
    *
    * @return the teleporter database.
    */
   public TeleporterDatabase getTeleporterDatabase() {
      return _teleporterDatabase;
   }

   /**
    * Gets the treasure database.
    *
    * @return the treasure database.
    */
   public TreasureDatabase getTreasureDatabase() {
      return _treasureDatabase;
   }

   /**
    * Gets the command trigger database.
    *
    * @return the command trigger database.
    */
   public CommandTriggerDatabase getCommandTriggerDatabase() {
      return _commandTriggerDatabase;
   }

}
