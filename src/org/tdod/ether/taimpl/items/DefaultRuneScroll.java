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

package org.tdod.ether.taimpl.items;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.RuneScroll;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.enums.Town;

/**
 * The default implementation class of a rune scroll.
 * @author Ron Kinney
 */
public class DefaultRuneScroll implements RuneScroll {

   private static final long serialVersionUID = 2385701928111260356L;

   private static Log _log = LogFactory.getLog(DefaultRuneScroll.class);

   private int    _vnum;
   private String _name;
   private String _longDescription;
   private int    _cost;
   private int    _weight;
   private int    _level;
   private int    _allowableClasses;
   private boolean _activated;
   private int    _runeNumber;

   //**********
   // Methods inherited from Item.
   //**********

   /**
    * Gets the item vnum.
    * @return the item vnum.
    */
   public int getVnum() {
      return _vnum;
   }

   /**
    * Sets the item vnum.
    * @param vnum the item vnum.
    */
   public void setVnum(int vnum) {
      _vnum = vnum;
   }

   /**
    * Gets the item name.
    * @return the item name.
    */
   public String getName() {
      return _name;
   }

   /**
    * Sets the item name.
    * @param name the item name.
    */
   public void setName(String name) {
      _name = name;
   }

   /**
    * Gets the items long description.
    * @return the items long description.
    */
   public String getLongDescription() {
      return _longDescription;
   }

   /**
    * Sets the items long description.
    * @param longDescription the items long description.
    */
   public void setLongDescription(String longDescription) {
      _longDescription = longDescription;
   }

   /**
    * Gets the item long description with charges.
    * @return the item long description with charges.
    */
   public String getLongDescriptionWithCharges() {
      return _name;
   }

   /**
    * Gets the item cost.
    * @return the item cost.
    */
   public int getCost() {
      return _cost;
   }

   /**
    * Sets the item cost.
    * @param cost the item cost.
    */
   public void setCost(int cost) {
      _cost = cost;
   }

   /**
    * Gets the item weight.
    * @return the item weight.
    */
   public int getWeight() {
      return _weight;
   }

   /**
    * Sets the item weight.
    * @param weight the item weight.
    */
   public void setWeight(int weight) {
      _weight = weight;
   }

   /**
    * Gets the item level.
    * @return the item level.
    */
   public int getLevel() {
      return _level;
   }

   /**
    * Sets the item level.
    * @param level the item level.
    */
   public void setLevel(int level) {
      _level = level;
   }

   /**
    * Determines if the player class can use this item.
    * @param playerClass the player class in question.
    * @return true if the player class can use this item, false otherwise.
    */
   public boolean canUse(PlayerClass playerClass) {
      return true;
   }

   /**
    * Sets the allowable classes for this item.
    * @param allowableClasses the allowable classes for this item.
    */
   public void setAllowableClasses(int allowableClasses) {
      _allowableClasses = allowableClasses;
   }

   /**
    * Gets the allowable classes for this item.
    * @return the allowable classes for this item.
    */
   public int getAllowableClasses() {
      return _allowableClasses;
   }

   /**
    * Gets the item type.
    * @return the item type.
    */
   public ItemType getItemType() {
      return ItemType.RUNE_SCROLL;
   }

   /**
    * Sets the activated state of this item.
    * @param activated the activated state of this item.
    */
   public void setActivated(boolean activated) {
      _activated = activated;
   }

   /**
    * Gets the activated state of this item.
    * @return the activated state of this item.
    */
   public boolean isActivated() {
      return _activated;
   }

   /**
    * Sets the effect timer.
    * @param timer the effect timer.
    */
   public void setEffectTimer(int timer) {
   }

   /**
    * Decreases the effect timer.
    * @param value decreases the effect timer by the value.
    * @return true if the timer hit 0 or below.
    */
   public boolean decreaseTimer(int value) {
      return true;
   }

   /**
    * Handles an expired timer.
    * @param entity the entity who owns the item.
    * @param displayMessage the message to display.
    */
   public void handleExpiredTimer(Entity entity, boolean displayMessage) {
   }

   /**
    * Clones this item. It is important to destroy items if they are not in use!
    *
    * @param origin A descriptive string indicating what cloned this object. Used
    * for debugging.
    * @return a clone of this item.
    * @see destroy()
    */
   public Item clone(String origin) {
      try {
         RuneScroll rs = (RuneScroll) super.clone();

         rs.setName(_name);
         rs.setVnum(_vnum);
         rs.setLongDescription(_longDescription);
         rs.setCost(_cost);
         rs.setWeight(_weight);
         rs.setLevel(_level);
         rs.setAllowableClasses(_allowableClasses);
         rs.setActivated(_activated);
         rs.setRuneNumber(_runeNumber);

         WorldManager.getItemsInExistance().put(rs, origin);
         return rs;
      } catch (CloneNotSupportedException e) {
         _log.error("Error cloning " + _name, e);
         return new DefaultRuneScroll();
      }

   }

   /**
    * Destroy's this item.
    *
    * @see clone()
    */
   public void destroy() {
      WorldManager.getItemsInExistance().remove(this);
   }

   /**
    * Checks if this item is similar to another item.  Since items are clones of each other, the equals call will only
    * return true if the other item is an exact copy.  This call determines if one torch is the same as another torch.
    * @param item the item in question.
    * @return true if the item is similar.
    */
   public boolean isSimilar(Item item) {
      return false;
   }

   /**
    * Gets the town that sells this rune scroll.
    * @return the town that sells this ru.
    */
   public Town getTown() {
      return Town.NONE;
   }

   //**********
   // Methods inherited from RuneScroll.
   //**********

   /**
    * Sets the rune number.
    * @param runeNumber the rune number.
    */
   public void setRuneNumber(int runeNumber) {
      _runeNumber = runeNumber;
   }

   /**
    * Gets the rune number.
    * @return the rune number.
    */
   public int getRuneNumber() {
      return _runeNumber;
   }

   //**********
   // Methods inherited from Comparable.
   //**********

   /**
    * Compares this item to another item.
    * @param o1 the other object.
    * @return -1, 0, or 1
    */
   public int compareTo(Item o1) {
      if (this.getCost() == o1.getCost()) {
         return 0;
      } else if (this.getCost() > o1.getCost()) {
         return 1;
      } else {
         return -1;
      }
   }

}
