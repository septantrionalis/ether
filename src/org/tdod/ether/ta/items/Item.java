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

package org.tdod.ether.ta.items;

import java.io.Serializable;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.enums.Town;

/**
 * The item interface.  Anything that can be held by a player should extend the
 * Item interface.  Armor, weapons, equipment, etc.
 *
 * @author Ron Kinney
 */
public interface Item extends Cloneable, Serializable, Comparable<Item> {

   /**
    * Gets the item vnum.
    * @return the item vnum.
    */
   int getVnum();

   /**
    * Sets the item vnum.
    * @param vnum the item vnum.
    */
   void setVnum(int vnum);

   /**
    * Gets the item name.
    * @return the item name.
    */
   String getName();

   /**
    * Sets the item name.
    * @param name the item name.
    */
   void setName(String name);

   /**
    * Gets the items long description.
    * @return the items long description.
    */
   String getLongDescription();

   /**
    * Sets the items long description.
    * @param longDescription the items long description.
    */
   void setLongDescription(String longDescription);

   /**
    * Gets the item long description with charges.
    * @return the item long description with charges.
    */
   String getLongDescriptionWithCharges();

   /**
    * Gets the item cost.
    * @return the item cost.
    */
   int getCost();

   /**
    * Sets the item cost.
    * @param cost the item cost.
    */
   void setCost(int cost);

   /**
    * Gets the item weight.
    * @return the item weight.
    */
   int getWeight();

   /**
    * Sets the item weight.
    * @param weight the item weight.
    */
   void setWeight(int weight);

   /**
    * Gets the item level.
    * @return the item level.
    */
   int getLevel();

   /**
    * Sets the item level.
    * @param level the item level.
    */
   void setLevel(int level);

   /**
    * Determines if the player class can use this item.
    * @param playerClass the player class in question.
    * @return true if the player class can use this item, false otherwise.
    */
   boolean canUse(PlayerClass playerClass);

   /**
    * Sets the allowable classes for this item.
    * @param allowableClasses the allowable classes for this item.
    */
   void setAllowableClasses(int allowableClasses);

   /**
    * Gets the allowable classes for this item.
    * @return the allowable classes for this item.
    */
   int getAllowableClasses();

   /**
    * Gets the item type.
    * @return the item type.
    */
   ItemType getItemType();

   /**
    * Sets the activated state of this item.
    * @param activated the activated state of this item.
    */
   void setActivated(boolean activated);

   /**
    * Gets the activated state of this item.
    * @return the activated state of this item.
    */
   boolean isActivated();

   /**
    * Sets the effect timer.
    * @param timer the effect timer.
    */
   void setEffectTimer(int timer);

   /**
    * Decreases the effect timer.
    * @param value decreases the effect timer by the value.
    * @return true if the timer hit 0 or below.
    */
   boolean decreaseTimer(int value);

   /**
    * Handles an expired timer.
    * @param entity the entity who owns the item.
    * @param displayMessage the message to display.
    */
   void handleExpiredTimer(Entity entity, boolean displayMessage);

   /**
    * Clones this item. It is important to destroy items if they are not in use!
    *
    * @param origin A descriptive string indicating what cloned this object. Used
    * for debugging.
    * @return a clone of this item.
    * @see destroy()
    */
   Item clone(String origin);

   /**
    * Destroy's this item.
    *
    * @see clone()
    */
   void destroy();

   /**
    * Checks if this item is similar to another item.  Since items are clones of each other, the equals call will only
    * return true if the other item is an exact copy.  This call determines if one torch is the same as another torch.
    * @param item the item in question.
    * @return true if the item is similar.
    */
   boolean isSimilar(Item item);

   /**
    * Gets the town that this piece of equipment is sold at.
    * @return the town that this piece of equipment is sold at.
    */
   Town getTown();
}
