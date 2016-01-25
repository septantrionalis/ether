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

package org.tdod.ether.ta.items;


/**
 * This is an interface defining the configuration of an item when used by a
 * player.
 *
 * @author Ron Kinney
 */
public interface ItemUseConfig {

   /**
    * Sets the amount of damage done.
    * @param damage the amount of damage done.
    */
   void setDamage(int damage);

   /**
    * Gets the amount of damage done.
    * @return the amount of damage done.
    */
   int getDamage();

   /**
    * Returns true if this item is not useable.
    * @return true if this item is not useable.
    */
   boolean isNotUseable();

   /**
    * Sets the useable flag of this item.
    * @param notUseable the useable flag of this item.
    */
   void setNotUseable(boolean notUseable);

   /**
    * Gets the message sent to the player when this item is used.
    * @return the message sent to the player when this item is used.
    */
   String getPlayerMessage();

   /**
    * Sets the message sent to the player when this item is used.
    * @param playerMessage the message sent to the player when this item is used.
    */
   void setPlayerMessage(String playerMessage);

   /**
    * Gets the message sent to the room when this item is used.
    * @return the message sent to the room when this item is used.
    */
   String getRoomMessage();

   /**
    * Sets the message sent to the room when this item is used.
    * @param roomMessage the message sent to the room when this item is used.
    */
   void setRoomMessage(String roomMessage);

   /**
    * Gets the message sent to the victim when this item is used on him.
    * @return the message sent to the victim when this item is used on him.
    */
   String getVictimMessage();

   /**
    * Sets the message sent to the victim when this item is used on him.
    * @param victimMessage the message sent to the victim when this item is used on him.
    */
   void setVictimMessage(String victimMessage);

   /**
    * Returns true if this item burns charges when used.
    * @return true if this item burns charges when used.
    */
   boolean isBurnsCharges();

   /**
    * Sets the item to burn charges when used.
    * @param burnsCharges true if the item should burn charges when used.
    */
   void setBurnsCharges(boolean burnsCharges);

   /**
    * Checks if this item is a runestaff.
    * @return true if this item is a runestaff.
    */
   boolean isRunestaff();

   /**
    * Turns this item into a runestaff.
    * @param isRunestaff true if this item is a runestaff.
    */
   void setIsRunestaff(boolean isRunestaff);

   /**
    * Checks if this item is a rod of power.
    * @return true if this item is a rod of power.
    */
   boolean isRodOfPower();

   /**
    * Sets if this item is a rod of power.
    * @param isRodOfPower true if this item is a rod of power.
    */
   void setIsRodOfPower(boolean isRodOfPower);

   /**
    * Checks if this item is a manastone.
    * @return true if this item is a manastone.
    */
   boolean isManastone();

   /**
    * Sets if this item is a manastone.
    * @param isManastone true if this item is a manastone.
    */
   void setIsManastone(boolean isManastone);

   /**
    * Checks if this item is a heartstone.
    * @return true if this item is a heartstone.
    */
   boolean isHeartstone();
   
   /**
    * Sets if this item is a heartstone.
    * @param isHeartstone true if this item is a heartstone.
    */
   void setIsHeartstone(boolean isHeartstone);

   /**
    * Sets various attributes of this use configuration object.
    * @param item the item.
    */
   public void setUseConfigAttributes(Item item);

   /**
    * Checks if this is a non-combat item.
    * @return true if this is a noncombat item.
    */
   public boolean isNonCombatItem();

}
