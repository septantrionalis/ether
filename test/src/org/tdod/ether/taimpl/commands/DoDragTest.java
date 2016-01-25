package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDragTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoDrag());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      getPlayerB().teleportToRoom(NORTH_PLAZA);
      getPlayerC().teleportToRoom(-97);
      clearAllOutput();

      // Test no params
      if (getCommand().execute(getPlayer(), "")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // Test that exit exists
      if (!getCommand().execute(getPlayer(), "drag " + getPlayerA().getName() + " d")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NOEXIT.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();
      
      // Test no target.
      String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), "asdf");
      if (!getCommand().execute(getPlayer(), "drag asdf nw")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), message);
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();
      
      // Drag self.
      if (!getCommand().execute(getPlayer(), "drag " + getPlayer().getName() + " nw")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NODSLF.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // Not incapacitated.
      message = MessageFormat.format(TaMessageManager.CNTDRG.getMessage(), getPlayerA().getName());
      if (!getCommand().execute(getPlayer(), "drag " + getPlayerA().getName() + " nw")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), message);
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // Drag
      getPlayerA().setStatus(Status.PARALYSED);
      if (!getCommand().execute(getPlayer(), "drag " + getPlayerA().getName() + " nw")) {
         Assert.fail();
      }
      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUDRG.getMessage(), getPlayerA().getName(), "northwest");
      TestUtil.assertContains(getPlayerOutput(), messageToPlayer);
      String messageToTarget = MessageFormat.format(TaMessageManager.DRGYOU.getMessage(), getPlayer().getName(), "northwest");
      TestUtil.assertContains(getPlayerAOutput(), messageToTarget);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHDRO.getMessage(), getPlayer().getName(), getPlayerA().getName());
      TestUtil.assertContains(getPlayerBOutput(), messageToRoom);
      String messageOtherRoom = MessageFormat.format(TaMessageManager.OTHDRI.getMessage(), getPlayer().getName(), getPlayerA().getName());
      TestUtil.assertContains(getPlayerCOutput(), messageOtherRoom);
      clearAllOutput();
      
   }

}
