package org.tdod.ether.functional;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.taimpl.commands.DoEast;
import org.tdod.ether.taimpl.commands.DoNorth;
import org.tdod.ether.taimpl.commands.DoNorthEast;
import org.tdod.ether.taimpl.commands.DoNorthWest;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoSouthEast;
import org.tdod.ether.taimpl.commands.DoSouthWest;
import org.tdod.ether.taimpl.commands.DoUse;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.taimpl.commands.debug.DoSlay;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.Test;

/**
 * Green rune functional test case.
 *
 * @author Ron Kinney
 */
public class GreenRuneTests extends AbstractTestCase {

   private static final int RUNESTAFF_VNUM = 50;

   @Test(groups = { "functional", "Green Rune" })
   public void testGreenRunes() {
      testGreenRune(Rune.YELLOW,
            MessageFormat.format(TaMessageManager.YOURN2.getMessage(), "runestaff") +
            TaMessageManager.TRS011.getMessage(),
            MessageFormat.format(TaMessageManager.OTHRN2.getMessage(), "Minex", "a runestaff"));
      
      testGreenRune(Rune.GREEN,
            MessageFormat.format(TaMessageManager.YOURN2.getMessage(), "runestaff"),
            MessageFormat.format(TaMessageManager.OTHRN2.getMessage(), "Minex", "a runestaff"));
      
      testGreenRune(Rune.BLUE,
            MessageFormat.format(TaMessageManager.YOURN2.getMessage(), "runestaff"),
            MessageFormat.format(TaMessageManager.OTHRN2.getMessage(), "Minex", "a runestaff"));
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport1() {
      teleport(483, 485, new DoNorthWest());
   }
   
   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap1() {
      testTrap(new DoSouth(), 485, 
            "&RA scything blade slices into your stomach!");
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport2() {
      teleport(524, 553, new DoSouthEast());
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap2() {
      testTrap(new DoNorth(), 499, 
            "&RA ball of flame explodes from an opening in the wall and engulfs you!");
   }

   @Test(groups = { "Locked Door", "Green Rune" })
   public void pearlKeyTest() {
      lockedDoorTest(499, new DoSouth(), ExitDirectionEnum.SOUTH,
            "The locked stone door prevents your exit in that direction.", 
            "Your pearl key unlocks the stone door and allows you to pass through." +
            "&YYou're in a cave.\n",
            496,
            "slay stone",
            "While searching the area, you notice pearl key, which you add to your\npossessions.",
            2);
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport3() {
      teleport(488, 498, new DoSouth());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void movement1() {
      move(499, new DoEast(), 455, "&YMinex has just arrived from the west.");
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void testBarrier() {
      hasRuneBarrier();
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport4() {
      teleport(562, 515, new DoSouth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport5() {
      teleport(524, 564, new DoSouthWest());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport6() {
      teleport(573, 575, new DoSouth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport7() {
      teleport(621, 656, new DoNorth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport8() {
      teleport(643, 671, new DoSouthWest());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport9() {
      teleport(672, 618, new DoSouth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport10() {
      teleport(662, 623, new DoNorth());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport11() {
      teleport(640, 662, new DoNorth());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport12() {
      teleport(633, 678, new DoNorth());
   }

   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport13() {
      teleport(682, 633, new DoSouth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport14() {
      teleport(708, 753, new DoSouthEast());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport15() {
      teleport(730, 723, new DoEast());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport16() {
      teleport(724, 762, new DoNorth());
   }
   
   @Test(groups = { "Teleport", "Green Rune" })
   public void teleport17() {
      teleport(759, 737, new DoSouthEast());
   }
   
   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap3() {
      testTrap(new DoNorthEast(), 613, 
            "&RA scything blade slices into your stomach!");
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap4() {
      testTrap(new DoSouthEast(), 610, 
            "&RA scything blade slices into your stomach!");
   }
   
   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap5() {
      testTrap(new DoSouthWest(), 653, 
            "&RA ball of flame explodes from an opening in the wall and engulfs you!");
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap6() {
      testTrap(new DoSouthEast(), 658, 
            "&RA ball of flame explodes from an opening in the wall and engulfs you!");
   }
   
   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap7() {
      testTrap(new DoEast(), 626, 
            "&RSeveral large stones fall on you from above!");
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap8() {
      testTrap(new DoSouth(), 681, 
            "&RSeveral large stones fall on you from above!");
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap9() {
      testTrap(new DoSouth(), 686, 
            "&RA scything blade slices into your stomach!");
   }
   
   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap10() {
      testTrap(new DoWest(), 820, 
            "&RA ball of flame explodes from an opening in the wall and engulfs you!");
   }

   @Test(groups = { "Trap", "Green Rune" })
   public void testTrap11() {
      testTrap(new DoEast(), 824, 
            "&RA ball of flame explodes from an opening in the wall and engulfs you!");
   }
   
   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll1(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 562;
      int mobCount = 2;
      String mob = "stygian dragon";
      int runeCount = 7;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll2(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 580;
      int mobCount = 2;
      String mob = "ice giantess";
      int runeCount = 6;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll3(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 618;
      int mobCount = 2;
      String mob = "hydra";
      int runeCount = 5;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll4(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 672;
      int mobCount = 2;
      String mob = "stygian dragon";
      int runeCount = 4;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll5(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 738;
      int mobCount = 3;
      String mob = "stygian dragon";
      int runeCount = 3;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll6(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 788;
      int mobCount = 1;
      String mob = "apollyon dragon";
      int runeCount = 2;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll7(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 842;
      int mobCount = 2;
      String mob = "apollyon dragon";
      int runeCount = 1;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   @Test(groups = { "Rune Scroll", "Green Rune" })
   public void testRuneScroll8(MockOutput playerOutput, Player player) {
      int runeScrollRoom = 822;
      int mobCount = 1;
      String mob = "flame giant";
      int runeCount = 0;
      getRuneScroll(playerOutput, player, runeScrollRoom, mobCount, mob, runeCount);
   }

   public void testFinalRunestaff(MockOutput playerOutput, Player player, String expectedPlayerOutput, String expectedRoomOutput) {
      int ancientTemple = 429;
      
      MockOutput observerOutput = new MockOutput();
      Player observer = TestUtil.createDefaultPlayer(observerOutput);
      
      player.teleportToRoom(ancientTemple);
      observer.teleportToRoom(ancientTemple);
      
      playerOutput.clearBuffer();
      observerOutput.clearBuffer();

      new DoUse().execute(player, "use runestaff");
      
      TestUtil.assertOutput(playerOutput, expectedPlayerOutput);
      TestUtil.assertOutput(observerOutput, expectedRoomOutput);
   }
   
   private Item getRunestaff(Player player) {
      for (Item item:player.getInventory()) {
         if (item.getVnum() == RUNESTAFF_VNUM) {
            return item;
         }
      }
      
      return null;
   }
   
   private void verifyRunestaff(Player player, MockOutput output, int runes) {
      Item runestaff = getRunestaff(player);
      output.clearBuffer();
      new DoUse().execute(player, "use " + runestaff.getName());
      
      if (runes == 0) {
         String expectedOutput = MessageFormat.format(TaMessageManager.NRNHER.getMessage(), runestaff.getName());
         TestUtil.assertOutput(output, expectedOutput);
      } else {
         String expectedString = MessageFormat.format(TaMessageManager.YOURN1.getMessage(), runestaff.getName(), runes);
         TestUtil.assertOutput(output, expectedString);         
      }
   }
   
   private void getRuneScroll(MockOutput playerOutput, Player player, int runeScrollRoom, int mobCount,
         String mob, int runeCount) {
      
      player.teleportToRoom(runeScrollRoom);
      playerOutput.clearBuffer();
      
      Command slayCommand = new DoSlay();
      player.setIsSysop(true);
      for (int count = 0; count < mobCount; count++) {
         slayCommand.execute(player, "slay " + mob);
      }
      TestUtil.assertContains(playerOutput, TaMessageManager.TRS012.getMessage());
      player.setIsSysop(false);
      
      verifyRunestaff(player, playerOutput, runeCount);

   }
   
   private void testGreenRune(Rune startingRune, String expectedPlayerCompletedOutput, String expectedRoomCompletedOutput) {
      // Initialize
      MockOutput playerOutput = new MockOutput();
      Player player = TestUtil.createDefaultPlayer(playerOutput);
      player.setRune(startingRune);
      
      TestUtil.reset(player);
      obtainRuneStaff(playerOutput, player);
      
      testRuneScroll1(playerOutput, player);
      testRuneScroll2(playerOutput, player);
      testRuneScroll3(playerOutput, player);
      testRuneScroll4(playerOutput, player);
      testRuneScroll5(playerOutput, player);
      testRuneScroll6(playerOutput, player);
      testRuneScroll7(playerOutput, player);
      testRuneScroll8(playerOutput, player);
      
      testFinalRunestaff(playerOutput, player, expectedPlayerCompletedOutput, expectedRoomCompletedOutput);
      
      if (startingRune.getIndex() <= Rune.GREEN.getIndex() && !player.getRune().equals(Rune.GREEN)) {
         Assert.fail("Player should have the green rune, but has " + player.getRune());
      }
   }

   private void obtainRuneStaff(MockOutput playerOutput, Player player) {
      int runestaffRoom = 499;
      String slayString = "slay ice giant";
      String expectedDropString = "While searching the area, you notice runestaff, which you add to your\npossessions.";

      // Initialize
      player.teleportToRoom(runestaffRoom);

      player.setIsSysop(true);
      new DoSlay().execute(player, slayString);
      player.setIsSysop(false);

      TestUtil.assertContains(playerOutput, expectedDropString);

      Item runestaff = getRunestaff(player);
      if (runestaff == null) {
         Assert.fail(slayString + " did not drop item # " + RUNESTAFF_VNUM);
         return;
      }
      
      verifyRunestaff(player, playerOutput, 8);
   }
   

}
