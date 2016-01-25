package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoConferTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoConfer());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      getPlayer().setGroupLeader(getPlayer());
      getPlayerA().setGroupLeader(getPlayer());
      getPlayer().getGroupList().add(getPlayerA());
      
      if (getCommand().execute(getPlayer(), "confer")) {
         Assert.fail();
      }
      
      if (!getCommand().execute(getPlayer(), "confer hello world")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.GRPSNT.getMessage());
      String expectedOutput = MessageFormat.format(TaMessageManager.GTALKU.getMessage(), getPlayer().getName(), "hello world");
      TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);
   }

}
