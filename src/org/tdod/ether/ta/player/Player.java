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

package org.tdod.ether.ta.player;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.enums.Complexion;
import org.tdod.ether.ta.player.enums.EyeColor;
import org.tdod.ether.ta.player.enums.HairColor;
import org.tdod.ether.ta.player.enums.HairLength;
import org.tdod.ether.ta.player.enums.HairStyle;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.ta.player.enums.RaceEnum;
import org.tdod.ether.ta.player.enums.Rune;

/**
 * An interface defining the common API of a player.
 * @author Ron Kinney
 */
public interface Player extends Entity {

   /**
    * Sets the players password.
    * @param password the players password.
    */
   void setPassword(String password);

   /**
    * Gets the players password.
    * @return the players password.
    */
   String getPassword();

   /**
    * Checks if this player is a sysop.
    * @return true if this player is a sysop.
    */
   boolean isSysop();

   /**
    * Sets the sysop flag.
    * @param isAdmin the sysop flag.
    */
   void setIsSysop(boolean isAdmin);

   /**
    * Gets the players unique number.
    * @return the players unique number.
    */
   int getPnum();

   /**
    * Sets the players unique number.
    * @param pnum the players unique number.
    */
   void setPnum(int pnum);

   /**
    * Gets the race.
    * @return the race.
    */
   RaceEnum getRace();

   /**
    * Sets the race.
    * @param race the race.
    */
   void setRace(RaceEnum race);

   /**
    * Gets the complexion.
    * @return the complexion.
    */
   Complexion getComplexion();

   /**
    * Sets the complexion.
    * @param complexion the complexion.
    */
   void setComplexion(Complexion complexion);

   /**
    * Gets the eye color.
    * @return the eye color.
    */
   EyeColor getEyeColor();

   /**
    * Sets the eye color.
    * @param eyeColor the eye color.
    */
   void setEyeColor(EyeColor eyeColor);

   /**
    * Sets the hair color.
    * @return the hair color.
    */
   HairColor getHairColor();

   /**
    * Sets the hair color.
    * @param hairColor the hair color.
    */
   void setHairColor(HairColor hairColor);

   /**
    * Gets the hair color.
    * @return the hair color.
    */
   HairLength getHairLength();

   /**
    * Sets the hair length.
    * @param hairLength the hair length.
    */
   void setHairLength(HairLength hairLength);

   /**
    * Sets the hair style.
    * @return the hair style.
    */
   HairStyle getHairStyle();

   /**
    * Sets the hair style.
    * @param hairStyle the hair style.
    */
   void setHairStyle(HairStyle hairStyle);

   /**
    * Sets the players class.
    * @param playerClass the players class.
    */
   void setPlayerClass(PlayerClass playerClass);

   /**
    * Gets the experience.
    * @return the experience.
    */
   long getExperience();

   /**
    * Sets the experience.
    * @param experience the experience.
    */
   void setExperience(long experience);

   /**
    * Gets the rune.
    * @return the rune.
    */
   Rune getRune();

   /**
    * Sets the rune.
    * @param rune the rune.
    */
   void setRune(Rune rune);

   /**
    * Gets the encumbrance.
    * @return the encumbrance.
    */
   Encumbrance getEncumbrance();

   /**
    * Gets an item in the players inventory based on the full or partial string.  The first
    * match is returned or null if one is not found.
    * @param str the full or partial string name.
    * @return an item, or null.
    */
   Item getItem(String str);

   /**
    * Sets every variable for the player back to level one.  This should only be called when
    * the player has just been created.
    */
   void setDefaults();

   /**
    * Rerolls the players stats.
    */
   void rerollStats();

   /**
    * Gets the current weapon being wielded.
    * @return the wielded weapon.
    */
   Weapon getWeapon();

   /**
    * Sets the wielded weapon.  This method has no error checking and the previous weapon
    * will be wiped out.
    * @param weapon the weapon to wield.
    */
   void setWeapon(Weapon weapon);

   /**
    * Gets the currently equipped armor.
    * @return the currently equipped armor.
    */
   Armor getArmor();

   /**
    * Equips the armor.  This method has no error checking and the previous armor
    * will be wiped out.
    * @param armor the armor to equip.
    */
   void setArmor(Armor armor);

   /**
    * Gets the description.
    * @return the description.
    */
   String getDescription();

   /**
    * Gets the current playing state.
    * @return the playing state.
    */
   PlayerStateEnum getState();

   /**
    * Sets the playing state.
    * @param state the playing state.
    */
   void setState(PlayerStateEnum state);

   /**
    * Called when this player dies.
    * @param restTicker the amount of time the player must rest.
    */
   void handleDeath(int restTicker);

   /**
    * Sets the game output.
    * @param output the game output.
    */
   void setOutput(GameOutput output);

   /**
    * Gets the disconnected flag of this player.  Set this to true to disconnect the connection
    * the next time the engine recieves the disconnect event.
    * @return true if this player is disconnected.
    */
   boolean isDisconnected();

   /**
    * Sets the disconnected flag of this player.  Set this to true to disconnect the connection
    * the next time the engine recieves the disconnect event.
    * @param disconnected the disconnected flag.
    */
   void setDisconnected(boolean disconnected);

   /**
    * Gets the total number of times the player has played a game of chance this level.
    * @return the total number of times the player has played a game of chance this level.
    */
   int getGameOfChanceCount();

   /**
    * Sets the total number of times the player has played a game of chance this level.
    * @param gameOfChanceCount the total number of times the player has played a game of chance this level.
    */
   void setGameOfChanceCount(int gameOfChanceCount);

   /**
    * Saves the player.
    */
   void save();

   /**
    * Cleans up any resources taken by this object.  This should be called when this player has logged.
    */
   void cleanup();

   /**
    * Gets the vault balance.
    * @return the vault balance.
    */
   int getVaultBalance();

   /**
    * Sets the vault balance.
    * @param vaultBalance the vault balance.
    */
   void setVaultBalance(int vaultBalance);

   /**
    * Checks if this player is promoted.
    * @return true if this player is promoted.
    */
   boolean isPromoted();

   /**
    * Sets the promoted flag of this player.
    * @param promoted the promoted flag.
    */
   void setPromoted(boolean promoted);

   /**
    * Gets the promoted class of the player, if the player was promoted.  Otherwise,
    * return the base class.
    * @return the promoted class of the player, if the player was promoted.  Otherwise,
    * return the base class.
    */
   PlayerClass getPromotedClass();

   /**
    * Gets the promoted equivalent level.
    * @return the promoted equivalent level.
    */
   int getPromotedLevel();

   /**
    * Sets the last global chat time.
    * @param lastGlobalChat the last global chat time.
    */
   void setLastGlobalChat(long lastGlobalChat);

   /**
    * Gets the last global chat time.
    * @return the last global chat time.
    */
   long getLastGlobalChat();

   /**
    * Sets the global chat count.
    * @param globalChatCount the global chat count.
    */
   void setGlobalChatCount(int globalChatCount);

   /**
    * Gets the global chat count.
    * @return the global chat count.
    */
   int getGlobalChatCount();

   /**
    * This is a debug command.  This gets the sustenance ignore flag.
    * @return true if sustenance does not affect this player.
    */
   boolean getIgnoreSustenance();

   /**
    * This is a debug command.  This sets the player so that sustenance does not effect him.
    * @param ignoreSustenance The sustenance ignore flag.
    */
   void setIgnoreSustenance(boolean ignoreSustenance);

   /**
    * This method clears the players inventory of all objects.  All resources should
    * be cleaned up within this call.
    */
   public void clearInventory();

}
