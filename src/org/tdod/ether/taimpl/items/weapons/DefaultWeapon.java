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

package org.tdod.ether.taimpl.items.weapons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.weapons.enums.SpecialWeaponFunction;

/**
 * The default implementation class of a weapon.
 *
 * @author Ron Kinney
 */
public class DefaultWeapon implements Weapon {

   private static final long serialVersionUID = 6727689801377121218L;

   private static Log _log = LogFactory.getLog(DefaultWeapon.class);

   private String _shortDescription;
   private String _longDescription;

   private int    _cost;
   private int    _weight;
   private int    _minDamage;
   private int    _maxDamage;
   private int    _type;
   private int    _armorRating;
   private SpecialWeaponFunction _specFunction = SpecialWeaponFunction.INVALID;
   private int    _level;
   private Town   _town = Town.INVALID;
   private String _magicAttackMessage;
   private int    _allowableClasses = 0;
   private int    _vnum;
   private int    _ammoVnum;
   private int    _range;

   // Should not be used.  Place holders for values that I have no idea what they do.
   private int _v9;
   private int _v10;
   private int _v11;
   private int _v12;
   private int _v13;

   /**
    * Creates a new DefaultWeapon.
    */
   public DefaultWeapon() {
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
    * Gets the item long description with charges.
    * @return the item long description with charges.
    */
   public String getLongDescriptionWithCharges() {
      return _longDescription;
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
      if ((playerClass.getBit() & _allowableClasses) == playerClass.getBit()) {
         return true;
      }
      return false;
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
      return ItemType.WEAPON;
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
   public Weapon clone(String origin) {
      try {
         Weapon weapon = (Weapon) super.clone();

         weapon.setName(_shortDescription);
         weapon.setLongDescription(_longDescription);
         weapon.setCost(_cost);
         weapon.setWeight(_weight);
         weapon.setMinDamage(_minDamage);
         weapon.setMaxDamage(_maxDamage);
         weapon.setType(_type);
         weapon.setArmorRating(_armorRating);
         weapon.setSpecFunction(_specFunction);
         weapon.setLevel(_level);
         weapon.setTown(_town);
         weapon.setMagicAttackMessage(_magicAttackMessage);
         weapon.setAmmoVnum(_ammoVnum);
         weapon.setRange(_range);
         weapon.setV9(_v9);
         weapon.setV10(_v10);
         weapon.setV11(_v11);
         weapon.setV12(_v12);
         weapon.setV13(_v13);

         WorldManager.getItemsInExistance().put(weapon, origin);
         return weapon;
      } catch (CloneNotSupportedException e) {
         _log.error("Error cloning " + _shortDescription, e);
         return new DefaultWeapon();
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
   // Methods inherited from Weapon.
   //**********

   /**
    * Gets the minimum damage.
    * @return the minimum damage.
    */
   public int getMinDamage() {
      return _minDamage;
   }

   /**
    * Sets the minimum damage.
    * @param minDamage the minimum damage.
    */
   public void setMinDamage(int minDamage) {
      _minDamage = minDamage;
   }

   /**
    * Gets the maximum damage.
    * @return the maximum damage.
    */
   public int getMaxDamage() {
      return _maxDamage;
   }

   /**
    * Sets the maximum damage.
    * @param maxDamage the maximum damage.
    */
   public void setMaxDamage(int maxDamage) {
      _maxDamage = maxDamage;
   }

   /**
    * Gets the magic attack message.
    * @return the magic attack message.
    */
   public String getMagicAttackMessage() {
      return _magicAttackMessage;
   }

   /**
    * Sets the magic attack message.
    * @param magicAttackMessage the magic attack message.
    */
   public void setMagicAttackMessage(String magicAttackMessage) {
      _magicAttackMessage = magicAttackMessage;
   }

   /**
    * Gets the weapon type.
    * @return the weapon type.
    */
   public int getType() {
      return _type;
   }

   /**
    * Sets the weapon type.
    * @param type the weapon type.
    */
   public void setType(int type) {
      _type = type;
   }

   /**
    * Gets the armor rating for the weapon.
    * @return the armor rating for the weapon.
    */
   public int getArmorRating() {
      return _armorRating;
   }

   /**
    * Sets the armor rating for the weapon.
    * @param armorRating the armor rating for the weapon.
    */
   public void setArmorRating(int armorRating) {
      _armorRating = armorRating;
   }

   /**
    * Gets the special weapon function.
    * @return the special weapon function.
    */
   public SpecialWeaponFunction getSpecFunction() {
      return _specFunction;
   }

   /**
    * Sets the special weapon function.
    * @param specFunction the special weapon function.
    */
   public void setSpecFunction(SpecialWeaponFunction specFunction) {
      _specFunction = specFunction;
   }

   /**
    * Gets the town that sells this weapon.
    * @return the town that sells this weapon.
    */
   public Town getTown() {
      return _town;
   }

   /**
    * Sets the town that sells this weapon.
    * @param town the town that sells this weapon.
    */
   public void setTown(Town town) {
      _town = town;
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
    * Sets the weapon ammo vnum.
    * @param ammoVnum the ammo vnum.
    */
   public void setAmmoVnum(int ammoVnum) {
      _ammoVnum = ammoVnum;
   }

   /**
    * Gets the weapon ammo vnum.
    * @return the weapon ammo vnum.
    */
   public int getAmmoVnum() {
      return _ammoVnum;
   }

   /**
    * Sets the weapon range.
    * @param range the weapon range.
    */
   public void setRange(int range) {
      _range = range;
   }

   /**
    * Gets the weapon range.
    * @return the weapon range.
    */
   public int getRange() {
      return _range;
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
