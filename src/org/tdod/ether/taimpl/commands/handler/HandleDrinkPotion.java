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
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the drink command.
 * @author rkinney
 */
public final class HandleDrinkPotion {

   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOW_STATUS_TIMER_WAKEUP)).intValue();

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleDrinkPotion() {
   }

   /**
    * Executes this command.
    * @param potion the potion the entity will be drinking.
    * @param entity the entity that issued the command.
    * @return true if the command was successful.
    */
   public static boolean execute(Equipment potion, Entity entity) {
      Room room = entity.getRoom();

      if (potion.getEquipmentSubType().equals(EquipmentSubType.HEAL)) {
         int amount = Dice.roll(potion.getMinEquipmentEffect(), potion.getMaxEquipmentEffect());
         entity.getVitality().addCurVitality(amount);
      } else if (potion.getEquipmentSubType().equals(EquipmentSubType.CURE_POISON)) {
         if (entity.getStatus().equals(Status.POISONED)) {
            entity.setStatus(Status.HEALTHY);
         }
      } else if (potion.getEquipmentSubType().equals(EquipmentSubType.MINOR_MANA_BOOST)) {
         entity.getMana().restoreMana();
      } else if (potion.getEquipmentSubType().equals(EquipmentSubType.PHYSIQUE_BOOST)) {
         if (entity.getStats().getPhysique().getBoostTimer() < 1) {
            int effect = Dice.roll(10, potion.getMinEquipmentEffect());
            float duration = potion.getMaxEquipmentEffect() / TIMER_WAKEUP;
            entity.getStats().getPhysique().setBoostTimer((int) duration);
            entity.getStats().getPhysique().setBoost(effect);
         }
      } else if (potion.getEquipmentSubType().equals(EquipmentSubType.AGILITY_BOOST)) {
         if (entity.getStats().getAgility().getBoostTimer() < 1) {
            int effect = Dice.roll(10, potion.getMinEquipmentEffect());
            float duration = potion.getMaxEquipmentEffect() / TIMER_WAKEUP;
            entity.getStats().getAgility().setBoostTimer((int) duration);
            entity.getStats().getAgility().setBoost(effect);
         }
      } else if (potion.getEquipmentSubType().equals(EquipmentSubType.INVISIBILITY)) {
         int amount = Dice.roll(potion.getMinEquipmentEffect(), potion.getMaxEquipmentEffect());
         entity.setInvisibiltyTimer(amount);
      }

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHDRK.getMessage(), entity.getName());

      entity.print(TaMessageManager.YOUDPT.getMessage());
      room.print(entity, messageToRoom, false);

      entity.removeItemFromInventory(potion);
      potion.destroy();
      potion = null;

      return true;
   }

}
