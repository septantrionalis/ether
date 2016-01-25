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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityMessageHandler;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Lair;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.GeneralAttack;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.MobSpells;
import org.tdod.ether.ta.mobs.MobWeapon;
import org.tdod.ether.ta.mobs.SpecialAbility;
import org.tdod.ether.ta.mobs.SpecialAttack;
import org.tdod.ether.ta.mobs.specialability.SpecialAbilityCommand;
import org.tdod.ether.ta.mobs.specialability.SpecialAbilityCommandManager;
import org.tdod.ether.ta.player.Attacks;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.Mana;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Spellbook;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.taimpl.mobs.enums.AttackEffectEnum;
import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.taimpl.mobs.enums.SpecialAbilityEnum;
import org.tdod.ether.taimpl.mobs.enums.SubType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.taimpl.player.DefaultAttacks;
import org.tdod.ether.taimpl.player.DefaultBaseStats;
import org.tdod.ether.taimpl.player.DefaultMana;
import org.tdod.ether.taimpl.player.DefaultSpellbook;
import org.tdod.ether.taimpl.player.DefaultVitality;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * This is the default implementation class of a Mob.
 * @author Ron Kinney
 */
public class DefaultMob implements Mob {

   private static final int MIN_REST_PERIOD = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_INITIAL_REST_PERIOD_MIN));
   private static final int MAX_REST_PERIOD = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_INITIAL_REST_PERIOD_MAX));
   private static final int ACTIVITY_TIMER_WAKEUP = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_ACTIVITY_TIMER_WAKEUP));
   private static final int MAX_INVENTORY_GOLD = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_INVENTORY_GOLD));
   private static final int HP_PERCENT_FLEE_CHECK = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_HP_PERCENT_FLEE_CHECK));
   private static final int MOB_SPEC_ABILITY_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_SPEC_ABILITY_CHANCE));
   private static final int MELEE_EFFECT_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MELEE_EFFECT_CHANCE));
   private static final int PARALYSIS_MULTIPLIER = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PARALYSIS_MUTIPLIER));
   private static final int MOB_SPELL_HIT_DIFFICULTY = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_SPELL_HIT_DIFFICULTY)).intValue();

   private static Log _log = LogFactory.getLog(DefaultMob.class);

   private int       _vnum;
   private String    _name;
   private String    _prefix;
   private String    _plural;
   private Gender    _gender = Gender.INVALID;
   private Terrain   _terrain = Terrain.INVALID;
   private String    _description;
   private int       _level;
   private int       _morale;
   private int       _gold;
   private int       _treasure;
   private int       _armor;
   private int       _hitDice;
   private int       _regeneration;
   private int       _combatSkill;
   private int       _specialAttackPercent;
   private SubType   _subType = SubType.INVALID;
   private Room      _room;
   private Status    _status = Status.HEALTHY;
   private int       _poisonDamage = 0;
   private Mana      _mana = new DefaultMana();
   private BaseStats _stats = new DefaultBaseStats();
   private boolean   _canTrack;

   private boolean   _isFriendly;
   private int       _mesmerizedTicker;
   private Player    _tamedBy;
   private int       _paralysisTimer = 0;

   private Vitality  _vitality = new DefaultVitality();
   private float     _mobVariance = 1;
   private int       _activityTicker;

   private Item      _inventoryItem;

   private MobWeapon     _mobWeapon = null;

   private GeneralAttack  _generalAttack = new DefaultGeneralAttack();
   private SpecialAttack  _specialAttack = new DefaultSpecialAttack();
   private SpecialAbility _specialAbility = new DefaultSpecialAbility();
   private MobSpells      _mobSpells = new DefaultMobSpells();

   private Entity         _groupLeader = this;
   private boolean        _isFleeing = false;
   private Player         _chasing;
   private boolean        _npc;

   // Spell Buffs -- may want to move this into another object at a later time.
   private int            _regenerationTicker = 0;

   private Spellbook      _spellbook = new DefaultSpellbook();

   private EntityMessageHandler _messageHandler = new MobMessageHandler(this);

   private transient ArrayList<Entity> _groupList = new ArrayList<Entity>();

   /**
    * Creates a new DefaultMob.
    */
   public DefaultMob() {
   }

   //**********
   // Methods inherited from Mob.
   //**********

   /**
    * The mob vnum.
    * @return the mob vnum.
    */
   public int getVnum() {
      return _vnum;
   }

   /**
    * Sets the mob vnum.
    * @param vnum the mob vnum.
    */
   public void setVnum(int vnum) {
      _vnum = vnum;
   }

   /**
    * Gets the mob prefix description.
    * @return the mob prefix description.
    */
   public String getPrefix() {
      return _prefix;
   }

   /**
    * Sets the mob prefix description.
    * @param prefix the mob prefix description.
    */
   public void setPrefix(String prefix) {
      _prefix = prefix;
   }

   /**
    * Gets the plural form of the mobs name.
    * @return the plural form of the mobs name.
    */
   public String getPlural() {
      return _plural;
   }

   /**
    * Sets the plural form of the mobs name.
    * @param plural the plural form of the mobs name.
    */
   public void setPlural(String plural) {
      _plural = plural;
   }

   /**
    * Sets the mob terrain.
    * @return the mob terrain.
    */
   public Terrain getTerrain() {
      return _terrain;
   }

   /**
    * Sets the mob terrain.
    * @param terrain the mob terrain.
    */
   public void setTerrain(Terrain terrain) {
      _terrain = terrain;
   }

   /**
    * Gets the mob description.
    * @return the mob description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Sets the mob description.
    * @param description the mob description.
    */
   public void setDescription(String description) {
      _description = description;
   }

   /**
    * Gets the mob's morale.
    * @return the mob's morale.
    */
   public int getMorale() {
      return _morale;
   }

   /**
    * Sets the mobs morale.
    * @param morale the mobs morale.
    */
   public void setMorale(int morale) {
      _morale = morale;
   }

   /**
    * Subtracts the amount from the mobs morale.
    * @param value the amount to subtract from the mobs morale.
    */
   public void subtractMorale(int value) {
      _morale -= value;
   }

   /**
    * Adds the amount to the mobs morale.
    * @param value the amount to add.
    */
   public void addMorale(int value) {
      _morale += value;
   }

   /**
    * Gets the mobs treasure drop.
    * @return the mobs treasure drop.
    */
   public int getTreasure() {
      return _treasure;
   }

   /**
    * Sets the mobs treasure drop.
    * @param treasure the mobs treasure drop.
    */
   public void setTreasure(int treasure) {
      _treasure = treasure;
   }

   /**
    * Sets the mobs armor rating.
    * @param armor the mobs armor rating.
    */
   public void setArmorRating(int armor) {
      _armor = armor;
   }

   /**
    * Gets the mobs hit dice.
    * @return the mobs hit dice.
    */
   public int getHitDice() {
      return _hitDice;
   }

   /**
    * Sets the mobs hit dice.
    * @param hitDice the mobs hit dice.
    */
   public void setHitDice(int hitDice) {
      _hitDice = hitDice;
   }

   /**
    * Gets the mobs regeneration.
    * @return the mobs regeneration.
    */
   public int getRegeneration() {
      return _regeneration;
   }

   /**
    * Sets the mobs regeneration.
    * @param regeneration the mobs regeneration.
    */
   public void setRegeneration(int regeneration) {
      _regeneration = regeneration;
   }

   /**
    * Sets the combat skill.
    * @param combatSkill the combat skill.
    */
   public void setCombatSkill(int combatSkill) {
      _combatSkill = combatSkill;
   }

   /**
    * Gets the general attack.
    * @return the general attack.
    */
   public GeneralAttack getGeneralAttack() {
      return _generalAttack;
   }

   /**
    * Sets the general attack.
    * @param generalAttack the general attack.
    */
   public void setGeneralAttack(GeneralAttack generalAttack) {
      _generalAttack = generalAttack;
   }

   /**
    * Gets the special attack percentage.
    * @return the special attack percentage.
    */
   public int getSpecialAttackPercent() {
      return _specialAttackPercent;
   }

   /**
    * Sets the special attack percentage.
    * @param specialAttackPercent the special attack percentage.
    */
   public void setSpecialAttackPercent(int specialAttackPercent) {
      _specialAttackPercent = specialAttackPercent;
   }

   /**
    * Gets the special attack.
    * @return the special attack.
    */
   public SpecialAttack getSpecialAttack() {
      return _specialAttack;
   }

   /**
    * Sets the special attack.
    * @param specialAttack the special attack.
    */
   public void setSpecialAttack(SpecialAttack specialAttack) {
      _specialAttack = specialAttack;
   }

   /**
    * Gets the special ability.
    * @return the special ability.
    */
   public SpecialAbility getSpecialAbility() {
      return _specialAbility;
   }

   /**
    * Sets the special ability.
    * @param specialAbility the special ability.
    */
   public void setSpecialAbility(SpecialAbility specialAbility) {
      _specialAbility = specialAbility;
   }

   /**
    * Gets the mobs spells.
    * @return the mobs spells.
    */
   public MobSpells getMobSpells() {
      return _mobSpells;
   }

   /**
    * Sets the mobs spells.
    * @param mobSpells the mobs spells.
    */
   public void setMobSpells(MobSpells mobSpells) {
      _mobSpells = mobSpells;
   }

   /**
    * Gets the mobs sub-type.
    * @return the mobs sub-type.
    */
   public SubType getSubType() {
      return _subType;
   }

   /**
    * Sets the mobs sub-type.
    * @param subType the mobs sub-type.
    */
   public void setSubType(SubType subType) {
      _subType = subType;
   }

   /**
    * Sets the vitality.
    * @param vitality the vitality.
    */
   public void setVitality(Vitality vitality) {
      _vitality = vitality;
   }

   /**
    * Gets the mobs weapon.
    * @return the mobs weapon.
    */
   public MobWeapon getMobWeapon() {
      return _mobWeapon;
   }

   /**
    * Sets the mobs weapon.
    * @param mobWeapon the mobs weapon.
    */
   public void setMobWeapon(MobWeapon mobWeapon) {
      _mobWeapon = mobWeapon;
   }

   /**
    * Destroys this mob.  This method needs to be called to cleanup
    * any resources taken by the mob.
    */
   public void destroy() {
   }

   /**
    * Clones this mob.  This method is called whenever a new mob is needed to
    * roam the world.  This takes resources, so whenever the mobs is not needed,
    * the engine needs to call destroy.
    * @param room the room that the mob will start in.
    * @return the cloned mob.
    */
   public Mob clone(Room room) {
      try {
         Mob mob = (Mob) super.clone();

         // Easily cloned stuff.
         mob.setName(_name);
         mob.setPrefix(_prefix);
         mob.setPlural(_plural);
         mob.setGender(_gender);
         mob.setTerrain(_terrain);
         mob.setDescription(_description);
         mob.setLevel(_level);
         mob.setMorale(_morale);
         mob.setGold(_gold);
         mob.setTreasure(_treasure);
         mob.setArmorRating(_armor);
         mob.setHitDice(_hitDice);
         mob.setRegeneration(_regeneration);
         mob.setCombatSkill(_combatSkill);
         mob.setSubType(_subType);
         mob.setStatus(Status.HEALTHY);
         mob.setMessageHandler(_messageHandler);

         // These objects are pass by reference, and hence, not cloned.  I don't see a need to at this point.
         mob.setGeneralAttack(_generalAttack);
         mob.setSpecialAttack(_specialAttack);
         mob.setSpecialAbility(_specialAbility);
         mob.setMobSpells(_mobSpells);

         // Parameters that can change between mobs
         float mobVariance = Dice.generateNumberVariance();
         mob.setVitality(WorldManager.getGameMechanics().calculateMobHealth(_combatSkill, _level, mobVariance));
         mob.setMobWeapon(generateMobWeapon());
         mob.setActivityTicker(Dice.roll(MIN_REST_PERIOD, MAX_REST_PERIOD));

         mob.setRoom(room);

         // Basically ,give mobs unlimited mana.
         mob.getMana().setCurMana(1000000);
         mob.getMana().setMaxMana(1000000);

         mob.setGroupLeader(mob);

         return mob;
      } catch (CloneNotSupportedException e) {
         _log.error("Error cloning " + _name, e);
         return new DefaultMob();
      }
   }

   /**
    * Gets the look description.
    * @return the look description of the mob.
    */
   public String getLookDescription() {
      StringBuffer buffer = new StringBuffer();

      buffer.append(_description);

      if (!isNpc()) {
         buffer.append(_vitality.getMobDescription(_name));
      }

      if (_generalAttack.getMobWeaponType() != null) {
         buffer.append(" ");
         String prefix = MyStringUtil.generatePrefix(_mobWeapon.getName()).toLowerCase();
         String weaponName = _mobWeapon.getName();
         String mobWeaponString = MessageFormat.format(buffer.toString(), prefix, weaponName);
         return GameUtil.formatStringTo80Columns(mobWeaponString);
      }

      return GameUtil.formatStringTo80Columns(buffer.toString());
   }

   /**
    * Sets the activity ticker.  The mob will rest when this ticker is greater than 0 and
    * will do something when it reaches 0.
    * @param value the activity ticker.
    * @see isResting, decreaseActivityTicker
    */
   public void setActivityTicker(int value) {
      _activityTicker = value;
   }

   /**
    * Decreases the mobs activity ticker.
    * @see isResting, setActivityTicker
    */
   public void decreaseActivityTicker() {
      if (_activityTicker > 0) {
         _activityTicker -= ACTIVITY_TIMER_WAKEUP;
      }
   }

   /**
    * Makes the mob do something if possible.  The mob will pass if it is mesmerized,
    * has health below 0, is fleeing, etc.
    */
   public void doSomething() {
      if (_vitality.getCurVitality() <= 0) {
         // Since this method is called in a separate thread, its possible that a mob can be dead and attack directly
         // afterward (after hitting a trap, for example).  This check should prevent that.
         return;
      }

      if (getStatus().equals(Status.PARALYSED)) {
         return;
      }

      if (!isFleeing() && !GameUtil.isTown(_room)) {
         if (_vitality.getVitalityPercent() < HP_PERCENT_FLEE_CHECK) {
            int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
            if (roll < getMorale()) {
               setIsFleeing(true);
            }
         }
      }

      if (getMesmerizedTicker() > 0) {
         setMesmerizedTicker(getMesmerizedTicker() - 1);
      } else if (isFleeing()) {
         flee();
      } else {
         attack();
      }

      resetActivityTicker();
   }

   /**
    * Determines if this is a lair mob.
    * @return true if this is a lair mob.
    */
   public boolean isLairMob() {
      for (Lair lair : _room.getLairs()) {
         if (lair.getMob() == _vnum) {
            return true;
         }
      }

      return false;
   }

   /**
    * Checks if this mob is friendly.  I.E., won't attack a player.
    * @return true if this is a friendly mob.
    */
   public boolean isFriendly() {
      return _isFriendly;
   }

   /**
    * Sets the friendly flag.
    * @param isFriendly the friendly flag.
    */
   public void setIsFriendly(boolean isFriendly) {
      _isFriendly = isFriendly;
   }

   /**
    * Gets the mesmerized ticker.  A value of zero indicates that the mob
    * is not mesmerized.
    * @return the mesmerized ticker.
    */
   public int getMesmerizedTicker() {
      return _mesmerizedTicker;
   }

   /**
    * Sets the mesmerized ticker.  A value of zero indicates that the mob
    * is not mesmerized.
    * @param mesmerizedTicker the mesmerized ticker.
    */
   public void setMesmerizedTicker(int mesmerizedTicker) {
      _mesmerizedTicker = mesmerizedTicker;
   }

   /**
    * Gets the player that tamed this mob, if any.  Null otherwise.
    * @return the player that tamed this mob, if any.  Null otherwise.
    */
   public Player getTamedBy() {
      return _tamedBy;
   }

   /**
    * Sets the player who tamed this mob.
    * @param tamedBy the player who tamed this mob.
    */
   public void setTamedBy(Player tamedBy) {
      if (getGroupLeader() != null) {
         getGroupLeader().getGroupList().remove(this);
      }

      if (tamedBy == null) {
         _tamedBy = null;
         setGroupLeader(this);
      } else {
         _tamedBy = tamedBy;
         setGroupLeader(tamedBy);
         tamedBy.getGroupList().add(this);
      }
   }

   /**
    * Finds a random hostile entity within the room.
    * @param room the room that the mob will find a random hostile entity in.
    * @return a random hostile entity within the room, or null if one was not found.
    */
   public Entity findRandomHostileEntity(Room room) {
      ArrayList<Entity> potentialTargets = new ArrayList<Entity>();
      for (Entity entity : room.getPlayers()) {
         addHostile(potentialTargets, entity);
      }

      for (Entity entity : room.getMobs()) {
         addHostile(potentialTargets, entity);
      }

      if (potentialTargets.size() == 0) {
         return null;
      }

      int index = Dice.roll(0, potentialTargets.size() - 1);
      return potentialTargets.get(index);
   }

   /**
    * Checks if the mob is fleeing.
    * @return true if the mob is fleeing.
    */
   public boolean isFleeing() {
      return _isFleeing;
   }

   /**
    * Sets the mobs fleeing flag.
    * @param isFleeing the fleeing flag.
    */
   public void setIsFleeing(boolean isFleeing) {
      _isFleeing = isFleeing;
   }

   /**
    * Forces the mob to flee.
    */
   public void flee() {
      if (_room.getPlayers().size() == 0) {
         return;
      }

      Exit fleeDirection = _room.getRandomExit();
      moveMob(fleeDirection.getExitDirection());
   }

   /**
    * Gets the player that the mob is chasing, or null.
    * @return the player that the mob is chasing, or null.
    */
   public Player getChasing() {
      return _chasing;
   }

   /**
    * Sets the player that the mob will chase.
    * @param chasing the player that the mob is chasing, or null.
    */
   public void setChasing(Player chasing) {
      _chasing = chasing;
   }

   /**
    * Forces the mob to chase the given player.  If a player isn't defined,
    * then nothing will happen.
    */
   public void chase() {
      if (_chasing == null) {
         return;
      }

      if (GameUtil.isArena(_room)) {
         return;
      }

      for (Player player : getRoom().getPlayers()) {
         if (player.equals(_chasing)) {
            return;
         }
      }

      ExitDirectionEnum chaseDirection = _chasing.getTrackingMap().get(getRoom());

      if (chaseDirection == null) {
         _log.error(_name + " chasing " + _chasing.getName() + " but exit direction was null.  _chasing will be set to null.");
         _chasing = null;
         return;
      }

      moveMob(chaseDirection);
   }

   /**
    * Gets the mobs name with the 'a' or 'an' prefix.
    * @return the mobs name with the 'a' or 'an' prefix.
    */
   public String getNameWithPrefix() {
      return getPrefix() + " " + getName();
   }

   /**
    * Sets it so that this mob can track a player with the order command.
    * @param canTrack the can tracking flag.
    */
   public void setCanTrack(boolean canTrack) {
      _canTrack = canTrack;
   }

   /**
    * Checks if this mob can track a player with the order command.
    * @return true if this mob can track a player with the order command.
    */
   public boolean canTrack() {
      return _canTrack;
   }

   /**
    * Checks if this mob is an npc.
    * @return true if this mob is an npc.
    */
   public boolean isNpc() {
      return _npc;
   }

   /**
    * Sets the npc flag of this mob.
    * @param npc the npc flag of this mob.
    */
   public void setNpc(boolean npc) {
      _npc = npc;
   }

   //**********
   // Methods inherited from Entity.
   //**********

   /**
    * Gets the name of the entity.
    * @return the name of the entity.
    */
   public String getName() {
      return _name;
   }

   /**
    * Sets the name of the entity.
    * @param name the name of the entity.
    */
   public void setName(String name) {
      if (name == null || name.length() == 0) {
         name = "ERROR";
      }
      _name = name;
      _prefix = MyStringUtil.generatePrefix(_name);
   }

   /**
    * Gets the vitality.
    * @return the vitality.
    */
   public Vitality getVitality() {
      return _vitality;
   }

   /**
    * Gets the amount of experience given per point of damage.
    * @param playerLevel the attacking players level.
    * @return the amount of experience given per point of damage.
    */
   public float getExpPerPointOfDamage(int playerLevel) {
      return WorldManager.getGameMechanics().getExpPerPointOfDamage(playerLevel, _level, _mobVariance);
   }

   /**
    * Gets the mob variance.  Not really used, yet, but a means to make mobs slightly different.
    * @return the mob variance.
    */
   public float getVariance() {
      return _mobVariance;
   }

   /**
    * Takes the given amount of damage.
    * @param amount the amount of damage taken.
    * @return true if the entity's health goes below 0.
    */
   public synchronized boolean takeDamage(int amount) {
      _vitality.subtractCurVitality(amount);
      if (_vitality.getCurVitality() <= 0) {
         return true;
      }
      return false;
   }

   /**
    * Sends a message to the entity followed by a carriage return.
    * @param str the message to send.
    */
   public void println(String str) {
      // do nothing since mobs do not have output.
   }

   /**
    * Sends a message to the entity without the carriage return.
    * @param str the message to send.
    */
   public void print(String str) {
      // do nothing since mobs do not have output.
   }

   /**
    * Gets the entity's level.
    * @return the entity's level.
    */
   public int getLevel() {
      return _level;
   }

   /**
    * Sets the entity's level.
    * @param level the entity's level.
    */
   public void setLevel(int level) {
      _level = level;
   }

   /**
    * Gets the entity's combat skill.
    * @return the entity's combat skill.
    */
   public int getCombatSkill() {
      return _combatSkill;
   }

   /**
    * Gets the entity type.
    * @return the entity type.
    */
   public EntityType getEntityType() {
      return EntityType.MOB;
   }

   /**
    * Gets the armor rating.
    * @return the armor rating.
    */
   public int getArmorRating() {
      return _armor;
   }

   /**
    * Gets the room the entity currently resides in.
    * @return the room the entity currently resides in.
    */
   public Room getRoom() {
      return _room;
   }

   /**
    * A convenience method that sets the room the entity will reside in.
    * @param room the room the entity will reside in.
    */
   public void setRoom(Room room) {
      _room = room;
   }

   /**
    * Sets the room that the entity will reside in based on the room number.
    * @param room the room number.
    */
   public void setRoom(int room) {
      _room = WorldManager.getRoom(room);
   }

   /**
    * Teleports this player to the specified room, ignoring all triggers.
    * @param toRoomNumber the room to teleport to.
    */
   public void teleportToRoomIgnoreTriggers(int toRoomNumber) {
      teleportToRoom(toRoomNumber);
   }

   /**
    * Moves the entity to this room.  No messages are displayed.
    * @param toRoomNumber the room number to move the entity to.
    * @return a TriggerResult, since teleporting an entity can still trigger a trigger.
    */
   public synchronized TriggerResult teleportToRoom(int toRoomNumber) {
      Room toRoom = WorldManager.getRoom(toRoomNumber);

      // Certain rooms do not allow mobs.
      MoveFailCode code = toRoom.placeMob(this);
      if (code.equals(MoveFailCode.ROOM_FULL)) {
         return TriggerResult.NOTHING;
      }

      // Remove from old room first!
      Room fromRoom = WorldManager.getRoom(_room.getRoomNumber());
      fromRoom.removeMob(this);

      // Add to new room.
      _room = toRoom;

      return TriggerResult.NOTHING;
   }

   /**
    * Move the entity to this room.
    * @param toRoomNumber the destination room number.
    * @param ignoreBarrier true if any barriers can be ignored.
    * @return a move fail code.
    */
   public synchronized MoveFailCode moveToRoom(int toRoomNumber, boolean ignoreBarrier) {
      if (toRoomNumber == -1) {
         setChasing(null);
         return MoveFailCode.NO_EXIT;
      }

      // Check if mob can go this way (locked door, etc)
      Room toRoom = WorldManager.getRoom(toRoomNumber);

      if (RoomFlags.SAFE.isSet(toRoom.getRoomFlags())
            || RoomFlags.ARENA.isSet(toRoom.getRoomFlags())) {
         setChasing(null);
         return MoveFailCode.NO_MOBS_ALLOWED;
      }

      // Don't allow a mob to enter a room that teleports.
      for (Trigger trigger : toRoom.getTriggers()) {
         if (trigger.getTriggerType().equals(TriggerType.TELEPORT)) {
            setChasing(null);
            return MoveFailCode.NO_MOBS_ALLOWED;
         }
      }

      Room fromRoom = WorldManager.getRoom(_room.getRoomNumber());
      Exit exit = fromRoom.getExit(toRoomNumber);
      if (!ignoreBarrier && !exit.isPassable()) {
         setChasing(null);
         return MoveFailCode.BARRIER;
      }

      // Certain rooms do not allow mobs.
      MoveFailCode code = toRoom.placeMob(this);
      if (code.equals(MoveFailCode.ROOM_FULL)) {
         return MoveFailCode.ROOM_FULL;
      }

      // Remove from old room first!
      fromRoom.removeMob(this);

      // Add to new room.
      _room = toRoom;

      return MoveFailCode.NONE;
   }

   /**
    * Gets the status.
    * @return the status.
    */
   public Status getStatus() {
      return _status;
   }

   /**
    * Sets the status.
    * @param status the status.
    */
   public void setStatus(Status status) {
      _status = status;
   }

   /**
    * Gets the poison damage per tick.
    * @return the poison damage per tick.
    */
   public int getPoisonDamage() {
      return _poisonDamage;
   }

   /**
    * Sets the poison damage per tick.
    * @param poisonDamage the poison damage per tick.
    */
   public void setPoisonDamage(int poisonDamage) {
      _poisonDamage = poisonDamage;
   }

   /**
    * Gets the mana.
    * @return the mana.
    */
   public Mana getMana() {
      return _mana;
   }

   /**
    * Gets the regeneration ticker.  If this value is above 0, the entity has higher than
    * normal vitality regeneration.
    * @return the regeneration ticker.
    */
   public int getRegenerationTicker() {
      return _regenerationTicker;
   }

   /**
    * Sets the regeneration ticker.  If this value is above 0, the entity has higher than
    * normal vitality regeneration.
    * @param value the regeneration ticker.
    */
   public void setRegenerationTicker(int value) {
      _regenerationTicker = value;
   }

   /**
    * Reduces the regeneration ticker.  If the ticker is above 0, the entity has higher than
    * normal vitality regeneration.
    * @param value the amount to reduce the ticker.
    */
   public void reduceRegenerationTicker(int value) {
      _regenerationTicker = _regenerationTicker - value;
      if (_regenerationTicker < 0) {
         _regenerationTicker = 0;
      }
   }

   /**
    * Gets the hunger ticker.  If this value is below zero, the entity will starve.
    * @return the hunger ticker.
    */
   public int getHungerTicker() {
      return 3600;
   }

   /**
    * Increases the hunger ticker by the specified amount.
    * @param value the amount to increase the hunger ticker.
    */
   public void addHungerTicker(int value) {
      return;
   }

   /**
    * Sets the hunger ticker by the specified amount.
    * @param value the amount to set the hunger ticker.
    */
   public void setHungerTicker(int value){
      return;
   }

   /**
    * Gets the thirst ticker.  If this value is below zero, the entity will be thirsty.
    * @return the thirst ticker.
    */
   public int getThirstTicker() {
      return 3600;
   }

   /**
    * Increases the thirst ticker by the specified amount.
    * @param value the amount to increase the thirst ticker.
    */
   public void addThirstTicker(int value) {
      return;
   }

   /**
    * Sets the thirst ticker by the specified amount.
    * @param value the amount to set the thirst ticker.
    */
   public void setThirstTicker(int value) {
      return;
   }

   /**
    * Decreases both the hunger and thirst ticker and accordingly updates the entities status
    * if any of these tickers are reduced below 0.
    * @param value the amount to decrease the hunger and thirst ticker.
    */
   public void subtractSustenance(int value) {
      return;
   }

   /**
    * Increase both the hunger and thirst ticker by the specified amount.
    * @param value the amount to increase both the thirst and hunger ticker.
    */
   public void addSustenance(int value) {
      return;
   }


   /**
    * Gets the base stats.
    * @return the base stats.
    */
   public BaseStats getStats() {
      return _stats;
   }

   /**
    * Decreases the effect timers.
    * @return true if the effect timer hits 0.
    */
   public boolean decreaseEffectTimers() {
      return false;
   }

   /**
    * Sets the magic protection.
    * @param magicProtection the magic protection.
    */
   public void setMagicProtection(int magicProtection) {
   }

   /**
    * Gets the magic protection.
    * @return the magic protection.
    */
   public int getMagicProtection() {
      return 0;
   }

   /**
    * Sets the magic protection timer.
    * @param magicProtectionTimer the magic protection timer value.
    */
   public void setMagicProtectionTimer(int magicProtectionTimer) {
   }

   /**
    * Sets the invisibility timer.
    * @param invisibilityTimer the invisibility timer value.
    */
   public void setInvisibiltyTimer(int invisibilityTimer) {
   }

   /**
    * Checks if this entity is invisible.
    * @return true if this entity is invisible.
    */
   public boolean isInvisible() {
      return false;
   }

   /**
    * Gets the resting string.  This is currently used for the DoOrder command.
    * @return the resting string.
    */
   public String getRestingString() {
      return MessageManager.READY_STRING.getMessage();
   }

   /**
    * Gets the group leader.  If the entity is not in a group, this method returns
    * the entity itself.  Hopefully, this method never returns null.
    * @return the group leader.
    */
   public Entity getGroupLeader() {
      return _groupLeader;
   }

   /**
    * Sets the group leader.  If the entity is not in a group, set this to the
    * entity itself.  This value should not be null.
    * @param leader the group leader.
    */
   public void setGroupLeader(Entity leader) {
      _groupLeader = leader;
   }

   /**
    * Gets the list of entities in the group if this entity is the leader of a group.
    * An empty list is returned otherwise.
    * @return the list of entities in the group if this entity is the leader of a group.
    */
   public ArrayList<Entity> getGroupList() {
      return _groupList;      
      // return new ArrayList<Entity>();
   }

   /**
    * Sets the group list.
    * @param groupList the group list.
    */
   public void setGroupList(ArrayList<Entity> groupList) {
      _groupList = groupList;
   }

   /**
    * Checks if this entity is following a group.
    * @return true if this entity is following a group.
    */
   public boolean isFollowingGroup() {
      return true;
   }

   /**
    * Sets the is following flag.
    * @param isFollowingGroup true if this entity is in a group.
    */
   public void setFollowingGroup(boolean isFollowingGroup) {
   }

   /**
    * Checks if this entity is in the middle of attacking.
    * @return true if this entity is in the middle of attacking.
    */
   public boolean isAttacking() {
      return false;
   }

   /**
    * Checks if this entity is resting.
    * @return true if this entity is resting.
    */
   public boolean isResting() {
      if (_activityTicker > 0) {
         return true;
      }
      return false;
   }

   /**
    * Checks if this entity is mentally exhausted.
    * @return true if this entity is mentally exhausted.
    */
   public boolean isMentallyExhausted() {
      return false;
   }

   /**
    * Gets the amount of gold the mob can drop.
    * @return the amount of gold the mobs can drop.
    */
   public int getGold() {
      return _gold;
   }

   /**
    * Subtracts the amount of gold from the entity.  If the entity's gold goes
    * below 0, the gold amount will be set to 0 and the overflow will be returned.
    * @param amount the amount of gold to subtract.
    * @return the overflow amount.
    */
   public int subtractGold(int amount) {
      _gold -= amount;

      int overflow = 0;
      if (_gold < 0) {
         overflow = 0 - _gold;
         _gold = 0;
      }

      return overflow;
   }

   /**
    * This method adds to the amount of gold that the player has. Note that this
    * method is constrained by the MAX_INVENTORY_GOLD properties value. This
    * method returns the amount of over flow, or 0 if the set did not go over
    * the MAX_INVENTORY_GOLD value.
    *
    * @param amount The amount of gold to give the player.
    * @return the amount of over flow, or 0 if the set did not go over the
    *         MAX_INVENTORY_GOLD value.
    */
   public int addGold(int amount) {
      _gold += amount;

      return checkGoldOverflow();
   }

   /**
    * This method sets the amount of gold that the player has. Note that this
    * method is constrained by the MAX_INVENTORY_GOLD properties value. This
    * method returns the amount of over flow, or 0 if the set did not go over
    * the MAX_INVENTORY_GOLD value.
    *
    * @param gold The amount of gold the player will have.
    * @return the amount of over flow, or 0 if the set did not go over the
    *         MAX_INVENTORY_GOLD value.
    */
   public int setGold(int gold) {
      _gold = gold;

      return checkGoldOverflow();
   }

   /**
    * Checks if this entity has a lit torch.
    * @return true if a lit torch is being carried.
    */
   public boolean hasLitTorch() {
      return true;
   }

   /**
    * Checks if this entity has light.
    * @return true if this entity has light.
    */
   public boolean hasLight() {
      return true;
   }

   /**
    * This removes an item of the same vnum from the inventory.
    *
    * @param vnum The vnum of the item to remove.
    * @return true if the item was removed, false otherwise.
    */
   public boolean removeItemFromInventory(int vnum) {
      if (_inventoryItem.getVnum() == vnum) {
         _inventoryItem = null;
         return true;
      }
      return false;
   }

   /**
    * This removes the exact item from the inventory. That is, the object has to
    * be of the same instance.
    *
    * @param item The item to remove.
    * @return true if the item was removed, false otherwise.
    */
   public boolean removeItemFromInventory(Item item) {
      if (_inventoryItem.equals(item)) {
         _inventoryItem = null;
         return true;
      }
      return false;
   }

   /**
    * Places an item into the entities inventory.
    * @param item the item to place.
    * @param ignoreWeight set to true if the item should ignore weight constraints.
    * @return an InventoryFailCode.
    */
   public InventoryFailCode placeItemInInventory(Item item, boolean ignoreWeight) {
      _inventoryItem = item;

      return InventoryFailCode.NONE;
   }

   /**
    * Gets the entire inventory list.
    * @return the inventory list.
    */
   public ArrayList<Item> getInventory() {
      if (_inventoryItem != null) {
         ArrayList<Item> list = new ArrayList<Item>();
         list.add(_inventoryItem);
         return list;
      } else {
         return new ArrayList<Item>();
      }
   }

   /**
    * Gets the player class.
    * @return the player class.
    */
   public PlayerClass getPlayerClass() {
      return PlayerClass.WARRIOR;
   }

   /**
    * Gets the gender.
    * @return the gender.
    */
   public Gender getGender() {
      return _gender;
   }

   /**
    * Sets the gender.
    * @param gender the gender.
    */
   public void setGender(Gender gender) {
      _gender = gender;
   }

   /**
    * Gets the tracking map.  This is a map of rooms the entity went through along
    * with the direction.  This is used for tracking.
    * @return the tracking map.
    */
   public HashMap<Room, ExitDirectionEnum> getTrackingMap() {
      // Can't track mobs.
      return new HashMap<Room, ExitDirectionEnum>();
   }

   /**
    * Checks if this entity is similar to the specified entity.
    * @param entity the entity to compare.
    * @return true if this entity is similar to the specified entity.
    */
   public boolean isSimilar(Entity entity) {
      if (!entity.getEntityType().equals(EntityType.MOB)) {
         return false;
      }

      Mob mob = (Mob) entity;
      if (mob.getVnum() == _vnum) {
         return true;
      }
      return false;
   }

   /**
    * Gets the spell book.
    * @return the spell book.
    */
   public Spellbook getSpellbook() {
      scribeSpellBook();
      return _spellbook;
   }

   /**
    * Sets the spell book.
    * @param spellbook the spell book.
    */
   public void setSpellbook(Spellbook spellbook) {
      _spellbook = spellbook;
   }

   /**
    * Decreases the mental exhaustion ticker.
    */
   public void decreaseMentalExhaustionTicker() {
   }

   /**
    * Sets the mental exhaustion ticker.  If this value is above 0, then the entity is
    * mentally exhausted.
    * @param value the mental exhaustion ticker.
    */
   public void setMentalExhaustionTicker(int value) {
   }

   /**
    * Adds the amount of experience.
    * @param experience the amount of experience to add.
    */
   public void addExperience(long experience) {
   }

   /**
    * Gets the attacks.
    * @return the attacks.
    */
   public Attacks getAttacks() {
      return new DefaultAttacks(this);
   }

   /**
    * Decreases the rest ticker.
    */
   public void decreaseRestTicker() {
   }

   /**
    * Decrease the combat ticker.
    */
   public void decreaseCombatTicker() {
   }

   /**
    * Sets the rest ticker.  If this number is greater than 0, then the
    * entity is resting.
    * @param value the rest ticker value.
    */
   public void setRestTicker(int value) {
   }

   /**
    * Sets the combat ticker.  If this number is greater than 0, then the entity
    * has attacked recently and is considered in combat.
    * @param value the combat ticker.
    */
   public void setCombatTicker(int value) {
   }

   /**
    * Return a number between 1 to 20.  This method converts the Spell Skill of a mob to the modifier used
    * within this engine.  The mob spell skill is a range from 1 to 100, it seems, where higher is better.
    * Unfortunately, spell success is based on a number from 1 to 20, where lower is better.
    *
    * Gets the spell hit difficulty for this entity.
    * @return the spell hit difficulty for this entity.
    */
   public int getSpellHitDifficulty() {
      float hitDifficulty = MOB_SPELL_HIT_DIFFICULTY + ((100F - (float) _mobSpells.getSpellSkill()) / 10F);
      if (hitDifficulty < 1) {
         hitDifficulty = 1;
      }
      if (hitDifficulty > 20) {
         hitDifficulty = 20;
      }
      return (int) hitDifficulty;
   }

   /**
    * Gets the message handler.
    * @return the message handler.
    */
   public EntityMessageHandler getMessageHandler() {
      return _messageHandler;
   }

   /**
    * Sets the message handler.
    * @param messageHandler the message handler.
    */
   public void setMessageHandler(EntityMessageHandler messageHandler) {
      _messageHandler = messageHandler;
   }

   /**
    * Sets the paralysis ticker.  If this value is greater than 0, than the entity
    * is considered to be paralyzed.
    * @param value the paralysis ticker value.
    */
   public void setParalysisTicker(int value) {
      _paralysisTimer = value;
   }

   /**
    * Decreases the paralysis ticker.
    * @param amount the amount to decrease the ticker.
    * @return true if the entity is no longer paralyzed.
    */
   public boolean decreaseParalysisTicker(int amount) {
      if (_paralysisTimer > 0) {
         _paralysisTimer -= amount;
      }

      if (_paralysisTimer <= 0) {
         return true;
      }

      return false;
   }

   /**
    * Gets the player that this entity is dragging.  Null if none.
    * @return the player that this entity is dragging.  Null if none.
    */
   public Player getDragging() {
      return null;
   }

   /**
    * Sets the dragging player.
    * @param dragging the dragging player.
    */
   public void setDragging(Player dragging) {
   }

   /**
    * Gets the player that this entity is being dragged by.
    * @return the player that this entity is being dragged by.
    */
   public Player getDraggedBy() {
      return null;
   }

   /**
    * Sets the player that this entity is being dragged by.
    * @param draggedBy the player that this entity is being dragged by.
    */
   public void setDraggedBy(Player draggedBy) {
   }

   /**
    * Gets the last time this entity has moved.
    * @return the last time this entity has moved.
    */
   public long getLastMove() {
      return 0;
   }

   /**
    * Sets the last time this entity has moved.
    * @param lastMove the last time this entity has moved.
    */
   public void setLastMove(long lastMove) {
   }

   /**
    * This checks if the entity is currently tripping.
    * @return true if this entity is currently tripping.
    */
   public boolean isTripping() {
      return false;
   }

   /**
    * This sets the entity into a tripping state.
    * @param isTripping the tripping flag.
    */
   public void setIsTripping(boolean isTripping) {
   }

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @return true if this entity can ignore the tripping mechanism when moving too fast.
    */
   public boolean getIgnoreTrip() {
      return true;
   }

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @param ignoreTrip the ignore trip flag.
    */
   public void setIgnoreTrip(boolean ignoreTrip) {
   }

   /**
    * Gets the debug string for this mob.
    * @return the debug string for this mob.
    */
   public String getDebugString() {
      StringBuffer buffer = new StringBuffer();

      Formatter formatter = new Formatter(buffer, Locale.US);
      formatter.format("&CVnum:&W%d  &CName:&W%s  &CPrefix:&W%s  &CPlural:&W%s\n",
            _vnum, _name, _prefix, _plural);
      formatter.format("&CLevel:&W%s  &CMorale:&W%s  &CGender:&W%s  &CTerrain:&W%s\n",
            _level, _morale, _gender, _terrain);
      formatter.format("&CArmor:&W%d  &CHit  Dice:&W%d  &CRegen:&W%s  &CCombat Skill:&W%d\n",
            _armor, _hitDice, _regeneration, _combatSkill);
      formatter.format("&CSpecial Attack Percent:&W%s  &CSubtype:&W%s  &CMana:&W%d&C/&W%d\n",
            _specialAttackPercent, _subType, _mana.getCurMana(), _mana.getMaxMana());
      formatter.format("&CInt:&W%d/%d &CKno:&W%d/%d &CPhy:&W%d/%d &CSta:&W%d/%d &CAgi:&W%d/%d &CCha:&W%d/%d \n",
            _stats.getIntellect().getValue(), _stats.getIntellect().getModifiedStat(), 
            _stats.getKnowledge().getValue(), _stats.getKnowledge().getModifiedStat(),
            _stats.getPhysique().getValue(), _stats.getPhysique().getModifiedStat(), 
            _stats.getStamina().getValue(), _stats.getStamina().getModifiedStat(), 
            _stats.getAgility().getValue(), _stats.getAgility().getModifiedStat(),
            _stats.getCharisma().getValue(), _stats.getCharisma().getModifiedStat());
      formatter.format("&CGold:&W%d  &CTreasure:&W%d  &CVariance:&W%f\n", _gold, _treasure, _mobVariance);
      formatter.format("&CInventory:&W%s\n", _inventoryItem);
      formatter.format("&CDescription:\n&W%s\n", _description);

      if (_mobWeapon != null) {
         formatter.format("&CWeapon Name:&W%s  &CType:&W%s  &CMin Damage:&W%d  &CMax Damage:&W%d\n",
               _mobWeapon.getName(), _mobWeapon.getMobWeaponType(), _mobWeapon.getMinDamage(), _mobWeapon.getMaxDamage());
      } else {
         buffer.append("&CWeapon Name:&Wnone\n");
      }

      buffer.append("&CGeneral Attack\n");
      formatter.format("  &CWeapon:&W%s\n", _generalAttack.getWeapon());
      formatter.format("  &CAttacks:&W%d  &CDamage:&W%d&C/&W%d  &CEffect:&W%s  &CEffect Range:&W%d&C/&W%d  \n",
            _generalAttack.getNumAttacks(), _generalAttack.getMaxDamage(), _generalAttack.getMaxDamage(),
            _generalAttack.getAttackEffect(), _generalAttack.getMinAttackEffect(), _generalAttack.getMaxAttackEffect());

      buffer.append("&CSpecial Attack\n");
      formatter.format("  &CDescription:&W%s  &CDamage:&W%d&C/&W%d\n",
            _specialAttack.getSpecialAttackDescription(), _specialAttack.getMinSpecialDamage(),
            _specialAttack.getMaxSpecialDamage());

      buffer.append("&CSpecial Ability\n");
      formatter.format("  &CAbility:&W%s  &CDescription:&W%s\n",
            _specialAbility.getSpecialAbility(), _specialAbility.getSpecialAbilityDescription());

      try {
         buffer.append("&CSpells");
         formatter.format("  &CType:&W%s  &CSkill:&W%d  &CRange:&W%d&C-&W%d\n",
               _mobSpells.getMobSpellType(), _mobSpells.getSpellSkill(), _mobSpells.getMinSpell(),
               _mobSpells.getMaxSpell());
         buffer.append("  &CSpells:&W ");
         for (Spell spell : getSpellbook().getSpells()) {
            formatter.format("%s ", spell.getName());
         }
         buffer.append("\n");
      } catch (NullPointerException e) {
         // TODO I saw a null pointer exception here.
         _log.error("Error listing spells for mob " + _name + ".  vnum is " + _vnum);
         throw e;
      }

      formatter.format("&CVitality:&W%d&C/&W%d&C(&W%d%%&C)  &CStatus:&W%s  &CPoison Damage:&W%d\n",
            _vitality.getCurVitality(), _vitality.getMaxVitality(), _vitality.getVitalityPercent(),
            _status, _poisonDamage);
      formatter.format("&CFriendly:&W%s  &CMesmerized Ticker:&W%s  &CTamed By:&W%s\n",
            _isFriendly, _mesmerizedTicker, _tamedBy);
      formatter.format("&CActivity Ticker:&W%d  &CRegeneration Ticker:&W%d\n",
            _activityTicker, _regenerationTicker);
      formatter.format("&CGroup Leader:&W%s  &CChasing:&W%s  &CFleeing:&W%s\n",
            (_groupLeader != null ? _groupLeader.getName() : "none"),
            (_chasing != null ? _chasing.getName() : "none"),
            _isFleeing);

      return buffer.toString();
   }

   //**********
   // Private methods.
   //**********

   /**
    * Attack a random entity.  This includes special attacks, special abilities,
    * and basic melee attacks.
    */
   private void attack() {
      if (_specialAttack.hasSpecialAttack()) {
         int roll = Dice.roll(1, 100);
         if (roll <= _specialAttackPercent) {
            handleSpecialAttack(_room);
            return;
         }
      }

      if (!_specialAbility.getSpecialAbility().equals(SpecialAbilityEnum.NONE)) {
         int roll = Dice.roll(1, 100);
         if (roll <= MOB_SPEC_ABILITY_CHANCE) {
            handleSpecialAbility(_room);
            return;
         }
      }

      handleMeleeEntityAttack(_room);
   }

   /**
    * Move the mob in the specified direction.
    * @param direction the direction to move this mob.
    */
   private void moveMob(ExitDirectionEnum direction) {
      Command command = GameUtil.getDirectionCommand(direction);
      if (command != null) {
         command.execute(this, "");
      }
   }

   /**
    * Gives the mob a random weapon that is defined within the data file.
    * @return a MobWeapon.
    */
   private MobWeapon generateMobWeapon() {
      if (_generalAttack.getMobWeaponType() != null) {
         ArrayList<MobWeapon> eligableWeapons = WorldManager.getMobWeapons(_generalAttack.getMobWeaponType());
         int randomNumber = Dice.roll(0, eligableWeapons.size() - 1);
         return eligableWeapons.get(randomNumber);
      } else {
         return null;
      }
   }

   /**
    * Sets the activity ticker of the mob.
    */
   private void resetActivityTicker() {
      int random = Dice.roll(0, 2);
      int activityTicker;
      if (random == 0) {
         activityTicker = 12;
      } else if (random == 1) {
         activityTicker = 16;
      } else if (random == 2) {
         activityTicker = 20;
      } else {
         _log.error("Unhandled random value of " + random);
         activityTicker = 16;
      }
      setActivityTicker(activityTicker);
   }

   /**
    * Forces the mob to do a special attack.
    * @param room the room that the mob will perform the special attack.
    */
   private void handleSpecialAttack(Room room) {
      Entity victim = findRandomHostileEntity(room);
      if (victim == null) {
         return;
      }

      String messageToPlayer = "&RThe " + _name + " " + _specialAttack.getSpecialAttackDescription() + " you.";
      victim.println(messageToPlayer);
      String messageToRoom = "&MThe " + _name + " " + _specialAttack.getSpecialAttackDescription()
         + " " + victim.getName() + ".";
      room.println(victim, messageToRoom, false);
      victim.takeDamage(_specialAttack.getRandomizedDamage());
      GameUtil.checkAndHandleKill(this, victim);

      return;
   }

   /**
    * Forces the mob to do a special ability.
    * @param room the room that the mob will perform the special ability.
    */
   private void handleSpecialAbility(Room room) {
      SpecialAbilityCommand specialAbilityCommand
         = SpecialAbilityCommandManager.getInstance().getSpecialAbilityCommand(_specialAbility.getSpecialAbility());

      if (specialAbilityCommand == null) {
         _log.error("Unhandled special ability for " + _name + ".");
         return;
      }

      specialAbilityCommand.execute(this, room);

   }

   /**
    * Forces the mob to attack a random entity.
    * @param room the room where the mob will attack.
    */
   private void handleMeleeEntityAttack(Room room) {
      Entity victim = findRandomHostileEntity(room);
      if (victim == null) {
         return;
      }

      int numberOfAttacks = 1;
      if (_generalAttack.getNumAttacks() != 0
            && _generalAttack.getNumAttacks() != 1) {
         numberOfAttacks = Dice.roll(1, _generalAttack.getNumAttacks());
      }
      for (int attackNumber = 0; attackNumber < numberOfAttacks; attackNumber++) {
         MeleeResult result = WorldManager.getGameMechanics().doMobAttackEntity(this, victim);

         String messageToPlayer;
         switch (result.getMeleeResultEnum()) {
         case MISS:
            messageToPlayer = MessageFormat.format(TaMessageManager.MFMYOU.getMessage(), _name);
            victim.print(messageToPlayer);

            room.print(victim, victim.getMessageHandler().getMobMissRoomMessage(_name), false);
            break;
         case HIT:
            messageToPlayer = MessageFormat.format(TaMessageManager.MATYOU.getMessage(),
                  _name, _gender.getPronoun().toLowerCase() + " " + result.getWeapon(), result.getDamage());
            victim.print(messageToPlayer);

            try {
               room.print(victim, victim.getMessageHandler().getMobHitRoomMessage(_name, result), false);
            } catch (NullPointerException e) {
               // TODO I seem to be getting null pointer exceptions here.
               _log.error(e);
               _log.error("Room = " + room);
               _log.error("victim = " + victim);
               _log.error("message handler = " + victim.getMessageHandler());
               _log.error("getMobHitRoomMessage = " + victim.getMessageHandler().getMobHitRoomMessage(_name, result));
            }
            handleMeleeAttackEffect(victim, result);
            victim.takeDamage(result.getDamage());
            break;
         case DODGE:
            messageToPlayer = MessageFormat.format(TaMessageManager.MDGYOU.getMessage(), _name);
            victim.print(messageToPlayer);

            room.print(victim, victim.getMessageHandler().getMobDodgeRoomMessage(_name), false);
            break;
         case GLANCE:
            messageToPlayer = MessageFormat.format(TaMessageManager.MGNYOU.getMessage(),
                  _name, "its " + result.getWeapon());
            victim.print(messageToPlayer);

            room.print(victim, victim.getMessageHandler().getMobGlanceRoomMessage(_name, result), false);
            break;
         default :
            _log.error("Got unhandled melee result of " + result.getMeleeResultEnum());
            break;
         }
      }
      GameUtil.checkAndHandleKill(this, victim);
   }

   /**
    * Handles any melee attack effects.
    * @param victim the victim.
    * @param result the MeleeResult.
    */
   private void handleMeleeAttackEffect(Entity victim, MeleeResult result) {
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
      if (roll < MELEE_EFFECT_CHANCE) {
         if (_generalAttack.getAttackEffect().equals(AttackEffectEnum.POISON)) {
            victim.print(TaMessageManager.POISON.getMessage());
            victim.setStatus(Status.POISONED);
            victim.setPoisonDamage(_level);
         } else if (_generalAttack.getAttackEffect().equals(AttackEffectEnum.MANA_DRAIN)) {
            victim.print(TaMessageManager.DRAIN.getMessage());
            victim.getMana().subtractCurMana(result.getDamage());
         } else if (_generalAttack.getAttackEffect().equals(AttackEffectEnum.VITALITY_DRAIN)) {
            victim.print(TaMessageManager.DRAIN.getMessage());
            int drain = Dice.roll(_generalAttack.getMinAttackEffect(), _generalAttack.getMaxAttackEffect())
               + victim.getVitality().getDrained();
            victim.getVitality().setDrained(drain);
         } else if (_generalAttack.getAttackEffect().equals(AttackEffectEnum.PARALYSIS)) {
            int minDuration = PARALYSIS_MULTIPLIER * _generalAttack.getMinAttackEffect();
            int maxDuration = PARALYSIS_MULTIPLIER * _generalAttack.getMaxAttackEffect();
            int duration = Dice.roll(minDuration, maxDuration);
            victim.print(TaMessageManager.PARLYZ.getMessage());
            victim.setParalysisTicker(duration);
            victim.setStatus(Status.PARALYSED);
         }
      }
   }

   /**
    * Adds the entity to the list of potential targets if the target is hostile.
    * @param potentialTargets the list of potential targets.
    * @param entity the entity to check.
    */
   private void addHostile(ArrayList<Entity> potentialTargets, Entity entity) {
      boolean isInGroup = false;
      if (getGroupLeader() != null) {
         if (getGroupLeader().getGroupList().contains(entity) || getGroupLeader().equals(entity)) {
            isInGroup = true;
         }
      }
      if (!isInGroup && !this.equals(entity)) {
         potentialTargets.add(entity);
      }
   }

   /**
    * Checks for gold overflow and sets the amount to the max.
    * @return the amount of gold that went over the max amount.
    */
   private int checkGoldOverflow() {
      int overflow = 0;

      if (_gold > MAX_INVENTORY_GOLD) {
         overflow = _gold - MAX_INVENTORY_GOLD;
         _gold = MAX_INVENTORY_GOLD;
      }

      return overflow;
   }

   /**
    * Places the spell in the mobs spell book.
    */
   private void scribeSpellBook() {
      synchronized (_spellbook) {
         if (_mobSpells == null) {
            return;
         } else if (_spellbook.getSpells().size() == 0) {
            for (int index = _mobSpells.getMinSpell(); index < _mobSpells.getMaxSpell(); index++) {
               Spell spell = WorldManager.getAttackSpellFor(_mobSpells.getMobSpellType(), index);
               _spellbook.scribeSpell(spell);
            }
         }
      }
   }


}
