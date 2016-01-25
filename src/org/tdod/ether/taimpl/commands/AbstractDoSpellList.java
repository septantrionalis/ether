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
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.TaMessageManager;

/**
 * This is an abstract class that handles the listing of spells at the guild hall.
 *
 * @author Ron Kinney
 */
public abstract class AbstractDoSpellList extends Command {

   /**
    * The class of the player issuing the command.
    */
   private PlayerClass _spellClass;

   /**
    * Executes the listing of spells command.
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

      if (input.split(" ").length > 2) {
         return false;
      }

      if (!RoomFlags.GUILD_HALL.isSet(room.getRoomFlags())) {
         return false;
      }


      doSpellList(entity, input);

      entity.println("&W+======================+=======+=======+");

      String messageToRoom = MessageFormat.format(TaMessageManager.SSNOTH.getMessage(),
            entity.getName());
      room.println(entity, messageToRoom, false);

      return true;
   }

   /**
    * Print the header for sorceror spells.
    *
    * @param entity The entity to display the header.
    */
   protected final void printSorcerorHeader(Entity entity) {
      entity.println("&W+======================================+");
      entity.println("&W||           &MSorceror Spells&W          ||");
      entity.println("&W+======================+=======+=======+");
      entity.println("&W| &MName&W                 | &MMana&W  | &MPrice&W |");
      entity.println("&W+----------------------+-------+-------+");

   }

   /**
    * Print the header for acoloyte spells.
    *
    * @param entity The entity to display the header.
    */
   protected final void printAcolyteHeader(Entity entity) {
      entity.println("&W+======================================+");
      entity.println("&W||           &CAcolyte Spells           ||");
      entity.println("&W+======================+=======+=======+");
      entity.println("&W| &CName&W                 | &CMana&W  | &CPrice&W |");
      entity.println("&W+----------------------+-------+-------+");

   }

   /**
    * Print the header for druid spells.
    *
    * @param entity The entity to display the header.
    */
   protected final void printDruidHeader(Entity entity) {
      entity.println("&W+======================================+");
      entity.println("&W||            &GDruid Spells            ||");
      entity.println("&W+======================+=======+=======+");
      entity.println("&W| &GName&W                 | &GMana&W  | &GPrice&W |");
      entity.println("&W+----------------------+-------+-------+");

   }

   /**
    * Print the header for necrolyte spells.
    *
    * @param entity The entity to display the header.
    */
   protected final void printNecrolyteHeader(Entity entity) {
      entity.println("&W+======================================+");
      entity.println("&W||           &RNecrolyte Spells         ||");
      entity.println("&W+======================+=======+=======+");
      entity.println("&W| &RName&W                 | &RMana&W  | &RPrice&W |");
      entity.println("&W+----------------------+-------+-------+");

   }

   /**
    * Lists the spells for the appropriate class.
    *
    * @param entity The entity issuing the command.
    * @param input The input string.
    */
   protected abstract void doSpellList(Entity entity, String input);

   /**
    * Gets the player class.
    *
    * @return the player class.
    */
   public final PlayerClass getSpellClass() {
      return _spellClass;
   }

   /**
    * Sets the player class.
    *
    * @param spellClass the player class.
    */
   public final void setSpellClass(PlayerClass spellClass) {
      _spellClass = spellClass;
   }

}
