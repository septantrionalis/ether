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

package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * &YYou just rang the great gong!
 * -> nothing happens if room is full.
 * &GA lizard woman enters the arena through the dungeon gate!
 *
 */
public class DoRingGong extends Command {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoRingGong.class);

   /**
    * Combat rest period.
    */
   private static final int COMBAT_TICKER =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.RING_GONG_COMBAT_TIME));

   /**
    * Rest ticker.
    */
   private static final int REST_TICKER =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.RING_GONG_REST_TIME));

   /**
    * Executes the "ring gong" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return false;
      }
      Player player = (Player) entity;

      Room room = player.getRoom();

      String[] split = input.split(" ", THREE_PARAMS);
      if (split.length < 2) {
         return false;
      }

      if (RoomFlags.ARENA.isSet(room.getRoomFlags())) {

         // Can't ring the gong if the player is resting.
         if (GameUtil.isResting(entity)) {
            player.print(TaMessageManager.CNTMOV.getMessage());
            return true;
         }

         // Display the messages
         String messageToPlayer = MessageFormat.format(TaMessageManager.RNGGNG.getMessage(), "You");
         player.print(messageToPlayer);
         String messageToRoom = MessageFormat.format(TaMessageManager.RNGGNG.getMessage(), player.getName());
         room.print(player, messageToRoom, false);

         // Attempt to summon the mob and place it in the room.
         Mob conjuredMob;
         if (room.getServiceLevel() == 1) {
            conjuredMob = generateArenaMob(player, Terrain.DUNGEON1);
         } else if (room.getServiceLevel() == 2) {
            conjuredMob = generateArenaMob(player, Terrain.DUNGEON3);
         } else if (room.getServiceLevel() == 3) {
            conjuredMob = generateArenaMob(player, Terrain.FIRE2);
         } else {
            _log.error("Attempted to summon a mob in an unsupported terrain : "
                  + room.getTerrain() + ", room " + room.getRoomNumber());
            conjuredMob = generateArenaMob(player, Terrain.DUNGEON1);
         }

         MoveFailCode code = room.placeMob(conjuredMob);

         // If the mob is summoned, display it, and make the player rest.
         if (code.equals(MoveFailCode.NONE)) {
            messageToRoom = MessageFormat.format(TaMessageManager.MONENT.getMessage(),
                  conjuredMob.getPrefix(), conjuredMob.getName());
            room.print(null, messageToRoom, false);
         } else {
            conjuredMob.destroy();
            conjuredMob = null;
         }
         player.setCombatTicker(COMBAT_TICKER);
         player.setRestTicker(REST_TICKER);

         return true;
      } else {
         return false;
      }
   }

   /**
    * Generates the arena mobs for a given terrain.
    *
    * @param player The player who rung the gong.
    * @param terrain The
    *
    * @return The generated mob.
    */
   private Mob generateArenaMob(Player player, Terrain terrain) {
      ArrayList<Mob> arena1Mobs = WorldManager.getMobByTerrain(terrain);
      int index = Dice.roll(0, arena1Mobs.size() - 1);
      return arena1Mobs.get(index).clone(player.getRoom());
   }

}
