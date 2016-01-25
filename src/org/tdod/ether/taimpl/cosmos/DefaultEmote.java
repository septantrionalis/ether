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

import org.tdod.ether.ta.cosmos.Emote;

/**
 * A default implementation class for an emote.
 *
 * @author Ron Kinney
 */
public class DefaultEmote implements Emote {

   private String _keyword = null;
   private String _toPlayer = null;
   private String _toTarget = null;
   private String _toRoom = null;

   /**
    * Creates a new DefaultEmote.
    */
   public DefaultEmote() {
   }

   /**
    * Gets the emote keyword.
    *
    * @return the emote keyword.
    */
   public String getKeyword() {
      return _keyword;
   }

   /**
    * Sets the emote keyword.
    *
    * @param keyword the emote keyword.
    */
   public void setKeyword(String keyword) {
      _keyword = keyword;
   }

   /**
    * Gets the player message.
    *
    * @return the player message.
    */
   public String getToPlayer() {
      return _toPlayer;
   }

   /**
    * Sets the player message.
    *
    * @param toPlayer the player message.
    */
   public void setToPlayer(String toPlayer) {
      _toPlayer = toPlayer;
   }

   /**
    * Gets the target message.
    *
    * @return the target message.
    */
   public String getToTarget() {
      return _toTarget;
   }

   /**
    * Sets the target message.
    *
    * @param toTarget target message.
    */
   public void setToTarget(String toTarget) {
      _toTarget = toTarget;
   }

   /**
    * Gets the room message.
    *
    * @return the room message.
    */
   public String getToRoom() {
      return _toRoom;
   }

   /**
    * Sets the room message.
    *
    * @param toRoom the room message.
    */
   public void setToRoom(String toRoom) {
      _toRoom = toRoom;
   }

   //**********
   // Methods inherited from Comparable.
   //**********

   /**
    * Compares this item to another item.
    * @param o1 the other object.
    * @return -1, 0, or 1
    */
   public int compareTo(Emote o1) {
      return this.getKeyword().compareTo(o1.getKeyword());
   }

}
