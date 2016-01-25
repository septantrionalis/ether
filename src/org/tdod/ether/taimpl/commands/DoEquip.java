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
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * eq water
 * &MYou can't equip that!
 *
 * eq asd
 * &MSorry, but you don't seem to have one.
 *
 * eq staff
 * &MYou're already wielding weapon.
 *
 * eq robes
 * &MYou're already wearing armor.
 *
 * eq war
 * &WOk, you equipped your warhammer.
 */
public class DoEquip extends Command {

   /**
    * Executes the "equip" command.
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

      // Get the item
      Item itemToEquip = player.getItem(param);

      // The item was not found.
      if (itemToEquip == null) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      // Equip the right type of item.
      if (itemToEquip instanceof Weapon) {
         equipWeapon(player, (Weapon) itemToEquip);
      } else if (itemToEquip instanceof Armor) {
         equipArmor(player, (Armor) itemToEquip);
      } else {
         player.print(TaMessageManager.NOEQTH.getMessage());
         return true;
      }

      return true;
   }

   /**
    * Equip a weapon.
    *
    * @param player The player equiping the weapon.
    * @param weapon The weapon to equip.
    */
   private synchronized void equipWeapon(Player player, Weapon weapon) {
      // Check if the player can use the item.
      if (!weapon.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTWEP.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return;
      }

      // If the item isn't the default weapon, then they will have to unequip it first.
      if (!player.getWeapon().isSimilar(WorldManager.getDefaultWeapon())) {
         player.print(TaMessageManager.ALRHWP.getMessage());
         return;
      }

      // Equip it...
      player.setWeapon(weapon);
      player.removeItemFromInventory(weapon);

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUEQU.getMessage(), weapon.getName());
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHEQU.getMessage(),
            player.getName(), weapon.getLongDescription());
      player.getRoom().print(player, messageToRoom, false);
   }

   /**
    * Equip a piece of armor.
    *
    * @param player The player equiping the armor.
    * @param armor The armor to equip.
    */
   private synchronized void equipArmor(Player player, Armor armor) {
      // If the item isn't the default armor, then they will have to unequip it first.
      if (!player.getArmor().isSimilar(WorldManager.getDefaultArmor())) {
         player.print(TaMessageManager.ALRHAR.getMessage());
         return;
      }

      // Check if the player can use the item.
      if (!armor.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTARM.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return;
      }

      // Equip it...
      player.setArmor(armor);
      player.removeItemFromInventory(armor);

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUEQU.getMessage(), armor.getName());
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHEQU.getMessage(),
            player.getName(), armor.getLongDescription());
      player.getRoom().print(player, messageToRoom, false);
      return;

   }
}
