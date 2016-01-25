package org.tdod.ether.taimpl.commands;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoD());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      getCommand().execute(getPlayer(), "d");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NOEXIT.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");

      getCommand().execute(getPlayer(), "d asdf");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NODHER.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
   }

}
