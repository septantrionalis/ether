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

package org.tdod.ether.ta.cosmos;

import java.io.Serializable;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;

/**
 * An interface defining the API for a trigger.
 *
 * @author Ron Kinney
 */
public interface Trigger extends Serializable {

   /**
    * Gets the trigger type.
    *
    * @return the trigger type.
    */
   TriggerType getTriggerType();

   /**
    * Sets the trigger type.
    *
    * @param triggerType the trigger type.
    */
   void setTriggerType(TriggerType triggerType);

   /**
    * Gets the value at index 2.
    *
    * @return the value at index 2.
    */
   int getV2();

   /**
    * Sets the value at index 2.
    *
    * @param value the value at index 2.
    */
   void setV2(int value);

   /**
    * Gets the value at index 3.
    *
    * @return the value at index 3.
    */
   int getV3();

   /**
    * Sets the value at index 3.
    *
    * @param v3 the value at index 3.
    */
   void setV3(int v3);

   /**
    * Gets the value at index 4.
    *
    * @return the value at index 4.
    */
   int getV4();

   /**
    * Sets the value at index 4.
    *
    * @param v4 the value at index 4.
    */
   void setV4(int v4);

   /**
    * Gets the value at index 5.
    *
    * @return the value at index 5.
    */
   int getV5();

   /**
    * Sets the value at index 5.
    *
    * @param v5 the value at index 5.
    */
   void setV5(int v5);

   /**
    * Gets the value at index 6.
    *
    * @return the value at index 6.
    */
   int getV6();

   /**
    * Sets the value at index 6.
    *
    * @param v6 the value at index 6.
    */
   void setV6(int v6);

   /**
    * Gets the value at index 7.
    *
    * @return the value at index 7.
    */
   int getV7();

   /**
    * Sets the value at index 7.
    *
    * @param v7 the value at index 7.
    */
   void setV7(int v7);

   /**
    * Checks if this trigger has been triggered.
    *
    * @return true if this trigger has been triggered.
    */
   boolean isTriggered();

   /**
    * Sets the triggered state.
    *
    * @param triggered the triggered state.
    */
   void setTriggered(boolean triggered);

   /**
    * Handles the room trigger.
    *
    * @param entity the entity that triggered the trigger.
    *
    * @return the trigger result.
    */
   TriggerResult handleTrigger(Entity entity);

}
