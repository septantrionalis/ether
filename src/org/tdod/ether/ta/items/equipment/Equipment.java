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

package org.tdod.ether.ta.items.equipment;

import org.tdod.ether.ta.items.Item;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;

/**
 * The interface for a piece of equipment.
 * @author Ron Kinney
 */
public interface Equipment extends Item {

   /**
    * Sets the value at index 2.
    * @param v2 the value at index 2.
    */
   void setV2(int v2);

   /**
    * Gets the value at index 2.
    * @return the value at index 2.
    */
   int getV2();

   /**
    * Gets the minimum equipment effect.
    * @return the minimum equipment effect.
    */
   int getMinEquipmentEffect();

   /**
    * Sets the minimum equipment effect.
    * @param minEquipmentEffect the minimum equipment effect.
    */
   void setMinEquipmentEffect(int minEquipmentEffect);

   /**
    * Gets the maximum equipment effect.
    * @return the maximum equipment effect.
    */
   int getMaxEquipmentEffect();

   /**
    * Sets the maximum equipment effect.
    * @param maxEquipmentEffect the maximum equipment effect.
    */
   void setMaxEquipmentEffect(int maxEquipmentEffect);

   /**
    * Gets the equipment type.
    * @return the equipment type.
    */
   EquipmentType getEquipmentType();

   /**
    * Sets the equipment type.
    * @param equipmentType the equipment type.
    */
   void setEquipmentType(EquipmentType equipmentType);

   /**
    * Gets the value at index 6.
    * @return the value at index 6.
    */
   int getV6();

   /**
    * Sets the value at index 6.
    * @param v6 the value at index 6.
    */
   void setV6(int v6);

   /**
    * Gets the charges left on this equipment.
    * @return the charges left on this equipment.
    */
   int getCharges();

   /**
    * Sets the charges left on this equipment.
    * @param charges the charges left on this equipment.
    */
   void setCharges(int charges);

   /**
    * Subtract a charge.
    */
   void subractCharge();

   /**
    * Gets the equipment subtype.
    * @return the equipment subtype.
    */
   EquipmentSubType getEquipmentSubType();

   /**
    * Sets the equipment subtype.
    * @param equipmentSubType the equipment subtype.
    */
   void setEquipmentSubType(EquipmentSubType equipmentSubType);

   /**
    * Gets the equipment range.
    * @return the equipment range.
    */
   int getRange();

   /**
    *  Sets the equipment range.
    *  @param range the equipment range.
    */
   void setRange(int range);

   /**
    * Gets the value at index 10.
    * @return the value at index 10.
    */
   int getV10();

   /**
    * Sets the value at index 10.
    * @param v10 the value at index 10.
    */
   void setV10(int v10);

   /**
    * Gets the stat associated with the quest.
    * @return the stat associated with the quest.
    */
   int getQuestStat();

   /**
    * Sets the stat associated with the quest.
    * @param questStat the stat associated with the quest.
    */
   void setQuestStat(int questStat);

   /**
    * Gets the value at index 12.
    * @return the value at index 12.
    */
   int getV12();

   /**
    * Sets the value at index 12.
    * @param v12 the value at index 12.
    */
   void setV12(int v12);

   /**
    * Gets the value at index 13.
    * @return the value at index 13.
    */
   int getV13();

   /**
    * Sets the value at index 13.
    * @param v13 the value at index 13.
    */
   void setV13(int v13);

   /**
    * Sets the town that this piece of equipment is sold at.
    * @param town the town that this piece of equipment is sold at.
    */
   void setTown(Town town);

   /**
    * Gets the equipment message.
    * @return the equipment message.
    */
   String getMessage();

   /**
    * Sets the equipment message.
    * @param message the equipment message.
    */
   void setMessage(String message);

}
