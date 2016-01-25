package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoBalanceTest extends AbstractCommandTest {

   private static final int VAULT_ROOM   = -66;

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoBalance());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
	  // Test the command in the vault room.
      getPlayer().teleportToRoom(VAULT_ROOM);
      getPlayerA().teleportToRoom(VAULT_ROOM);
      clearAllOutput();

      if (getCommand().execute(getPlayer(), "ba asd")) {
         Assert.fail();
      }
      
      if (!getCommand().execute(getPlayer(), "ba")) {
         Assert.fail();
      }
      
      String balanceString = MessageFormat.format(TaMessageManager.BNKBAL.getMessage(), "0");
      TestUtil.assertOutput(getPlayerOutput(), balanceString);
      TestUtil.assertOutput(getPlayerAOutput(), "");
      
      getPlayer().setVaultBalance(100);
      
      if (!getCommand().execute(getPlayer(), "ba")) {
          Assert.fail();
       }

      balanceString = MessageFormat.format(TaMessageManager.BNKBAL.getMessage(), "100");
      TestUtil.assertOutput(getPlayerOutput(), balanceString);
      TestUtil.assertOutput(getPlayerAOutput(), "");

      // Test the command in an invalid room.
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      if (getCommand().execute(getPlayer(), "ba")) {
          Assert.fail();
      }

   }

}
