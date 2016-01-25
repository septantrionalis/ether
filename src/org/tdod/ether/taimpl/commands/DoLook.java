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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * l
 *
 * l e
 * &CSorry, you don't see "e" nearby.
 *
 * l <direction> does not work in town.
 *
 * l d
 * &CSorry, there's no exit in that direction.
 * l u
 * Sorry, there's no exit in that direction.
 * @author minex
 * Minex is looking to the west.
 *
 * You can't see southeast.
 *
 * l mino, &WThe minotaur is to the south!
 */
public class DoLook extends Command {

   public static final int LOOK_ROOM_DEPTH = Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.LOOK_DEPTH));

   /**
    * Executes the "look" command.
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
      String[] split = input.split(" ");

      // TODO is the dark check at the top of the look method?
      if (!room.isIlluminated()) {
         player.print(TaMessageManager.TOODRK.getMessage());
         return true;
      }

      // Just examine the room.
      if (split.length == 1) {
         player.println(player.getRoom().getLongDescription());
         String messageToRoom = MessageFormat.format(TaMessageManager.LOKOTH.getMessage(), player.getName());
         room.print(player, messageToRoom, false);
         return true;
      }

      if (split.length == 2) {
         String param = split[1].toLowerCase();

         Entity target = GameUtil.getTarget(player, player.getRoom(), param);
         if (target != null) {
            if (target.getEntityType().equals(EntityType.PLAYER)) {
               return handlePlayerLook(player, (Player) target);
            } else {
               return handleMobLook(player, (Mob) target);
            }
         }

         if (findTarget(player, param)) {
            return true;
         }

         // Finally, check if its an exit keyword.
         ExitDirectionEnum exitDirEnum = ExitDirectionEnum.getExitKeyword(param);
         if (exitDirEnum != null) {
            // Looking is displayed to room, even if it doesn't exist.
            String messageToRoom =
               MessageFormat.format(TaMessageManager.INSOTH2.getMessage(), player.getName(), exitDirEnum.getLongDescription());
            entity.getRoom().print(player, messageToRoom, false);

            // The keyword is a direction... check if it exists.
            Exit exit = room.getExit(exitDirEnum);
            if (exit != null) {
               // Can't look through a closed barrier.
               if (!exit.isPassable()) {
                  String messageToPlayer =
                     MessageFormat.format(TaMessageManager.CNTSEE.getMessage(), exitDirEnum.toString().toLowerCase());
                  entity.print(messageToPlayer);
                  return true;
               }

               Room roomLook = WorldManager.getRoom(exit.getToRoom());
               if (roomLook != null) {
                  // Exit found.  Display it.
                  if (!GameUtil.isTown(roomLook)) {
                     player.print(roomLook.getDefaultRoomString(player));
                     return true;
                  }
               } else {
                  // Exit not found.
                  player.print(TaMessageManager.NOEXIT.getMessage());
                  return true;
               }
            } else {
               // Exit not found.
               player.print(TaMessageManager.NOEXIT.getMessage());
               return true;
            }

         }

         // Not sure what the player is looking for...
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         player.print(message);
         return true;
      }

      return false;
   }

   /**
    * Looks at another player.
    *
    * @param player The player who is looking.
    * @param victimPlayer The target player.
    *
    * @return true if the command is successful.
    */
   private boolean handlePlayerLook(Player player, Player victimPlayer) {
      // Can't look at yourself.
      if (victimPlayer.equals(player)) {
         player.print(TaMessageManager.NOLSLF.getMessage());
         return true;
      }

      // Message to the player.
      player.print(victimPlayer.getDescription());

      // Message to the target
      String messageToTarget = MessageFormat.format(TaMessageManager.INSPCT.getMessage(), player.getName());
      victimPlayer.print(messageToTarget);

      // Message to everyone else.
      String messageToOthers =
         MessageFormat.format(TaMessageManager.INSOTH1.getMessage(), player.getName(), victimPlayer.getName());
      player.getRoom().println(player, victimPlayer, messageToOthers, false);

      return true;
   }

   /**
    * The kobold is a small humanoid with doglike facial features and is covered
    * with coarse body hair. He stands just under four feet in height, is wearing
    * a filthy tunic, and is armed with a dagger. The kobold seems to be in good
    * physical health.
    *
    * Minex is looking the cave bear over.
    *
    * @param player The player who is looking.
    * @param mob The target mob.
    *
    * @return true if the command is successful.
    */
   private boolean handleMobLook(Player player, Mob mob) {
      if (mob != null) {
         player.print(mob.getLookDescription());

         String messageToRoom = MessageFormat.format(TaMessageManager.INMOTH.getMessage(),
               player.getName(), mob.getName());
         player.getRoom().print(player, messageToRoom, false);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Iterates through the rooms in a straight direction to find a target.
    * @param player the player who is attempting to find the target.
    * @param param the parameter passed in by the player.
    * @return true if the target was found.
    */
   private boolean findTarget(Player player, String param) {
      if (RoomFlags.SAFE.isSet(player.getRoom().getRoomFlags())) {
         return false;
      }

      for (ExitDirectionEnum exitDirection : GameUtil.getValidExitDirectionsForLook()) {
         Exit exit = player.getRoom().getExit(exitDirection);
         Room room;
         if (exit != null) {
            if (exit.isPassable()) {
               room = WorldManager.getRoom(exit.getToRoom());
               Entity entity = traverseDirection(player, room, exitDirection, param, LOOK_ROOM_DEPTH);
               if (entity != null) {
                  String messageToPlayer = MessageFormat.format(TaMessageManager.THRITIS.getMessage(),
                        entity.getName(), exitDirection.getLongDescription());
                  String messageToRoom = MessageFormat.format(TaMessageManager.INSOTH2.getMessage(),
                        player.getName(), exitDirection.getLongDescription());
                  player.print(messageToPlayer);
                  player.getRoom().print(player, messageToRoom, false);
                  return true;
               }
            }
         }
      }
      
      return false;
   }

   /**
    * Traverses in the specified direction.
    * @param player the player who issued the command.
    * @param room the current room.
    * @param exitDirectionEnum the exit direction.
    * @param param the parameter passed in by the player.
    * @param maxDepth the maximum depth to iterate.
    * @return the entity, if found.  Otherwise null.
    */
   private final Entity traverseDirection(Player player, Room room, ExitDirectionEnum exitDirectionEnum,
         String param, int maxDepth) {
      int depth = 0;
      do {
         depth++;
         if (depth > maxDepth) {
            return null;
         }
         Entity target = GameUtil.getTarget(player, room, param);
         if (target == null || target.equals(player)) {
            Exit exit = room.getExit(exitDirectionEnum);
            if (exit == null) {
               return null;
            } else {
               room = WorldManager.getRoom(exit.getToRoom());
            }
         } else {
            return target;
         }
      } while(true);

   }

}
