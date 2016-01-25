package org.tdod.ether.functional;

import junit.framework.Assert;

import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.AbstractMovementCommand;
import org.tdod.ether.taimpl.commands.DoEast;
import org.tdod.ether.taimpl.commands.DoNorth;
import org.tdod.ether.taimpl.commands.DoNorthEast;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoSouthEast;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.Test;

public class WhiteRuneTests extends AbstractTestCase {
   
   @Test(groups = { "Locked Door", "White Rune" })
   public void copperKeyTest() {
      lockedDoorTest(2, new DoSouthEast(), ExitDirectionEnum.SOUTHEAST,
            "The locked oak door prevents your exit in that direction.", 
            "Your copper key unlocks the oak door and allows you to pass through." +
            "&YYou're in a cave.\n",
            2,
            "slay minotaur",
            "While searching the area, you notice copper key, which you add to your\npossessions.",
            1);
   }
   
   @Test(groups = { "Locked Door", "White Rune" })
   public void brassKeyTest() {
      lockedDoorTest(3, new DoEast(), ExitDirectionEnum.EAST,
            "The locked iron door prevents your exit in that direction.", 
            "Your brass key unlocks the iron door and allows you to pass through." +
            "&YYou're in a cave.\n",
            3,
            "slay ogre",
            "While searching the area, you notice brass key, which you add to your\npossessions.",
            1);      
   }
   
   @Test(groups = { "Locked Door", "White Rune" })
   public void ironKeyTest() {
      lockedDoorTest(1, new DoNorthEast(), ExitDirectionEnum.NORTHEAST,
            "The locked stone door prevents your exit in that direction.", 
            "Your iron key unlocks the stone door and allows you to pass through." +
            "&YYou're in a cave.\n",
            4,
            "slay cyclops",
            "While searching the area, you notice iron key, which you add to your\npossessions.",
            1);
   }
   
   @Test(groups = { "Locked Door", "White Rune" })
   public void bronzeKeyTest() {
      lockedDoorTest(5, new DoNorthEast(), ExitDirectionEnum.NORTHEAST,
            "The locked bronze door prevents your exit in that direction.", 
            "Your bronze key unlocks the bronze door and allows you to pass through." +
            "&YYou're at the top of a circular stairwell.\n",
            5,
            "slay cyclops",
            "While searching the area, you notice bronze key, which you add to your\npossessions.",
            2);
   }

   @Test(groups = { "Locked Door", "White Rune" })
   public void silverKeyTest() {
      lockedDoorTest(106, new DoSouth(), ExitDirectionEnum.SOUTH,
            "The locked iron door prevents your exit in that direction.", 
            "Your silver key unlocks the iron door and allows you to pass through." +
            "&YYou are in a grand hall.\n",
            55,
            "slay cyclops",
            "While searching the area, you notice silver key, which you add to your\npossessions.",
            1);
   }

   @Test(groups = { "Locked Door", "White Rune" })
   public void electrumKeyTest() {
      lockedDoorTest(111, new DoNorth(), ExitDirectionEnum.NORTH,
            "The locked brass door prevents your exit in that direction.", 
            "Your electrum key unlocks the brass door and allows you to pass through." +
            "&YYou're in a cave.\n",
            111,
            "slay stygian",
            "While searching the area, you notice electrum key, which you add to your\npossessions.",
            1);      
   }

   @Test(groups = { "Trap", "White Rune" })
   public void testSpikedTrap() {
      testTrap(new DoSouthEast(), 19, 
            "&RA spiked trap catches your foot and pain shoots up your leg!");
   }
   
   @Test(groups = { "Trap", "White Rune" })
   public void testCrossbowTrap() {
      testTrap(new DoSouth(), 104, 
            "&RSeveral crossbow bolts fire from holes in the walls, striking you!");
   }
   
   @Test(groups = { "Trap", "White Rune" })
   public void testStonesTrap() {
      testTrap(new DoSouth(), 182, 
            "&RSeveral large stones fall on you from above!");
   }
   
   @Test(groups = { "Pit Trap", "White Rune" })
   public void dungeon2PitTrapTest() {
      pitTrapTest(179,
            "The walls of this pit are too steep to climb unaided.",
            "You climb out of the pit using your rope.&YYou're in a cave.") ;
   }

   @Test(groups = { "Rune Completion", "White Rune" })
   public void whiteRuneTest() {
      int startRoom = 181;
      int runeRoom = 182;
      AbstractMovementCommand dirCommand = new DoNorth();
      Rune startRune = Rune.NONE;
      Rune expectedRune = Rune.WHITE;
      
      runeTest(runeRoom, startRoom, dirCommand, startRune, expectedRune,
            TaMessageManager.TRS001.getMessage() +
            TaMessageManager.TRS011.getMessage() +
            "&YYou find a chest of coins and gemstones worth"
            );
   }
 
   @Test(groups = { "Barriers", "HAS_RUNE", "White Rune" })
   public void northPlazaNoRuneTest() {
      try {
         MockOutput output = new MockOutput();
         Player player = TestUtil.createDefaultPlayer(output);
         
         player.teleportToRoom(TestUtil.NORTH_PLAZA_ROOM);
         output.clearBuffer();
         player.setRune(Rune.NONE);
         new DoEast().execute(player, "");
         
         String expectedString = "&YYou're in the arena.\n&G&R&M" + TaMessageManager.BYSELF.getMessage() + TaMessageManager.NOTING.getMessage();
         if (!output.getBuffer().equals(expectedString)) {
            Assert.fail("Expected output did not match.");
         }
         output.clearBuffer();
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());         
      }
   }

   @Test(groups = { "Barriers", "HAS_RUNE", "White Rune" })
   public void northPlazaRuneTest() {
      MockOutput output = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(output);

      player.teleportToRoom(TestUtil.NORTH_PLAZA_ROOM);
      output.clearBuffer();
      player.setRune(Rune.WHITE);
      new DoEast().execute(player, "");

      String expectedString = "A mystical force prevents your exit in that direction.";
      if (!output.getBuffer().equals(expectedString)) {
         Assert.fail("Expected output did not match.");
      }
      output.clearBuffer();
   }

}
