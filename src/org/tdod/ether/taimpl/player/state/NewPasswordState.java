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

import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;

/**
 * Pick a good password for Asdf:
 * Please retype the password to confirm:
 * Passwords don't match.
 * Retype password:
 */
public class NewPasswordState implements PlayerState {

   /* public void execute1(PlayerStateContext stateContext, String input) {
      String password = stateContext.getPlayerConnection().getPlayer().getPassword();
      String inputHash = byteArrayToHexString(computeHash(password));
      String hash = byteArrayToHexString(computeHash(input));
      if (hash.equals(inputHash)) {
         stateContext.getPlayerConnection().getPlayer().setPassword(hash);
         stateContext.getPlayerConnection().getShell().setHideInput(false);
         stateContext.getOutput().print("\n" + MessageManager.PROMPT_GENDER.getMessage());
         stateContext.setState(new GenderState());
      } else if (password == null) {
         stateContext.getPlayerConnection().getPlayer().setPassword(hash);
         stateContext.getOutput().print("\n" + MessageManager.RETYPE_PASSWORD_LONG.getMessage());
      } else {
         stateContext.getOutput().print("\n" + MessageManager.PASSWORD_MISMATCH.getMessage());
         stateContext.getOutput().print("\n" + MessageManager.RETYPE_PASSWORD_SHORT.getMessage());
         stateContext.getPlayerConnection().getPlayer().setPassword(null);
      }
      
   } */

   public void execute(PlayerStateContext stateContext, String input) {
      String password = stateContext.getPlayerConnection().getPlayer().getPassword();
      String inputHash = GameUtil.byteArrayToHexString(GameUtil.computeHash(input));
      if (inputHash.equals(password)) {
         stateContext.getPlayerConnection().getPlayer().setPassword(input);
         stateContext.getPlayerConnection().getShell().setHideInput(false);
         stateContext.getOutput().print("\n" + MessageManager.PROMPT_GENDER.getMessage());
         stateContext.setState(new GenderState());
      } else if (password == null) {
         stateContext.getPlayerConnection().getPlayer().setPassword(input);
         stateContext.getOutput().print("\n" + MessageManager.RETYPE_PASSWORD_LONG.getMessage());
      } else {
         stateContext.getOutput().print("\n" + MessageManager.PASSWORD_MISMATCH.getMessage());
         stateContext.getOutput().print("\n" + MessageManager.RETYPE_PASSWORD_SHORT.getMessage());
         stateContext.getPlayerConnection().getPlayer().setPassword(null);
      }
      
   }
   

}
