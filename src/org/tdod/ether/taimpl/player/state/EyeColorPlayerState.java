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

import org.tdod.ether.ta.player.enums.EyeColor;
import org.tdod.ether.util.TaMessageManager;

public class EyeColorPlayerState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      // Message already displayed.  Attempt to process input.
      String invalidMessage = MessageFormat.format(TaMessageManager.INVALID.getMessage(), EyeColor.values().length - 1);
      try {         
         Integer intValue = new Integer(input);
         EyeColor eyeColor = EyeColor.getEyeColor(intValue);
         if (eyeColor.equals(EyeColor.INVALID)) {
            // Invalid input.  Stay in the same state.
            stateContext.getOutput().print(invalidMessage);
            stateContext.getPlayerConnection().getPlayer().setEyeColor(EyeColor.INVALID);                                 
         } else {
            // Valid input.  Set EyeColor and move on.
            stateContext.getPlayerConnection().getPlayer().setEyeColor(eyeColor);
            stateContext.getOutput().print(TaMessageManager.HARCOL.getMessage());                                    
            stateContext.setState(new HairColorPlayerState());
         }
      } catch (NumberFormatException nfe) {
         stateContext.getOutput().print(invalidMessage);
         stateContext.getPlayerConnection().getPlayer().setEyeColor(EyeColor.INVALID);               
      } catch (ArrayIndexOutOfBoundsException e2) {
         stateContext.getOutput().print(invalidMessage);
         stateContext.getPlayerConnection().getPlayer().setEyeColor(EyeColor.INVALID);               
      }
   }
   
}
