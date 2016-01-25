package org.tdod.ether.functional;

import java.text.MessageFormat;

import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.AbstractMovementCommand;
import org.tdod.ether.taimpl.commands.DoNorth;
import org.tdod.ether.taimpl.commands.DoNorthEast;
import org.tdod.ether.taimpl.commands.DoNorthWest;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.TaMessageManager;
import org.testng.annotations.Test;

public class YellowRuneTests extends AbstractTestCase {

   @Test(groups = { "Say", "Yellow Rune" })
   public void sayKomiTest() {
      sayTest(1192, new DoSouth(),
            "The locked iron door prevents your exit in that direction.",
            "&YMinex has just gone to the south." + MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), "Minex", "his"),
            "As the answer passes your lips, you feel the floor vibrate faintly.",
            "Minex whispers something, and you feel the floor vibrate faintly.",
            "&YYou're in a stonework corridor.",
            "&YMinex has just gone to the south.",
            "say komi"
      );
   }
   
   @Test(groups = { "Say", "Yellow Rune" })
   public void sayArokTest() {
      sayTest(1284, new DoNorth(),
            TaMessageManager.NOEXIT.getMessage(),
            "",
            "As the answer passes your lips, the northern wall slides silently into the\nceiling.",
            "Minex whispers something, and you feel the floor vibrate faintly.",
            "&YYou're in a stonework chamber.",
            "&YMinex has just gone to the north.",
            "say arok"
      );
   }
   
   @Test(groups = { "Push to Teleport", "Yellow Rune" })
   public void pushToTeleportTest1() {
      pushStoneToTeleportTest(1231, 1232, 
            "You push the protruding stone into it's recess...&YYou're in a stonework corridor.",
            MessageFormat.format(TaMessageManager.EXT11.getMessage(), "Minex"),
            MessageFormat.format(TaMessageManager.ENT11.getMessage(), "Minex"));
   }

   @Test(groups = { "Push to Teleport", "Yellow Rune" })
   public void pushToTeleportTest2() {
      pushStoneToTeleportTest(1232, 1236, 
            "You push the protruding stone into it's recess...&YYou're in a stonework corridor.", 
            MessageFormat.format(TaMessageManager.EXT11.getMessage(), "Minex"),
            MessageFormat.format(TaMessageManager.ENT11.getMessage(), "Minex"));
   }

   @Test(groups = { "Push to Open Passage", "Yellow Rune" })
   public void pushStoneToOpenPassageInStoneworks() {
      int stoneRoom = 1243;
      int passageRoom = 1222;
      int passageDestinationRoom = 1223;
      Command dirCommand = new DoSouth();
      String closedPassageLookDesc = "You are standing in a well kept stonework corridor. The tightly fit stone\n" +
      "tiles that make up the floor and walls here bear unusal designs and are of\n" +
      "very fine craftsmanship. The corridor continues to the northeast.";
      String closedPassageString = TaMessageManager.NOEXIT.getMessage();
      String pushStonePlayerString = "As you push the protruding stone into it's recess, you feel the floor vibrate\nfaintly.";
      String pushStoneRoomString = "You notice Minex doing something out of the corner of your eye, and feel the\nfloor vibrate faintly.";
      String openPassageLookDesc = "You are standing in a well kept stonework corridor. The tightly fit stone\n" +
      "tiles that make up the floor and walls here bear unusal designs and are of\n" +
      "very fine craftsmanship. The corridor runs to the northeast and south.";
      String destinationRoom = "You're in a stonework chamber.";
      pushStoneToOpenPassage(stoneRoom, passageRoom, passageDestinationRoom, dirCommand, 
            closedPassageLookDesc, 
            closedPassageString, 
            pushStonePlayerString, 
            pushStoneRoomString, 
            openPassageLookDesc, 
            destinationRoom);
   }
 
   @Test(groups = { "Disable Trap", "Yellow Rune" })
   public void disableStonework1Trap() {
      int leverRoom = 1213;
      int trapRoom = 1218;
      int entranceRoom = 1220;
      Command dirCommand = new DoNorth();
      String expectedTrapText = "&RSeveral large stones fall on you from above!";
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";
      disableTrapTest(leverRoom, trapRoom, entranceRoom, dirCommand, expectedTrapText, playerPullLeverStringText, roomPullLeverStringText);
   }
   
   @Test(groups = { "Disable Trap", "Yellow Rune" })
   public void disableStonework2Trap() {
      int leverRoom = 1266;
      int trapRoom = 1281;
      int entranceRoom = 1278;
      Command dirCommand = new DoNorthEast();
      String expectedTrapText = "&RSeveral large stones fall on you from above!";
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";
      disableTrapTest(leverRoom, trapRoom, entranceRoom, dirCommand, expectedTrapText, playerPullLeverStringText, roomPullLeverStringText);
   }

   @Test(groups = { "Disable Trap", "Yellow Rune" })
   public void disableStonework3Trap() {
      int leverRoom = 1316;
      int trapRoom = 1297;
      int entranceRoom = 1296;
      Command dirCommand = new DoNorthWest();
      String expectedTrapText = "&RA huge stone block slams down on you from above!";
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";
      disableTrapTest(leverRoom, trapRoom, entranceRoom, dirCommand, expectedTrapText, playerPullLeverStringText, roomPullLeverStringText);      
   }

   @Test(groups = { "Disable Trap", "Yellow Rune" })
   public void disableStonework4Trap() {
      int leverRoom = 1344;
      int trapRoom = 1327;
      int entranceRoom = 1328;
      Command dirCommand = new DoNorthWest();
      String expectedTrapText = "&RA huge stone block slams down on you from above!";
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";
      disableTrapTest(leverRoom, trapRoom, entranceRoom, dirCommand, expectedTrapText, playerPullLeverStringText, roomPullLeverStringText);      
   }

   @Test(groups = { "Disable Trap", "Yellow Rune" })
   public void disableStonework5Trap() {
      int leverRoom = 1364;
      int trapRoom = 1366;
      int entranceRoom = 1365;
      Command dirCommand = new DoNorthWest();
      String expectedTrapText = "&RA huge stone block slams down on you from above!";
      String playerPullLeverStringText = "You pulled the lever.";
      String roomPullLeverStringText = "You notice Minex doing something out of the corner of your eye.";
      disableTrapTest(leverRoom, trapRoom, entranceRoom, dirCommand, expectedTrapText, playerPullLeverStringText, roomPullLeverStringText);      
   }

   @Test(groups = { "Trap", "Yellow Rune" })
   public void testStonework6Trap1() {
      testTrap(new DoSouth(), 1403, "&RA huge stone block slams down on you from above!");
   }
   
   @Test(groups = { "Trap", "Yellow Rune" })
   public void testStonework6Trap2() {
      testTrap(new DoNorth(), 1440, "&RA huge stone block slams down on you from above!");      
   }

   @Test(groups = { "Open West Passage", "Yellow Rune" })
   public void openWestStoneworkPassage1() {
      int blockedPassageRoom = 1452;
      int leverRoom = 1403;
      int destinationRoom = 1453;
      String playerBlockedPassageString = "The wide chasm prevents your exit in that direction.";
      String roomBlockedPassageString = "&YMinex has just gone to the west." + MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), "Minex", "his");
      String playerPullLeverString = "You pulled the lever.";
      String roomPullLeverString = "You notice Minex doing something out of the corner of your eye.";
      String playerMoveString = "&YYou're in a stonework corridor.";
      String roomMoveString = "&YMinex has just gone to the west.";
      String blockedRoomDescription = "You are standing in a well kept stonework corridor. The tightly fit stone\n" +
         "tiles that make up the floor and walls here bear unusal designs and are of\n" +
         "very fine craftsmanship. The corridor runs to the east and west. A very wide\n" +
         "natural chasm blocks the passage to the west.";
      String unblockedRoomDescription = "You are standing in a well kept stonework corridor. The tightly fit stone\n" + 
         "tiles that make up the floor and walls here bear unusal designs and are of\n" +
         "very fine craftsmanship. The corridor runs to the east and west.";
      pullLeverToOpenPassage(blockedPassageRoom, leverRoom, destinationRoom, playerBlockedPassageString, roomBlockedPassageString, 
            playerPullLeverString, roomPullLeverString, playerMoveString, roomMoveString, blockedRoomDescription, unblockedRoomDescription);
   }

   @Test(groups = { "Open West Passage", "Yellow Rune" })
   public void openWestStoneworkPassage2() {
      int blockedPassageRoom = 1456;
      int leverRoom = 1440;
      int destinationRoom = 1457;
      String playerBlockedPassageString = "The wide chasm prevents your exit in that direction.";
      String roomBlockedPassageString = "&YMinex has just gone to the west." + MessageFormat.format(TaMessageManager.CAMBAK.getMessage(), "Minex", "his");
      String playerPullLeverString = "You pulled the lever.";
      String roomPullLeverString = "You notice Minex doing something out of the corner of your eye.";
      String playerMoveString = "&YYou're in a stonework corridor.";
      String roomMoveString = "&YMinex has just gone to the west.";
      String blockedRoomDescription = "You are standing in a well kept stonework corridor. The tightly fit stone\n" + 
         "tiles that make up the floor and walls here bear unusal designs and are of\n" + 
         "very fine craftsmanship. The corridor runs to the east and west. A very wide\n" +
         "natural chasm blocks the passage to the west.";
      String unblockedRoomDescription = "You are standing in a well kept stonework corridor. The tightly fit stone\n" +
         "tiles that make up the floor and walls here bear unusal designs and are of\n" +
         "very fine craftsmanship. The corridor runs to the east and west.";
      pullLeverToOpenPassage(blockedPassageRoom, leverRoom, destinationRoom, playerBlockedPassageString, roomBlockedPassageString, 
            playerPullLeverString, roomPullLeverString, playerMoveString, roomMoveString, blockedRoomDescription, unblockedRoomDescription);
   }

   @Test(groups = { "Rune Completion", "Yellow Rune" })
   public void yellowRuneTest() {
      int startRoom = 1456;
      int runeRoom = 1457;
      AbstractMovementCommand dirCommand = new DoWest();
      Rune startRune = Rune.WHITE;
      Rune expectedRune = Rune.YELLOW;

      boolean isDoorLocked = WorldManager.getRoom(startRoom).getExit(ExitDirectionEnum.WEST).getDoor().isUnlocked();
      
      runeTest(runeRoom, startRoom, dirCommand, startRune, expectedRune,
            TaMessageManager.TRS002.getMessage() + TaMessageManager.TRS011.getMessage());

      WorldManager.getRoom(startRoom).getExit(ExitDirectionEnum.WEST).getDoor().setIsUnlocked(isDoorLocked);

   }

}
