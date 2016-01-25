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

package org.tdod.ether.taimpl.commands.handler;

import java.text.MessageFormat;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles commands that are issued in the temple.
 * @author rkinney
 */
public final class HandleTemple {

   private static final String HEALING = MessageManager.HEALING_STRING.getMessage();
   private static final String CURING = MessageManager.CURING_STRING.getMessage();
   private static final String REMOVAL = MessageManager.REMOVAL_STRING.getMessage();
   private static final String RESTORING = MessageManager.RESTORING_STRING.getMessage();

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleTemple() {
   }

   /**
    * Executes the temple command.
    * @param entity the entity issuing the command.
    * @param parameter any parameters passed in by the player.
    * @return true if the command was successful.
    */
   public static boolean execute(Entity entity, String parameter) {
      if (HEALING.equals(parameter.toLowerCase())) {
         return handleHealing(entity);
      } else if (CURING.equals(parameter.toLowerCase())) {
         return handleCuring(entity);
      } else if (REMOVAL.equals(parameter.toLowerCase())) {
         return handleRemoval(entity);
      } else if (RESTORING.equals(parameter.toLowerCase())) {
         return handleRestoring(entity);
      }

      return false;
   }

   /**
    * &MYou can't afford healing.
    * &CThe priests heal all your wounds for 1 crowns.
    * &WThe temple priests take Minex into another chamber briefly, after which
    * they return. You notice that all of Minex's wounds have been healed...
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleHealing(Entity entity) {
      int damage = entity.getVitality().getMaxVitality() - entity.getVitality().getCurVitality();
      int cost = (damage / 10) + 1;

      String messageToPlayer;
      String messageToRoom;
      // Check if the player has the cash.  Capitalism FTW... no socialized medicine here.
      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), HEALING);
         entity.print(messageToPlayer);
         return true;
      }

      // Do it
      entity.subtractGold(cost);
      entity.getVitality().setCurVitality(entity.getVitality().getMaxVitality());

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTH.getMessage(), cost);
      entity.print(messageToPlayer);
      messageToRoom = MessageFormat.format(TaMessageManager.OTHGTH.getMessage(), entity.getName());
      entity.getRoom().print(entity, messageToRoom, false);
      return true;
   }

   /**
    * &MYou can't afford curing.
    * &MYou do not need curing, there is no poison in your system.
    *
    * &CThe priests remove the poison from your system for 3 crowns.
    *
    * &WThe temple priests take Minex into another chamber briefly, after which
    * they return. You notice that Minex looks much healthier than before...
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleCuring(Entity entity) {
      String messageToPlayer;
      String messageToRoom;

      // Check if the player is actually poisoned.
      if (!entity.getStatus().equals(Status.POISONED)) {
         entity.print(TaMessageManager.NOCURE.getMessage());
         return true;
      }

      // Check if the player has the cash.  Capitalism FTW... no socialized medicine here.
      int cost = entity.getLevel();
      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), CURING);
         entity.print(messageToPlayer);
         return true;
      }

      // Do it
      entity.subtractGold(cost);
      entity.setStatus(Status.HEALTHY);

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTC.getMessage(), cost);
      entity.print(messageToPlayer);
      messageToRoom = MessageFormat.format(TaMessageManager.OTHGTC.getMessage(), entity.getName());
      entity.getRoom().print(entity, messageToRoom, false);

      return true;
   }

   /**
    * &MYou can't afford removal.
    * &MYou do not need that service, you are not paralyzed.
    *
    * &CThe priests purge the paralysis from your body for %d crowns.
    *
    * &WThe temple priests take %s into another chamber briefly, after which
    * they return. You notice that %s looks much more mobile than before...
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleRemoval(Entity entity) {
      String messageToPlayer;
      String messageToRoom;

      // Check if the player is actually paralyized.
      if (!entity.getStatus().equals(Status.PARALYSED)) {
         entity.print(TaMessageManager.NOREMO.getMessage());
         return true;
      }

      // Check if the player has the cash.  Capitalism FTW... no socialized medicine here.
      int cost = entity.getLevel();
      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), REMOVAL);
         entity.print(messageToPlayer);
         return true;
      }

      // Do it
      entity.subtractGold(cost);
      entity.setStatus(Status.HEALTHY);

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTP.getMessage(), cost);
      entity.print(messageToPlayer);
      messageToRoom = MessageFormat.format(TaMessageManager.OTHGTP.getMessage(), entity.getName());
      entity.getRoom().print(entity, messageToRoom, false);

      return true;
   }

   /**
    * &WYou do not need restoring, your body and mind are whole.
    *
    * &CThe priests restore your body and mind to it's former state for %d crowns.
    *
    * &WThe temple priests take %s into another chamber briefly, after which
    * they return. You notice that %s looks somehow different than before...
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleRestoring(Entity entity) {
      String messageToPlayer;
      String messageToRoom;

      if (!GameUtil.isEntityDrained(entity)) {
         entity.print(TaMessageManager.NOREST.getMessage());
         return true;
      }

      // Check if the player has the cash.  Capitalism FTW... no socialized medicine here.
      int cost = entity.getLevel();
      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), RESTORING);
         entity.println(messageToPlayer);
         return true;
      }

      // Do it
      entity.subtractGold(cost);
      entity.getStats().removeDrains();
      entity.getVitality().setDrained(0);

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTR.getMessage(), cost);
      entity.print(messageToPlayer);
      messageToRoom = MessageFormat.format(TaMessageManager.OTHGTR.getMessage(), entity.getName());
      entity.getRoom().print(entity, messageToRoom, false);

      return true;
   }
}
