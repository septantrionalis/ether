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

package org.tdod.ether.factory;

import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.cosmos.World;
import org.tdod.ether.ta.engine.GameEngine;
import org.tdod.ether.ta.engine.GameMechanics;
import org.tdod.ether.ta.engine.RandomMobEngine;
import org.tdod.ether.ta.items.RuneScroll;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.PlayerConnectedEvent;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.taimpl.combat.DefaultMeleeResult;
import org.tdod.ether.taimpl.combat.DefaultSpellResult;
import org.tdod.ether.taimpl.cosmos.DefaultWorld;
import org.tdod.ether.taimpl.engine.DefaultGameEngine;
import org.tdod.ether.taimpl.engine.DefaultGameMechanics;
import org.tdod.ether.taimpl.engine.DefaultRandomMobEngine;
import org.tdod.ether.taimpl.items.DefaultRuneScroll;
import org.tdod.ether.taimpl.output.ShellOutput;
import org.tdod.ether.taimpl.player.DefaultPlayerConnection;
import org.tdod.ether.taimpl.player.DefaultVitality;

import com.meyling.telnet.shell.ShellIo;

/**
 * This is a factory class.
 *
 * @author Ron Kinney
 */
public final class DefaultAppFactory {

   /**
    * The bit map to allow all classes.
    */
   private static final int ALL_ALLOWABLE_CLASSES = 255;

   /**
    * The vnum for rune scrolls.
    */
   private static final int RUNE_SCROLL_VNUM = 1000;

   /**
    * Creates a new DefaultAppFactory.  The constructor is private.  This object should not be instantiated.
    */
   private DefaultAppFactory() {
   }

   /**
    * Creates a default game engine.
    *
    * @return a DefaultGameEngine.
    */
   public static GameEngine createDefaultGameEngine() {
      return new DefaultGameEngine();
   }

   /**
    * Creates a default player.
    *
    * @param e a PlayerConnectedEvent.
    *
    * @return a DefaultPlayer.
    */
   public static PlayerConnection createDefaultPlayer(PlayerConnectedEvent e) {
      return new DefaultPlayerConnection(e);
   }

   /**
    * Creates a shell output.
    *
    * @param shellIo a ShellIo.
    *
    * @return a ShellOutput.
    */
   public static GameOutput createShellOutput(ShellIo shellIo) {
      return new ShellOutput(shellIo);
   }

   /**
    * Creates the world.
    *
    * @return a DefaultWorld.
    */
   public static World createWorld() {
      return new DefaultWorld();
   }

   /**
    * Creates a default melee result object.
    *
    * @return a DefaultMeleeResult.
    */
   public static MeleeResult createMeleeResult() {
      return new DefaultMeleeResult();
   }

   /**
    * Creates a default vitality object.
    *
    * @return a DefaultVitality.
    */
   public static Vitality createDefaultVitality() {
      return new DefaultVitality();
   }

   /**
    * Creates a default game mechanics.
    *
    * @return a DefaultGameMechanics.
    */
   public static GameMechanics createGameMechanics() {
      return new DefaultGameMechanics();
   }

   /**
    * Creates a default spell result.
    *
    * @return a DefaultSpellResult.
    */
   public static SpellResult createSpellResult() {
      return new DefaultSpellResult();
   }

   /**
    * Creats a RandomMobEngine.
    *
    * @return a DefaultRandomMobEngine.
    */
   public static RandomMobEngine createRandomMobEngine() {
      return new DefaultRandomMobEngine();
   }

   /**
    * Creates a RuneScroll.
    *
    * @param runeNumber The rune number to create.
    * @return a DefaultRuneScroll.
    */
   public static RuneScroll createRuneScroll(int runeNumber) {
      RuneScroll rs = new DefaultRuneScroll();

      rs.setActivated(true);
      rs.setAllowableClasses(ALL_ALLOWABLE_CLASSES);
      rs.setCost(0);
      rs.setEffectTimer(0);
      rs.setLevel(0);
      rs.setLongDescription("a rune scroll number " + runeNumber);
      rs.setName("rune scroll " + runeNumber);
      rs.setVnum(RUNE_SCROLL_VNUM);
      rs.setWeight(0);
      rs.setRuneNumber(runeNumber);

      return rs;
   }

}
