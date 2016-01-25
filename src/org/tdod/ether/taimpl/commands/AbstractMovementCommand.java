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
import org.tdod.ether.ta.cosmos.Barrier;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Teleporter;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.doors.PrivateRoomDoor;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Wow, did this class balloon out.
 *
 * @author rkinney
 */
public abstract class AbstractMovementCommand extends Command {

   /**
    * Logging variable.
    */
   private static Log _log = LogFactory.getLog(AbstractMovementCommand.class);

   /**
    * Chance for a mob to chase the victim whenever that player leaves the room.
    */
   private static final int MOB_CHASE_CHANCE =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_CHASE_CHANCE)).intValue();

   /**
    * Executes the movement command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      if (input.split(" ").length > 1) {
         return false;
      }

      if (GameUtil.isResting(entity)) {
         entity.print(TaMessageManager.CNTMOV.getMessage());
         return true;
      }

      if (entity.isAttacking()) {
         entity.print(TaMessageManager.LEVCBT.getMessage());
         return true;
      }

      boolean inGroup = GameUtil.isLeaderInGroup(entity);

      return doMove(entity, false, inGroup);
   }

   /**
    * Move an entity from one room to another.
    *
    * @param entity The entity performing the movement command.
    * @param exit The exit that the entity will be going through.
    * @param exitDirection The direction of the exit.
    * @param ignoreBarrier true if the entity can ignore a barrier.
    * @param inGroup true if the entity is in a group.
    */
   protected final void doMovePlayer(Entity entity,
         Exit exit, ExitDirectionEnum exitDirection, boolean ignoreBarrier, boolean inGroup) {
      if (WorldManager.getGameMechanics().entityTripped(entity)) {
         return;
      }

      String fromRoomMessage;
      String toRoomMessage;
      if (inGroup) {
         fromRoomMessage = getGroupLeaveMessage(entity);
         toRoomMessage = getGroupEnterMessage(entity);
      } else {
         fromRoomMessage = getLeaveMessage(entity);
         toRoomMessage = getEnterMessage(entity);
      }

      if (exit == null) {
         entity.print(TaMessageManager.NOEXIT.getMessage());
         return;
      }

      int fromRoomNumber = entity.getRoom().getRoomNumber();
      Room fromRoom = WorldManager.getRoom(fromRoomNumber);

      MoveFailCode code = entity.moveToRoom(exit.getToRoom(), ignoreBarrier);

      if (code.equals(MoveFailCode.NONE)
            || code.equals(MoveFailCode.UNLOCK_BARRIER)
            || code.equals(MoveFailCode.PICKED_BARRIER)) {
         success(code, fromRoom, entity, exit, exitDirection, fromRoomMessage, toRoomMessage, ignoreBarrier, inGroup);
      } else if (code.equals(MoveFailCode.NO_EXIT)) {
         entity.print(TaMessageManager.NOEXIT.getMessage());
      } else if (code.equals(MoveFailCode.BARRIER)) {
         barrier(entity, fromRoom, fromRoomMessage, exit);
      } else if (code.equals(MoveFailCode.EXIT_LOCKED)) {
         entity.println(WorldManager.getBarrier(PrivateRoomDoor.BARRIER_INDEX).getLockedMessage());
         fromRoom.println(entity, fromRoomMessage, false);
         fromRoom.print(entity, entity.getMessageHandler().getCameBackMessage(), false);
      }
   }

   /**
    * Force any people in the group to follow the leader, if they are following.
    *
    * @param follower The entity following the leader.
    * @param ignoreBarrier true if the entity can ignore any barriers.
    * @param inGroup true if the entity is in a group.
    * @param exitDirection The direction of the exit.
    * @param fromRoomMessage The message to be dispalyed to the room that the entity is leaving.
    *
    * @return true if the following entity moved, false if not.
    */
   protected final boolean followGroup(Entity follower,
         boolean ignoreBarrier, boolean inGroup, ExitDirectionEnum exitDirection, String fromRoomMessage) {
      if (follower.isResting() || follower.isMentallyExhausted() || follower.isAttacking()) {
         // Mobs can attack and move.
         if (follower.getEntityType().equals(EntityType.PLAYER)) {
            follower.print(TaMessageManager.TOOSLW.getMessage());
            return false;
         }
      }

      if (follower.isAttacking()) {
         follower.print(TaMessageManager.TOOSLW.getMessage());
         return false;
      }

      follower.println("&Y" + fromRoomMessage);
      if (follower.getDraggedBy() == null) {
         follower.print(TaMessageManager.YOUFOL.getMessage());
      } else {
         String exitKeyword;
         if (exitDirection.equals(ExitDirectionEnum.UP) || exitDirection.equals(ExitDirectionEnum.DOWN)) {
            exitKeyword = exitDirection.getAlt2Description();
         } else {
            exitKeyword =  exitDirection.getLongDescription();
         }
         String messageToFollower =
            MessageFormat.format(TaMessageManager.DRGYOU.getMessage(), follower.getDraggedBy().getName(), exitKeyword);
         follower.print(messageToFollower);
      }

      return doMove(follower, ignoreBarrier, inGroup);
   }

   /**
    * The player was able to move.  Actually perform the move.
    *
    * @param code The movement code.  This should be the reason that the entity actually moved
    * (lock picked, barrier removed, etc).
    * @param fromRoom The room that the entity came from.
    * @param entity The entity that moved.
    * @param exit The exit that the entity went through.
    * @param exitDirection The direction of the exit.
    * @param fromRoomMessage The message displayed to the room the entity came from.
    * @param toRoomMessage The message displayed to the room the entity is going to.
    * @param ignoreBarrier true if a barrier can be ignored.
    * @param inGroup true if the entity is in a group.
    */
   private void success(MoveFailCode code, Room fromRoom, Entity entity, Exit exit, ExitDirectionEnum exitDirection,
         String fromRoomMessage, String toRoomMessage, boolean ignoreBarrier, boolean inGroup) {
      if (code.equals(MoveFailCode.UNLOCK_BARRIER)) {
         Barrier barrier = WorldManager.getBarrier(exit.getDoor().getV3());
         Item key = WorldManager.getItem(exit.getDoor().getV0());
         String messageToPlayer = MessageFormat.format(barrier.getUnlockedMessage(), key.getName());
         entity.println(messageToPlayer);
         ignoreBarrier = true;
      } else if (code.equals(MoveFailCode.PICKED_BARRIER)) {
         Barrier barrier = WorldManager.getBarrier(exit.getDoor().getV3());
         entity.println(barrier.getRogueMessage());
         ignoreBarrier = true;
      } else if (code.equals(MoveFailCode.NONE)) {
         ignoreBarrier = true;
      }

      Room toRoom = WorldManager.getRoom(exit.getToRoom());

      determineChasingMobs(entity, fromRoom);

      displayPlayerDragMessage(entity, exitDirection);

      entity.setLastMove(System.currentTimeMillis());

      DoDefault.displayRoomDescription(entity);

      // Trigger player first before group.
      TriggerResult triggerResult = toRoom.handleTriggers(entity);

      int followers = 0;

      handleTeleportTrigger(entity, triggerResult, toRoom);

      // Handle group move.
      if (!triggerResult.equals(TriggerResult.DEATH)) {
         followers = doMoveGroup(entity, exitDirection, fromRoom, ignoreBarrier, inGroup, fromRoomMessage);
      } else {
         followers = 0;
      }

      // Display any dragging messages.
      if (entity.getDragging() != null) {
         String dragToRoomMessage = MessageFormat.format(TaMessageManager.OTHDRI.getMessage(), entity, entity.getDragging());
         toRoom.print(entity, entity.getDragging(), dragToRoomMessage, false);
      }

      // Handle movement messages.
      if (followers > 0) {
         toRoom.printlnToNonGroup(entity, toRoomMessage);
         fromRoom.printlnToNonGroup(entity, fromRoomMessage);
      } else {
         if (!inGroup) {
            toRoom.println(entity, toRoomMessage, false);
            fromRoom.println(entity, fromRoomMessage, false);
         }
      }

      // Display any dragging messages.
      if (entity.getDragging() != null) {
         String dragFromRoomMessage = MessageFormat.format(TaMessageManager.OTHDRO.getMessage(), entity, entity.getDragging());
         fromRoom.print(entity, entity.getDragging(), dragFromRoomMessage, false);
      }

      handleDeathTrigger(entity, triggerResult, toRoom);

      handleExitTrigger(entity, triggerResult, toRoom, inGroup);

      if (inGroup && triggerResult.equals(TriggerResult.DEATH)) {
         // The leader has entered the room and died.  Disband the group and send the regular exit/enter message.
         toRoom.println(entity, getEnterMessage(entity), false);
         fromRoom.println(entity, getLeaveMessage(entity), false);
         disbandGroup(entity);
      }
   }

   /**
    * Moves a group.
    *
    * @param leader The leader of the group.
    * @param exitDirection The exit direction.
    * @param fromRoom The room that the leader is coming from.
    * @param ignoreBarrier true if the leader can ignore the barrier.
    * @param inGroup true if the leader is in a group.
    * @param fromRoomMessage The message to the room that the leader came from.
    *
    * @return the total number of players that succeeded in following the leader.
    */
   protected final int doMoveGroup(Entity leader,
         ExitDirectionEnum exitDirection, Room fromRoom, boolean ignoreBarrier,
         boolean inGroup, String fromRoomMessage) {
      int followedCount = 0;
      int totalFollowers = 0;
      for (Entity follower : leader.getGroupList()) {
         if (follower.isFollowingGroup()) {
            if (follower.getRoom().equals(fromRoom)) {
               totalFollowers++;
               if (followGroup(follower, ignoreBarrier, inGroup, exitDirection, fromRoomMessage)) {
                  followedCount++;
               }
            }
         }
      }
      if (inGroup && totalFollowers > 0) {
         String messageToLeader = MessageFormat.format(TaMessageManager.GRPARV.getMessage(), followedCount);
         leader.print(messageToLeader);
      }

      return followedCount;
   }

   /**
    * The entity hit a barrier.
    *
    * @param entity The entity that is moving.
    * @param fromRoom The room that the entity came from.
    * @param fromRoomMessage The message that will be displayed to the room that the entity came from.
    * @param exit The exit that the entity attempted to go through.
    */
   private void barrier(Entity entity, Room fromRoom, String fromRoomMessage, Exit exit) {
      Barrier barrier = WorldManager.getBarrier(exit.getDoor().getV3());
      entity.println(barrier.getLockedMessage());
      if (entity.getPlayerClass().equals(PlayerClass.ROGUE)) {
         entity.print(TaMessageManager.RGFALD.getMessage());
      }
      fromRoom.println(entity, fromRoomMessage, false);
      fromRoom.print(entity, entity.getMessageHandler().getCameBackMessage(), false);
   }

   /**
    * Handles a teleportation trigger.
    *
    * @param entity The entity going through the teleporter.
    * @param triggerResult The trigger result -- should be "TELEPORTED" for this method to do something.
    * @param toRoom The room to teleport to.
    */
   private void handleTeleportTrigger(Entity entity, TriggerResult triggerResult, Room toRoom) {
      if (triggerResult.equals(TriggerResult.TELEPORTED)) {
         for (Trigger trigger : toRoom.getTriggers()) {
            if (trigger.getTriggerType().equals(TriggerType.TELEPORT)) {
               // Just handle the first teleport trigger, if multiple ones are defined.
               Teleporter teleporter = WorldManager.getTeleporter(trigger.getV3());

               Room teleportedRoom = WorldManager.getRoom(trigger.getV2());
               String toRoomMessage = MessageFormat.format("&Y" + teleporter.getToRoomMessage(), entity.getName());

               teleportedRoom.println(entity, toRoomMessage, false);

               return;
            }
         }
      }
   }

   /**
    * Handles a trigger that resulted in a death.
    *
    * @param entity The entity that died.
    * @param triggerResult The trigger result -- should be "DEATH" for this method to do something.
    * @param toRoom The room that the entity attempted to enter.
    */
   private void handleDeathTrigger(Entity entity, TriggerResult triggerResult, Room toRoom) {
      if (triggerResult.equals(TriggerResult.DEATH) || entity.getVitality().getCurVitality() < 0) {
         if (entity.getEntityType().equals(EntityType.PLAYER)) {
            WorldManager.getGameMechanics().handlePlayerDeath(
                  (Player) entity, TaMessageManager.YOUDED1.getMessage());
         } else if (entity.getEntityType().equals(EntityType.MOB)) {
            WorldManager.getGameMechanics().handleMobDeath(null, (Mob) entity);
         } else {
            _log.error("Unhandled entity type " + entity.getEntityType());
         }
      }
   }

   /**
    * Not sure what the fuck I was doing here... I called this an exit trigger, but it appears to
    * handle a TELEPORT trigger, even though I already have a method that handles a teleport.  Oh well.
    * The code seems to work, so...
    *
    * @param entity The entity
    * @param triggerResult The trigger result.
    * @param toRoom The room that the player is entering.
    * @param inGroup True if the player is in a group.
    */
   private void handleExitTrigger(Entity entity, TriggerResult triggerResult,  Room toRoom, boolean inGroup) {
      if (triggerResult.equals(TriggerResult.TELEPORTED)) {
         for (Trigger trigger : toRoom.getTriggers()) {
            if (trigger.getTriggerType().equals(TriggerType.TELEPORT)) {
               // Handle the first teleporter in a room.  A room should only have one teleporter.
               if (!inGroup) {
                  Teleporter teleporter = WorldManager.getTeleporter(trigger.getV3());
                  String toRoomMessage = MessageFormat.format("&Y" + teleporter.getFromRoomMessage(), entity.getName());
                  toRoom.println(entity, toRoomMessage, false);
               } else if (entity.equals(entity.getGroupLeader())) {
                  // This is a hack.  I know Telearena doesn't handle group teleport messages for groups in this fashion,
                  // but I don't want to spend the next couple of weeks trying to get the message flow correct.
                  // Besides, this is a cleaner approach.
                  Teleporter teleporter = WorldManager.getTeleporter(trigger.getV3());
                  String name = entity.getName() + "'s group";
                  String toRoomMessage = MessageFormat.format("&Y" + teleporter.getFromRoomMessage(), name);
                  toRoom.println(entity, toRoomMessage, false);
               }
               return;
            }
         }
      }
   }

   /**
    * Disbands the group.
    *
    * @param entity The entity whos group will be disbanded.
    */
   private void disbandGroup(Entity entity) {
      if (entity.getGroupLeader().getEntityType().equals(EntityType.PLAYER)) {
         Player leader = (Player) entity.getGroupLeader();
         DoLeave doLeave = new DoLeave();
         ArrayList<Entity> groupList = new ArrayList<Entity>();
         for (Entity follower : leader.getGroupList()) {
            groupList.add(follower);
         }
         for (Entity follower : groupList) {
            doLeave.execute(follower, "");
         }
      } else {
         _log.error("How the hell did a mob become a group leader?");
      }
   }

   /**
    * This is as good as a guess as any.  Determine if the mob chases the player when that player enters the room.
    *
    * @param entity The entity that the mobs will try and chase.
    * @param room The room containing the mobs.
    */
   private void determineChasingMobs(Entity entity, Room room) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return;
      }
      Player player = (Player) entity;
      for (Mob mob : room.getMobs()) {
         if (mob.getChasing() == null && mob.getGroupLeader().equals(mob)) {
            int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
            if (roll < MOB_CHASE_CHANCE) {
               mob.setChasing(player);
            }
         }
      }
   }

   /**
    * Displays any dragging messages.
    *
    * @param entity The entity performing that drag command.
    * @param exitDirection The direction that the player is dragging the target.
    */
   private void displayPlayerDragMessage(Entity entity, ExitDirectionEnum exitDirection) {
      if (entity.getDragging() != null) {
         String exitKeyword;
         if (exitDirection.equals(ExitDirectionEnum.UP) || exitDirection.equals(ExitDirectionEnum.DOWN)) {
            exitKeyword = exitDirection.getAlt2Description();
         } else {
            exitKeyword =  exitDirection.getLongDescription();
         }

         String messageToPlayer =
            MessageFormat.format(TaMessageManager.YOUDRG.getMessage(), entity.getDragging().getName(), exitKeyword);
         entity.print(messageToPlayer);
      }
   }

   /**
    * An abstract method that moves the player.
    *
    * @param entity The entity moving.
    * @param ignoreBarrier True if the entity ignores any barriers.
    * @param inGroup True if the entity is in a group.
    *
    * @return True if the player was able to move.
    */
   protected abstract boolean doMove(Entity entity, boolean ignoreBarrier, boolean inGroup);

   /**
    * An abstract method defining the group leave message.
    *
    * @param entity The entity moving.
    *
    * @return The group leave message.
    */
   protected abstract String getGroupLeaveMessage(Entity entity);

   /**
    * An abstract method defining the group enter message.
    *
    * @param entity The entity moving.
    *
    * @return The group enter message.
    */
   protected abstract String getGroupEnterMessage(Entity entity);

   /**
    * An abstract method defining an entity leave message.
    *
    * @param entity The entity moving.
    *
    * @return The entity leave message.
    */
   protected abstract String getLeaveMessage(Entity entity);

   /**
    * An abstract method defining an entity enter message.
    *
    * @param entity The entity moving.
    *
    * @return The entity enter message.
    */
   public abstract String getEnterMessage(Entity entity);

}
