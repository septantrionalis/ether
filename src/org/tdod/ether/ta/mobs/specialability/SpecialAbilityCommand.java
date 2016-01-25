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

package org.tdod.ether.ta.mobs.specialability;

import java.util.ArrayList;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;

/**
 * This abstract class contains common code for special ability commands.
 * @author Ron Kinney
 */
public abstract class SpecialAbilityCommand {

   /**
    * Gets a random player in the specified room.
    * @param room the room.
    * @return a random player.
    */
   protected Player getRandomPlayerInRoom(Room room) {
      ArrayList<Player> playerList = room.getPlayers();
      int index = Dice.roll(0, playerList.size() - 1);
      Player victimPlayer = playerList.get(index);
      return victimPlayer;
   }

   /**
    * Tumbles a single entity.
    * @param mob the mob that is doing the tumble.
    * @param victim the victim.
    */
   protected void tumbleSingleEntity(Mob mob, Entity victim) {
      WorldManager.getGameMechanics().tumbleEntityOutOfRoom(victim);

      int damage = Dice.roll(mob.getGeneralAttack().getMinDamage(), mob.getGeneralAttack().getMaxDamage());

      victim.takeDamage(damage);
      GameUtil.checkAndHandleKill(mob, victim);

   }

   /**
    * Executes the special ability command.
    * @param mob the mob that executed the special ability.
    * @param room the room that the special ability was executed in.
    */
   public abstract void execute(Mob mob, Room room);

}
