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
 * This is an enumeration of all messages that the game displays to the player. 
 * @author rkinney
 */
public enum MessageManager {
   LOGIN("login"),
   PROMPT_NAME("prompt_name"),
   NAME_TAKEN("name_taken"),
   INVALID_NAME("invalid_name"),
   ALREADY_CONNECTED("already_connected"),
   VALID_NAME("valid_name"),
   NAME_NOT_FOUND("name_not_found"),
   PROMPT_PASSWORD("prompt_password"),
   PROMPT_NEW_PASSWORD("prompt_new_password"),
   PASSWORD_MISMATCH("password_mismatch"),
   RETYPE_PASSWORD_SHORT("retype_password_short"),
   RETYPE_PASSWORD_LONG("retype_password_long"),
   PROMPT_GENDER("prompt_gender"),
   WRONG_PASSWORD("wrong_password"),

   SYSOP_EDIT("sysop_edit"),

   CONJURE("CONJURE"),
   CONJURE_FAIL("CONJURE_FAIL"),

   KMSG3("KMSG3"),
   AMSG3("AMSG3"),
   IMSG3("IMSG3"),
   
   EXT1("EXT1"),
   ENT1("ENT1"),
   EXT2("EXT2"),
   ENT2("ENT2"),

   CINDERS_STRING("cinders_string"),
   ETHER_STRING("ether_string"),

   
   
   
   
   
   
   
   
   
   

   
   
   
   
   
   
   
   
   
   
   
   
   
   GROUP_ROOM_ARRIVED1("group_room_arrived1"),
   GROUP_ROOM_MOVE1("group_room_move1"),
   GROUP_ROOM_ARRIVED2("group_room_arrived2"),
   GROUP_ROOM_MOVE2("group_room_move2"),

   READY_STRING("ready_string"),
   RESTING_STRING("resting_string"),

   TRAINING_STRING("training_string"),
   TRAINING_SESSION_STRING("training_session_string"),
   PROMOTION_STRING("promotion_string"),

   HEALING_STRING("healing_string"),
   CURING_STRING("curing_string"),
   REMOVAL_STRING("removal_string"),
   RESTORING_STRING("restoring_string"),

   DRINK_STRING("drink_string"),
   MEAL_STRING("meal_string"),
   SLOTS_STRING("slots_string"),
   BONES_STRING("bones_string"),
   BONES_STRING2("bones_string2"),
   PASSAGE_STRING("passage_string"),

   FOLLOW_STRING("follow_string"),
   TRACK_STRING("track_string"),
   STOP_STRING("stop_string"),
   FO_STRING("fo_string"),
   TR_STRING("tr_string"),
   ST_STRING("st_string"),

   /**
    * Not really a message.  I added this so that additional messages are placed above this enumeration without having
    * to constantly add a semicolon at the end of the new enum.  This is out of lazyness.
    */
   LAST_ITEM("");

   private static Log _log = LogFactory.getLog(MessageManager.class);

   private final String      _key;
   private static Properties _props = initialize();

   /**
    * Creates a new MessageManager.
    * @param key the message key.
    */
   MessageManager(String key) {
      _key = key;
   }

   /**
    * Gets the message.
    * @return the message.
    */
   public String getMessage() {
      String message = _props.getProperty(_key);
      if (message == null) {
         _log.error("key " + _key + " was null!");
      }
      return  message;
   }

   /**
    * Initializes all messages by reading the data from a file.
    * @return the Properties object.
    */
   private static Properties initialize() {
      Properties props = new Properties();
      FileInputStream fileInputStream = null;
      try {
         String fileName = PropertiesManager.getInstance().getProperty(PropertiesManager.MESSAGE_DATA_FILE);
         _log.info("Loading message file " + fileName);
         fileInputStream = new FileInputStream(fileName);
         props.load(fileInputStream);
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
      return props;
   }
}
