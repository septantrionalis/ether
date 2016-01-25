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
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.ItemUseConfig;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.player.Mana;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.debug.DoGoto;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.items.DefaultItemUseConfig;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.items.weapons.enums.SpecialWeaponFunction;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Notes -- uses all attacks, can still cast.
 *    No level Check
 * Rest is same as melee
 *
 * &MSorry, but you don't seem to have one.                                         WE
 * &MSorry, that object cannot be used in that manner.                              WE
 * &MSorry, hostility is not permitted here.                                        WE
 * &MYou are still physically exhausted from your previous activities!              WE
 * &MSorry, warriors may not use that item.                                         WE
 * &MSorry, you can't use harmful items on yourself.                                WE
 * &MThat item needs to be aimed at a specific person or creature.                  WE
 * &MSorry, you don't see "great" nearby.                                           WE
 *
 * &BRon Kinney just discharged a rod of flame at the white orchid!
 * &MYou discharged your rod of flame at the white orchid for 24 damage!
 *
 * &MYou discharged your rod of flame at Minex for 47 damage!
 * &RRon Kinney discharged a rod of flame at you for 47 damage!
 * ----
 * &MYou discharged a bolt of lightning from your levinblade at the greater demon
 * for 22 damage!
 * &BMinex just discharged a bolt of lightning from a levinblade at the greater
 * demon!
 * ----
 * &MYou discharged your rod of flame at the greater demon for 43 damage!
 * &WYour rod of flame just burned out.
 * ->consume rod
 * @author minex
 *
 */
public class DoUse extends Command {

   private static final String RECALL_ROOM_VNUM = PropertiesManager.getInstance().getProperty(PropertiesManager.RECALL_ROOM_VNUM);

   /**
    * Executes the "withdraw" command.
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

      String[] split = input.split(" ", THREE_PARAMS);

      if (split.length < 2) {
         return false;
      }

      String param = split[1].toLowerCase();

      Player player = (Player) entity;

      // Check inventory first.
      Item item = player.getItem(param);
      // Nothing found... check if the player wants the wielded weapon.
      if (item == null && MyStringUtil.contains(player.getWeapon().getName(), param)) {
         item = player.getWeapon();
      }

      // Sorry, but you don't seem to have one.
      if (item == null) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      Entity victim = null;
      if (split.length == THREE_PARAMS) {
         victim = GameUtil.getTarget(player, player.getRoom(), split[2]);
      }
      ItemUseConfig itemUseConfig;
      if (item.getItemType().equals(ItemType.WEAPON)) {
         itemUseConfig = getWeaponUseConfig(player, victim, (Weapon) item);
      } else if (item.getItemType().equals(ItemType.EQUIPMENT)) {
         itemUseConfig = getEquipmentUseConfig(player, victim, (Equipment) item);
      } else {
         itemUseConfig = new DefaultItemUseConfig();
         itemUseConfig.setNotUseable(true);
      }

      if (itemUseConfig.isHeartstone()) {
         handleHeartstone(player, (Equipment) item);
         return true;
      }

      if (itemUseConfig.isManastone()) {
         handleManastone(player, (Equipment) item);
         return true;
      }

      if (itemUseConfig.isRunestaff()) {
         handleRunestaff(player, (Equipment) item);
         return true;
      }

      if (itemUseConfig.isRodOfPower()) {
         handleRodOfPower(player, (Equipment) item);
         return true;
      }

      // Sorry, that object cannot be used in that manner.
      if (itemUseConfig.isNotUseable()) {
         player.print(TaMessageManager.NOTUSE.getMessage());
         return true;
      }

      // Sorry, hostility is not permitted here.
      if (RoomFlags.SAFE.isSet(player.getRoom().getRoomFlags())) {
         player.print(TaMessageManager.NOFTHR.getMessage());
         return true;
      }

      // You are still physically exhausted from your previous activities!
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // Sorry, warriors may not use that item.
      if (!item.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTITM.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // Sorry, you can't use harmful items on yourself.
      if (victim != null && victim.equals(player)) {
         player.print(TaMessageManager.NOUSLF.getMessage());
         return true;
      }

      // That item needs to be aimed at a specific person or creature.
      if (split.length < THREE_PARAMS) {
         player.print(TaMessageManager.INDTRG.getMessage());
         return true;
      }

      // Sorry, you don't see "great" nearby.
      if (victim == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), split[2]);
         entity.print(message);
         return true;
      }

      // Geeze... finally.
      handleAttack(player, victim, itemUseConfig, item);

      return true;
   }

   /**
    * Creates an ItemUseConfig object from a weapon.
    *
    * @param player The player using the item.
    * @param victim The target.
    * @param weapon The weapon being used.
    *
    * @return an ItemUseConfig
    */
   private ItemUseConfig getWeaponUseConfig(Player player, Entity victim, Weapon weapon) {
      ItemUseConfig useConfig = new DefaultItemUseConfig();
      if (!weapon.getSpecFunction().equals(SpecialWeaponFunction.MAGIC)
            || player.getInventory().contains(weapon)) {
         useConfig.setNotUseable(true);
         return useConfig;
      }

      int damage = Dice.roll(weapon.getMinDamage(), weapon.getMaxDamage());
      useConfig.setDamage(damage);
      useConfig.setBurnsCharges(false);

      if (victim != null) {
         String messageToPlayer;
         String messageToVictim;
         String messageToRoom;
         if (victim.getEntityType().equals(EntityType.MOB)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.USMDM2.getMessage(),
                  weapon.getMagicAttackMessage(), weapon.getName(), victim.getName(), useConfig.getDamage());

            messageToRoom = MessageFormat.format(TaMessageManager.USMOT2.getMessage(),
                  player.getName(), weapon.getMagicAttackMessage(), weapon.getLongDescription(), victim.getName());

            messageToVictim = "";
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.USEDM2.getMessage(),
                  weapon.getMagicAttackMessage(), weapon.getName(), victim.getName(), useConfig.getDamage());

            messageToRoom = MessageFormat.format(TaMessageManager.USEOT2.getMessage(),
                  player.getName(), weapon.getMagicAttackMessage(), weapon.getLongDescription(), victim.getName());

            messageToVictim = MessageFormat.format(TaMessageManager.USEYU2.getMessage(),
                  player.getName(), weapon.getMagicAttackMessage(), weapon.getLongDescription(), useConfig.getDamage());
         }

         useConfig.setPlayerMessage(messageToPlayer);
         useConfig.setVictimMessage(messageToVictim);
         useConfig.setRoomMessage(messageToRoom);

      }

      return useConfig;
   }

   /**
    * Creates an ItemUseConfig object from a piece of equipment.
    *
    * @param player The player using the item.
    * @param victim The target.
    * @param equipment The equipment being used.
    *
    * @return an ItemUseConfig
    */
   private ItemUseConfig getEquipmentUseConfig(Player player, Entity victim, Equipment equipment) {
      ItemUseConfig useConfig = new DefaultItemUseConfig();
      
      useConfig.setUseConfigAttributes(equipment);
      if (useConfig.isNonCombatItem()) {
         return useConfig;
      }

      if (!equipment.getEquipmentSubType().equals(EquipmentSubType.WAND)) {
         useConfig.setNotUseable(true);
         return useConfig;
      }

      int damage = Dice.roll(equipment.getMinEquipmentEffect(), equipment.getMaxEquipmentEffect());
      useConfig.setDamage(damage);
      useConfig.setBurnsCharges(true);

      if (victim != null) {
         String messageToPlayer;
         String messageToVictim;
         String messageToRoom;

         if (victim.getEntityType().equals(EntityType.MOB)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.USMDM1.getMessage(),
                  equipment.getName(), victim.getName(), useConfig.getDamage());

            messageToRoom = MessageFormat.format(TaMessageManager.USMOT1.getMessage(),
                  player.getName(), equipment.getLongDescription(), victim.getName());

            messageToVictim = "";
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.USEDM1.getMessage(),
                  equipment.getName(), victim.getName(), useConfig.getDamage());

            messageToRoom = MessageFormat.format(TaMessageManager.USEOT1.getMessage(),
                  player.getName(), equipment.getLongDescription(), victim.getName());

            messageToVictim = MessageFormat.format(TaMessageManager.USEYU1.getMessage(),
                  player.getName(), equipment.getLongDescription(), useConfig.getDamage());
         }
         useConfig.setPlayerMessage(messageToPlayer);
         useConfig.setRoomMessage(messageToRoom);
         useConfig.setVictimMessage(messageToVictim);
      }

      return useConfig;
   }

   /**
    * Handles an attack.
    *
    * @param player The player issuing the use command.
    * @param victim The target.
    * @param itemUseConfig An ItemUseConfig object.
    * @param item The item being used.
    */
   private void handleAttack(Player player, Entity victim, ItemUseConfig itemUseConfig, Item item) {
      int vitalityBefore = victim.getVitality().getCurVitality();

      player.print(itemUseConfig.getPlayerMessage());
      victim.print(itemUseConfig.getVictimMessage());
      player.getRoom().print(player, victim, itemUseConfig.getRoomMessage(), false);

      victim.takeDamage(itemUseConfig.getDamage());

      if (itemUseConfig.isBurnsCharges() && item.getItemType().equals(ItemType.EQUIPMENT)) {
         Equipment eq = (Equipment) item;
         eq.subractCharge();
         System.out.println(eq.getCharges());
         if (eq.getCharges() <= 0) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.BRNOUT.getMessage(), eq.getName());
            player.print(messageToPlayer);
            player.removeItemFromInventory(eq);
            eq.destroy();
            eq = null;
         }
      }

      GameUtil.giveExperience(player, victim, vitalityBefore);
      player.getAttacks().setAttacksLeft(0);
      player.setRestTicker(GameUtil.randomizeRestWaitTime());
      GameUtil.checkAndHandleKill(player, victim);
   }

   /**
    * Handles the runestaff.
    *
    * @param player The player using the runestaff.
    * @param eq The runestaff object.
    */
   private void handleRunestaff(Player player, Equipment eq) {
      if (eq.getCharges() == 0) {
         if (player.getRoom().getRoomNumber() == eq.getQuestStat()) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.YOURN2.getMessage(), eq.getName());
            player.print(messageToPlayer);
            String messageToRoom = MessageFormat.format(TaMessageManager.OTHRN2.getMessage(),
                  player.getName(), eq.getLongDescription());
            player.getRoom().print(player, messageToRoom, false);

            if (player.getRune().getIndex() < Rune.GREEN.getIndex()) {
               player.print(TaMessageManager.TRS011.getMessage());
               player.setRune(Rune.GREEN);
            }

            player.removeItemFromInventory(eq);
            eq.destroy();
         } else {
            String messageToPlayer = MessageFormat.format(TaMessageManager.NRNHER.getMessage(), eq.getName());
            player.print(messageToPlayer);
         }

      } else {
         String messageToPlayer = MessageFormat.format(TaMessageManager.YOURN1.getMessage(), eq.getName(), eq.getCharges());
         player.print(messageToPlayer);
         String messageToRoom = MessageFormat.format(TaMessageManager.OTHRN1.getMessage(),
               player.getName(), eq.getLongDescription());
         player.getRoom().print(player, messageToRoom, false);
      }
   }

   /**
    * Handles the rod of power.
    * @param player the player using the rod.
    * @param eq the rod of power.
    */
   private void handleRodOfPower(Player player, Equipment eq) {
      int groupSize = eq.getV10();
      int room = eq.getQuestStat();
      String messageToPlayer;

      // Can only be used in a specific room.
      if (player.getRoom().getRoomNumber() != room) {
         messageToPlayer = MessageFormat.format(TaMessageManager.NRNHER.getMessage(), eq.getName());
         player.print(messageToPlayer);
         return;
      }

      // Need a minimum number of players in the group.
      if (GameUtil.getPlayersInGroup(player).size() < groupSize) {
         player.print(TaMessageManager.NOSUPP.getMessage());
         return;
      }

      // Can't use the rod with mobs in the room -- not sure if TA actually does this.
      if (!player.getRoom().getMobs().isEmpty()) {
         player.print(TaMessageManager.CDTNOW.getMessage());
         return;
      }

      // Send messages that the player used the rod.
      messageToPlayer = MessageFormat.format(TaMessageManager.YOURN2.getMessage(), eq.getName());
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHRN2.getMessage(), player.getName(), eq.getName());
      player.getRoom().print(player, messageToRoom, false);

      // Leader of the group gets the rune.
      if (player.getGroupLeader().getEntityType().equals(EntityType.PLAYER)) {
         Player targetPlayer = (Player) player.getGroupLeader();
         if (targetPlayer.getRune().getIndex() < Rune.VIOLET.getIndex()) {
            targetPlayer.print(TaMessageManager.TRS011.getMessage());
            targetPlayer.setRune(Rune.VIOLET);
         }
      }

      player.removeItemFromInventory(eq);
      eq.destroy();
   }

   /**
    * Handles the heartstone.
    * @param player the player using the heartstone.
    * @param eq the heartstone.
    */
   private void handleHeartstone(Player player, Equipment eq) {
      if (!eq.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTITM.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return;
      }      

      DoGoto cmd = new DoGoto();
      cmd.executeSysopCommand(player, "goto " + RECALL_ROOM_VNUM);

      player.removeItemFromInventory(eq);
      eq.destroy();      
   }

   /**
    * use manastone
    * &WYour manastone hums softly, and glows with a bluish light as you rub it.
    * &WThe glow from your manastone moves up your arm to permeate your entire
    * &Wbody.
    * &WThe glow and your manastone slowly fade away...
    * ---
    * &WMinex is rubbing a manastone.
    * &WMinex is suddenly surrounded by an aura of bluish light which slowly fades
    * &Waway...
    * if spellcaster:
    * cur_mana += maxMana * .5; if cur_mana > maxMana, nothing
    * if nonspellcaster:
    * can use like normal, but no mana gain.
    * @param player
    * @param eq
    */
   private void handleManastone(Player player, Equipment eq) {
      if (!eq.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTITM.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return;
      }

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOURUB2.getMessage(),
            eq.getName(), eq.getName(), eq.getName());
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHRUB.getMessage(),
            player.getName(), eq.getName(), player.getName());
      player.getRoom().print(player, messageToRoom, false);

      Mana playerMana = player.getMana();
      int modifiedMana = playerMana.getCurMana();
      if (playerMana.getCurMana() <= playerMana.getMaxMana()) {
         double manaPlus = playerMana.getMaxMana() * 1;
         modifiedMana = playerMana.getCurMana() + (int)(manaPlus);
      }
      playerMana.setCurMana(modifiedMana);

      player.removeItemFromInventory(eq);
      eq.destroy();
   }
   
}
