package org.tdod.ether.functional;

import java.util.ArrayList;

import junit.framework.Assert;

import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.DoDefault;
import org.tdod.ether.taimpl.commands.DoDrop;
import org.tdod.ether.taimpl.commands.DoEast;
import org.tdod.ether.taimpl.commands.DoGet;
import org.tdod.ether.taimpl.commands.DoNorth;
import org.tdod.ether.taimpl.commands.DoPush;
import org.tdod.ether.taimpl.commands.DoSay;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;

/**
 * Blue rune functional test case.
 *
 * @author Ron Kinney
 */
public class BlueRuneTests extends AbstractTestCase {

   public void testLever1() {
      int leverRoom = 1534;
      int lockedRoom = 1473;
      int destinationRoom = 1483;
      Command dirCommand = new DoSouth();
      String expectedBlockedPlayerString = "The locked stone door prevents your exit in that direction.";
      
      pullLeverToOpenDoor(leverRoom, lockedRoom, destinationRoom, dirCommand, expectedBlockedPlayerString);
   }

   public void testLever2() {
      int leverRoom = 1483;
      int lockedRoom = 1515;
      int destinationRoom = 1505;
      Command dirCommand = new DoNorth();
      String expectedBlockedPlayerString = "The locked stone door prevents your exit in that direction.";
      
      pullLeverToOpenDoor(leverRoom, lockedRoom, destinationRoom, dirCommand, expectedBlockedPlayerString);
   }

   public void testLever3() {
      int leverRoom = 1731;
      int lockedRoom = 1711;
      int destinationRoom = 1701;
      Command dirCommand = new DoNorth();
      String expectedBlockedPlayerString = "The locked stone door prevents your exit in that direction.";
      
      pullLeverToOpenDoor(leverRoom, lockedRoom, destinationRoom, dirCommand, expectedBlockedPlayerString);
   }

   public void testDarkness1() {
      int startRoomNumber = 1764;
      int endRoomNumber = 1863;

      ArrayList<Integer> darkRooms = new ArrayList<Integer>();
      
      for (int index = startRoomNumber;index <= endRoomNumber; index++) {
         darkRooms.add(index);
      }
      darkRooms.remove(new Integer(1812));
      darkRooms.remove(new Integer(1827));
      
      int stoneRoom = 1912;
      
      testDarknessLevel(darkRooms, stoneRoom);
   }
   
   public void testDarkness2() {
      int startRoomNumber = 1564;
      int endRoomNumber = 1663;

      ArrayList<Integer> darkRooms = new ArrayList<Integer>();
      
      for (int index = startRoomNumber;index <= endRoomNumber; index++) {
         darkRooms.add(index);
      }
      darkRooms.remove(new Integer(1605));
      darkRooms.remove(new Integer(1630));

      int stoneRoom = 1808;
      
      testDarknessLevel(darkRooms, stoneRoom);
   }

   public void testSpring1() {
      spring(1761, 1851, new DoNorth());
   }

   public void testSpring2() {
      spring(1723, 1833, new DoSouth());
   }

   public void testSpring3() {
      spring(1682, 1781, new DoWest());
   }

   public void testSpring4() {
      spring(1899, 1700, new DoEast());
   }

   public void testSpring5() {
      spring(1870, 1680, new DoSouth());
   }

   public void testSpring6() {
      spring(1901, 1711, new DoSouth());
   }

   public void getRune() {
      int runeRoom = 1576;
      int destinationRoom = 2101;
 
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);
      player.teleportToRoom(runeRoom);
      observer.teleportToRoom(runeRoom);
      
      player.setRune(Rune.GREEN);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();
      
      // Attempt to solve the 2nd part of the riddle first.  This should not solve it.
      if (new DoSay().execute(player, "say ether")) {
         Assert.fail();
      }
      
      // Solve the first riddle correctly.
      new DoSay().execute(player, "say cinders");
      TestUtil.assertContains(playerOutput, "As the answer passes your lips a voice in your mind says: \"Correct...\nNow complete the riddle to gain passage...\"");
      TestUtil.assertContains(observerOutput, "Minex whispers something, then pauses for a moment as if in deep thought...");
      
      // Solve the second riddle correctly.
      if (!new DoSay().execute(player, "say ether")) {
         Assert.fail();
      }
      String runeString = TaMessageManager.TRS004.getMessage() + TaMessageManager.TRS011.getMessage();
      TestUtil.assertContains(playerOutput, runeString);
      TestUtil.assertContains(observerOutput, "");
      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player is not in the correct room.");
      }
      if (!player.getRune().equals(Rune.BLUE)) {
         Assert.fail("Player does not have the correct rune.");         
      }
      
      // Attempt to get the rune a second time.
      observer.setRune(Rune.GREEN);
      new DoSay().execute(observer, "say cinders");
      new DoSay().execute(observer, "say ether");
      TestUtil.assertDoesNotContains(observerOutput, runeString);
      if (observer.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Observer is not in the correct room.");
      }

   }
   
   private void spring(int startRoom, int destinationRoom, Command dirCommand) {
      String playerTeleportString = "&bA spring loaded pressure plate rapidly lifts you through the ceiling!";
      String roomArrivedString = "&YMinex just appeared suddenly through an aperature in the floor!";
      
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);
      player.teleportToRoom(startRoom);
      observer.teleportToRoom(destinationRoom);
      
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();

      // Do it.
      dirCommand.execute(player, "");
      
      // Test all variables.
      TestUtil.assertContains(playerOutput, playerTeleportString);
      TestUtil.assertOutput(observerOutput, roomArrivedString);
      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player is in room " + player.getRoom().getRoomNumber() + " instead of " + destinationRoom);
      }
   }

   private void testDarknessLevel(ArrayList<Integer> darkRooms, int stoneRoom) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);

      // Verify dark rooms with glowstone
      teleportThroughRooms(player, playerOutput, darkRooms, true);
      
      // Verify dark rooms with without glowstone
      player.teleportToRoom(darkRooms.get(0));
      new DoDrop().execute(player, "drop glowstone");
      teleportThroughRooms(player, playerOutput, darkRooms, true);

      // Pull the lever.
      player.teleportToRoom(stoneRoom);
      observer.teleportToRoom(stoneRoom);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();

      new DoPush().execute(player, "push stone");
      TestUtil.assertContains(playerOutput, "You push the protruding stone into it's recess...");
      TestUtil.assertContains(observerOutput, "");
      
      // Verify dark rooms are still dark without glowstone.
      teleportThroughRooms(player, playerOutput, darkRooms, true);      

      // Verify dark rooms are now lit with a glowstone.
      player.teleportToRoom(darkRooms.get(0));
      new DoGet().execute(player, "get glowstone");
      teleportThroughRooms(player, playerOutput, darkRooms, false);      
   }
   
   private void teleportThroughRooms(Player player, MockOutput playerOutput, ArrayList<Integer> darkRooms, boolean contains) {
      String darkString = "It's too dark to see.";
      for (Integer index:darkRooms) {
         player.teleportToRoom(index);
         playerOutput.clearBuffer();
         new DoDefault().execute(player, "");
         if (contains) {
            TestUtil.assertContains(playerOutput, darkString);            
         } else {
            TestUtil.assertDoesNotContains(playerOutput, darkString);
         }
      }      
   }
}
