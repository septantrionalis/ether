package org.tdod.ether.taimpl.commands;

import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDefaultTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoDefault());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(TEST_ROOM_2);
      getPlayerA().teleportToRoom(TEST_ROOM_2);
      new DoDrop().execute(getPlayer(), "drop glowstone");
      new DoDrop().execute(getPlayerA(), "drop glowstone");
      clearAllOutput();
      
      getCommand().execute(getPlayer(), "");
      TestUtil.assertContains(getPlayerOutput(), "It's too dark to see.");
      TestUtil.assertOutput(getPlayerAOutput(), "");
      
      DoDefault.displayRoomDescription(getPlayer(), getPlayer().getRoom(), true);
      TestUtil.assertContains(getPlayerOutput(), "&YYou are in a large cavern.");
      TestUtil.assertOutput(getPlayerAOutput(), "");
   }      

}
