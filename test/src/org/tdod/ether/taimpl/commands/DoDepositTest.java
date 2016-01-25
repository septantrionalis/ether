package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDepositTest extends AbstractCommandTest {

   private static final int VAULT_ROOM   = -66;

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoDeposit());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(VAULT_ROOM);
      getPlayerA().teleportToRoom(VAULT_ROOM);
      clearAllOutput();

      if (!getCommand().execute(getPlayer(), "de asd")) {
    	  Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.BNKTMC.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
      
      if (!getCommand().execute(getPlayer(), "de 100")) {
    	  Assert.fail();    	  
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.BNKNHA.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");      
      
      if (getCommand().execute(getPlayer(), "de 100 gold")) {
    	  Assert.fail();    	      	  
      }
      
      getPlayer().setGold(100);
      
      if (!getCommand().execute(getPlayer(), "de 101")) {
    	  Assert.fail();    	      	      	  
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.BNKNHA.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");      
      
      if (getPlayer().getVaultBalance() != 0) {
         Assert.fail();
      }

      if (getPlayer().getGold() != 100) {
         Assert.fail();
      }
      
      if (!getCommand().execute(getPlayer(), "de 100")) {
    	  Assert.fail();    	      	      	  
      }
      String depositString = MessageFormat.format(TaMessageManager.BNKDEP.getMessage(), "100");
      TestUtil.assertOutput(getPlayerOutput(), depositString);
      TestUtil.assertOutput(getPlayerAOutput(), "");      

      if (getPlayer().getVaultBalance() != 100) {
         Assert.fail();
      }

      if (getPlayer().getGold() != 0) {
         Assert.fail();
      }
      
      // Test the command in an invalid room.
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      if (getCommand().execute(getPlayer(), "ba")) {
          Assert.fail();
      }
      
   }

}
