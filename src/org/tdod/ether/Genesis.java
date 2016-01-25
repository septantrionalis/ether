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

package org.tdod.ether;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.engine.GameEngine;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PlayerConnectedManager;

/**
 * This class is the start of Telearena.
 *
 * @author Ron Kinney
 */
public final class Genesis {

   /**
    * Logging variable.
    */
   private static Log _log = LogFactory.getLog(Genesis.class);

   /**
    * Creates a new Genesis.
    */
   private Genesis() {
      _log.info("...and so it begins!");
     GameEngine gameEngine = DefaultAppFactory.createDefaultGameEngine();

     try {
        gameEngine.initialize();
        WorldManager.setGameEngine(gameEngine);
     } catch (Exception e) {
        _log.fatal(e, e);
        e.printStackTrace();
        System.exit(1);
     }

     PlayerConnectedManager.getInstance().addPlayerConnectedListener(gameEngine);
     _log.info("Server up at " + GameUtil.getServerUptime());
   }

   /**
    * The main method for this application.  The start of it all.
    *
    * @param args Any args for this application.
    * @throws Exception if something goes wrong.
    */
   public static void main(String [] args) throws Exception {
      new Genesis();
   }

}

