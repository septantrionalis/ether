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

import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * What is your sex (M/F)?
 */
public class GenderState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      
      if (input.toLowerCase().equals("m") || input.toLowerCase().equals("f")) {

         if (input.toLowerCase().equals("m")) {
            stateContext.getPlayerConnection().getPlayer().setGender(Gender.MALE);
         } else {
            stateContext.getPlayerConnection().getPlayer().setGender(Gender.FEMALE);            
         }
         String message = MessageFormat.format(MessageManager.VALID_NAME.getMessage(), 
               stateContext.getPlayerConnection().getPlayer().getName());
         stateContext.getOutput().println("\n" + message);         
         stateContext.getOutput().print("\n" + TaMessageManager.HITRET.getMessage());
         stateContext.setState(new Intro1PlayerState());
      } else {
         stateContext.getOutput().print("\nWhat is your sex (M/F)? ");
      }
   }

}
