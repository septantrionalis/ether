package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDonateTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoDonate());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      // Can only donate at the temple.
      if (getCommand().execute(getPlayer(), "donate 100 gold")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      getPlayer().teleportToRoom(TEMPLE);
      getPlayerA().teleportToRoom(TEMPLE);
      clearAllOutput();

      // Check if the player input three keywords.
      if (getCommand().execute(getPlayer(), "donate 100")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // check if the 3rd keyword is "gold" or "g".
      if (getCommand().execute(getPlayer(), "donate 100 dollars")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // non-numeric value.
      getCommand().execute(getPlayer(), "donate one gold");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.DPLGDS.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // negative value
      getCommand().execute(getPlayer(), "donate -1 gold");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.DPLGDS.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // zero value
      getCommand().execute(getPlayer(), "donate 0 gold");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.DPLGDS.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      getPlayer().setGold(100);
      
      // More gold than the player has.
      getCommand().execute(getPlayer(), "donate " + (getPlayer().getGold() + 1) + " gold");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.DNTHVG.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      clearAllOutput();

      // Success test
      getCommand().execute(getPlayer(), "donate " + getPlayer().getGold() + " gold");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.YOUDON.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.JSTDON.getMessage(), getPlayer().getName());
      TestUtil.assertOutput(getPlayerAOutput(), messageToRoom);
      clearAllOutput();
      
   }

}
