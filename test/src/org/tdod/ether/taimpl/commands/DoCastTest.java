package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoCastTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoCast());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      getPlayerA().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      getPlayer().setPlayerClass(PlayerClass.WARRIOR);
      cast();

      getPlayer().setPlayerClass(PlayerClass.ACOLYTE);
      getPlayer().getMana().setCurMana(100);
      getPlayer().getMana().setMaxMana(100);
      cast();
      
      Spell spell = WorldManager.getSpell("motu");
      getPlayer().getSpellbook().getSpells().add(spell);
      
      if (!getCommand().execute(getPlayer(), "cast motu " + getPlayer().getName())) {
         Assert.fail();
      }
      TestUtil.assertContains(getPlayerOutput(), "You intoned the spell for " + getPlayer().getName() + " which healed");
      String playerAOutput = MessageFormat.format(TaMessageManager.HELOTH.getMessage(), getPlayer().getName(), "a minor healing spell", getPlayer().getName());
      TestUtil.assertOutput(getPlayerAOutput(), playerAOutput);
      
   }
   
   private void cast() {
      if (getCommand().execute(getPlayer(), "cast")) {
         Assert.fail();
      }

      if (!getCommand().execute(getPlayer(), "cast asdf")) {
         Assert.fail();
      }
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NOSPEL.getMessage());
      TestUtil.assertOutput(getPlayerAOutput(), "");
   }
}
