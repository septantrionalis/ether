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

import java.text.MessageFormat;

import org.tdod.ether.ta.EntityMessageHandler;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Message handler for a player.
 *
 * @author Ron Kinney
 */
public class PlayerMessageHandler implements EntityMessageHandler {

   private Player _player;

   /**
    * Creates a new PlayerMessageHandler.
    *
    * @param player the player.
    */
   public PlayerMessageHandler(Player player) {
      _player = player;
   }

   /**
    * Message to the room when a mob misses an attack.
    *
    * @param mobName The name of the mob.
    *
    * @return the Message to the room when a mob misses an attack.
    */
   public final String getMobMissRoomMessage(String mobName) {
      return MessageFormat.format(TaMessageManager.MFMOTH.getMessage(), mobName, _player.getName());
   }

   /**
    * Message to the room when a mob hits with an attack.
    *
    * @param mobName The name of the mob being hit.
    * @param result The melee result.
    *
    * @return the message to the room when a mob hits with an attack.
    */
   public final String getMobHitRoomMessage(String mobName, MeleeResult result) {
      return MessageFormat.format(TaMessageManager.MATOTH.getMessage(),
            mobName, _player.getName(), _player.getGender().getPronoun().toLowerCase() + " " + result.getWeapon());
   }

   /**
    * Message to the room when a mob dodges the attack.
    *
    * @param mobName The name of the mob that dodged.
    *
    * @return the message to the room when a mob dodges the attack.
    */
   public final String getMobDodgeRoomMessage(String mobName) {
      return MessageFormat.format(TaMessageManager.MDGOTH.getMessage(), _player.getName(), mobName);
   }

   /**
    * Message to the room when the attack glances of a mobs armor.
    *
    * @param mobName The name of the mob that was being attacked.
    * @param result The melee result.
    *
    * @return The message to the room when the attack glances of a mobs armor.
    */
   public final String getMobGlanceRoomMessage(String mobName, MeleeResult result) {
      return MessageFormat.format(TaMessageManager.MGNOTH.getMessage(),
            mobName, _player.getName(), "its " + result.getWeapon(), _player.getName());
   }

   /**
    * Gets the "came back" message.
    *
    * @return the "came back" message.
    */
   public final String getCameBackMessage() {
      return MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), _player.getName(),
            _player.getGender().getPronoun().toLowerCase());
   }

   /**
    * Gets the string that is displayed when the entity enters a room.
    * @param exitDirection the exit direction.
    * @return the string that is displayed when the entity enters a room.
    */
   public String getEnterMessage(ExitDirectionEnum exitDirection) {
      if (exitDirection.equals(ExitDirectionEnum.UP) || exitDirection.equals(ExitDirectionEnum.DOWN)) {
         return MessageFormat.format("&Y" + MessageManager.ENT2.getMessage(),
               _player.getName(), exitDirection.getAltDescription());
      }
      return MessageFormat.format("&Y" + MessageManager.ENT1.getMessage(),
            _player.getName(), exitDirection.getLongDescription());

   }

   /**
    * Gets the string that is displayed when the entity leaves a room.
    * @param exitDirection the exit direction.
    * @return the string that is displayed when the entity leaves a room.
    */
   public String getLeaveMessage(ExitDirectionEnum exitDirection) {
      if (exitDirection.equals(ExitDirectionEnum.UP) || exitDirection.equals(ExitDirectionEnum.DOWN)) {
         return MessageFormat.format("&Y" + MessageManager.EXT2.getMessage(),
               _player.getName(), exitDirection.getLongDescription());
      }
      return MessageFormat.format("&Y" + MessageManager.EXT1.getMessage(),
            _player.getName(), exitDirection.getLongDescription());
   }

}
