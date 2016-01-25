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

package org.tdod.ether.taimpl.items.armor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.enums.Town;

/**
 * .
 * 0 = Cost
 * 1 = Weight
 * 5 = Type
 * 6 = Armor Rating
 * 14 = Level
 * 15 = Town
 *                                   0    1   2 3 4 5  6  7 8 9 10 11 12 1314 15
 * cloak:a cloak:                    10   20  0 0 0 11 0  0 0 0 0  0  0  0 0  0
 * robes:robes:                      20   30  0 0 0 11 1  0 0 0 0  0  0  0 1  1
 * cuirass:a leather cuirass:        40   50  0 0 0 12 2  0 0 0 0  0  0  0 2  1
 * breastplate:a steel breastplate:  200  200 0 0 0 13 4  0 0 0 0  0  0  0 2  1
 * ringmail:ringmail armor:          250  300 0 0 0 13 5  0 0 0 0  0  0  0 3  1
 * chainmail:chainmail armor:        300  400 0 0 0 13 6  0 0 0 0  0  0  0 3  1
 * scalemail;scalemail armor:        400  450 0 0 0 14 8  0 0 0 0  0  0  0 4  1
 * bandmail:banded mail armor:       500  500 0 0 0 14 10 0 0 0 0  0  0  0 6  1
 * platemail:platemail armor:        600  600 0 0 0 14 12 0 0 0 0  0  0  0 8  1
 * demonhide:demonhide armor:        1500 250 0 0 0 15 15 0 0 0 0  0  0  0 10 2
 * dragonscale:dragonscale armor:    1800 500 0 0 0 16 18 0 0 0 0  0  0  0 12 2
 *
 * @author Ron Kinney
 */
public class DefaultArmor implements Armor {

   private static final long serialVersionUID = -5335902344564846879L;

   private static Log _log = LogFactory.getLog(DefaultArmor.class);

   private int    _vnum;

   private String _shortDescription;
   private String _longDescription;

   private int  _cost;
   private int  _weight;
   private int  _armorRating;
   private int  _level;
   private int  _type;
   private Town _town = Town.INVALID;
   private int  _allowableClasses = 0;

   private int _v2;
   private int _v3;
   private int _v4;
   private int _v7;
   private int _v8;
   private int _v9;
   private int _v10;
   private int _v11;
   private int _v12;
   private int _v13;

   /**
    * Creates a new DefaultArmor.
    */
   public DefaultArmor() {
   }

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
      return _shortDescription;
   }

   /**
    * Sets the item name.
    * @param shortDescription the item name.
    */
   public void setName(String shortDescription) {
      _shortDescription = shortDescription;
   }

   /**
    * Gets the items long description.
    * @return the items long description.
    */
   public String getLongDescription() {
      return _longDescription;
   }

   /**
    * Gets the item long description with charges.
    * @return the item long description with charges.
    */
   public String getLongDescriptionWithCharges() {
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
    * Determines if the player class can use this item.
    * @param playerClass the player class in question.
    * @return true if the player class can use this item, false otherwise.
    */
   public boolean canUse(PlayerClass playerClass) {
      if ((playerClass.getBit() & _allowableClasses) == playerClass.getBit()) {
         return true;
      }

      return false;
   }

   /**
    * Gets the item type.
    * @return the item type.
    */
   public ItemType getItemType() {
      return ItemType.ARMOR;
   }

   /**
    * Sets the activated state of this item.
    * @param activated the activated state of this item.
    */
   public void setActivated(boolean activated) {
   }

   /**
    * Gets the activated state of this item.
    * @return the activated state of this item.
    */
   public boolean isActivated() {
      return false;
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
      return false;
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
   public Armor clone(String origin) {
      try {
         Armor armor = (Armor) super.clone();

         armor.setName(_shortDescription);
         armor.setLongDescription(_longDescription);
         armor.setCost(_cost);
         armor.setWeight(_weight);
         armor.setArmorRating(_armorRating);
         armor.setLevel(_level);
         armor.setType(_type);
         armor.setTown(_town);
         armor.setV2(_v2);
         armor.setV3(_v3);
         armor.setV4(_v4);
         armor.setV7(_v7);
         armor.setV8(_v8);
         armor.setV9(_v9);
         armor.setV10(_v10);
         armor.setV11(_v11);
         armor.setV12(_v12);
         armor.setV13(_v13);

         WorldManager.getItemsInExistance().put(armor, origin);
         return armor;
      } catch (CloneNotSupportedException e) {
         _log.error("Error cloning " + _shortDescription, e);
         return new DefaultArmor();
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
      if (item.getVnum() == _vnum) {
         return true;
      }
      return false;
   }

   //**********
   // Methods inherited from Armor.
   //**********

   /**
    * Gets the armor rating.
    * @return the armor rating.
    */
   public int getArmorRating() {
      return _armorRating;
   }

   /**
    * Sets the armor rating.
    * @param armorRating the armor rating.
    */
   public void setArmorRating(int armorRating) {
      _armorRating = armorRating;
   }

   /**
    * Sets the value at index 2.
    * @param v2 the value at index 2.
    */
   public void setV2(int v2) {
      _v2 = v2;
   }

   /**
    * Gets the value at index 2.
    * @return the value at index 2.
    */
   public int getV2() {
      return _v2;
   }

   /**
    * Sets  the value at index 3.
    * @param v3 the value at index 3.
    */
   public void setV3(int v3) {
      _v3 = v3;
   }

   /**
    * Gets the value at index 3.
    * @return the value at index 3.
    */
   public int getV3() {
      return _v3;
   }

   /**
    * Sets the value at index 4.
    * @param v4 the value at index 4.
    */
   public void setV4(int v4) {
      _v4 = v4;
   }

   /**
    * Gets the value at index 4.
    * @return the value at index 4.
    */
   public int getV4() {
      return _v4;
   }

   /**
    * Sets the value at index 7.
    * @param v7 the value at index 7.
    */
   public void setV7(int v7) {
      _v7 = v7;
   }

   /**
    * Gets the value at index 7.
    * @return the value at index 7.
    */
   public int getV7() {
      return _v7;
   }

   /**
    * Sets the value at index 8.
    * @param v8 the value at index 8.
    */
   public void setV8(int v8) {
      _v8 = v8;
   }

   /**
    * Gets the value at index 8.
    * @return the value at index 8.
    */
   public int getV8() {
      return _v8;
   }

   /**
    * Sets the value at index 9.
    * @param v9 the value at index 9.
    */
   public void setV9(int v9) {
      _v9 = v9;
   }

   /**
    * Gets the value at index 9.
    * @return the value at index 9.
    */
   public int getV9() {
      return _v9;
   }

   /**
    * Sets the value at index 10.
    * @param v10 the value at index 10.
    */
   public void setV10(int v10) {
      _v10 = v10;
   }

   /**
    * Gets the value at index 10.
    * @return the value at index 10.
    */
   public int getV10() {
      return _v10;
   }

   /**
    * Sets the value at index 11.
    * @param v11 the value at index 11.
    */
   public void setV11(int v11) {
      _v11 = v11;
   }

   /**
    * Gets the value at index 11.
    * @return the value at index 11.
    */
   public int getV11() {
      return _v11;
   }

   /**
    * Sets the value at index 12.
    * @param v12 the value at index 12.
    */
   public void setV12(int v12) {
      _v12 = v12;
   }

   /**
    * Gets the value at index 12.
    * @return the value at index 12.
    */
   public int getV12() {
      return _v12;
   }

   /**
    * Sets the value at index 13.
    * @param v13 the value at index 13.
    */
   public void setV13(int v13) {
      _v13 = v13;
   }

   /**
    * Gets the value at index 13.
    * @return the value at index 13.
    */
   public int getV13() {
      return _v13;
   }

   /**
    * Gets the armor type.
    * @return the armor type.
    */
   public int getType() {
      return _type;
   }

   /**
    * Sets the armor type.
    * @param type the armor type.
    */
   public void setType(int type) {
      _type = type;
   }

   /**
    * Gets the town that this item is sold in.
    * @return the town that this item is sold in.
    */
   public Town getTown() {
      return _town;
   }

   /**
    * Sets the town this item is sold in.
    * @param town the town this item is sold in.
    */
   public void setTown(Town town) {
      _town = town;
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
