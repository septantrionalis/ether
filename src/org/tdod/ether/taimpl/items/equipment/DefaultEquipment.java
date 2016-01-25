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

package org.tdod.ether.taimpl.items.equipment;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default implementation class for a piece of equipment.
 * @author Ron Kinney
 */
public class DefaultEquipment implements Equipment {

   private static final long serialVersionUID = 8511844522548684316L;

   private static Log _log = LogFactory.getLog(DefaultEquipment.class);

   private int              _vnum;
   private String           _name;
   private String           _description;
   private int              _cost;
   private int              _weight;
   private int              _v2;
   private int              _minEquipmentEffect;
   private int              _maxEquipmentEffect;
   private EquipmentType    _equipmentType = EquipmentType.INVALID;
   private int              _v6;
   private int              _charges;
   private EquipmentSubType _equipmentSubType = EquipmentSubType.INVALID;
   private int              _range;
   private int              _v10;
   private int              _questStat;
   private int              _v12;
   private int              _v13;
   private int              _level;
   private Town             _town = Town.INVALID;
   private String           _message;
   private int              _allowableClasses = 0;

   private int              _effectTimer = 0;
   private boolean          _isActivated;

   /**
    * Creates a new DefaultEquipment.
    */
   public DefaultEquipment() {
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
    * Gets the item long description with charges.
    * @return the item long description with charges.
    */
   public String getLongDescriptionWithCharges() {
      StringBuffer buffer = new StringBuffer();

      buffer.append(_description);
      switch (_equipmentSubType) {
      case WATER :
      case AMMO :
         buffer.append("(" + _charges + ")");
         break;
      default:
      }

      return buffer.toString();
   }

   /**
    * Gets the items long description.
    * @return the items long description.
    */
   public String getLongDescription() {
      return _description;
   }

   /**
    * Sets the items long description.
    * @param description the items long description.
    */
   public void setLongDescription(String description) {
      _description = description;
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
      return ItemType.EQUIPMENT;
   }

   /**
    * Sets the activated state of this item.
    * @param activated the activated state of this item.
    */
   public void setActivated(boolean activated) {
      _isActivated = activated;
   }

   /**
    * Gets the activated state of this item.
    * @return the activated state of this item.
    */
   public boolean isActivated() {
      return _isActivated;
   }

   /**
    * Sets the effect timer.
    * @param timer the effect timer.
    */
   public void setEffectTimer(int timer) {
      _effectTimer = timer;
   }

   /**
    * Decreases the effect timer.
    * @param value decreases the effect timer by the value.
    * @return true if the timer hit 0 or below.
    */
   public boolean decreaseTimer(int value) {
      _effectTimer -= value;
      if (_effectTimer  <= 0) {
         return true;
      }
      return false;
   }

   /**
    * Handles an expired timer.
    * @param entity the entity who owns the item.
    * @param displayMessage the message to display.
    */
   public void handleExpiredTimer(Entity entity, boolean displayMessage) {
      if (_equipmentSubType.equals(EquipmentSubType.LIGHT)) {
         if (displayMessage) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.BRNOUT.getMessage(), _name);
            String messageToRoom = MessageFormat.format(TaMessageManager.OTHOUT.getMessage(), entity.getName(), _name);
            entity.println(messageToPlayer);
            entity.getRoom().println(entity, messageToRoom, false);
         }
      }
   }

   /**
    * Destroy's this item.
    * @see clone()
    */
   public void destroy() {
      WorldManager.getItemsInExistance().remove(this);
   }

   /**
    * Clones this item. It is important to destroy items if they are not in use!
    *
    * @param origin A descriptive string indicating what cloned this object. Used
    * for debugging.
    * @return a clone of this item.
    * @see destroy()
    */
   public Equipment clone(String origin) {
      try {
         Equipment eq = (Equipment) super.clone();

         eq.setName(_name);
         eq.setLongDescription(_description);
         eq.setCost(_cost);
         eq.setWeight(_weight);
         eq.setV2(_v2);
         eq.setMinEquipmentEffect(_minEquipmentEffect);
         eq.setMaxEquipmentEffect(_maxEquipmentEffect);
         eq.setEquipmentType(_equipmentType);
         eq.setV6(_v6);
         eq.setCharges(_charges);
         eq.setEquipmentSubType(_equipmentSubType);
         eq.setRange(_range);
         eq.setV10(_v10);
         eq.setQuestStat(_questStat);
         eq.setV12(_v12);
         eq.setV13(_v13);
         eq.setLevel(_level);
         eq.setTown(_town);
         eq.setMessage(_message);

         WorldManager.getItemsInExistance().put(eq, origin);
         return eq;
      } catch (CloneNotSupportedException e) {
         _log.error("Error cloning " + _name, e);
         return new DefaultEquipment();
      }

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
   // Methods inherited from Equipment.
   //**********

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
    * Gets the minimum equipment effect.
    * @return the minimum equipment effect.
    */
   public int getMinEquipmentEffect() {
      return _minEquipmentEffect;
   }

   /**
    * Sets the minimum equipment effect.
    * @param minEquipmentEffect the minimum equipment effect.
    */
   public void setMinEquipmentEffect(int minEquipmentEffect) {
      _minEquipmentEffect = minEquipmentEffect;
   }

   /**
    * Gets the maximum equipment effect.
    * @return the maximum equipment effect.
    */
   public int getMaxEquipmentEffect() {
      return _maxEquipmentEffect;
   }

   /**
    * Sets the maximum equipment effect.
    * @param maxEquipmentEffect the maximum equipment effect.
    */
   public void setMaxEquipmentEffect(int maxEquipmentEffect) {
      _maxEquipmentEffect = maxEquipmentEffect;
   }

   /**
    * Gets the equipment type.
    * @return the equipment type.
    */
   public EquipmentType getEquipmentType() {
      return _equipmentType;
   }

   /**
    * Sets the equipment type.
    * @param equipmentType the equipment type.
    */
   public void setEquipmentType(EquipmentType equipmentType) {
      _equipmentType = equipmentType;
   }

   /**
    * Gets the value at index 6.
    * @return the value at index 6.
    */
   public int getV6() {
      return _v6;
   }

   /**
    * Sets the value at index 6.
    * @param v6 the value at index 6.
    */
   public void setV6(int v6) {
      _v6 = v6;
   }

   /**
    * Gets the charges left on this equipment.
    * @return the charges left on this equipment.
    */
   public int getCharges() {
      return _charges;
   }

   /**
    * Sets the charges left on this equipment.
    * @param charges the charges left on this equipment.
    */
   public void setCharges(int charges) {
      _charges = charges;
   }

   /**
    * Subtract a charge.
    */
   public void subractCharge() {
      _charges--;
   }

   /**
    * Gets the equipment subtype.
    * @return the equipment subtype.
    */
   public EquipmentSubType getEquipmentSubType() {
      return _equipmentSubType;
   }

   /**
    * Sets the equipment subtype.
    * @param equipmentSubType the equipment subtype.
    */
   public void setEquipmentSubType(EquipmentSubType equipmentSubType) {
      _equipmentSubType = equipmentSubType;
   }

   /**
    * Gets the equipment range.
    * @return the equipment range.
    */
   public int getRange() {
      return _range;
   }

   /**
    *  Sets the equipment range.
    *  @param range the equipment range.
    */
   public void setRange(int range) {
      _range = range;
   }

   /**
    * Gets the value at index 10.
    * @return the value at index 10.
    */
   public int getV10() {
      return _v10;
   }

   /**
    * Sets the value at index 10.
    * @param v10 the value at index 10.
    */
   public void setV10(int v10) {
      _v10 = v10;
   }

   /**
    * Gets the stat associated with the quest.
    * @return the stat associated with the quest.
    */
   public int getQuestStat() {
      return _questStat;
   }

   /**
    * Sets the stat associated with the quest.
    * @param questStat the stat associated with the quest.
    */
   public void setQuestStat(int questStat) {
      _questStat = questStat;
   }

   /**
    * Gets the value at index 12.
    * @return the value at index 12.
    */
   public int getV12() {
      return _v12;
   }

   /**
    * Sets the value at index 12.
    * @param v12 the value at index 12.
    */
   public void setV12(int v12) {
      _v12 = v12;
   }

   /**
    * Gets the value at index 13.
    * @return the value at index 13.
    */
   public int getV13() {
      return _v13;
   }

   /**
    * Sets the value at index 13.
    * @param v13 the value at index 13.
    */
   public void setV13(int v13) {
      _v13 = v13;
   }

   /**
    * Gets the town that this piece of equipment is sold at.
    * @return the town that this piece of equipment is sold at.
    */
   public Town getTown() {
      return _town;
   }

   /**
    * Sets the town that this piece of equipment is sold at.
    * @param town the town that this piece of equipment is sold at.
    */
   public void setTown(Town town) {
      _town = town;
   }

   /**
    * Gets the equipment message.
    * @return the equipment message.
    */
   public String getMessage() {
      return _message;
   }

   /**
    * Sets the equipment message.
    * @param message the equipment message.
    */
   public void setMessage(String message) {
      _message = message;
   }

   //**********
   // Methods overridden from Object.
   //**********

   /**
    * Returns a string representation of this object.
    *
    * @return a string representation of this object.
    */
   public String toString() {
      return _name;
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
