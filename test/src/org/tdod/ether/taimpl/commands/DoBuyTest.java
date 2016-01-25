package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoBuyTest extends AbstractCommandTest {

	   private static final int ARMOR_SHOP   = -93;

	   /**
	    * Sets up the unit test.
	    */
	   @BeforeClass
	   public void setUp() {
	      super.commandSetUp();
	      setCommand(new DoBuy());
	   }

	   /**
	    * Runs the test case.
	    */
	   @Test(groups = { "unit" })
	   public void testCase() {
         // Test the command in the armor room.
         getPlayer().teleportToRoom(ARMOR_SHOP);
         getPlayerA().teleportToRoom(ARMOR_SHOP);
         clearAllOutput();
         
         if (getCommand().execute(getPlayer(), "buy")) {
            Assert.fail();
         }
         
         if (!getCommand().execute(getPlayer(), "buy levin")) {
            Assert.fail();
         }
         TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NSOHER.getMessage());
         TestUtil.assertOutput(getPlayerAOutput(), "");

         if (!getCommand().execute(getPlayer(), "buy cuirass")) {
            Assert.fail();
         }
         String armorName = "a leather cuirass";
         String expectedOutput = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), armorName);
         TestUtil.assertOutput(getPlayerOutput(), expectedOutput);
         TestUtil.assertOutput(getPlayerAOutput(), "");

         getPlayer().setGold(1000);
         
         if (!getCommand().execute(getPlayer(), "buy cuirass")) {
            Assert.fail();
         }
         TestUtil.assertContains(getPlayerOutput(), "Ok, you bought a leather cuirass for");
         expectedOutput = MessageFormat.format(TaMessageManager.BUYOTH.getMessage(), getPlayer().getName(), armorName);
         TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);

         if (!getPlayer().getInventory().get(1).getName().equals("cuirass")) {
            Assert.fail();            
         }
         
         if (getPlayer().getGold() >= 1000) {
            Assert.fail();
         }
         
         // Test the command in an invalid room.
         getPlayer().teleportToRoom(NORTH_PLAZA);
         getPlayerA().teleportToRoom(NORTH_PLAZA);
         clearAllOutput();

         if (getCommand().execute(getPlayer(), "buy cuirass")) {
             Assert.fail();
         }

	   }
}
