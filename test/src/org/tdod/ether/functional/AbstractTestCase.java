package org.tdod.ether.functional;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.commands.AbstractMovementCommand;
import org.tdod.ether.taimpl.commands.DoLook;
import org.tdod.ether.taimpl.commands.DoPull;
import org.tdod.ether.taimpl.commands.DoPush;
import org.tdod.ether.taimpl.commands.DoSay;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoUp;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.taimpl.commands.debug.DoSlay;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;

public abstract class AbstractTestCase {

   protected void pitTrapTest(int pitRoom, String expectedFailString, String expectedSuccessString) {
      MockOutput output = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(output);
      
      // Attempt to go through the locked door.
      player.teleportToRoom(pitRoom);
      output.clearBuffer();
      new DoUp().execute(player, "");

      TestUtil.assertContains(output, expectedFailString);
      
      player.placeItemInInventory(WorldManager.getItem(22), false);

      new DoUp().execute(player, "");
      
      TestUtil.assertContains(output, expectedSuccessString);
   }

   protected void runeTest(int runeRoom, int startRoom, AbstractMovementCommand dirCommand, Rune startRune, Rune expectedRune, String expectedString) {
      MockOutput output = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(output);

      for (Trigger t:WorldManager.getRoom(runeRoom).getTriggers()) {
         t.setTriggered(false);
      }
      player.teleportToRoom(startRoom);
      output.clearBuffer();
      player.setRune(startRune);
      
      if (!player.getRune().equals(startRune)) {
         Assert.fail("Player has rune of " + player.getRune() + "instead of " + startRune);
      }
      output.clearBuffer();
      
      dirCommand.execute(player, "");
      
      TestUtil.assertContains(output, expectedString);
      
      if (!player.getRune().equals(expectedRune)) {
         Assert.fail("Player has rune of " + player.getRune() + "instead of " + expectedRune);
      }

   }

   protected void lockedDoorTest(int doorRoom, AbstractMovementCommand dirCommand, ExitDirectionEnum exitDir, String expectedLockedString,
         String expectedSuccessString, int keyDropRoom, String slayString, String expectedKeyDropString, int mobCount) {
      try {
         MockOutput output = new MockOutput();
         Player player = TestUtil.createDefaultPlayer(output);

         // Attempt to go through the locked door.
         player.teleportToRoom(doorRoom);
         output.clearBuffer();
         dirCommand.execute(player, "");
         
         TestUtil.assertOutput(output, expectedLockedString);

         getKey(output, player, keyDropRoom, slayString, expectedKeyDropString, mobCount);

         Item key = player.getInventory().get(player.getInventory().size() - 1);
         if (!key.getName().contains("key")) {
            Assert.fail("Mob did not drop a key.");
         }
         
         player.teleportToRoom(doorRoom);
         output.clearBuffer(); 

         Exit exit = player.getRoom().getExit(exitDir);
         
         // Attempt to go through the door with the key.
         dirCommand.execute(player, "");

         TestUtil.assertContains(output, expectedSuccessString);
         
         if (exit.getDoor().getV4() == 1) {
            if (player.getInventory().contains(key)) {
               Assert.fail("The key was not consumed.");
            }
            if (!exit.getDoor().isUnlocked()) {
               Assert.fail("The exit is not unlocked.");               
            }
         } else {
            if (!player.getInventory().contains(key)) {
               Assert.fail("The key was consumed.");
            }
            if (exit.getDoor().isUnlocked()) {
               Assert.fail("The exit is unlocked.");               
            }
         }
         
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());         
      }            
   }

   
   protected void getKey(MockOutput output, Player player, int keyDropRoom, String slayString, String expectedString, int mobCount) {
      player.teleportToRoom(keyDropRoom);
      output.clearBuffer();
      
      player.setIsSysop(true);
      for (int count = 0; count < mobCount; count++) {
         new DoSlay().execute(player, slayString);
         player.setRestTicker(0);         
      }
      player.setIsSysop(false);
      
      TestUtil.assertContains(output, expectedString);
   }
   
   protected void testTrap(Command dirCommand, int startRoom, String expectedTrapText) {
      MockOutput output = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(output);

      player.teleportToRoom(startRoom);
      output.clearBuffer();
      dirCommand.execute(player, "");
      
      TestUtil.assertContains(output, expectedTrapText);
      
      if (player.getVitality().getCurVitality() >= player.getVitality().getMaxVitality()) {
         Assert.fail("The trap did not do any damage.");
      }

      Trigger trigger = null;
      for (Trigger t:player.getRoom().getTriggers()) {
         if (t.getTriggerType().equals(TriggerType.TRAP)) {
            trigger = t;
         }
      }
      
      Assert.assertNotNull(trigger);
      
      if (trigger.getV3() > 0) {
         if (!player.getStatus().equals(Status.POISONED)) {
            Assert.fail("Trap should poison the player but didn't.");
         }
      } else {
         if (player.getStatus().equals(Status.POISONED)) {
            Assert.fail("Trap poisoned the player but should not have.");
         }         
      }
   }

   protected void sayTest(int riddleRoom, AbstractMovementCommand dirCommand, String playerLockedString, String observerLockedString,
         String playerSayString, String observerSayString, String playerMoveString, String observerMoveString, String say) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);

      player.teleportToRoom(riddleRoom);
      observer.teleportToRoom(riddleRoom);
      
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();
      
      // Go through the blocked passage.
      dirCommand.execute(player, "");
      TestUtil.assertOutput(playerOutput, playerLockedString);
      TestUtil.assertOutput(observerOutput, observerLockedString);
      
      // Unlock the passage.
      Command command = new DoSay();
      command.execute(player, say);
      TestUtil.assertOutput(playerOutput, playerSayString);
      TestUtil.assertOutput(observerOutput, observerSayString);

      // Go through the opened passage.
      dirCommand.execute(player, "");
      TestUtil.assertContains(playerOutput, playerMoveString);
      TestUtil.assertOutput(observerOutput, observerMoveString);
   }
   
   protected void pushStoneToTeleportTest(int stoneRoom, int destinationRoom, String pushStoneString, String fromPushStoneString,
         String toPushStoneString) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput fromObserverOutput = new MockOutput();
      Player fromObserver = TestUtil.createDefaultPlayer(fromObserverOutput);
      MockOutput toObserverOutput = new MockOutput();
      Player toObserver = TestUtil.createDefaultPlayer(toObserverOutput);

      player.teleportToRoom(stoneRoom);
      fromObserver.teleportToRoom(stoneRoom);
      toObserver.teleportToRoom(destinationRoom);
      
      playerOutput.clearBuffer();
      fromObserverOutput.clearBuffer();
      toObserverOutput.clearBuffer();

      Command cmd = new DoPush();
      cmd.execute(player, "push stone");
      
      TestUtil.assertContains(playerOutput, pushStoneString);
      TestUtil.assertOutput(fromObserverOutput, fromPushStoneString);
      TestUtil.assertOutput(toObserverOutput, toPushStoneString);
   }
   
   protected void pushStoneToOpenPassage(int stoneRoom, int passageRoom, int passageDestinationRoom, 
         Command dirCommand, String closedPassageLookDesc, String closedPassageString, String pushStonePlayerString,
         String pushStoneRoomString, String openPassageLookDesc, String destinationRoom) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput stoneRoomObserverOutput = new MockOutput();
      Player stoneRoomObserver = TestUtil.createDefaultPlayer(stoneRoomObserverOutput);
      MockOutput passageObserverOutput = new MockOutput();
      Player passageObserver = TestUtil.createDefaultPlayer(passageObserverOutput);

      player.teleportToRoom(stoneRoom);
      stoneRoomObserver.teleportToRoom(stoneRoom);
      passageObserver.teleportToRoom(passageRoom);
      
      playerOutput.clearBuffer();
      stoneRoomObserverOutput.clearBuffer();
      passageObserverOutput.clearBuffer();

      // Verify the closed passage room description.
      new DoLook().execute(passageObserver, "");
      TestUtil.assertOutput(passageObserverOutput, closedPassageLookDesc);
      
      // Attempt to move through blocked passage.
      dirCommand.execute(passageObserver, "");
      TestUtil.assertOutput(passageObserverOutput, closedPassageString);
      
      // Open the passage
      new DoPush().execute(player, "push stone");
      TestUtil.assertOutput(playerOutput, pushStonePlayerString);
      TestUtil.assertOutput(stoneRoomObserverOutput, pushStoneRoomString);
      
      // Verify the open passage room description.
      new DoLook().execute(passageObserver, "");
      TestUtil.assertOutput(passageObserverOutput, openPassageLookDesc);

      // Move
      new DoSouth().execute(passageObserver, "");
      TestUtil.assertContains(passageObserverOutput, destinationRoom);
      if (passageObserver.getRoom().getRoomNumber() != passageDestinationRoom) {
         Assert.fail("Open passage from " + passageRoom + " lead to room " + passageObserver.getRoom().getRoomNumber() + " instead of " + passageDestinationRoom);
      }
   }
   
   protected void disableTrapTest(int leverRoom, int trapRoom, int entranceRoom, Command dirCommand, 
         String expectedTrapText, String playerPullLeverStringText, String roomPullLeverStringText) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);

      // Test the trap.
      testTrap(dirCommand, entranceRoom, expectedTrapText);
      
      player.teleportToRoom(leverRoom);
      observer.teleportToRoom(leverRoom);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();

      // Pull the lever.
      new DoPull().execute(player, "pull lever");
      TestUtil.assertOutput(playerOutput, playerPullLeverStringText);
      TestUtil.assertOutput(observerOutput, roomPullLeverStringText);
      
      // Test the trap again.
      player.teleportToRoom(entranceRoom);
      playerOutput.clearBuffer();
      dirCommand.execute(player, "");
      TestUtil.assertDoesNotContains(playerOutput, expectedTrapText);
      if (player.getRoom().getRoomNumber() != trapRoom) {
         Assert.fail("The player is not in the trap room.");
      }
      for (Trigger t:WorldManager.getRoom(trapRoom).getTriggers()) {
         if (!t.isTriggered()) {
            Assert.fail("The trap was not disabled.");                     
         }
      }
   }
   
   protected void pullLeverToOpenPassage(int blockedPassageRoom, int leverRoom, int destinationRoom, String playerBlockedPassageString,
         String roomBlockedPassageString, String playerPullLeverString, String roomPullLeverString, String playerMoveString,
         String roomMoveString, String blockedRoomDescription, String unblockedRoomDescription) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);

      // Check the blocked passage.
      player.teleportToRoom(blockedPassageRoom);
      observer.teleportToRoom(blockedPassageRoom);
      playerOutput.clearBuffer();

      new DoLook().execute(player, "");
      TestUtil.assertOutput(playerOutput, blockedRoomDescription);
      observerOutput.clearBuffer();
      
      new DoWest().execute(player, "");
      TestUtil.assertOutput(playerOutput, playerBlockedPassageString);
      TestUtil.assertOutput(observerOutput, roomBlockedPassageString);
      
      // Unlock the passage.
      player.teleportToRoom(leverRoom);
      observer.teleportToRoom(leverRoom);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();
      
      new DoPull().execute(player, "pull lever");
      TestUtil.assertOutput(playerOutput, playerPullLeverString);
      TestUtil.assertOutput(observerOutput, roomPullLeverString);

      // Check the removed passage.
      player.teleportToRoom(blockedPassageRoom);
      observer.teleportToRoom(blockedPassageRoom);
      playerOutput.clearBuffer();
      
      new DoLook().execute(player, "");
      TestUtil.assertOutput(playerOutput, unblockedRoomDescription);
      observerOutput.clearBuffer();

      new DoWest().execute(player, "");
      TestUtil.assertContains(playerOutput, playerMoveString);
      TestUtil.assertOutput(observerOutput, roomMoveString);
      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player is in room " + player.getRoom() + " instead of " + destinationRoom);
      }
   }
   
   protected void getTreasure(int startRoom, Command dirCommand, String treasureString) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      player.setGold(0);
      
      // Get the treasure.
      player.teleportToRoom(startRoom);
      playerOutput.clearBuffer();
      dirCommand.execute(player, "");
      TestUtil.assertContains(playerOutput, treasureString);
      if (player.getGold() <= 0) {
         Assert.fail("Player did not get the treasure.");
      }
      
      // Try again, the treasure should be gone.
      int currentGold = player.getGold();
      player.teleportToRoom(startRoom);
      playerOutput.clearBuffer();
      dirCommand.execute(player, "");
      TestUtil.assertDoesNotContains(playerOutput, treasureString);
      if (player.getGold() != currentGold) {
         Assert.fail("Player received treasure when he should not have.");
      }
   }
   
   protected void teleport(int startRoom, int destinationRoom, Command dirCommand) {
      String playerTeleportString = "&bA sudden flash of light momentarily blinds you!";
      String roomArrivedString = "&YMinex has just appeared in a blinding flash of light!";
      
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
   
   protected void move(int startRoom, Command dirCommand, int destinationRoom, String observerString) {
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
      
      // Verify.
      TestUtil.assertOutput(observerOutput, observerString);
      
      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player landed in room " + player.getRoom() + " instead of " + destinationRoom);
      }
   }
   
   protected void hasRuneBarrier() {
      int startRoom = 507;
      int destinationRoom = 508;
      Rune blockedRune = Rune.WHITE;
      Rune passableRune = Rune.YELLOW;
      String expectedPlayerString = "A mystical force prevents your exit in that direction.";
      String expectedBlockedRoomString = MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), "Minex", "his");
      String expectedEnterRoomString = "&YMinex has just arrived from the north.";
      Command dirCommand = new DoSouth();
      
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);
      player.teleportToRoom(startRoom);
      observer.teleportToRoom(startRoom);
      
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();
      player.setRune(blockedRune);
      
      dirCommand.execute(player, "");
      
      TestUtil.assertOutput(playerOutput, expectedPlayerString);
      TestUtil.assertContains(observerOutput, expectedBlockedRoomString);
      
      // Attempt it again, but with the correct rune.
      player.setRune(passableRune);
      observer.teleportToRoom(destinationRoom);
      observerOutput.clearBuffer();
      
      dirCommand.execute(player, "");
      
      TestUtil.assertOutput(observerOutput, expectedEnterRoomString);
      
      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player landed in room " + player.getRoom() + " instead of " + destinationRoom);
      }
   }

   protected void pullLeverToOpenDoor(int leverRoom, int lockedRoom, int destinationRoom, Command dirCommand,
         String expectedBlockedPlayerString) {
      String expectedBlockedRoomString = MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), "Minex", "his"); 
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";

      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);

      // Verify that the door is closed.
      player.teleportToRoom(lockedRoom);
      observer.teleportToRoom(lockedRoom);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();

      dirCommand.execute(player, "");
      
      TestUtil.assertOutput(playerOutput, expectedBlockedPlayerString);
      TestUtil.assertContains(observerOutput, expectedBlockedRoomString);

      // Pull the lever.
      player.teleportToRoom(leverRoom);
      observer.teleportToRoom(leverRoom);
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();
      
      new DoPull().execute(player, "pull lever");
      TestUtil.assertOutput(playerOutput, playerPullLeverStringText);
      TestUtil.assertOutput(observerOutput, roomPullLeverStringText);

      // Attempt to go through the unlock door.
      player.teleportToRoom(lockedRoom);
      playerOutput.clearBuffer();

      dirCommand.execute(player, "");

      if (player.getRoom().getRoomNumber() != destinationRoom) {
         Assert.fail("Player should be in room " + destinationRoom + " but was in room " + player.getRoom().getRoomNumber());
      }

   }
}
