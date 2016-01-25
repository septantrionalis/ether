package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.commands.sysop.DoConjure;
import org.tdod.ether.taimpl.commands.sysop.DoSummon;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the attack command.
 * @author Ron Kinney
 */
public class DoAttackTest extends AbstractCommandTest {

   private static final int NORTH_PLAZA   = -99;
   private static final int FIGHTING_ROOM = 1;
   private static final int OGRE_VMUM     = 55;

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoAttack());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(NORTH_PLAZA);
      clearAllOutput();

      // Can't fight in a safe room.
      getCommand().execute(getPlayer(), "a asdf");
      TestUtil.assertOutput(getPlayerOutput(), TaMessageManager.NOFTHR.getMessage());

      // That is not a melee weapon!
      getPlayer().setIsSysop(true);
      getPlayer().setPlayerClass(PlayerClass.ARCHER);
      new DoConjure().execute(getPlayer(), "conjure short bow");
      new DoEquip().execute(getPlayer(), "eq short bow");
      getPlayer().teleportToRoom(FIGHTING_ROOM);
      new DoSummon().execute(getPlayer(), "summon " + OGRE_VMUM + " " + FIGHTING_ROOM);
      new DoDefault().execute(getPlayer(), "");
      getCommand().execute(getPlayer(), "a ogre");
      TestUtil.assertContains(getPlayerOutput(), TaMessageManager.NOTMLE.getMessage());

      // Nothing found...
      new DoUnequip().execute(getPlayer(), "un short");
      getCommand().execute(getPlayer(), "a asdf");
      TestUtil.assertContains(getPlayerOutput(), 
            MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), "asdf"));

      // Can't attack self
      getCommand().execute(getPlayer(), "a " + getPlayer().getName());
      TestUtil.assertContains(getPlayerOutput(), TaMessageManager.NOASLF.getMessage());

      // Attack
      getPlayer().getAttacks().setAttacksLeft(3);
      int totalAttacks = getPlayer().getAttacks().getAttacksLeft();
      for (int count = 0; count < totalAttacks; count++) {
         getCommand().execute(getPlayer(), "a ogre");
      }
      TestUtil.assertDoesNotContains(getPlayerOutput(), TaMessageManager.ATTEXH.getMessage());
      getCommand().execute(getPlayer(), "a ogre");
      TestUtil.assertContains(getPlayerOutput(), TaMessageManager.ATTEXH.getMessage());
   }

}
