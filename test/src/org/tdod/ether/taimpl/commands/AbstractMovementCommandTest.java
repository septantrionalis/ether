package org.tdod.ether.taimpl.commands;

import junit.framework.Assert;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;

public abstract class AbstractMovementCommandTest extends AbstractCommandTest {

   protected void executeMovementTest(int blockedRoom, String command, int startRoom, int endRoom,
         String endRoomTitle) {
      getPlayer().teleportToRoom(blockedRoom);
      getPlayerA().teleportToRoom(blockedRoom);
      getPlayerB().teleportToRoom(blockedRoom);
      clearAllOutput();

      // Empty
      if (!getCommand().execute(getPlayer(), "")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerAOutput(), "");
      TestUtil.assertOutput(getPlayerBOutput(), "");
      clearAllOutput();
      
      // Blocked
      if (!getCommand().execute(getPlayer(), command)) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NOEXIT.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      TestUtil.assertOutput(getPlayerBOutput(), "");
      clearAllOutput();

      getPlayer().teleportToRoom(startRoom);
      getPlayerA().teleportToRoom(startRoom);
      getPlayerB().teleportToRoom(endRoom);
      clearAllOutput();

      // Move
      AbstractMovementCommand movementCommand = (AbstractMovementCommand)getCommand();
      if (!getCommand().execute(getPlayer(), command)) {
         Assert.fail();
      }
      TestUtil.assertContains(getPlayerOutput(), endRoomTitle);
      TestUtil.assertOutput(getPlayerAOutput(), movementCommand.getLeaveMessage(getPlayer()));
      TestUtil.assertOutput(getPlayerBOutput(), movementCommand.getEnterMessage(getPlayer()));      
   }
   
}
