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

package org.tdod.ether.taimpl.engine;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.combat.MeleeResultEnum;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.combat.SpellResultEnum;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.DropItemFailCode;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.engine.GameMechanics;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Stat;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default implementation class of the game mechanics.
 *
 * @author Ron Kinney
 */
public class DefaultGameMechanics implements GameMechanics {

   private static Log _log = LogFactory.getLog(DefaultGameMechanics.class);

   private static final int DEATH_TIMER = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.DEATH_REST_TIME));
   private static final int MOB_DEATH_REST_WAIT_TIME = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_DEATH_REST_WAIT_TIME));
   private static final int PLAYER_MELEE_HIT_DIFFICULTY = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_MELEE_HIT_DIFFICULTY));
   private static final int MOB_MELEE_HIT_DIFFICULTY =  Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_MELEE_HIT_DIFFICULTY));
   private static final int MAX_STAT = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_STAT));
   private static final int PICK_LOCK_DIFFICULTY = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PICK_LOCK_DIFFICULTY));
   private static final double EXP_DEATH_PENALTY = Double.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.EXP_DEATH_PENALTY));
   private static final int TRIP_MS_BASE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TRIP_MS_BASE));
   private static final int TRIP_STAT_MS_STEP = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TRIP_STAT_MS_STEP));
   private static final int TRIP_MS_MIN = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TRIP_MS_MIN));
   private static final int TRIP_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TRIP_CHANCE));
   private static final int TRIP_ITEM_DROP_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.TRIP_ITEM_DROP_CHANCE));
   private static final int CHARM_SPELL_FAILURE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.CHARM_SPELL_FAILURE));

   /**
    * Creates a new DefaultGameMechanics.
    */
   public DefaultGameMechanics() {
      _log.info("Using " + this.getClass());
   }

   /**
    * Determines if an entity trips while moving.
    *
    * @param entity the entity
    * @return true if the entity tripped.
    */
   public boolean entityTripped(Entity entity) {
      if (entity.getIgnoreTrip()) {
         return false;
      }

      long moveTimeDiff = System.currentTimeMillis() - entity.getLastMove();
      int tripModifier = TRIP_MS_BASE - (entity.getStats().getAgility().getTripModifier() * TRIP_STAT_MS_STEP);

      if (tripModifier < TRIP_MS_MIN) {
         tripModifier = TRIP_MS_MIN;
      }

      if (moveTimeDiff < tripModifier) {
         int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
         if (roll < TRIP_CHANCE || entity.isTripping()) {
            entity.print(TaMessageManager.YOUTRP.getMessage());
            String messageToRoom = MessageFormat.format(TaMessageManager.OTHTRP.getMessage(), entity.getName());
            entity.getRoom().print(entity, messageToRoom, false);
            entity.setIsTripping(true);
            entity.setLastMove(System.currentTimeMillis());

            // Determine if an item gets dropped.
            int inventorySize = entity.getInventory().size();
            if (inventorySize > 0) {
               roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
               if (roll < TRIP_ITEM_DROP_CHANCE) {
                  roll = Dice.roll(0, inventorySize - 1);
                  Item item = entity.getInventory().get(roll);
                  DropItemFailCode code = entity.getRoom().dropItemInRoom(item);
                  if (code.equals(DropItemFailCode.NONE)) {
                     entity.removeItemFromInventory(item);
                  }
               }
            }

            return true;
         }
      } else {
         entity.setIsTripping(false);
      }

      return false;
   }

   /**
    * Determines if an entity picked a locked door.
    *
    * @param entity the entity
    * @param consumes set to 1 if the door can be picked by a rogue.
    * @return true if the door is picked.
    */
   public boolean pickedLock(Entity entity, int consumes) {
      if (entity.getEntityType().equals(EntityType.MOB)) {
         return false;
      }

      int chance = 0;
      if (consumes == 1) {
         chance = PICK_LOCK_DIFFICULTY + entity.getStats().getIntellect().getRogueSkillBonus();
      }

      if (Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE) < chance) {
         return true;
      }

      return false;
   }

   /**
    * Does whatever is needed when the player pleases the gods.  In this case, a random stat will be increased
    * by one point up to the maximum specified in the properties file.
    *
    * @param entity the entity who please the gods.
    */
   public void handlePleasedGods(Entity entity) {
      int roll = Dice.roll(0, 5);

      if (roll == 0) {
         increaseStat(entity.getStats().getIntellect());
      } else if (roll == 1) {
         increaseStat(entity.getStats().getKnowledge());
      } else if (roll == 2) {
         increaseStat(entity.getStats().getPhysique());
      } else if (roll == 3) {
         increaseStat(entity.getStats().getStamina());
      } else if (roll == 4) {
         increaseStat(entity.getStats().getAgility());
      } else if (roll == 5) {
         increaseStat(entity.getStats().getCharisma());
      }

      return;
   }

   /**
    * The chance to please the gods when donating.
    *
    * @param entity The player donating.
    * @param amount The amount being donated.
    *
    * @return true of the player pleased the gods, false otherwise.
    */
   public boolean donate(Entity entity, int amount) {
      if (amount < 10) {
         return false;
      }

      int chance = Dice.roll(1, 60000);
      if (chance < 10) {
         return true;
      }

      return false;
   }

   /**
    * Determines the amount of experience gained for casting utility and heal spells.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell
    * @return the amount of exp gained.
    */
   public int getExpForUtilitySpells(Entity entity, Spell spell) {
      return 10 * entity.getLevel();
   }

   /**
    * &YThe frog has just gone to the northeast.
    * &YA frog returned with a perplexed look on its face.
    *
    * This method tumbles both players and mobs out of the room.  I'm not sure if TA handles tumbling this
    * way.
    *
    * @param entity the entity.
    */
   public void tumbleEntityOutOfRoom(Entity entity) {
      // Dont let a an entity tumble out of the arena or a safe room.
      Room room = entity.getRoom();
      if (RoomFlags.ARENA.isSet(room.getRoomFlags())
            || RoomFlags.SAFE.isSet(room.getRoomFlags())) {
         return;
      }

      Exit randomExit = room.getRandomExit();
      Room destinationRoom = WorldManager.getRoom(randomExit.getToRoom());

      // Don't tumble into a safe room or the arena.
      if (RoomFlags.ARENA.isSet(destinationRoom.getRoomFlags())
            || RoomFlags.SAFE.isSet(destinationRoom.getRoomFlags())) {
           return;
        }

      MoveFailCode code = entity.moveToRoom(randomExit.getToRoom(), false);

      if (code.equals(MoveFailCode.BARRIER)
            || code.equals(MoveFailCode.EXIT_LOCKED)
            || code.equals(MoveFailCode.NO_EXIT)
            || code.equals(MoveFailCode.NO_MOBS_ALLOWED)
            || code.equals(MoveFailCode.ROOM_FULL)) {
         room.print(entity, entity.getMessageHandler().getCameBackMessage(), false);
         return;
      }

      String messageToDestinationRoom;
      String targetName;

      if (entity.getEntityType().equals(EntityType.PLAYER)) {
         targetName = entity.getName();
      } else {
         targetName = "The " + entity.getName();
      }

      ExitDirectionEnum destinationExitEnum = GameUtil.getOppositeExit(randomExit.getExitDirection());
      if (randomExit.getExitDirection().equals(ExitDirectionEnum.DOWN)
            || randomExit.getExitDirection().equals(ExitDirectionEnum.UP)) {
         messageToDestinationRoom = MessageFormat.format(TaMessageManager.ENT13.getMessage(),
               targetName, "", destinationExitEnum.getAltDescription());
      } else {
         messageToDestinationRoom = MessageFormat.format(TaMessageManager.ENT13.getMessage(),
               targetName, "the ", destinationExitEnum.getLongDescription());
      }

      String messageToPlayer = MessageFormat.format(
            TaMessageManager.OOP2.getMessage(), randomExit.getExitDirection().getLongDescription());
      String messageToOriginatingRoom = MessageFormat.format(
            TaMessageManager.EXT13.getMessage(), targetName, randomExit.getExitDirection().getLongDescription());

      entity.print(messageToPlayer);
      room.print(entity, messageToOriginatingRoom, false);
      destinationRoom.print(entity, messageToDestinationRoom, false);

   }

   /**
    * Handles a players death.
    *
    * @param player the player who died.
    * @param playerDeathMsg the death message sent to the player.
    */
   public void handlePlayerDeath(Player player, String playerDeathMsg) {
      player.print(playerDeathMsg);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHDED.getMessage(), player.getName());

      player.getRoom().print(player, messageToRoom, false);

      player.handleDeath(DEATH_TIMER);

      Item soulstone = null;
      for (Item item:player.getInventory()) {
         if (ItemType.EQUIPMENT.equals(item.getItemType())) {
            Equipment eq = (Equipment) item;
            if (EquipmentSubType.SOULSTONE.equals(eq.getEquipmentSubType())) {
               soulstone = eq;
               break;
            }
         }
      }
      
      if (soulstone != null) {
         player.removeItemFromInventory(soulstone);
         soulstone.destroy();
      } else {
         player.setExperience((long) ((float) player.getExperience() * EXP_DEATH_PENALTY));
         player.clearInventory();
         player.setGold(0);
      }
   }

   /**
    * Handles the death of a mob.
    *
    * @param source the entity who killed the mob.
    * @param mob the mob who died.
    */
   public void handleMobDeath(Entity source, Mob mob) {
      String messageToRoom = MessageFormat.format(TaMessageManager.MONDEF.getMessage(), mob.getName());
      mob.getRoom().print(null, messageToRoom, false);

      // Determine if the mob was killed from a distance.
      boolean rangedAttack = false;
      if (source == null || !source.getRoom().equals(mob.getRoom())) {
         rangedAttack = true;
      }

      if (source != null) {
         int gold = WorldManager.getGameMechanics().calculateMobGoldDrop(mob);
         String message;
         if (!rangedAttack) {
            if (gold > 0) {
               if (mob.isLairMob()) {
                  message = MessageFormat.format(TaMessageManager.GTLTRS.getMessage(), gold, mob.getName());
               } else {
                  message = MessageFormat.format(TaMessageManager.GOTTRS.getMessage(), gold, mob.getName());
               }
               source.print(message);
               source.addGold(gold);
            }
         }

         source.setCombatTicker(0);
         source.setRestTicker(MOB_DEATH_REST_WAIT_TIME);
         source.getAttacks().reset();

         if (mob.getInventory().size() > 0) {
            if (rangedAttack) {
               Item item = mob.getInventory().get(0);
               mob.removeItemFromInventory(item);
               DropItemFailCode code = mob.getRoom().dropItemInRoom(item);
               if (!code.equals(DropItemFailCode.NONE)) {
                  // Oh well!
                  _log.info(mob.getName() + " dropped " + item.getName() + " and got error code " + code);
               }
            } else {
               Item item = mob.getInventory().get(0);
               mob.removeItemFromInventory(item);
               InventoryFailCode inventoryFailCode = source.placeItemInInventory(item, false);
               if (inventoryFailCode.equals(InventoryFailCode.NONE)) {
                  String messageToPlayer = MessageFormat.format(TaMessageManager.GOTITM.getMessage(), item.getName());
                  source.print(messageToPlayer);
               } else if (inventoryFailCode.equals(InventoryFailCode.RUNE_SCROLL)) {
                  source.print(TaMessageManager.TRS012.getMessage());
               } else {
                  if (!inventoryFailCode.equals(InventoryFailCode.RUNE_SCROLL_DUD)) {
                     String messageToPlayer = MessageFormat.format(TaMessageManager.NGTITM.getMessage(), item.getName());
                     source.print(messageToPlayer);
                     DropItemFailCode dropItemFailCode = mob.getRoom().dropItemInRoom(item);
                     if (!dropItemFailCode.equals(DropItemFailCode.NONE)) {
                        // Oh well!
                        _log.info(mob.getName() + " dropped " + item.getName() + " and got error code " + dropItemFailCode);
                     }
                  }
               }
            }
         }
      }

      mob.getGroupLeader().getGroupList().remove(mob);
      mob.getRoom().removeMob(mob);
      mob.destroy();
      mob = null;
   }

   /**
    * Determines if a spell is successful.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell being cast.
    * @return true if the spell is successful.
    */
   public boolean isSpellSuccess(Entity entity, Spell spell) {
      // lower to make spells succeed more, lower for more failures.
      int baseHit = entity.getSpellHitDifficulty();
      int spellDifficulty = entity.getLevel() - spell.getLevel();

      int toHit = baseHit - spellDifficulty;

      // Some chance of failure.
      if (toHit < 1) {
         toHit = 2;
      }

      if (Dice.roll(1, 20) < toHit) {
         return false;
      }

       return true;
   }

   /**
    * Determines if a spell is successful.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell being cast.
    * @return true if the spell is successful.
    */
   public boolean isCharmSpellSuccess(Entity entity, Mob mob, Spell spell) {
      // Not really sure what to base the charm spell success on.  Don't want players charming the demon king.
      // The regeneration flag works for now since powerful mobs have this enabled.
      if (mob.getRegeneration() > 0) {
         return false;
      }
      // lower to make spells succeed more, lower for more failures.
      int baseHit = entity.getSpellHitDifficulty();
      int spellDifficulty = entity.getLevel() - spell.getLevel();

      int toHit = baseHit - spellDifficulty;

      // Some chance of failure.
      if (toHit < 1) {
         toHit = 2;
      }

      if (Dice.roll(1, 20) < toHit) {
         return false;
      }

      // Some random failure chance.
      if (Dice.roll(1, 100) < CHARM_SPELL_FAILURE) {
         return false;
      }
      
     
      return true;      
   }

   /**
    * Calculates the spell result of an area utility spell.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell.
    * @return a SpellResult
    */
   public SpellResult calculateAreaUtilitySpellResult(Entity entity, Spell spell) {
      SpellResult spellResult = DefaultAppFactory.createSpellResult();

      spellResult.setSpellResultEnum(SpellResultEnum.SUCCESS);

      int value = Dice.roll(spell.getMinSpellEffect(), spell.getMaxSpellEffect());

      if (spell.scalesWithLevel()) {
         value = value * entity.getLevel();
      }

      value += value * (entity.getStats().getKnowledge().getFriendlySpellBonus() / 100F);

      spellResult.setNumberEffect(value);
      return spellResult;
   }

   /**
    * Calculates the spell result of an area attack spell.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell.
    * @return a SpellResult
    */
   public SpellResult handleAreaSpellDamage(Entity entity, Spell spell) {
      SpellResult spellResult = DefaultAppFactory.createSpellResult();

      spellResult.setSpellResultEnum(SpellResultEnum.SUCCESS);

      int damage = Dice.roll(spell.getMinSpellEffect(), spell.getMaxSpellEffect());

      if (spell.scalesWithLevel()) {
         damage = damage * entity.getLevel();
      }

      damage += damage * (entity.getStats().getIntellect().getOffensiveSpellPotency() / 100F);

      spellResult.setNumberEffect(damage);
      return spellResult;

   }

   /**
    * Calculates the spell result of a utility spell.
    *
    * @param entity the entity who cast the spell.
    * @param victim the target.
    * @param spell the spell.
    * @return a SpellResult
    */
   public SpellResult calculateUtilitySpellResult(Entity entity, Entity victim, Spell spell) {
      SpellResult spellResult = DefaultAppFactory.createSpellResult();

      int effect = Dice.roll(spell.getMinSpellEffect(), spell.getMaxSpellEffect());
      if (spell.scalesWithLevel()) {
         effect = effect * entity.getLevel();
      }

      effect += effect * (entity.getStats().getKnowledge().getFriendlySpellBonus() / 100F);

      spellResult.setSpellResultEnum(SpellResultEnum.SUCCESS);
      spellResult.setNumberEffect(effect);

      return spellResult;
   }

   /**
    * Calculates the spell result of an offensive spell.
    *
    * @param source the entity who cast the spell.
    * @param victim the target.
    * @param spell the spell.
    * @return a SpellResult
    */
   public SpellResult calculateOffensiveSpellResult(Entity source, Entity victim, Spell spell) {
      SpellResult spellResult = DefaultAppFactory.createSpellResult();

      int levelDifference = source.getLevel() - victim.getLevel();
      int combatSkillMod = (victim.getCombatSkill() - 70) / 10;
      int spellDefense = 1 - levelDifference + combatSkillMod;
      if (spellDefense < 1) {
         spellDefense = 1;
      }
      if (Dice.roll(1, 20) <= spellDefense) {
         spellResult.setSpellResultEnum(SpellResultEnum.NEGATED);
         return spellResult;
      }

      spellResult.setSpellResultEnum(SpellResultEnum.SUCCESS);

      int damage = Dice.roll(spell.getMinSpellEffect(), spell.getMaxSpellEffect());

      if (spell.scalesWithLevel()) {
         damage = damage * source.getLevel();
      }

      damage += damage * (source.getStats().getIntellect().getOffensiveSpellPotency() / 100F);

      spellResult.setNumberEffect(damage);
      return spellResult;
   }

   /**
    * Sometimes, even I wonder how I come about getting these formulas.  In summary, this method does the following:
    *
    * if spellLevel == 1, baseWaitTime = 15
    * else baseWaitTime=27 + spell level.
    * minimum baseWaitTime is 5
    *
    * modify baseWaitTime by plus or minus (baseTime/10) * 2
    *
    * In other words, the higher the level of the spell, the greater the variance and wait time.
    *
    * @param entity the entity casting the spell.
    * @param spell the spell.
    */
   public void handleMentalRestDelay(Entity entity, Spell spell) {
      float baseWait;
      if (spell.getLevel() == 1) {
         baseWait = 15;
      } else {
         baseWait = 27 + spell.getLevel();
      }
      baseWait -= entity.getLevel();
      if (baseWait < 5) {
         baseWait = 5;
      }

      float variance = (baseWait / 10) * 2;
      int random = Dice.roll(1, (int) variance);
      if (Dice.roll(0, 1) == 0) {
         random = -random;
      }
      baseWait += random;
      entity.setMentalExhaustionTicker((int) baseWait);
   }

   /**
    * Determine the mobs health.  The data file doesnt store vitality information directly, so I assume this is
    * calculated via other means... like mob level and mob combat skill.
    *
    * @param mobCombatSkill the combat skill of the mob.
    * @param mobLevel the level of the mob.
    * @param mobVariance the mob variance.
    *
    * @return the vitality of the mob.
    */
   public Vitality calculateMobHealth(int mobCombatSkill, int mobLevel, float mobVariance) {
      int num = (mobCombatSkill * mobLevel) / 3;
      Vitality vitality = DefaultAppFactory.createDefaultVitality();
      vitality.setCurVitality((int) (num * mobVariance));
      vitality.setMaxVitality((int) (num * mobVariance));

      return vitality;
   }

   /**
    * Fuck if I know how exp is calculated.  It would be easier to just generate a random number!
    * This is my best guess, which involves the players level and mobs level.
    *
    * @param playerLevel the level of the player.
    * @param mobLevel the level of the mob.
    * @param mobVariance the mob variance.
    *
    * @return experience point per point of damage.
    */
   public float getExpPerPointOfDamage(int playerLevel, int mobLevel, float mobVariance) {
      int levelDifference = mobLevel - playerLevel;
      float expBase;

      // Slider to adjust exp gained.  Lower = less exp.  Higher = more exp.  This should be a number greater than 0 and
      // less than 2 (ie, .66, 1, 1.2, etc)
      String floatString = PropertiesManager.getInstance().getProperty(PropertiesManager.EXP_GAIN_VARIANCE);
      float expBaseScale = new Float(floatString).floatValue();

      if (levelDifference == -1) {
//         expBase = 1.125f;
          expBase = 2.0f;
      } else if (levelDifference == 0) {
         expBase = 3.5f;
      } else if (levelDifference >= 2) {
         expBase = 9.5f;
      } else if (levelDifference >= 1) {
         expBase = 6.5f;
      } else {
//        expBase = 0;
          expBase = 2.0f;
      }

      return expBase * mobVariance * expBaseScale * playerLevel;
   }

   /**
    * Determines the melee result of a mob attacking an entity.
    *
    * @param attacker the attacking mobs.
    * @param victim the victim.
    * @return a MeleeResult.
    */
   public MeleeResult doMobAttackEntity(Mob attacker, Entity victim) {
      MeleeResult result = DefaultAppFactory.createMeleeResult();
      determineMobMeleeHit(attacker, victim, result);
      determineMobMeleeDamage(attacker, victim, result);

      return result;
   }

   /**
    * Determines if a mob attack hit.
    *
    * @param mob the mob who is attacking.
    * @param victim the target.
    * @param meleeResult The MeleeResult.
    */
   private void determineMobMeleeHit(Mob mob, Entity victim, MeleeResult meleeResult) {
      int base = MOB_MELEE_HIT_DIFFICULTY;          // high to make the mob miss more, lower to make them hit more often
      int mobLevel = mob.getLevel();
      int playerLevel = victim.getLevel();
      int diff = (playerLevel - mobLevel) * 2;      // Multiply by two to make level difference really count.
      int combatSkillMod = (mob.getCombatSkill() - 70) / 10;

      int toHit = base + diff - combatSkillMod - mob.getHitDice();

      // Determine dodge.
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);

      if (roll < toHit + victim.getStats().getAgility().getDodgeBonus()) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.DODGE);
         return;
      }

      // Determine miss.
      int diceSize = 20;
      roll = Dice.roll(1, diceSize);

      // Automatic hit or miss.
      if (roll == 20) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.HIT);
         return;
      }

      if (roll == 1) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.MISS);
         return;
      }

      // Determine hit/miss.
      if (roll >= toHit) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.HIT);
         return;
      } else {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.MISS);
         return;
      }

   }

   /**
    * Determines the amount of damage a mob did with an attack.
    *
    * @param mob the mob who is attacking.
    * @param victim the target.
    * @param meleeResult a MeleeResult.
    */
   private void determineMobMeleeDamage(Mob mob, Entity victim, MeleeResult meleeResult) {
      int finalDamage = 0;
      if (meleeResult.getMeleeResultEnum().equals(MeleeResultEnum.HIT)) {
         int minDamage;
         int maxDamage;
         String weaponString = null;
         if (mob.getGeneralAttack().getMobWeaponType() != null) {
            minDamage = mob.getMobWeapon().getMinDamage();
            maxDamage = mob.getMobWeapon().getMaxDamage();
            weaponString = mob.getMobWeapon().getName();
         } else {
            minDamage = mob.getGeneralAttack().getMinDamage();
            maxDamage = mob.getGeneralAttack().getMaxDamage();
            weaponString = mob.getGeneralAttack().getWeapon();
         }

         int baseDamage = Dice.roll(minDamage, maxDamage);
         finalDamage = baseDamage - victim.getArmorRating();
         if (finalDamage <= 0) {
            finalDamage = 0;
            meleeResult.setMeleeResultEnum(MeleeResultEnum.GLANCE);
         }
         meleeResult.setWeapon(weaponString);
      }
      meleeResult.setDamage(finalDamage);
   }

   /**
    * Determines the melee result of a thrown attack.
    *
    * @param player the attacker.
    * @param target the target.
    * @param thrownEq the equipment being thrown.
    * @return a MeleeResult.
    */
   public MeleeResult doPlayerThrowAttack(Player player, Entity target, Equipment thrownEq) {
      MeleeResult result = DefaultAppFactory.createMeleeResult();
      determinePlayerMeleeHit(player, target, result);
      determinePlayerThrowDamage(player, target, result, thrownEq);
      return result;
   }

   /**
    * Determines the damage from a thrown attack.
    *
    * @param player the attacking player.
    * @param target the target.
    * @param meleeResult A MeleeResult.
    * @param thrownEq the equipment being thrown.
    */
   private void determinePlayerThrowDamage(Player player, Entity target, MeleeResult meleeResult, Equipment thrownEq) {
      int finalDamage = 0;
      if (meleeResult.getMeleeResultEnum().equals(MeleeResultEnum.HIT)) {
         int baseDamage = Dice.roll(thrownEq.getMinEquipmentEffect(), thrownEq.getMaxEquipmentEffect());
         finalDamage = baseDamage + player.getStats().getPhysique().getPhysicalDamageBonus() - target.getArmorRating();
         if (finalDamage <= 0) {
            finalDamage = 0;
            meleeResult.setMeleeResultEnum(MeleeResultEnum.GLANCE);
         }
      }
      meleeResult.setDamage(finalDamage);
   }

   /**
    * Determines the melee result of a player melee attack.
    *
    * @param player the attacking player.
    * @param target the target.
    * @return a MeleeResult.
    */
   public MeleeResult doPlayerMeleeAttack(Player player, Entity target) {
      MeleeResult result = DefaultAppFactory.createMeleeResult();
      determinePlayerMeleeHit(player, target, result);
      determinePlayerMeleeDamage(player, target, result);
      return result;
   }

   /**
    * Calculates the amount of gold a mob drops.
    *
    * @param mob the mob
    * @return the amount of gold dropped.
    */
   public int calculateMobGoldDrop(Mob mob) {
      int minValue = mob.getGold();
      int maxValue = minValue * 2;

      return Dice.roll(minValue, maxValue);
   }

   /**
    * hit chance = base + diff + mobArmor - intBonus.
    * diff = mobLevel-charLevel;
    * base = 12 (high is harder, lower is easier)
    * d20 >= hit chance
    *
    * @param player the attacking player.
    * @param target the target.
    * @param meleeResult a MeleeResult.
    */
   private void determinePlayerMeleeHit(Player player, Entity target, MeleeResult meleeResult) {
      int base = PLAYER_MELEE_HIT_DIFFICULTY;       // Make higher for harder overall mobs.
      int mobLevel = target.getLevel();
      int playerLevel = player.getLevel();
      int diff = (mobLevel - playerLevel) * 2;      // Multiply by two to make level difference really count.
      int intBonus = player.getStats().getIntellect().getMeleeHitBonus();
      int combatSkillMod = (target.getCombatSkill() - 70) / 10;

      int toHit = base + diff + combatSkillMod - intBonus;

      // Determine dodge.  Use the same toHit number for both dodge and miss.  There's a smaller chance to dodge.
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);

      if (roll < toHit) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.DODGE);
         return;
      }

      // Determine miss.
      int diceSize = 20;
      roll = Dice.roll(1, diceSize);

      // Automatic hit or miss.
      if (roll == 20) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.HIT);
         return;
      }

      if (roll == 1) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.MISS);
         return;
      }

      // Determine hit/miss.
      if (roll >= toHit) {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.HIT);
         return;
      } else {
         meleeResult.setMeleeResultEnum(MeleeResultEnum.MISS);
         return;
      }

   }

   /**
    * Determines the amount of damage from a player melee attack.
    *
    * @param player the attacking player.
    * @param target the target.
    * @param meleeResult a MeleeResult.
    */
   private void determinePlayerMeleeDamage(Player player, Entity target, MeleeResult meleeResult) {
      int finalDamage = 0;
      if (meleeResult.getMeleeResultEnum().equals(MeleeResultEnum.HIT)) {
         int baseDamage = Dice.roll(player.getWeapon().getMinDamage(), player.getWeapon().getMaxDamage());
         finalDamage = baseDamage + player.getStats().getPhysique().getPhysicalDamageBonus() - target.getArmorRating();
         if (finalDamage <= 0) {
            finalDamage = 0;
            meleeResult.setMeleeResultEnum(MeleeResultEnum.GLANCE);
         } else if (isCriticalHit(player, target)) {
            meleeResult.setMeleeResultEnum(MeleeResultEnum.CRIT);
            finalDamage = finalDamage * 2;
         }
      }
      meleeResult.setDamage(finalDamage);
   }

   /**
    * Determines if an attack was a critical hit.
    *
    * @param player the attacking player.
    * @param target the target.
    * @return true if the attack was a critical hit.
    */
   private boolean isCriticalHit(Player player, Entity target) {
      if (!player.getPlayerClass().equals(PlayerClass.ROGUE)) {
         return false;
      }

      int mobLevel = target.getLevel();
      int playerLevel = player.getLevel();
      int diff = (playerLevel - mobLevel) * 2; // Multiply the difference by two to make mob level really count.

      int critChance = diff + player.getStats().getIntellect().getRogueSkillBonus();
      int diceSize = 20;
      int roll = Dice.roll(1, diceSize);

      // Some chance of success.
      if (roll == 1) {
         return true;
      }
      
      // Some chance of failure.
      if (roll == 20) {
         return false;
      }
      
      if (roll <= critChance) {
         return true;
      }

      return false;
   }

   /**
    * Permanently increases a stat.  This method checks for max stats.
    *
    * @param stat the stat to increase.
    */
   private void increaseStat(Stat stat) {
      int value = stat.getValue();
      if (value < MAX_STAT) {
         stat.setValue(value + 1);
      }
   }

}
