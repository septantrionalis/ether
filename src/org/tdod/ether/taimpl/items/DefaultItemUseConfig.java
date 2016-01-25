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

package org.tdod.ether.taimpl.items;

import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.ItemUseConfig;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.util.GameUtil;

/**
 * This is the default implementation class of an ItemUseConfig.
 * @author Ron Kinney
 */
public class DefaultItemUseConfig implements ItemUseConfig {

   private int     _damage;
   private boolean _notUseable;
   private String  _playerMessage;
   private String  _roomMessage;
   private String  _victimMessage;
   private boolean _burnsCharges;
   private boolean _isRunestaff;
   private boolean _isRodOfPower;
   private boolean _isManastone;
   private boolean _isHeartstone;

   /**
    * Creates a new DefaultItemUseConfig.
    */
   public DefaultItemUseConfig() {
   }

   /**
    * Sets the amount of damage done.
    * @param damage the amount of damage done.
    */
   public void setDamage(int damage) {
      _damage = damage;
   }

   /**
    * Gets the amount of damage done.
    * @return the amount of damage done.
    */
   public int getDamage() {
      return _damage;
   }

   /**
    * Returns true if this item is not useable.
    * @return true if this item is not useable.
    */
   public boolean isNotUseable() {
      return _notUseable;
   }

   /**
    * Sets the useable flag of this item.
    * @param notUseable the useable flag of this item.
    */
   public void setNotUseable(boolean notUseable) {
      _notUseable = notUseable;
   }

   /**
    * Gets the message sent to the player when this item is used.
    * @return the message sent to the player when this item is used.
    */
   public String getPlayerMessage() {
      return _playerMessage;
   }

   /**
    * Sets the message sent to the player when this item is used.
    * @param playerMessage the message sent to the player when this item is used.
    */
   public void setPlayerMessage(String playerMessage) {
      _playerMessage = playerMessage;
   }

   /**
    * Gets the message sent to the room when this item is used.
    * @return the message sent to the room when this item is used.
    */
   public String getRoomMessage() {
      return _roomMessage;
   }

   /**
    * Sets the message sent to the room when this item is used.
    * @param roomMessage the message sent to the room when this item is used.
    */
   public void setRoomMessage(String roomMessage) {
      _roomMessage = roomMessage;
   }

   /**
    * Gets the message sent to the victim when this item is used on him.
    * @return the message sent to the victim when this item is used on him.
    */
   public String getVictimMessage() {
      return _victimMessage;
   }

   /**
    * Sets the message sent to the victim when this item is used on him.
    * @param victimMessage the message sent to the victim when this item is used on him.
    */
   public void setVictimMessage(String victimMessage) {
      _victimMessage = victimMessage;
   }

   /**
    * Returns true if this item burns charges when used.
    * @return true if this item burns charges when used.
    */
   public boolean isBurnsCharges() {
      return _burnsCharges;
   }

   /**
    * Sets the item to burn charges when used.
    * @param burnsCharges true if the item should burn charges when used.
    */
   public void setBurnsCharges(boolean burnsCharges) {
      _burnsCharges = burnsCharges;
   }

   /**
    * Checks if this item is a runestaff.
    * @return true if this item is a runestaff.
    */
   public boolean isRunestaff() {
      return _isRunestaff;
   }

   /**
    * Turns this item into a runestaff.
    * @param isRunestaff true if this item is a runestaff.
    */
   public void setIsRunestaff(boolean isRunestaff) {
      _isRunestaff = isRunestaff;
   }

   /**
    * Checks if this item is a rod of power.
    * @return true if this item is a rod of power.
    */
   public boolean isRodOfPower() {
      return _isRodOfPower;
   }

   /**
    * Sets if this item is a rod of power.
    * @param isRodOfPower true if this item is a rod of power.
    */
   public void setIsRodOfPower(boolean isRodOfPower) {
      _isRodOfPower = isRodOfPower;
   }

   /**
    * Checks if this item is a manastone.
    * @return true if this item is a manastone.
    */
   public boolean isManastone() {
      return _isManastone;
   }

   /**
    * Sets if this item is a manastone.
    * @param isManastone true if this item is a manastone.
    */
   public void setIsManastone(boolean isManastone) {
      _isManastone = isManastone;
   }

   /**
    * Checks if this item is a heartstone.
    * @return true if this item is a heartstone.
    */
   public boolean isHeartstone() {
      return _isHeartstone;
   }
   
   /**
    * Sets if this item is a heartstone.
    * @param isHeartstone true if this item is a heartstone.
    */
   public void setIsHeartstone(boolean isHeartstone) {
      _isHeartstone = isHeartstone;
   }

   /**
    * Sets various attributes of this use configuration object.
    * @param item the item.
    */
   public void setUseConfigAttributes(Item item) {
      if (item.getItemType().equals(ItemType.EQUIPMENT)) {
         Equipment eq = (Equipment) item;
         setIsHeartstone(GameUtil.isHeartstone(eq));
         setIsManastone(GameUtil.isManastone(eq));
         setIsRunestaff(GameUtil.isRunestaff(eq));
         setIsRodOfPower(GameUtil.isRodOfPower(eq));
      }
   }
 
   /**
    * Checks if this is a non-combat item.
    * @return true if this is a noncombat item.
    */
   public boolean isNonCombatItem() {
      if (isHeartstone() ||
            isManastone() ||
            isRunestaff() ||
            isRodOfPower()) {
         return true;
      }

      return false;
   }
}
