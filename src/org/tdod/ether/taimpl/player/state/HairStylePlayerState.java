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

package org.tdod.ether.taimpl.player.state;

import java.text.MessageFormat;

import org.tdod.ether.ta.player.enums.HairStyle;
import org.tdod.ether.util.TaMessageManager;

public class HairStylePlayerState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      String invalidMessage = MessageFormat.format(TaMessageManager.INVALID.getMessage(), HairStyle.values().length - 1);
      try {         
         Integer intValue = new Integer(input);
         HairStyle hairStyle = HairStyle.getHairStyle(intValue);
         if (hairStyle.equals(HairStyle.INVALID)) {
            // Invalid input.  Stay in the same state.
            stateContext.getOutput().print(invalidMessage);
            stateContext.getPlayerConnection().getPlayer().setHairStyle(HairStyle.INVALID);                                 
         } else {
            // Valid input.  Set HairStyle and move on.
            stateContext.getPlayerConnection().getPlayer().setHairStyle(hairStyle);
            stateContext.getOutput().print(TaMessageManager.HARLNG.getMessage());  
            stateContext.setState(new HairLengthPlayerState());
         }
      } catch (NumberFormatException nfe) {
         stateContext.getOutput().print(invalidMessage);
         stateContext.getPlayerConnection().getPlayer().setHairStyle(HairStyle.INVALID);               
      } catch (ArrayIndexOutOfBoundsException e2) {
         stateContext.getOutput().print(invalidMessage);
         stateContext.getPlayerConnection().getPlayer().setHairStyle(HairStyle.INVALID);               
      }                     
   }
   
}
