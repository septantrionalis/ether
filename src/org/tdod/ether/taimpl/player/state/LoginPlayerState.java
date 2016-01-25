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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;

import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;

public class LoginPlayerState implements PlayerState {
   
   // private static Log _log = LogFactory.getLog(LoginPlayerState.class);

   public void execute(PlayerStateContext stateContext, String input) {
      if (input.toUpperCase().equals(PropertiesManager.getInstance().getProperty(PropertiesManager.NEW_PLAYER))) {
         stateContext.getOutput().print("\n" + MessageManager.PROMPT_NAME.getMessage());
         stateContext.setState(new GetNameState());
      } else if (input.equals("")) {
         stateContext.getOutput().print("\n" + MessageManager.LOGIN.getMessage());                                    
      } else {
         handlePlayerName(stateContext, input);
      }
   }

   private void handlePlayerName(PlayerStateContext stateContext, String input) {
      String name = MyStringUtil.up1(input);
      
      if (!MyStringUtil.isValidName(name)) {
         stateContext.getOutput().println(MessageManager.INVALID_NAME.getMessage());
         stateContext.getOutput().print("\n" + MessageManager.LOGIN.getMessage());
      } else {
         loadPlayer(stateContext, name);
      }
      
   }
   
   private void loadPlayer(PlayerStateContext stateContext, String name) {
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_DIR) + name;
         FileInputStream fis = new FileInputStream(filename);
         GZIPInputStream gzis = new GZIPInputStream(fis);
         ObjectInputStream in = new ObjectInputStream(gzis);
         Player player = (Player)in.readObject();
         in.close();
         player.setOutput(stateContext.getOutput());
         player.setState(PlayerStateEnum.LOGIN);
         stateContext.getPlayerConnection().setPlayer(player);

         stateContext.getPlayerConnection().getShell().setHideInput(true);
         stateContext.getOutput().print("\n" + MessageManager.PROMPT_PASSWORD.getMessage());         
         stateContext.setState(new PromptPasswordState());
      } catch (Exception e) {
         String message = MessageFormat.format(MessageManager.NAME_NOT_FOUND.getMessage(), name);
         stateContext.getOutput().println(message);         
         stateContext.getOutput().print("\n" + MessageManager.LOGIN.getMessage());
      }
   }

}
