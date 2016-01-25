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

package org.tdod.ether.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains properties that pertain to this game.  All properties are defined in a file.
 * @author rkinney
 */
public final class PropertiesManager {

   private static Log _log = LogFactory.getLog(PropertiesManager.class);

   // CHECKSTYLE:OFF
   public static final String AREA_DIR                     = "area_dir";
   public static final String PLAYER_DIR                   = "player_dir";
   public static final String AREA_DATA_FILE               = "area_data_file";
   public static final String ROOM_DESC_FILE               = "room_desc_file";
   public static final String HELP_DATA_FILE               = "help_data_file";
   public static final String MESSAGE_DATA_FILE            = "message_data_file";
   public static final String TA_MESSAGE_DATA_FILE         = "ta_message_data_file";
   public static final String COMMAND_DATA_FILE            = "command_data_file";
   public static final String COMMAND_TRIGGER_DATA_FILE    = "command_trigger_data_file";   
   public static final String SPELL_COMMAND_DATA_FILE      = "spell_command_data_file";
   public static final String WEAPON_DATA_FILE             = "weapon_data_file";
   public static final String ARMOR_DATA_FILE              = "armor_data_file";
   public static final String MOB_DATA_FILE                = "mob_data_file";
   public static final String RACE_DATA_FILE               = "race_data_file";
   public static final String CLASS_DATA_FILE              = "class_data_file";
   public static final String STAT_RANGE_FILE              = "stat_range_data_file";
   public static final String SPELL_DATA_FILE              = "spell_data_file";
   public static final String MOB_SPEC_ABILITY_DATA_FILE   = "mob_spec_ability_data_file";
   public static final String EQUIPMENT_DATA_FILE          = "equipment_data_file";
   public static final String MOB_WEAPONS_DATA_FILE        = "mob_weapons_data_file";
   public static final String MOTD_FILE                    = "motd_file";
   public static final String STARTING_ROOM                = "starting_room";
   public static final String MAX_INVENTORY_SIZE           = "max_inventory_size";
   public static final String MAX_ITEMS_IN_ROOM            = "max_items_in_room";
   public static final String CHA_MSG_1_LIMIT              = "cha_msg_1_limit";
   public static final String CHA_MSG_2_LIMIT              = "cha_msg_2_limit";
   public static final String CHA_MSG_3_LIMIT              = "cha_msg_3_limit";
   public static final String CHA_MSG_4_LIMIT              = "cha_msg_4_limit";
   public static final String PHY_MSG_1_LIMIT              = "phy_msg_1_limit";
   public static final String PHY_MSG_2_LIMIT              = "phy_msg_2_limit";
   public static final String PHY_MSG_3_LIMIT              = "phy_msg_3_limit";
   public static final String KNO_MSG_1_LIMIT              = "kno_msg_1_limit";
   public static final String KNO_MSG_2_LIMIT              = "kno_msg_2_limit";
   public static final String KNO_MSG_3_LIMIT              = "kno_msg_3_limit";
   public static final String AGI_MSG_1_LIMIT              = "agi_msg_1_limit";
   public static final String AGI_MSG_2_LIMIT              = "agi_msg_2_limit";
   public static final String AGI_MSG_3_LIMIT              = "agi_msg_3_limit";
   public static final String INT_MSG_1_LIMIT              = "int_msg_1_limit";
   public static final String INT_MSG_2_LIMIT              = "int_msg_2_limit";
   public static final String INT_MSG_3_LIMIT              = "int_msg_3_limit";
   public static final String HEALTH_MSG_1_PERCENT         = "health_msg_1_percent";
   public static final String HEALTH_MSG_2_PERCENT         = "health_msg_2_percent";
   public static final String HEALTH_MSG_3_PERCENT         = "health_msg_3_percent";
   public static final String HEALTH_MSG_4_PERCENT         = "health_msg_4_percent";
   public static final String HEALTH_MSG_5_PERCENT         = "health_msg_5_percent";
   public static final String NEW_PLAYER                   = "new_player";
   public static final String EXP_GAIN_VARIANCE            = "exp_gain_variance";
   public static final String DEFAULT_WEAPON               = "default_weapon";
   public static final String DEFAULT_ARMOR                = "default_armor";
   public static final String MAX_THIRST                   = "max_thirst";
   public static final String MAX_HUNGER                   = "max_hunger";
   public static final String ITEM_TIMER_WAKEUP            = "item_timer_wakeup";
   public static final String MIN_SUSTENANCE_DAMAGE        = "min_sustenance_damage";
   public static final String MAX_SUSTENANCE_DAMAGE        = "max_sustenance_damage";
   public static final String SLOW_STATUS_TIMER_WAKEUP     = "slow_status_timer_wakeup";
   public static final String SUSTENANCE_TIMER_WAKEUP      = "sustenance_timer_wakeup";
   public static final String LAIR_REPOP_TIMER_WAKEUP      = "lair_repop_timer_wakeup";
   public static final String LAIR_REPOP_CHANCE            = "lair_repop_chance";
   public static final String REST_TIMER_WAKEUP            = "rest_timer_wakeup";
   public static final String DEATH_ROOM_AWAKEN            = "death_room_awaken";
   public static final String DEATH_REST_TIME              = "death_rest_time";
   public static final String REROLL_REST_TIME             = "reroll_rest_time";
   public static final String ARENA1_MONSTERS              = "arena1_monsters";
   public static final String MAX_MONSTERS_PER_ROOM        = "max_monsters_per_room";
   public static final String RING_GONG_COMBAT_TIME        = "ring_gong_combat_time";
   public static final String RING_GONG_REST_TIME          = "ring_gong_rest_time";
   public static final String ATTACK_COMBAT_WAIT_TIME      = "attack_combat_wait_time";
   public static final String ATTACK_REST_WAIT_TIME        = "attack_rest_wait_time";
   public static final String STARTING_NUM_OF_ATTACKS      = "starting_number_of_attacks";
   public static final String MOB_DEATH_REST_WAIT_TIME     = "mob_death_rest_wait_time";
   public static final String MOB_INITIAL_REST_PERIOD_MIN  = "mob_initial_rest_period_min";
   public static final String MOB_INITIAL_REST_PERIOD_MAX  = "mob_initial_rest_period_max";
   public static final String MOB_ACTIVITY_TIMER_WAKEUP    = "mob_activity_timer_wakeup";
   public static final String NPC_DATA_FILE                = "npc_data_file";
   public static final String REGENERATION_TIMER_WAKEUP    = "regeneration_timer_wakeup";
   public static final String MOB_REGEN_PERCENT            = "mob_regen_percent";
   public static final String MOB_MIN_REGEN                = "mob_min_regen";
   public static final String MOB_MAX_REGEN                = "mob_max_regen";
   public static final String MAX_SPELLS                   = "max_spells";
   public static final String EMOTE_DATA_FILE              = "emote_date_file";
   public static final String BARRIER_DATA_FILE            = "barrier_data_file";
   public static final String TRAPS_DATA_FILE              = "traps_data_file";
   public static final String TELEPORTER_DATA_FILE         = "teleporter_data_file";
   public static final String TREASURES_DATA_FILE          = "treasures_data_file";
   public static final String MANA_REGEN                   = "mana_regen";
   public static final String PLAYER_MELEE_HIT_DIFFICULTY  = "player_melee_hit_difficulty";
   public static final String MOB_MELEE_HIT_DIFFICULTY     = "mob_melee_hit_difficulty";
   public static final String PLAYER_SPELL_HIT_DIFFICULTY  = "player_spell_hit_difficulty";
   public static final String MOB_SPELL_HIT_DIFFICULTY     = "mob_spell_hit_difficulty";
   public static final String PICK_LOCK_DIFFICULTY         = "pick_lock_difficulty";
   public static final String MIN_STAT                     = "min_stat";
   public static final String MAX_STAT                     = "max_stat";
   public static final String PLAYER_EFFECT_TIME           = "player_effect_time";
   public static final String MAX_INVENTORY_GOLD           = "max_inventory_gold";
   public static final String LEVEL_TO_DROP_ITEMS          = "level_to_drop_items";
   public static final String MIN_REROLL_LEVEL             = "min_reroll_level";
   public static final String TAVERN_DRINK_COST            = "tavern_drink_cost";
   public static final String TAVERN_DRINK_VALUE           = "tavern_drink_value";
   public static final String TAVERN_MEAL_COST             = "tavern_meal_cost";
   public static final String TAVERN_MEAL_VALUE            = "tavern_meal_value";
   public static final String SLOTS_COST                   = "slots_cost";
   public static final String SLOTS_JACKPOT_WINNINGS       = "slots_jackpot_winnings";
   public static final String SLOTS_TIER1_WINNINGS         = "slots_tier1_winnings";
   public static final String SLOTS_TIER2_WINNINGS         = "slots_tier2_winnings";
   public static final String SLOTS_TIER3_WINNINGS         = "slots_tier3_winnings";
   public static final String SLOTS_TIER4_WINNINGS         = "slots_tier4_winnings";
   public static final String GAMES_OF_CHANCE_LIMIT        = "games_of_chance_limit";
   public static final String BONES_COST                   = "bones_cost";
   public static final String BONES_LOW_WINNINGS           = "bones_low_winnings";
   public static final String BONES_HIGH_WINNINGS          = "bones_high_winnings";
   public static final String RANDOM_MOB_REPOP_TIMER_WAKEUP="random_mob_repop_timer_wakeup";
   public static final String DUNGEON1_MOB_THRESHOLD       = "dungeon1_mob_threshold";
   public static final String DUNGEON2_MOB_THRESHOLD       = "dungeon2_mob_threshold";
   public static final String DUNGEON3_MOB_THRESHOLD       = "dungeon3_mob_threshold";
   public static final String MOUNTAINS_MOB_THRESHOLD      = "mountains_mob_threshold";
   public static final String FOREST_MOB_THRESHOLD         = "forest_mob_threshold";
   public static final String SWAMP_MOB_THRESHOLD          = "swamp_mob_threshold";
   public static final String DESERT_MOB_THRESHOLD         = "desert_mob_threshold";
   public static final String JUNGLE_MOB_THRESHOLD         = "jungle_mob_threshold";
   public static final String FIRE1_MOB_THRESHOLD          = "fire1_mob_threshold";
   public static final String FIRE2_MOB_THRESHOLD          = "fire2_mob_threshold";
   public static final String SPECIAL_MOB_THRESHOLD        = "special_mob_threshold";
   public static final String TOWN1_MOB_THRESHOLD          = "town1_mob_threshold";
   public static final String TOWN2_MOB_THRESHOLD          = "town2_mob_threshold";
   public static final String TOWN3_MOB_THRESHOLD          = "town3_mob_threshold";
   public static final String TOWN4_MOB_THRESHOLD          = "town4_mob_threshold";
   public static final String LEVEL_TRAINING_COST          = "level_training_cost";
   public static final String STAT_INCREASE_CHANCE         = "stat_increase_chance";
   public static final String THROW_LEVEL                  = "throw_level";
   public static final String THROW_DEPTH                  = "throw_depth";
   public static final String SIGHT_DEPTH                  = "sight_depth";
   public static final String LOOK_DEPTH                   = "look_depth";
   public static final String FIRE_LEVEL                   = "fire_level";
   public static final String GOLD_WEIGHT                  = "gold_weight";
   public static final String MAX_VAULT_TRANSFER           = "max_vault_transfer";
   public static final String HUNTER_SKILL_START           = "hunter_skill_start";
   public static final String HUNTER_SKILL_MAX             = "hunter_skill_max";
   public static final String HUNTER_SKILL_PER_LEVEL       = "hunter_skill_per_level";
   public static final String HUNT_REST_MIN                = "hunt_rest_min";
   public static final String HUNT_REST_MAX                = "hunt_rest_max";
   public static final String HUNT_CONJURE_VNUM            = "hunt_conjure_vnum";
   public static final String TRACK_REST_MIN               = "track_rest_min";
   public static final String TRACK_REST_MAX               = "track_rest_max";
   public static final String MESMERIZE_REST_MIN           = "mesmerize_rest_min";
   public static final String MESMERIZE_REST_MAX           = "mesmerize_rest_max";
   public static final String MESMERIZE_SKILL_CHANCE       = "mesmerize_skill_chance";
   public static final String MESMERIZE_SKILL_PER_LEVEL    = "mesmerize_skill_per_level";
   public static final String MESMERIZE_CHANCE_MIN         = "mesmerize_chance_min";
   public static final String MESMERIZE_CHANCE_MAX         = "mesmerize_chance_max";
   public static final String MESMERIZE_TURN_SKIP          = "mesmerize_turn_skip";
   public static final String TAME_REST_MIN                = "tame_rest_min";
   public static final String TAME_REST_MAX                = "tame_rest_max";
   public static final String TAME_SKILL_CHANCE            = "tame_skill_chance";
   public static final String TAME_SKILL_PER_LEVEL         = "tame_skill_per_level";
   public static final String TAME_CHANCE_MIN              = "tame_chance_min";
   public static final String TAME_CHANCE_MAX              = "tame_chance_max";
   public static final String PASSAGE_COST                 = "passage_cost";
   public static final String PASSAGE_RAT_CHANCE           = "passage_rat_chance";
   public static final String PASSAGE_ROB_CHANCE           = "passage_rob_chance";
   public static final String PASSAGE_ROB_MIN              = "passage_rob_min";
   public static final String PASSAGE_ROB_MAX              = "passage_rob_max";
   public static final String PASSAGE_RAT_CONSUME_VNUM     = "passage_rat_consume_vnum";
   public static final String MOB_HP_PERCENT_FLEE_CHECK    = "mob_hp_percent_flee_check";
   public static final String MOB_FLEE_RESET_CHECK         = "mob_flee_reset_check";
   public static final String MOB_CHASE_CHANCE             = "mob_chase_chance";
   public static final String SAVE_TIMER_WAKEUP            = "save_timer_wakeup";
   public static final String MOB_SPEC_ABILITY_CHANCE      = "mob_spec_ability_chance";
   public static final String SPEC_ABILITY_HEAL_PERCENT    = "spec_ability_heal_percent";
   public static final String SPEC_ABILITY_STEAL_MIN       = "spec_ability_steal_min";
   public static final String SPEC_ABILITY_STEAL_MAX       = "spec_ability_steal_max";
   public static final String EXP_DEATH_PENALTY            = "exp_death_penalty";
   public static final String MAX_TRAILING_LEVEL           = "max_trailing_level";
   public static final String MELEE_EFFECT_CHANCE          = "melee_effect_chance";
   public static final String PARALYSIS_MUTIPLIER          = "paralysis_multiplier";
   public static final String TRIP_MS_BASE                 = "trip_ms_base";
   public static final String TRIP_STAT_MS_STEP            = "trip_stat_ms_step";
   public static final String TRIP_MS_MIN                  = "trip_ms_min";
   public static final String TRIP_CHANCE                  = "trip_chance";
   public static final String TRIP_ITEM_DROP_CHANCE        = "trip_item_drop_change";
   public static final String PROMOTION_COST               = "promotion_cost";
   public static final String PROMOTION_LEVEL              = "promotion_level";
   public static final String OVERHEATING_TIMER_WAKEUP     = "overheating_timer_wakeup";
   public static final String OVERHEATING_MIN_DAMAGE       = "overheating_min_damage";
   public static final String OVERHEATING_MAX_DAMAGE       = "overheating_max_damage";
   public static final String WORLD_RESET_CRON             = "world_reset_cron";
   public static final String WORLD_ITEM_DROPS             = "world_item_drops";
   public static final String DUNGEON1_ITEM_THRESHOLD      = "dungeon1_item_threshold";
   public static final String DUNGEON2_ITEM_THRESHOLD      = "dungeon2_item_threshold";
   public static final String DUNGEON3_ITEM_THRESHOLD      = "dungeon3_item_threshold";
   public static final String MOUNTAINS_ITEM_THRESHOLD     = "mountains_item_threshold";
   public static final String FOREST_ITEM_THRESHOLD        = "forest_item_threshold";
   public static final String SWAMP_ITEM_THRESHOLD         = "swamp_item_threshold";
   public static final String DESERT_ITEM_THRESHOLD        = "desert_item_threshold";
   public static final String JUNGLE_ITEM_THRESHOLD        = "jungle_item_threshold";
   public static final String FIRE1_ITEM_THRESHOLD         = "fire1_item_threshold";
   public static final String FIRE2_ITEM_THRESHOLD         = "fire2_item_threshold";
   public static final String SPECIAL_ITEM_THRESHOLD       = "special_item_threshold";
   public static final String TOWN1_ITEM_THRESHOLD         = "town1_item_threshold";
   public static final String TOWN2_ITEM_THRESHOLD         = "town2_item_threshold";
   public static final String TOWN3_ITEM_THRESHOLD         = "town3_item_threshold";
   public static final String TOWN4_ITEM_THRESHOLD         = "town4_item_threshold";
   public static final String PIT_BARRIER_NUM              = "pit_barrier_num";
   public static final String IDLE_JOB_WAKEUP              = "idle_job_wakeup";
   public static final String IDLE_DISCONNECT_TIME         = "idle_disconnect_time";
   public static final String RECALL_ROOM_VNUM             = "recall_room_vnum";
   public static final String ADMIN_PASSWORD               = "admin_password";
   public static final String CHARM_SPELL_FAILURE          = "charm_spell_failure";
   // CHECKSTYLE:ON
   
   private static final String PROPERTY_KEY = "TaConfigFile";

   private Properties _props = new Properties();
   private static PropertiesManager _instance;

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private PropertiesManager() {
      initialize();
   }

   /**
    * Initializes the PropertiesManager.
    */
   private void initialize() {
      FileInputStream fileInputStream = null;

      try {
         String fileName = System.getProperty(PROPERTY_KEY);
         _log.info("Loading config file " + fileName);
         fileInputStream = new FileInputStream(fileName);
         _props.load(fileInputStream);
      } catch (Exception e) {
         _log.fatal(e, e);
         System.exit(1);
      } finally {
         if (fileInputStream != null) {
            try {
               fileInputStream.close();
            } catch (IOException e) {
               _log.fatal(e, e);
               System.exit(1);
            }
         }
      }
   }

   /**
    * Gets the single instance of the PropertiesManager.
    * @return the single instance of the PropertiesManager.
    */
   public static PropertiesManager getInstance() {
      synchronized (PropertiesManager.class) {
         if (_instance == null) {
            _instance = new PropertiesManager();
         }
      }
      return _instance;
   }

   /**
    * Gets the property based on the key, or null if it was not found.
    * @param key the property key.
    * @return the property based on the key, or null if it was not found.
    */
   public String getProperty(String key) {
      String message = _props.getProperty(key);
      if (message == null) {
         _log.error("key " + key + "was null!");
      }
      return  message;
   }
}
