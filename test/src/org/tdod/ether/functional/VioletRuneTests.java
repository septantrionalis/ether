package org.tdod.ether.functional;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.DoAdd;
import org.tdod.ether.taimpl.commands.DoJoin;
import org.tdod.ether.taimpl.commands.DoUse;
import org.tdod.ether.taimpl.commands.debug.DoClear;
import org.tdod.ether.taimpl.commands.debug.DoSlay;
import org.tdod.ether.taimpl.commands.sysop.DoSummon;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;

public class VioletRuneTests extends AbstractTestCase {

   private static final int RUNE_ROOM = 2809;
   
   private Player _player;
   private Player _player1;
   private Player _player2;
   private Player _player3;
   private Player _player4;
   
   private MockOutput _playerOutput = new MockOutput();
   private MockOutput _player1Output = new MockOutput();
   private MockOutput _player2Output = new MockOutput();
   private MockOutput _player3Output = new MockOutput();
   private MockOutput _player4Output = new MockOutput();
   
   public void testRune() {
      // Initialize
      _player = TestUtil.createDefaultPlayer(_playerOutput);
      _player.teleportToRoom(RUNE_ROOM);
      _player.setIsSysop(true);
      _player.setRune(Rune.BLUE);
      
      _player1 = TestUtil.createDefaultPlayer(_player1Output);
      _player1.setName("A");
      _player1.teleportToRoom(RUNE_ROOM);
      
      _player2 = TestUtil.createDefaultPlayer(_player2Output);
      _player2.setName("B");
      _player2.teleportToRoom(RUNE_ROOM);

      _player3 = TestUtil.createDefaultPlayer(_player3Output);
      _player3.setName("C");
      _player3.teleportToRoom(RUNE_ROOM);

      _player4 = TestUtil.createDefaultPlayer(_player4Output);
      _player4.setName("D");
      _player4.teleportToRoom(RUNE_ROOM);

      getRodOfPower();
      useRodWithLackingSupport();
      useRodWithTwoGroupMembers();
      useRodWithMinimumGroupMembers();
   }

   /**
    * Gets the Rod Of Power from the Demon King.
    */
   private void getRodOfPower() {
      new DoSlay().execute(_player, "slay king");
      String obtainedRodOfPower = "While searching the area, you notice rod of power, which you add to your\npossessions.";
      String demonKingKilled = "The demon king falls to the ground lifeless!";
      TestUtil.assertContains(_playerOutput, obtainedRodOfPower);
      TestUtil.assertContains(_player1Output, demonKingKilled);
      TestUtil.assertContains(_player2Output, demonKingKilled);
      TestUtil.assertContains(_player3Output, demonKingKilled);
      TestUtil.assertContains(_player4Output, demonKingKilled);      
   }
   
   /**
    * Uses the rod with two group members.
    */
   private void useRodWithTwoGroupMembers() {
      Command joinCommand = new DoJoin();      
      joinCommand.execute(_player1, "join " + _player.getName());
      joinCommand.execute(_player2, "join " + _player.getName());
      
      Command addCommand = new DoAdd();
      addCommand.execute(_player, "add " + _player1.getName());
      addCommand.execute(_player, "add " + _player2.getName());
      
      TestUtil.assertContains(_playerOutput, "A is asking to join your group.\nB is asking to join your group.\nA is now a member of your group.\nB is now a member of your group.");
      TestUtil.assertContains(_player1Output, "Ok, you've asked to join Minex's group.\nB is asking to join Minex's group.\nYou are now a member of Minex's group.\nB has been accepted into Minex's group.");
      TestUtil.assertContains(_player2Output, "A is asking to join Minex's group.\nOk, you've asked to join Minex's group.\nA has been accepted into Minex's group.\nYou are now a member of Minex's group.");
      TestUtil.assertContains(_player3Output, "A is asking to join Minex's group.\nB is asking to join Minex's group.\nA has been accepted into Minex's group.\nB has been accepted into Minex's group.");
      TestUtil.assertContains(_player4Output, "A is asking to join Minex's group.\nB is asking to join Minex's group.\nA has been accepted into Minex's group.\nB has been accepted into Minex's group.");
      
      useRodWithLackingSupport();
   }
   
   /**
    * Uses the rod with the minimum number of players in the group to obtain the rune.
    */
   private void useRodWithMinimumGroupMembers() {
      Command joinCommand = new DoJoin();      
      joinCommand.execute(_player3, "join " + _player.getName());
      
      Command addCommand = new DoAdd();
      addCommand.execute(_player, "add " + _player3.getName());
      
      TestUtil.assertContains(_playerOutput, "C is asking to join your group.\nC is now a member of your group.");
      TestUtil.assertContains(_player1Output, "C is asking to join Minex's group.\nC has been accepted into Minex's group.");
      TestUtil.assertContains(_player2Output, "C is asking to join Minex's group.\nC has been accepted into Minex's group.");
      TestUtil.assertContains(_player3Output, "Ok, you've asked to join Minex's group.\nYou are now a member of Minex's group.");
      TestUtil.assertContains(_player4Output, "C is asking to join Minex's group.\nC has been accepted into Minex's group.");

      useRodWithMobsPresent();;
      useRodWithSufficientSupport();
   }
   
   /**
    * Uses the rod with no group members.
    */
   private void useRodWithLackingSupport() {
      _player1Output.clearBuffer();
      _player2Output.clearBuffer();
      _player3Output.clearBuffer();
      _player4Output.clearBuffer();
      
      new DoUse().execute(_player, "use rod");
      TestUtil.assertContains(_playerOutput, TaMessageManager.NOSUPP.getMessage());
      TestUtil.assertOutput(_player1Output, "");      
      TestUtil.assertOutput(_player2Output, "");      
      TestUtil.assertOutput(_player3Output, "");      
      TestUtil.assertOutput(_player4Output, "");            
   }

   /**
    * Attempts to use the rod with mobs in the room.
    */
   private void useRodWithMobsPresent() {
      new DoSummon().execute(_player, "summon 1 " + RUNE_ROOM);
      
      _player1Output.clearBuffer();
      _player2Output.clearBuffer();
      _player3Output.clearBuffer();
      _player4Output.clearBuffer();

      new DoUse().execute(_player, "use rod");      
      TestUtil.assertContains(_playerOutput, TaMessageManager.CDTNOW.getMessage());
   }
   
   /**
    * Uses the Rod of Power with sufficient group members to obtain the rune.
    */
   private void useRodWithSufficientSupport() {
      new DoClear().execute(_player, "clear");
      
      _player1Output.clearBuffer();
      _player2Output.clearBuffer();
      _player3Output.clearBuffer();
      _player4Output.clearBuffer();

      new DoUse().execute(_player, "use rod");
      String playerOutput = MessageFormat.format(TaMessageManager.YOURN2.getMessage(), "rod of power") + TaMessageManager.TRS011.getMessage();  
      String observerOutput = MessageFormat.format(TaMessageManager.OTHRN2.getMessage(), _player.getName(), "rod of power");
      TestUtil.assertContains(_playerOutput, playerOutput);
      TestUtil.assertOutput(_player1Output, observerOutput);
      TestUtil.assertOutput(_player2Output, observerOutput);
      TestUtil.assertOutput(_player3Output, observerOutput);
      TestUtil.assertOutput(_player4Output, observerOutput);
      
      if (!_player.getRune().equals(Rune.VIOLET)) {
         Assert.fail("Player has rune " + _player.getRune());
      }
   }
      
}
