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

import java.io.File;
import java.text.MessageFormat;

import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;

public class GetNameState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      String name = MyStringUtil.up1(input);
      
      if (isNameTaken(name)) {
         stateContext.getOutput().print("\n" + MessageManager.NAME_TAKEN.getMessage());
      } else if (MyStringUtil.isValidName(name)) {
         stateContext.getPlayerConnection().getPlayer().setName(name);
         String message = MessageFormat.format("\n" + MessageManager.PROMPT_NEW_PASSWORD.getMessage(), 
               stateContext.getPlayerConnection().getPlayer().getName());
         stateContext.getOutput().print(message);
         stateContext.getPlayerConnection().getShell().setHideInput(true);         
         stateContext.setState(new NewPasswordState());         
      } else {
         stateContext.getOutput().println(MessageManager.INVALID_NAME.getMessage());
         stateContext.getOutput().print("\n" + MessageManager.PROMPT_NAME.getMessage());         
      }
   }
   
   private boolean isNameTaken(String name) {
      File dir = new File(PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_DIR));
      String[] children = dir.list();
      for (String file:children) {
         if (file.equals(name)) {
            return true;
         }
      }
      
      for (PlayerConnection conn:WorldManager.getPlayers()) {
         if (name.equals(conn.getPlayer().getName())) {
            return true;
         }
      }
      return false;
   }
}
