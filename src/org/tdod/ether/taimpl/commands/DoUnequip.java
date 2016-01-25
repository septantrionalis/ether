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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * 1) Space First
 * 2) Then check if dagger.
 *
 * un asdf
 * &MSorry, but you don't seem to have one equipped.
 *
 * un war
 * &MYou can't carry anything else.
 *
 * un war
 * &WOk, you unequipped your warhammer.
 *
 * un dagger
 * &MWithout that dagger, you'd be defenseless!
 *
 * un cloak
 * &MWithout that cloak, you'd be awfully vulnerable!
 *
 */
public class DoUnequip extends Command {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoUnequip.class);

   /**
    * Executes the "unequip" command.
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
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();

      // Check if the item is a weapon first.
      Item item = player.getWeapon();
      if (MyStringUtil.contains(item.getName(), param)) {
         unequipWeapon(player, (Weapon) item);
         return true;
      }

      // Check if the item is a piece of armor next.
      item = player.getArmor();
      if (MyStringUtil.contains(item.getName(), param)) {
         unequipArmor(player, (Armor) item);
         return true;
      }

      // Neither is true...
      player.print(TaMessageManager.NOTEQU.getMessage());
      return true;
   }

   /**
    * Determines if an item can be unequiped.
    *
    * @param player The player who is attempting to unequip an item.
    * @param item The item being unequiped.
    *
    * @return true if the item can be unequiped.
    */
   private boolean validateItem(Player player, Item item) {
      InventoryFailCode code = player.placeItemInInventory(item, true);
      // Player does not have enough inventory space.
      if (code.equals(InventoryFailCode.SPACE)) {
         player.print(TaMessageManager.INVFUL.getMessage());
         return false;
      }

      // Shouldn't happen, but just incase.
      if (!code.equals(InventoryFailCode.NONE)) {
         _log.error("Got a fail code that isn't being handled: " + code + " while unequipping "
               + item.getName() + " for " + player.getName());
         return false;
      }

      return true;
   }

   /**
    * Unequip a weapon.
    *
    * @param player The player who is issuing the command.
    * @param weapon The weapon to unequip.
    */
   private synchronized void unequipWeapon(Player player, Weapon weapon) {
      // Don't unequip the default weapon.
      if (weapon.isSimilar(WorldManager.getDefaultWeapon())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NOTWEP.getMessage(), weapon.getName());
         player.print(messageToPlayer);
         return;
      }

      if (!validateItem(player, weapon)) {
         return;
      }

      // Success...
      player.setWeapon(WorldManager.getDefaultWeapon());

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUUEQ.getMessage(), weapon.getName());
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHUEQ.getMessage(),
            player.getName(), weapon.getLongDescription());
      player.getRoom().print(player, messageToRoom, false);

      return;
   }

   /**
    * Unequip an armor.
    *
    * @param player The player who is issuing the command.
    * @param armor The armor to unequip.
    */
   private synchronized void unequipArmor(Player player, Armor armor) {
      // Don't unequip the default armor.
      if (armor.isSimilar(WorldManager.getDefaultArmor())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NOTARM.getMessage(), armor.getName());
         player.print(messageToPlayer);
         return;
      }

      if (!validateItem(player, armor)) {
         return;
      }

      // Success...
      player.setArmor(WorldManager.getDefaultArmor());

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUUEQ.getMessage(), armor.getName());
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHUEQ.getMessage(),
            player.getName(), armor.getLongDescription());
      player.getRoom().print(player, messageToRoom, false);

      return;
   }
}
