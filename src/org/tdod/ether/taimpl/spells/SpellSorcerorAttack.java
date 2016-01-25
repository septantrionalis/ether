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

package org.tdod.ether.taimpl.spells;

import org.tdod.ether.ta.Entity;

public class SpellSorcerorAttack extends AbstractSpellDamage {

   // private static Log _log = LogFactory.getLog(SpellSorcerorAttack.class);

   /**
    * &MThat spell needs to be cast at a specific person or creature.
    * &MSorry, you don't see "asd" nearby.
    * self:
    * &WYou confuse the key syllables and the spell fails!
    * &WLimech unsuccessfully attempts to cast a spell!
    * 
    * &MSorry, combat spells are not permitted here.
    * 
    * &MYou discharged the spell at the huge rat for 2 damage!
    * &BLimech just discharged a small shard of ice at the huge rat!
    * 
    * &BYour spell was negated by the {0}''s magickal defenses!
    * &BLimech just discharged a small shard of ice at the kobold, which failed to affect the kobold!
    * 
    */
   protected void handleSpecificSpellFunctions(Entity target) {
      return;
   }

}
