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

package org.tdod.ether.taimpl.player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityMessageHandler;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.RuneScroll;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.manager.PlayerFacade;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.Attacks;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.Encumbrance;
import org.tdod.ether.ta.player.Mana;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerClassData;
import org.tdod.ether.ta.player.RaceData;
import org.tdod.ether.ta.player.Spellbook;
import org.tdod.ether.ta.player.Stat;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.player.enums.Complexion;
import org.tdod.ether.ta.player.enums.EyeColor;
import org.tdod.ether.ta.player.enums.HairColor;
import org.tdod.ether.ta.player.enums.HairLength;
import org.tdod.ether.ta.player.enums.HairStyle;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.ta.player.enums.RaceEnum;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.commands.DoDefault;
import org.tdod.ether.taimpl.commands.handler.HandlePromotion;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * This is the default implementation class of a player.
 * @author Ron Kinney
 */
public class DefaultPlayer implements Player, Serializable {

   private static final long serialVersionUID = -5236115285008480704L;

   private static Log _log = LogFactory.getLog(DefaultPlayer.class);

   private static final int STARTING_ROOM = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.STARTING_ROOM));
   private static final int DEATH_ROOM_AWAKEN = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.DEATH_ROOM_AWAKEN));
   private static final int REROLL_REST_TIME =  Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.REROLL_REST_TIME));
   private static final int MAX_INVENTORY_GOLD = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_INVENTORY_GOLD));
   private static final int MAX_TRAILING_LEVEL = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_TRAILING_LEVEL));
   private static final int PLAYER_SPELL_HIT_DIFFICULTY = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_SPELL_HIT_DIFFICULTY)).intValue();
   private static final int MAX_THIRST = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_HUNGER)).intValue();
   private static final int MAX_HUNGER = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_HUNGER)).intValue();

   private boolean         _isSysop;
   private int             _pnum;
   private String          _name = null;
   private RaceEnum        _race = null;
   private Complexion      _complexion = null;
   private EyeColor        _eyeColor = null;
   private HairColor       _hairColor = null;
   private HairLength      _hairLength = null;
   private HairStyle       _hairStyle = null;
   private PlayerClass     _playerClass = null;
   private int             _level;
   private long            _experience;
   private Rune            _rune = Rune.NONE;
   private BaseStats       _stats = new DefaultBaseStats();
   private Mana            _mana = new DefaultMana();
   private Vitality        _vitality = new DefaultVitality();
   private Status          _status = Status.HEALTHY;
   private int             _poisonDamage = 0;
   private Encumbrance     _encumbrance = new DefaultEncumbrance(this);
   private int             _hungerTicker;
   private int             _thirstTicker;
   private int             _restTicker;
   private int             _combatTicker;
   private int             _mentalExhaustionTicker;
   private int             _gold;
   private Gender          _gender = Gender.MALE;
   private PlayerStateEnum _state = PlayerStateEnum.LOGIN;
   private Spellbook       _spellbook = new DefaultSpellbook();
   private int             _gameOfChanceCount;
   private String          _password;
   private Attacks         _attacks = new DefaultAttacks(this);
   private int             _vaultBalance;
   private ArrayList<Item> _inventory = new ArrayList<Item>();
   private Weapon          _weapon;
   private Armor           _armor;
   private boolean         _promoted;

   // Spell Buffs -- may want to move this into another object at a later time.
   private int             _regenerationTicker = 0;
   private int             _magicProtectionTimer = 0;
   private int             _magicProtection = 0;
   private int             _invisibilityTimer = 0;
   private int             _paralysisTimer = 0;

   // Used for saving the player.  Just need to save the number, not the entire object.
   private int             _roomVnumSave;

   private transient long              _lastMove = 0;
   private transient long              _lastGlobalChat = 0;
   private transient int               _globalChatCount = 0;
   private transient boolean           _isTripping = false;
   private transient boolean           _ignoreTrip = false;
   private transient boolean           _ignoreSustenance = false;
   private transient Room              _room;
   private transient Entity            _groupLeader = this;
   private transient ArrayList<Entity> _groupList = new ArrayList<Entity>();
   private transient boolean           _isFollowingGroup;
   private transient Player            _dragging;
   private transient Player            _draggedBy;

   private transient HashMap<Room, ExitDirectionEnum> _trackingMap = new HashMap<Room, ExitDirectionEnum>();
   private transient GameOutput _output;
   private transient boolean    _disconnected;

   private transient EntityMessageHandler _messageHandler = new PlayerMessageHandler(this);

   /**
    * Creates a new DefaultPlayer.
    */
   public DefaultPlayer() {
   }

   //**********
   // Methods inherited from Player.
   //**********

   /**
    * Sets the players password.
    * @param password the players password.
    */
   public void setPassword(String password) {
      _password = GameUtil.byteArrayToHexString(GameUtil.computeHash(password));
   }

   /**
    * Gets the players password.
    * @return the players password.
    */
   public String getPassword() {
      return _password;
   }

   /**
    * Checks if this player is a sysop.
    * @return true if this player is a sysop.
    */
   public boolean isSysop() {
      return _isSysop;
   }

   /**
    * Sets the sysop flag.
    * @param isAdmin the sysop flag.
    */
   public void setIsSysop(boolean isAdmin) {
      _isSysop = isAdmin;
   }

   /**
    * Gets the players unique number.
    * @return the players unique number.
    */
   public int getPnum() {
      return _pnum;
   }

   /**
    * Sets the players unique number.
    * @param pnum the players unique number.
    */
   public void setPnum(int pnum) {
      _pnum = pnum;
   }

   /**
    * Gets the race.
    * @return the race.
    */
   public RaceEnum getRace() {
      return _race;
   }

   /**
    * Sets the race.
    * @param race the race.
    */
   public void setRace(RaceEnum race) {
      _race = race;
   }

   /**
    * Gets the complexion.
    * @return the complexion.
    */
   public Complexion getComplexion() {
      return _complexion;
   }

   /**
    * Sets the complexion.
    * @param complexion the complexion.
    */
   public void setComplexion(Complexion complexion) {
      _complexion = complexion;
   }

   /**
    * Gets the eye color.
    * @return the eye color.
    */
   public EyeColor getEyeColor() {
      return _eyeColor;
   }

   /**
    * Sets the eye color.
    * @param eyeColor the eye color.
    */
   public void setEyeColor(EyeColor eyeColor) {
      _eyeColor = eyeColor;
   }

   /**
    * Sets the hair color.
    * @return the hair color.
    */
   public HairColor getHairColor() {
      return _hairColor;
   }

   /**
    * Sets the hair color.
    * @param hairColor the hair color.
    */
   public void setHairColor(HairColor hairColor) {
      _hairColor = hairColor;
   }

   /**
    * Gets the hair color.
    * @return the hair color.
    */
   public HairLength getHairLength() {
      return _hairLength;
   }

   /**
    * Sets the hair length.
    * @param hairLength the hair length.
    */
   public void setHairLength(HairLength hairLength) {
      _hairLength = hairLength;
   }

   /**
    * Sets the hair style.
    * @return the hair style.
    */
   public HairStyle getHairStyle() {
      return _hairStyle;
   }

   /**
    * Sets the hair style.
    * @param hairStyle the hair style.
    */
   public void setHairStyle(HairStyle hairStyle) {
      _hairStyle = hairStyle;
   }

   /**
    * Sets the players class.
    * @param playerClass the players class.
    */
   public void setPlayerClass(PlayerClass playerClass) {
      _playerClass = playerClass;
   }

   /**
    * Gets the experience.
    * @return the experience.
    */
   public long getExperience() {
      return _experience;
   }

   /**
    * Sets the experience.
    * @param experience the experience.
    */
   public void setExperience(long experience) {
      _experience = experience;
   }

   /**
    * Gets the rune.
    * @return the rune.
    */
   public Rune getRune() {
      return _rune;
   }

   /**
    * Sets the rune.
    * @param rune the rune.
    */
   public void setRune(Rune rune) {
      _rune = rune;
   }

   /**
    * Gets the encumbrance.
    * @return the encumbrance.
    */
   public Encumbrance getEncumbrance() {
      return _encumbrance;
   }

   /**
    * Gets an item in the players inventory.  Returns null if none was found.
    * @param str the partial or full item name.
    * @return an item in the players inventory.  null if none was found.
    */
   public Item getItem(String str) {
      for (Item item : _inventory) {
         if (MyStringUtil.contains(item.getName(), str)) {
            return item;
         }
      }
      return null;
   }

   /**
    * Sets every variable for the player back to level one.  This should only be called when
    * the player has just been created.
    */
   public void setDefaults() {
      _log.info("Generating character stats for " + getName());
      rerollStats();
      _room = WorldManager.getRoom(STARTING_ROOM);
      _restTicker = 0;
   }

   /**
    * Rerolls the players stats.
    */
   public void rerollStats() {
      _level = 1;

      getStats().resetEnchants();

      BaseStats minStats = PlayerFacade.getStartingStats().getMinStats();
      BaseStats maxStats = PlayerFacade.getStartingStats().getMaxStats();

      getStats().getIntellect().setValue(Dice.roll(minStats.getIntellect().getValue(), maxStats.getIntellect().getValue()));
      getStats().getKnowledge().setValue(Dice.roll(minStats.getKnowledge().getValue(), maxStats.getKnowledge().getValue()));
      getStats().getPhysique().setValue(Dice.roll(minStats.getPhysique().getValue(), maxStats.getPhysique().getValue()));
      getStats().getStamina().setValue(Dice.roll(minStats.getStamina().getValue(), maxStats.getStamina().getValue()));
      getStats().getAgility().setValue(Dice.roll(minStats.getAgility().getValue(), maxStats.getAgility().getValue()));
      getStats().getCharisma().setValue(Dice.roll(minStats.getCharisma().getValue(), maxStats.getCharisma().getValue()));

      RaceData raceMod = PlayerFacade.getStartingStats().getRaceDatabase().getRaceData(getRace());
      PlayerClassData classMod = PlayerFacade.getStartingStats().getPlayerClassDatabase().getPlayerClassData(getPlayerClass());

      getStats().add(raceMod.getStatModifiers());
      getStats().add(classMod.getStatModifiers());

      int vitality = Dice.roll(PlayerFacade.getStartingStats().getMinVitality(),
                               PlayerFacade.getStartingStats().getMaxVitality());
      vitality += raceMod.getVitality();
      vitality += classMod.getVitality();
      vitality += getStats().getStamina().getHpBonus();
      _vitality.setCurVitality(vitality);
      _vitality.setMaxVitality(vitality);

      int raceGold = Dice.roll(raceMod.getMinStartingGold(), raceMod.getMaxStartingGold());
      int classGold = Dice.roll(classMod.getMinStartingGold(), classMod.getMaxStartingGold());

      _gold = raceGold + classGold;
      clearInventory();
      _hungerTicker = MAX_HUNGER;
      _thirstTicker = MAX_THIRST;

      _status = Status.HEALTHY;

      _restTicker = REROLL_REST_TIME;
      _combatTicker = 0;
      _experience = 0;
      _spellbook.getSpells().clear();

      if (_playerClass.equals(PlayerClass.ACOLYTE)
            || _playerClass.equals(PlayerClass.DRUID)
            || _playerClass.equals(PlayerClass.NECROLYTE)
            || _playerClass.equals(PlayerClass.SORCEROR)) {
         _mana.setMaxMana(_level * getPlayerClass().getManaIncreasePerLevel());
         _mana.setCurMana(_level * getPlayerClass().getManaIncreasePerLevel());

      }
   }

   /**
    * Gets the current weapon being wielded.
    * @return the wielded weapon.
    */
   public Weapon getWeapon() {
      return _weapon;
   }

   /**
    * Sets the wielded weapon.  This method has no error checking and the previous weapon
    * will be wiped out.
    * @param weapon the weapon to wield.
    */
   public void setWeapon(Weapon weapon) {
      _weapon = weapon;
   }

   /**
    * Gets the currently equipped armor.
    * @return the currently equipped armor.
    */
   public Armor getArmor() {
      return _armor;
   }

   /**
    * Equips the armor.  This method has no error checking and the previous armor
    * will be wiped out.
    * @param armor the armor to equip.
    */
   public void setArmor(Armor armor) {
      _armor = armor;
   }

   /**
    * Returns a string in the format of :
    *
    * 'Name' is a 'Charisma' 'Physique' 'Knowledge'.
    * 'Gender' has a 'complexion', 'eyes', 'color hair', 'style hair', 'length hair'.
    * 'Agility' 'Intellect'
    * 'Gender' is wearing 'armor', is armed with 'weapon' 'health'
    *
    * @return a String in the above format.
    */
   public String getDescription() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(_name);
      buffer.append(" is a ");
      buffer.append(_stats.getCharisma().getDescription());
      buffer.append(_stats.getPhysique().getDescription());
      
      String knowledgeString = MessageFormat.format(_stats.getKnowledge().getDescription(), _gender.getObjective().toLowerCase());
      buffer.append(knowledgeString);

      buffer.append("{3} has a ");
      buffer.append(_complexion.getDescription() + ", ");
      buffer.append(_eyeColor.getDescription() + ", and ");
      buffer.append(_hairColor.getDescription() + ", ");
      buffer.append(_hairStyle.getDescription() + ", ");
      buffer.append(_hairLength.getDescription() + ". ");

      String agilityString = MessageFormat.format(_stats.getAgility().getDescription(),
            _gender.getPronoun().toLowerCase());
      buffer.append(agilityString);

      String intellectString = MessageFormat.format(_stats.getIntellect().getDescription(),
            _gender.getNoun(),
            _gender.getPronoun().toLowerCase());
      buffer.append(intellectString);

      String wearingString = MessageFormat.format(TaMessageManager.INSP3.getMessage(), 
            _gender.getNoun(),
            _armor.getLongDescription(),
            _weapon.getLongDescription());
      buffer.append(wearingString);
      
      buffer.append(_vitality.getDescription(_gender));

      if (!_rune.equals(Rune.NONE)) {
         String runeString = MessageFormat.format(TaMessageManager.BMSG1.getMessage(),
               _rune.toString().toLowerCase(),
               _gender.getPronoun().toLowerCase());
         buffer.append(runeString);
      }

      String description = MessageFormat.format(buffer.toString(),
            _gender.getDescription().toLowerCase(),
            _race.getName().toLowerCase(),
            getPromotedClass().getName().toLowerCase(),
            _gender.getNoun());

      return GameUtil.formatStringTo80Columns(description);
   }

   /**
    * Gets the current playing state.
    * @return the playing state.
    */
   public PlayerStateEnum getState() {
      return _state;
   }

   /**
    * Sets the playing state.
    * @param state the playing state.
    */
   public void setState(PlayerStateEnum state) {
      _state = state;
   }

   /**
    * Called when this player dies.
    * @param restTicker the amount of time the player must rest.
    */
   public void handleDeath(int restTicker) {
      _hungerTicker = MAX_HUNGER;
      _thirstTicker = MAX_THIRST;
      _vitality.setCurVitality(_vitality.getMaxVitality());
      _restTicker = restTicker;
      setStatus(Status.HEALTHY);
      teleportToRoom(DEATH_ROOM_AWAKEN);

   }

   /**
    * Sets the game output.
    * @param output the game output.
    */
   public void setOutput(GameOutput output) {
      _output = output;
   }

   /**
    * Gets the disconnected flag of this player.  Set this to true to disconnect the connection
    * the next time the engine recieves the disconnect event.
    * @return true if this player is disconnected.
    */
   public boolean isDisconnected() {
      return _disconnected;
   }

   /**
    * Sets the disconnected flag of this player.  Set this to true to disconnect the connection
    * the next time the engine recieves the disconnect event.
    * @param disconnected the disconnected flag.
    */
   public void setDisconnected(boolean disconnected) {
      _disconnected = disconnected;
   }

   /**
    * Gets the total number of times the player has played a game of chance this level.
    * @return the total number of times the player has played a game of chance this level.
    */
   public int getGameOfChanceCount() {
      return _gameOfChanceCount;
   }

   /**
    * Sets the total number of times the player has played a game of chance this level.
    * @param gameOfChanceCount the total number of times the player has played a game of chance this level.
    */
   public void setGameOfChanceCount(int gameOfChanceCount) {
      _gameOfChanceCount = gameOfChanceCount;
   }

   /**
    * Saves the player.
    */
   public void save() {
      try {
         if (_name == null) {
            return;
         }

         if (_room != null) {
            _roomVnumSave = _room.getRoomNumber();
         } else {
            _roomVnumSave = 1;
         }

         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_DIR) + _name;
         FileOutputStream fos = new FileOutputStream(filename);
         GZIPOutputStream gzos = new GZIPOutputStream(fos);
         ObjectOutputStream out = new ObjectOutputStream(gzos);
         out.writeObject(this);
         out.flush();
         out.close();
      } catch (IOException e) {
         println("A problem occurred saving your character.  Contact an admin.");
         _log.fatal(e);
      }
   }

   /**
    * Cleans up any resources taken by this object.  This should be called when this player has logged.
    */
   public void cleanup() {
      if (getWeapon() != null) {
         getWeapon().destroy();
      }
      if (getArmor() != null) {
         getArmor().destroy();
      }
      for (Item item : getInventory()) {
         item.destroy();
      }
      for (Mob mob : WorldManager.getMobsInExistance()) {
         if (equals(mob.getChasing())) {
            mob.setChasing(null);
         }
         if (equals(mob.getTamedBy())) {
            mob.setTamedBy(null);
         }
      }
   }

   /**
    * Gets the vault balance.
    * @return the vault balance.
    */
   public int getVaultBalance() {
      return _vaultBalance;
   }

   /**
    * Sets the vault balance.
    * @param vaultBalance the vault balance.
    */
   public void setVaultBalance(int vaultBalance) {
      _vaultBalance = vaultBalance;
   }

   /**
    * Gets the promoted class of the player, if the player was promoted.  Otherwise,
    * return the base class.
    * @return the promoted class of the player, if the player was promoted.  Otherwise,
    * return the base class.
    */
   public PlayerClass getPromotedClass() {
      if (isPromoted()) {
         return getPlayerClass().getPromotedClass();
      } else {
         return getPlayerClass();
      }
   }

   /**
    * Gets the promoted equivalent level.
    * @return the promoted equivalent level.
    */
   public int getPromotedLevel() {
      if (isPromoted()) {
         return getLevel() - HandlePromotion.PROMOTION_LEVEL;
      } else {
         return getLevel();
      }
   }

   /**
    * Sets the last global chat time.
    * @param lastGlobalChat the last global chat time.
    */
   public void setLastGlobalChat(long lastGlobalChat) {
      _lastGlobalChat = lastGlobalChat;
   }

   /**
    * Gets the last global chat time.
    * @return the last global chat time.
    */
   public long getLastGlobalChat() {
      return _lastGlobalChat;
   }

   /**
    * Sets the global chat count.
    * @param globalChatCount the global chat count.
    */
   public void setGlobalChatCount(int globalChatCount) {
      _globalChatCount = globalChatCount;
   }

   /**
    * Gets the global chat count.
    * @return the global chat count.
    */
   public int getGlobalChatCount() {
      return _globalChatCount;
   }

   /**
    * This is a debug command.  This gets the sustenance ignore flag.
    * @return true if sustenance does not affect this player.
    */
   public boolean getIgnoreSustenance() {
      return _ignoreSustenance;
   }

   /**
    * This is a debug command.  This sets the player so that sustenance does not effect him.
    * @param ignoreSustenance The sustenance ignore flag.
    */
   public void setIgnoreSustenance(boolean ignoreSustenance) {
      _ignoreSustenance = ignoreSustenance;
   }

   /**
    * This method clears the players inventory of all objects.  All resources should
    * be cleaned up within this call.
    */
   public void clearInventory() {
      List<Item> itemsToDestroy = new ArrayList<Item>();
      for (Item item : _inventory) {
         itemsToDestroy.add(item);
      }

      for (Item item : itemsToDestroy) {
         _inventory.remove(item);
         item.destroy();
         item = null;
      }

      if (_weapon != null) {
         _weapon.destroy();         
      }

      if (_armor != null) {
         _armor.destroy();
      }

      PlayerClassData classMod = PlayerFacade.getStartingStats().getPlayerClassDatabase().getPlayerClassData(getPlayerClass());
      _weapon = WorldManager.getWeapon(classMod.getWeapon());
      _armor = WorldManager.getArmor(classMod.getArmor());
      
      _spellbook.clearSpells();
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
      _name = name;
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
      return 1f;
   }

   /**
    * Takes the given amount of damage.
    * @param amount the amount of damage taken.
    * @return true if the entity's health goes below 0.
    */
   public boolean takeDamage(int amount) {
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
      _output.println(str);
   }

   /**
    * Sends a message to the entity without the carriage return.
    * @param str the message to send.
    */
   public void print(String str) {
      _output.print(str);
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
      return 70;
   }

   /**
    * Gets the entity type.
    * @return the entity type.
    */
   public EntityType getEntityType() {
      return EntityType.PLAYER;
   }

   /**
    * Gets the armor rating.
    * @return the armor rating.
    */
   public int getArmorRating() {
      if (getMagicProtection() > 0) {
         return getMagicProtection();
      } else {
         return _armor.getArmorRating() + _weapon.getArmorRating();
      }
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
    * @param roomNumber the room number.
    */
   public void setRoom(int roomNumber) {
      _room = WorldManager.getRoom(roomNumber);
   }

   /**
    * Teleports this player to the specified room, ignoring all triggers.
    * @param toRoomNumber the room to teleport to.
    */
   public synchronized void teleportToRoomIgnoreTriggers(int toRoomNumber) {
      Room fromRoom = WorldManager.getRoom(_room.getRoomNumber());
      Room toRoom = WorldManager.getRoom(toRoomNumber);

      fromRoom.removePlayer(this);

      // Add to new room.
      _room = toRoom;
      toRoom.addPlayer(this);

      new DoDefault().execute(this, null);
   }

   /**
    * Moves the entity to this room.  No messages are displayed.
    * @param toRoomNumber the room number to move the entity to.
    * @return a TriggerResult, since teleporting an entity can still trigger a trigger.
    */
   public synchronized TriggerResult teleportToRoom(int toRoomNumber) {
      Room fromRoom = WorldManager.getRoom(_room.getRoomNumber());
      Room toRoom = WorldManager.getRoom(toRoomNumber);

      fromRoom.removePlayer(this);

      // Add to new room.
      _room = toRoom;
      toRoom.addPlayer(this);

      new DoDefault().execute(this, null);

      return toRoom.handleTriggers(this);
   }

   /**
    * Move the entity to this room.
    * @param toRoomNumber the destination room number.
    * @param ignoreBarrier true if any barriers can be ignored.
    * @return a move fail code.
    */
   public synchronized MoveFailCode moveToRoom(int toRoomNumber, boolean ignoreBarrier) {
      Room fromRoom = WorldManager.getRoom(_room.getRoomNumber());
      Room toRoom = WorldManager.getRoom(toRoomNumber);

      if (toRoom == null) {
         _log.error(_name + " attempted to move into null room " + toRoomNumber
               + " from " + _room.getRoomNumber());
         return MoveFailCode.NO_EXIT;
      }

      MoveFailCode code = MoveFailCode.NONE;
      Exit exit = fromRoom.getExit(toRoomNumber);
      if (exit == null) {
         _log.error(_name + " attempted to move through a null exit "
               + toRoomNumber + " from " + _room.getRoomNumber());
         return MoveFailCode.NO_EXIT;
      }
      // Handle barriers.
      if (!ignoreBarrier) {
         code = handleBarrier(fromRoom, exit);
      }

      if (code != MoveFailCode.NONE
            && code != MoveFailCode.PICKED_BARRIER
            && code != MoveFailCode.UNLOCK_BARRIER) {
         return code;
      }

      // Update the tracking information of the room.
      getTrackingMap().put(fromRoom, exit.getExitDirection());

      fromRoom.removePlayer(this);

      // Add to new room.
      _room = toRoom;
      toRoom.addPlayer(this);

      return code;
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
      return _hungerTicker;
   }

   /**
    * Increases the hunger ticker by the specified amount.
    * @param value the amount to increase the hunger ticker.
    */
   public void addHungerTicker(int value) {
      _hungerTicker += value;
      if (_hungerTicker > MAX_HUNGER) {
         _hungerTicker = MAX_HUNGER;
      }

      restoreHungerStatus();
   }

   /**
    * Sets the hunger ticker by the specified amount.
    * @param value the amount to set the hunger ticker.
    */
   public void setHungerTicker(int value){
      _hungerTicker = value;
      if (_hungerTicker > MAX_HUNGER) {
         _hungerTicker = MAX_HUNGER;
      }

      restoreHungerStatus();      
   }

   /**
    * Gets the thirst ticker.  If this value is below zero, the entity will be thirsty.
    * @return the thirst ticker.
    */
   public int getThirstTicker() {
      return _thirstTicker;
   }

   /**
    * Increases the thirst ticker by the specified amount.
    * @param value the amount to increase the thirst ticker.
    */
   public void addThirstTicker(int value) {
      _thirstTicker += value;
      if (_thirstTicker > MAX_THIRST) {
         _thirstTicker = MAX_THIRST;
      }
      
      restoreThirstStatus();
   }

   /**
    * Sets the thirst ticker by the specified amount.
    * @param value the amount to set the thirst ticker.
    */
   public void setThirstTicker(int value) {
      _thirstTicker = value;
      if (_thirstTicker > MAX_THIRST) {
         _thirstTicker = MAX_THIRST;
      }
      
      restoreThirstStatus();      
   }

   /**
    * Decreases both the hunger and thirst ticker and accordingly updates the entities status
    * if any of these tickers are reduced below 0.
    * @param value the amount to decrease the hunger and thirst ticker.
    */
   public void subtractSustenance(int value) {
      if (_ignoreSustenance) {
         return;
      }

      _hungerTicker -= value;
      _thirstTicker -= value;

      if (_hungerTicker <= 0) {
         _hungerTicker = 0;
         _status = Status.HUNGRY;
      }

      if (_thirstTicker <= 0) {
         _thirstTicker = 0;
         _status = Status.THIRSTY;
      }

      restoreHungerStatus();
      restoreThirstStatus();
   }

   /**
    * Increase both the hunger and thirst ticker by the specified amount.
    * @param value the amount to increase both the thirst and hunger ticker.
    */
   public void addSustenance(int value) {
      addThirstTicker(value);
      addHungerTicker(value);
   }

   /**
    * Gets the stats.
    * @return the stats.
    */
   public BaseStats getStats() {
      return _stats;
   }

   /**
    * Decreases the effect timers.
    * @return true if the effect timer hits 0.
    */
   public boolean decreaseEffectTimers() {
      boolean magicProtectionExpired = false;
      if (_magicProtectionTimer > 0) {
         _magicProtectionTimer--;
         if (_magicProtectionTimer <= 0) {
            _magicProtection = 0;
            magicProtectionExpired = true;
         }
      }

      boolean invisibilityExpired = false;
      if (_invisibilityTimer > 0) {
         _invisibilityTimer--;
         if (_invisibilityTimer <= 0) {
            invisibilityExpired = true;
         }
      }

      boolean statEffectsExpired = _stats.decreaseTimers();

      if (magicProtectionExpired || statEffectsExpired || invisibilityExpired) {
         return true;
      }
      return false;
   }

   /**
    * Sets the magic protection.
    * @param magicProtection the magic protection.
    */
   public void setMagicProtection(int magicProtection) {
      _magicProtection = magicProtection;
   }

   /**
    * Gets the magic protection.
    * @return the magic protection.
    */
   public int getMagicProtection() {
      return _magicProtection;
   }

   /**
    * Sets the magic protection timer.
    * @param magicProtectionTimer the magic protection timer value.
    */
   public void setMagicProtectionTimer(int magicProtectionTimer) {
      _magicProtectionTimer = magicProtectionTimer;
   }

   /**
    * Sets the invisibility timer.
    * @param invisibilityTimer the invisibility timer value.
    */
   public void setInvisibiltyTimer(int invisibilityTimer) {
      _invisibilityTimer = invisibilityTimer;
   }

   /**
    * Checks if this entity is invisible.
    * @return true if this entity is invisible.
    */
   public boolean isInvisible() {
      if (_invisibilityTimer > 0) {
         return true;
      }
      return false;
   }

   /**
    * Gets the resting string.  This is currently used for the DoOrder command.
    * @return the resting string.
    */
   public String getRestingString() {
      if (isMentallyExhausted() || isResting()) {
         return MessageManager.RESTING_STRING.getMessage();
      } else {
         return MessageManager.READY_STRING.getMessage();
      }
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
      return _isFollowingGroup;
   }

   /**
    * Sets the is following flag.
    * @param isFollowingGroup true if this entity is in a group.
    */
   public void setFollowingGroup(boolean isFollowingGroup) {
      _isFollowingGroup = isFollowingGroup;
   }

   /**
    * Checks if this entity is in the middle of attacking.
    * @return true if this entity is in the middle of attacking.
    */
   public boolean isAttacking() {
      if (_combatTicker > 0) {
         return true;
      }
      return false;
   }

   /**
    * Checks if this entity is resting.
    * @return true if this entity is resting.
    */
   public boolean isResting() {
      if (_restTicker > 0) {
         return true;
      }
      return false;
   }

   /**
    * Checks if this entity is mentally exhausted.
    * @return true if this entity is mentally exhausted.
    */
   public boolean isMentallyExhausted() {
      if (_mentalExhaustionTicker > 0) {
         return true;
      }
      return false;
   }

   /**
    * The amount of gold being carried.
    * @return the amount of gold being carried.
    */
   public int getGold() {
      return _gold;
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
    * Checks if this entity has a lit torch.
    * @return true if a lit torch is being carried.
    */
   public boolean hasLitTorch() {
      for (Item item : _inventory) {
         if (item.getItemType().equals(ItemType.EQUIPMENT)) {
            Equipment eq = (Equipment) item;
            if (eq.getEquipmentSubType().equals(EquipmentSubType.LIGHT)) {
               if (eq.isActivated()) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /**
    * Checks if this entity has light.
    * @return true if this entity has light.
    */
   public boolean hasLight() {
      for (Item item : _inventory) {
         if (item.getItemType().equals(ItemType.EQUIPMENT)) {
            Equipment eq = (Equipment) item;
            if (eq.getEquipmentSubType().equals(EquipmentSubType.LIGHT)) {
               if (eq.isActivated()) {
                  return true;
               }
            } else if (eq.getEquipmentSubType().equals(EquipmentSubType.ETERNAL_LIGHT)) {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * This removes an item of the same vnum from the inventory.
    *
    * @param vnum The vnum of the item to remove.
    * @return true if the item was removed, false otherwise.
    */
   public synchronized boolean removeItemFromInventory(int vnum) {
      Item itemToRemove = null;
      for (Item item : _inventory) {
         if (item.getVnum() == vnum) {
            itemToRemove = item;
            break;
         }
      }
      return _inventory.remove(itemToRemove);
   }

   /**
    * This removes the exact item from the inventory. That is, the object has to
    * be of the same instance.
    *
    * @param item The item to remove.
    * @return true if the item was removed, false otherwise.
    */
   public synchronized boolean removeItemFromInventory(Item item) {
      return _inventory.remove(item);
   }

   /**
    * Places an item into the entities inventory.
    * @param item the item to place.
    * @param ignoreWeight set to true if the item should ignore weight constraints.
    * @return an InventoryFailCode.
    */
   public synchronized InventoryFailCode placeItemInInventory(Item item, boolean ignoreWeight) {
      if (item.getItemType().equals(ItemType.RUNE_SCROLL)) {
         return handleRunestaff((RuneScroll) item);
      }

      int maxInventorySize = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_INVENTORY_SIZE));
      if (_inventory.size() >= maxInventorySize) {
         return InventoryFailCode.SPACE;
      }

      if (!ignoreWeight) {
         int modifiedEncumbrance = _encumbrance.getCurEncumbrance() + item.getWeight();
         if (modifiedEncumbrance > _encumbrance.getMaxEncumbrance()) {
            return InventoryFailCode.ENCUMBRANCE;
         }
      }

      _inventory.add(item);
      return InventoryFailCode.NONE;
   }

   /**
    * Gets the entire inventory list.
    * @return the inventory list.
    */
   public ArrayList<Item> getInventory() {
      return _inventory;
   }

   /**
    * Gets the player class.
    * @return the player class.
    */
   public PlayerClass getPlayerClass() {
      return _playerClass;
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
      return _trackingMap;
   }

   /**
    * Checks if this entity is similar to the specified entity.
    * @param entity the entity to compare.
    * @return true if this entity is similar to the specified entity.
    */
   public boolean isSimilar(Entity entity) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return false;
      }

      Player player = (Player) entity;
      if (player.equals(this)) {
         return true;
      }
      return false;
   }

   /**
    * Gets the spell book.
    * @return the spell book.
    */
   public Spellbook getSpellbook() {
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
      if (_mentalExhaustionTicker > 0) {
         _mentalExhaustionTicker -= 1;
      }
   }

   /**
    * Sets the mental exhaustion ticker.  If this value is above 0, then the entity is
    * mentally exhausted.
    * @param value the mental exhaustion ticker.
    */
   public void setMentalExhaustionTicker(int value) {
      _mentalExhaustionTicker = value;
   }

   /**
    * Adds the amount of experience.
    * @param experience the amount of experience to add.
    */
   public void addExperience(long experience) {
      int maxExp = getPromotedClass().getExpRequirement(getPromotedLevel() + MAX_TRAILING_LEVEL, isPromoted()) - 1;
      _experience += experience;
      if (_experience > maxExp) {
         _experience = maxExp;
      }
   }

   /**
    * Gets the attacks.
    * @return the attacks.
    */
   public Attacks getAttacks() {
      return _attacks;
   }

   /**
    * Decreases the rest ticker.
    */
   public void decreaseRestTicker() {
      if (_restTicker > 0) {
         _restTicker -= 1;
      }
   }

   /**
    * Sets the rest ticker.  If this number is greater than 0, then the
    * entity is resting.
    * @param value the rest ticker value.
    */
   public void setRestTicker(int value) {
      _restTicker = value;
   }

   /**
    * Sets the combat ticker.  If this number is greater than 0, then the entity
    * has attacked recently and is considered in combat.
    * @param value the combat ticker.
    */
   public void setCombatTicker(int value) {
      _combatTicker = value;
   }

   /**
    * Gets the spell hit difficulty for this entity.
    * @return the spell hit difficulty for this entity.
    */
   public int getSpellHitDifficulty() {
      return PLAYER_SPELL_HIT_DIFFICULTY;
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
      return _dragging;
   }

   /**
    * Sets the dragging player.
    * @param dragging the dragging player.
    */
   public void setDragging(Player dragging) {
      _dragging = dragging;
   }

   /**
    * Gets the player that this entity is being dragged by.
    * @return the player that this entity is being dragged by.
    */
   public Player getDraggedBy() {
      return _draggedBy;
   }

   /**
    * Sets the player that this entity is being dragged by.
    * @param draggedBy the player that this entity is being dragged by.
    */
   public void setDraggedBy(Player draggedBy) {
      _draggedBy = draggedBy;
   }

   /**
    * Gets the last time this entity has moved.
    * @return the last time this entity has moved.
    */
   public long getLastMove() {
      return _lastMove;
   }

   /**
    * Sets the last time this entity has moved.
    * @param lastMove the last time this entity has moved.
    */
   public void setLastMove(long lastMove) {
      _lastMove = lastMove;
   }

   /**
    * This checks if the entity is currently tripping.
    * @return true if this entity is currently tripping.
    */
   public boolean isTripping() {
      return _isTripping;
   }

   /**
    * This sets the entity into a tripping state.
    * @param isTripping the tripping flag.
    */
   public void setIsTripping(boolean isTripping) {
      _isTripping = isTripping;
   }

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @return true if this entity can ignore the tripping mechanism when moving too fast.
    */
   public boolean getIgnoreTrip() {
      return _ignoreTrip;
   }

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @param ignoreTrip the ignore trip flag.
    */
   public void setIgnoreTrip(boolean ignoreTrip) {
      _ignoreTrip = ignoreTrip;
   }

   /**
    * Decrease the combat ticker.
    */
   public void decreaseCombatTicker() {
      if (_combatTicker > 0) {
         _combatTicker -= 1;
      }
   }

   /**
    * Checks if this player is promoted.
    * @return true if this player is promoted.
    */
   public boolean isPromoted() {
      return _promoted;
   }

   /**
    * Sets the promoted flag of this player.
    * @param promoted the promoted flag.
    */
   public void setPromoted(boolean promoted) {
      _promoted = promoted;
   }

   /**
    * Gets the debug string for this entity.
    * @return the debug string for this entity.
    */
   public String getDebugString() {
      StringBuffer buffer = new StringBuffer();

      try {
         Formatter formatter = new Formatter(buffer, Locale.US);
         formatter.format("&CName:&W%s  &CPNum:&W%d  &CSysop:&W%s  &CState:&W%s\n",
               _name, _pnum, _isSysop, _state);
         formatter.format("&CClass:&W%s  &CRace:&W%s  &CGender:&W%s  &CPromoted:&W%s\n",
               _playerClass, _race, _gender, isPromoted());
         formatter.format("&CLevel:&W%d  &CExperience:&W%d  &CRune:&W%s\n",
               _level, _experience, _rune);

         formatter.format("%s %s %s\n", getStatDebugString("Int", _stats.getIntellect()),
               getStatDebugString("Kno", _stats.getKnowledge()), getStatDebugString("Phy", _stats.getPhysique()));
         formatter.format("%s %s %s\n", getStatDebugString("Sta", _stats.getStamina()),
               getStatDebugString("Agi", _stats.getAgility()), getStatDebugString("Cha", _stats.getCharisma()));

         formatter.format("&CVitality:&W%d&C/&W%d  &CDrained:&W%d  &CMana:&W%d&C/&W%d  &CStatus:&W%s  &CPoison:&W%d\n",
               _vitality.getCurVitality(), _vitality.getMaxVitality(), _vitality.getDrained(),
               _mana.getCurMana(), _mana.getMaxMana(), _status, _poisonDamage);
         formatter.format("&CComplexion:&W%s  &CEye Color:&W%s\n",
               _complexion, _eyeColor);
         formatter.format("&CHair Color:&W%s  &CHair Length:&W%s  &CHair Style:&W%s\n",
               _hairColor, _hairLength, _hairStyle);
         formatter.format("&CWeapon:&W%s  &CArmor:&W%s  &CAttacks:&W%d&C/&W%d\n",
               _weapon.getName(), _armor.getName(), _attacks.getAttacksLeft(), _attacks.getMaxAttacks());
         formatter.format("&CEncumbrance:&W%d&C/&W%d  &CGold:&W%d  &CVault:&W%d\n",
               _encumbrance.getCurEncumbrance(), _encumbrance.getMaxEncumbrance(), _gold, _vaultBalance);
         formatter.format("&CInventory:&W%s\n", _inventory);
         formatter.format("&CSpells:&W%s\n", _spellbook.getSpells());
         formatter.format("&CGame of Chance Count:&W%s\n", _gameOfChanceCount);
         formatter.format("&CRest:&W%d  &CCombat:&W%d  &CMental:&W%d  &CHunger:&W%d  &CThirst:&W%d\n",
               _restTicker, _combatTicker, _mentalExhaustionTicker, _hungerTicker, _thirstTicker);
         formatter.format("&CRegeneration Ticker:&W%d  &CMagic Protection Timer:&W%d  &CMagic Protection:&W%d\n",
               _regenerationTicker, _magicProtectionTimer, _magicProtection);
         formatter.format("&CInvisibility Timer:&W%d  &CParalysis Timer:&W%d\n",
               _invisibilityTimer, _paralysisTimer);
         formatter.format("&CLast Global Chat:&W%s  &CGlobal Chat Count:&W%d\n",
               new Date(_lastGlobalChat), _globalChatCount);
         formatter.format("&CLast Move:&W%s  &CIs Tripping:&W%s  &CIgnore Trip:&W%s\n",
               new Date(_lastMove), _isTripping, _ignoreTrip);
         if (_room != null) {
            formatter.format("&CRoom:&W%d&C(&W%s&C)\n",
                  _room.getRoomNumber(), _room.getShortDescription());
         }
         formatter.format("&CGroup Leader:&W%s  &CIs Following Group:&W%s\n",
               _groupLeader, _isFollowingGroup);
         formatter.format("&CGroup List:&W%s\n", _groupList);
         formatter.format("&CDragging:&W%s  &CDragged By:&W%s\n", _dragging, _draggedBy);
         formatter.format("&CTracking Map Size:&W%d  &CDisconnected:&W%s\n", _trackingMap.size(), _disconnected);
         formatter.format("&CGameOutput:&W%s\n", _output);
         formatter.format("&CMessage Handler:&W%s\n", _messageHandler);
      } catch (Exception e) {
         buffer.append("&RGot the following exception creating the DefaultPlayer debug string:\n");
         buffer.append("&R" + e.getMessage());
         _log.error(e);
      }

      return buffer.toString();
   }

   //**********
   // Methods overridden from Object.
   //**********

   /**
    * Gets the String representation of this object.
    * @return the String representation of this object.
    */
   public String toString() {
      return _name;
   }

   /**
    * This is the overridden readResolve method that initializes transient objects.
    * @return the instance of this class.
    * @throws ObjectStreamException if something goes wrong during the readResolve.
    */
   private Object readResolve() throws ObjectStreamException {
      _room = WorldManager.getRoom(_roomVnumSave);
      _attacks = new DefaultAttacks(this);
      _groupLeader = this;
      _groupList = new ArrayList<Entity>();
      _trackingMap = new HashMap<Room, ExitDirectionEnum>();
      _messageHandler = new PlayerMessageHandler(this);

      // Hack to fix mana for players who are already playing.
      if (_playerClass.isSpellCaster()) {
         int maxMana = _level * getPlayerClass().getManaIncreasePerLevel();
         if (_mana.getMaxMana() < maxMana) {
            _mana.setMaxMana(maxMana);         
         }         
      }
      return this;
   }

   //**********
   // Private methods.
   //**********

   /**
    * Adds a scroll to the runestaff.
    * @param runeScroll the RuneScroll.
    * @return an InventoryFailCode.
    */
   private InventoryFailCode handleRunestaff(RuneScroll runeScroll) {
      Equipment runestaff = null;
      InventoryFailCode code = InventoryFailCode.RUNE_SCROLL_DUD;
      for (Item item : _inventory) {
         if (GameUtil.isRunestaff(item)) {
            runestaff = (Equipment) item;
            break;
         }
      }

      if (runestaff != null) {
         if (runeScroll.getRuneNumber() > runestaff.getCharges()) {
            runestaff.subractCharge();
            code = InventoryFailCode.RUNE_SCROLL;
         }
      }

      runeScroll.destroy();
      return code;
   }

   /**
    * Handles movement into a barrier.
    * @param fromRoom the source room.
    * @param exit the exit.
    * @return a MoveFailCode.
    */
   private MoveFailCode handleBarrier(Room fromRoom, Exit exit) {
      if (exit.getDoor() != null && !exit.getDoor().isUnlocked()) {
         return exit.getDoor().getMoveFailCode(this);
      } else {
         return MoveFailCode.NONE;
      }
   }

   /**
    * Checks if the player has more gold than is allowed.
    * @return the amount of gold that went over the max.
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
    * Gets the stat as a string used for debugging purposes.
    * @param str the string representation of the stat to display.
    * @param stat the stat.
    * @return the stat as a string used for debugging purposes.
    */
   private String getStatDebugString(String str, Stat stat) {
      StringBuffer buffer = new StringBuffer();

      Formatter formatter = new Formatter(buffer, Locale.US);
      formatter.format("&C%s:&W%d&C/&W%d&C(+&W%d&C:&W%d&C/-&W%d&C:&W%d&C)",
            str, stat.getValue(), stat.getModifiedStat(),
            stat.getBoost(), stat.getBoostTimer(),
            stat.getDrain(), stat.getDrainTimer());

      return buffer.toString();
   }

   /**
    * Resets the hunger status if the player is no longer hungry.
    */
   private void restoreHungerStatus() {
      if (_hungerTicker > 0) {
         if (_status.equals(Status.HUNGRY)) {
            _status = Status.HEALTHY;
         }
      }
   }

   /**
    * Resets the thirst status if the player is no longer hungry.
    */
   private void restoreThirstStatus() {
      if (_thirstTicker > 0) {
         if (_status.equals(Status.THIRSTY)) {
            _status = Status.HEALTHY;
         }
      }      
   }
}
