package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the appoint command.
 * @author Ron Kinney
 */
public class DoAppointTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoAppoint());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(TEST_ROOM_1);
      getPlayerA().teleportToRoom(TEST_ROOM_2);
      getPlayerB().teleportToRoom(TEST_ROOM_1);
      clearAllOutput();
      getCommand().execute(getPlayer(), "appoint " + getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(),
            MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), getPlayerA().getName().toLowerCase()));
      TestUtil.assertOutput(getPlayerAOutput(), "");
      TestUtil.assertOutput(getPlayerBOutput(), "");

      getPlayer().teleportToRoom(TEST_ROOM_1);
      getPlayerA().teleportToRoom(TEST_ROOM_1);
      getPlayerB().teleportToRoom(TEST_ROOM_1);
      clearAllOutput();

      new DoJoin().execute(getPlayerA(), "join " + getPlayer().getName());
      new DoAdd().execute(getPlayer(), "add " + getPlayerA().getName());
      clearAllOutput();

      getCommand().execute(getPlayer(), "appoint " + getPlayerA().getName());
      String expectedOutput = MessageFormat.format(TaMessageManager.APTLDR.getMessage(), getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.APTYOU.getMessage(), getPlayer().getName());
      TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.OTHAPT.getMessage(), getPlayer().getName(), getPlayerA().getName());
      TestUtil.assertOutput(getPlayerBOutput(), expectedOutput);

      if (!getPlayer().getGroupLeader().equals(getPlayerA())) {
         Assert.fail("Group leader is " + getPlayer().getGroupLeader());
      }
   }
}
