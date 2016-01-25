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
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles commands issued in the tavern.
 * @author rkinney
 */
public final class HandleTavern {

   private static final String DRINK = MessageManager.DRINK_STRING.getMessage();
   private static final String MEAL = MessageManager.MEAL_STRING.getMessage();
   private static final int DRINK_COST = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TAVERN_DRINK_COST));
   private static final int DRINK_VALUE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TAVERN_DRINK_VALUE));
   private static final int MEAL_COST = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TAVERN_MEAL_COST));
   private static final int MEAL_VALUE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TAVERN_MEAL_VALUE));

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleTavern() {
   }

   /**
    * Executes most tavern commands.
    * @param player the player issuing the command.
    * @param parameter any parameters passed in by the player.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player, String parameter) {
      if (DRINK.equals(parameter.toLowerCase())) {
         return handleDrink(player);
      } else if (MEAL.equals(parameter.toLowerCase())) {
         return handleMeal(player);
      }

      player.print(TaMessageManager.NOSITM.getMessage());

      return true;
   }

   /**
    * Handles the drink command.
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleDrink(Entity entity) {
      String messageToPlayer;

      int cost = DRINK_COST;

      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), DRINK);
         entity.print(messageToPlayer);
         return true;
      }

      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTD.getMessage(), cost);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHGTD.getMessage(), entity.getName());

      entity.print(messageToPlayer);
      entity.getRoom().print(entity, messageToRoom, false);

      entity.subtractGold(cost);
      entity.addThirstTicker(DRINK_VALUE);
      return true;
   }

   /**
    * Handles the meal command.
    * @param entity the entity issuing the command.
    * @return true if the command was successful.
    */
   private static boolean handleMeal(Entity entity) {
      String messageToPlayer;

      int cost = MEAL_COST;

      if (cost > entity.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), MEAL);
         entity.print(messageToPlayer);
         return true;
      }

      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGTM.getMessage(), cost);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHGTM.getMessage(), entity.getName());

      entity.print(messageToPlayer);
      entity.getRoom().print(entity, messageToRoom, false);

      entity.subtractGold(cost);
      entity.addHungerTicker(MEAL_VALUE);

      return true;
   }
}
