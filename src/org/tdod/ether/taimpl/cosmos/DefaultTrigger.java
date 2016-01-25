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

package org.tdod.ether.taimpl.cosmos;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.cosmos.Teleporter;
import org.tdod.ether.ta.cosmos.Trap;
import org.tdod.ether.ta.cosmos.Treasure;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default implementation class for a trigger.
 *
 * @author Ron Kinney
 */
public class DefaultTrigger implements Trigger {

   private static final long serialVersionUID = 3646942416488761434L;

   private static Log _log = LogFactory.getLog(DefaultTrigger.class);

   private TriggerType _triggerType;
   private int         _v2;
   private int         _v3;
   private int         _v4;
   private int         _v5;
   private int         _v6;
   private int         _v7;

   private boolean     _isTriggered = false;

   /**
    * Creates a new DefaultTrigger.
    */
   public DefaultTrigger() {
   }

   /**
    * Gets the trigger type.
    *
    * @return the trigger type.
    */
   public TriggerType getTriggerType() {
      return _triggerType;
   }

   /**
    * Sets the trigger type.
    *
    * @param triggerType the trigger type.
    */
   public void setTriggerType(TriggerType triggerType) {
      _triggerType = triggerType;
   }

   /**
    * Gets the value at index 2.
    *
    * @return the value at index 2.
    */
   public int getV2() {
      return _v2;
   }

   /**
    * Sets the value at index 2.
    *
    * @param v2 the value at index 2.
    */
   public void setV2(int v2) {
      _v2 = v2;
   }

   /**
    * Gets the value at index 3.
    *
    * @return the value at index 3.
    */
   public int getV3() {
      return _v3;
   }

   /**
    * Sets the value at index 3.
    *
    * @param v3 the value at index 3.
    */
   public void setV3(int v3) {
      _v3 = v3;
   }

   /**
    * Gets the value at index 4.
    *
    * @return the value at index 4.
    */
   public int getV4() {
      return _v4;
   }

   /**
    * Sets the value at index 4.
    *
    * @param v4 the value at index 4.
    */
   public void setV4(int v4) {
      _v4 = v4;
   }

   /**
    * Gets the value at index 5.
    *
    * @return the value at index 5.
    */
   public int getV5() {
      return _v5;
   }

   /**
    * Sets the value at index 5.
    *
    * @param v5 the value at index 5.
    */
   public void setV5(int v5) {
      _v5 = v5;
   }

   /**
    * Gets the value at index 6.
    *
    * @return the value at index 6.
    */
   public int getV6() {
      return _v6;
   }

   /**
    * Sets the value at index 6.
    *
    * @param v6 the value at index 6.
    */
   public void setV6(int v6) {
      _v6 = v6;
   }

   /**
    * Gets the value at index 7.
    *
    * @return the value at index 7.
    */
   public int getV7() {
      return _v7;
   }

   /**
    * Sets the value at index 7.
    *
    * @param v7 the value at index 7.
    */
   public void setV7(int v7) {
      _v7 = v7;
   }

   /**
    * Checks if this trigger has been triggered.
    *
    * @return true if this trigger has been triggered.
    */
   public boolean isTriggered() {
      return _isTriggered;
   }

   /**
    * Sets the triggered state.
    *
    * @param triggered the triggered state.
    */
   public void setTriggered(boolean triggered) {
      _isTriggered = triggered;
   }

   /**
    * Gets a string representation of this object.
    *
    * @return a string representation of this object.
    */
   public String toString() {
      return getTriggerType() + ", triggered=" + _isTriggered + ", " + getV2() + " "
         + getV3() + " " + getV4() + " " + getV5() + " " + getV6() + " " + getV7();
   }

   /**
    * Handles the specified room trigger.
    *
    * @param entity the entity that triggered the trigger.
    *
    * @return the trigger result.
    */
   public TriggerResult handleTrigger(Entity entity) {
      if (getTriggerType().equals(TriggerType.TELEPORT)) {
         return doTeleport(entity);
      } else if (getTriggerType().equals(TriggerType.TRAP)) {
         return doTrap(entity);
      } else if (getTriggerType().equals(TriggerType.TREASURE)) {
         return doTreasure(entity);
      } else if (getTriggerType().equals(TriggerType.NONE)) {
         return TriggerResult.NOTHING;
       } else if (getTriggerType().equals(TriggerType.PUZZLE)) {
          System.currentTimeMillis(); // Suppress checkstyle error.
      } else {
         _log.error("Unhandled trigger " + getTriggerType() + " for room " + entity.getRoom().getRoomNumber());
         return TriggerResult.NOTHING;
      }

      return TriggerResult.NOTHING;
   }

   /**
    * Teleports an entity.
    *
    * @param entity the entity to teleport.
    *
    * @return a trigger result.
    */
   private TriggerResult doTeleport(Entity entity) {
      // A V7 of 1 indicates that the teleporter is enabled.  Otherwise, it is disabled.
      if (_v7 != 1) {
         return TriggerResult.NOTHING;
      }
      Teleporter teleporter = WorldManager.getTeleporter(getV3());
      entity.println("&b" + teleporter.getVictimMessage());

      int teleportTo = getV2();
      entity.teleportToRoom(teleportTo);

      return TriggerResult.TELEPORTED;
   }

   /**
    * Handles a trap.
    *
    * @param entity the lucky entity who triggered the trap.
    *
    * @return a trigger result.
    */
   private TriggerResult doTrap(Entity entity) {
      if (isTriggered()) {
         return TriggerResult.NOTHING;
      }

      Trap trap = WorldManager.getTrap(getV2());
      entity.println("&R" + trap.getMessage());

      int damage = Dice.roll(trap.getMinDamage(), trap.getMaxDamage());
      if (entity.takeDamage(damage)) {
         return TriggerResult.DEATH;
      }

      if (getV3() > 0) {
         entity.setStatus(Status.POISONED);
         entity.setPoisonDamage(getV3());
      }

      return TriggerResult.NOTHING;
   }

   /**
    * Handles treasure.
    *
    * @param entity the entity getting the treasure.
    *
    * @return a trigger result.
    */
   private TriggerResult doTreasure(Entity entity) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return TriggerResult.NOTHING;
      }

      if (isTriggered()) {
         return TriggerResult.NOTHING;
      }

      Player player = (Player) entity;

      Rune rune = Rune.getRune(getV3());
      if (player.getRune().getIndex() < rune.getIndex()) {
         if (rune.equals(Rune.WHITE)) {
            player.print(TaMessageManager.TRS001.getMessage());
            player.setRune(Rune.WHITE);
         } else if (rune.equals(Rune.YELLOW)) {
            player.print(TaMessageManager.TRS002.getMessage());
            player.setRune(Rune.YELLOW);
         } else if (rune.equals(Rune.BLUE)) {
            player.print(TaMessageManager.TRS004.getMessage());
            player.setRune(Rune.BLUE);
         } else {
            player.println("&C" + "Invalid rune found, notify an admin.");
            _log.error("Invalid rune " + rune + " for player " + player.getName() + " in room "
                  + entity.getRoom().getRoomNumber());
         }
         player.print(TaMessageManager.TRS011.getMessage());
      }

      Treasure treasure = WorldManager.getTreasure(getV2());
      if (treasure != null) {
         int amount = Dice.roll(treasure.getMinValue(), treasure.getMaxValue());
         String messageToPlayer = MessageFormat.format(treasure.getMessage(), amount);
         player.println("&Y" + messageToPlayer);
         player.addGold(amount);
      }

      setTriggered(true);

      _log.info(player.getName() + " got treasure in room " + entity.getRoom().getRoomNumber());

      return TriggerResult.NOTHING;
   }
}
