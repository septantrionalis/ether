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

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 *
 * joined group, but not added
 * &MSorry, you are not a member of any group.
 */
public class DoGroup extends Command {

   /**
    * Executes the "group" command.
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

      String[] split = input.split(" ", 2);
      if (split.length > 1) {
         return false;
      }

      Entity leader = player.getGroupLeader();

      ArrayList<Entity> entityList = leader.getGroupList();
      if (!player.equals(leader)) {
         if (!entityList.contains(player)) {
            player.print(TaMessageManager.NOTGRP.getMessage());
            return true;
         }
      }

      player.print(TaMessageManager.GRPHDR.getMessage());

      ArrayList<Entity> mobList = new ArrayList<Entity>();

      // Display the leader first.
      displayLine(player, leader, "(L)");

      // Display players next.
      for (Entity entityInGroup : entityList) {
         if (entityInGroup.getEntityType().equals(EntityType.MOB)) {
            mobList.add(entityInGroup);
            continue;
         }
         displayLine(player, entityInGroup, "   ");
      }

      // Display mobs last.
      for (Entity mob : mobList) {
         displayLine(player, mob, "   ");
      }

      return true;
   }

   /**
    * Displays a group line.
    *
    * @param player The player who issued the group command.
    * @param entity The entity in the group.
    * @param leader The leader string
    */
   private void displayLine(Player player, Entity entity, String leader) {
      String line = "  %-30s %3s [HE:%3s%% ST:%s]";

      if (player.getRoom().isRoomPopulatedWithOtherEntity(entity)) {
         StringBuilder sb1 = new StringBuilder();
         Formatter formatter1 = new Formatter(sb1, Locale.US);
         formatter1.format(line,
               entity.getName(), leader, entity.getVitality().getVitalityPercent(), entity.getRestingString());
         player.println(sb1.toString());
      }
   }

}
