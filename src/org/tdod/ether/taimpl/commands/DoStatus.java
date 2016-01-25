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
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "status" command.
 *
 * @author Ron Kinney
 */
public class DoStatus extends Command {

   /**
    * Executes the "status" command.
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

      if (input.split(" ").length > 1) {
         return false;
      }

      String unformattedMessage = TaMessageManager.STATS.getMessage();

      // The double empty quotes gets rid of the Java auto formatter.
      String formattedMessage = MessageFormat.format(unformattedMessage,
         player.getRace().getName(),
         player.getPromotedClass().getName(),
         player.getPromotedLevel(),
         "" + player.getExperience(),
         player.getRune().getName(),
         "" + player.getStats().getIntellect(),
         "" + player.getStats().getKnowledge(),
         "" + player.getStats().getPhysique(),
         "" + player.getStats().getStamina(),
         "" + player.getStats().getAgility(),
         "" + player.getStats().getCharisma(),
         "" + player.getMana().getCurMana(),
         "" + player.getMana().getMaxMana(),
         "" + player.getVitality().getCurVitality(),
         "" + player.getVitality().getMaxVitality(),
         player.getStatus().getName(),
         "" + player.getArmorRating(),
         MyStringUtil.up1(player.getWeapon().getName()),
         MyStringUtil.up1(player.getArmor().getName()),
         "" + player.getEncumbrance().getCurEncumbrance(),
         "" + player.getEncumbrance().getMaxEncumbrance()
      );

      player.print(formattedMessage);

      return true;
   }

   /**
    * Determines if this command can be used while the player is paralyzed.
    *
    * @return true if this command can be used while paralyzed.
    */
   public final boolean canUseParalyzed() {
      return true;
   }

}
