package org.tdod.ether.functional;

import org.tdod.ether.taimpl.commands.DoDown;
import org.tdod.ether.taimpl.commands.DoEast;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.testng.annotations.Test;

public class SewerTests extends AbstractTestCase {

   @Test(groups = { "Locked Door", "Sewers" })
   public void rubyKeyTest() {
      lockedDoorTest(889, new DoSouth(), ExitDirectionEnum.SOUTH,
            "The locked stone door prevents your exit in that direction.", 
            "Your ruby key unlocks the stone door and allows you to pass through." +
            "&YYou're in the town sewers.\n",
            865,
            "slay anaconda",
            "While searching the area, you notice ruby key, which you add to your\npossessions.",
            2); 
   }
   
   @Test(groups = { "Locked Door", "Sewers" })
   public void platinumKeyTest() {
      lockedDoorTest(909, new DoEast(), ExitDirectionEnum.EAST,
            "The locked stone door prevents your exit in that direction.",
            "Your platinum key unlocks the stone door and allows you to pass through." + 
            "&YYou're in the town sewers.\n",
            958,
            "slay mage",
            "While searching the area, you notice platinum key, which you add to your\npossessions.",
            1); 
   }
   
   @Test(groups = { "Locked Door", "Sewers" })
   public void onyxKeyTest() {
      lockedDoorTest(909, new DoSouth(), ExitDirectionEnum.SOUTH,
            "The locked stone door prevents your exit in that direction.",
            "Your onyx key unlocks the stone door and allows you to pass through." + 
            "&YYou're in the town sewers.\n",
            928,
            "slay troll",
            "While searching the area, you notice onyx key, which you add to your\npossessions.",
            4); 
   }

   @Test(groups = { "Pit Trap", "Sewers" })
   public void pitTrapTest() {
      pitTrapTest(970,
            "The walls of this pit are too steep to climb unaided.",
            "You climb out of the pit using your rope.&YYou're in the town sewers.") ;
   }

   @Test(groups = { "Locked Door", "Sewers" })
   public void pearlKeyTest() {
      lockedDoorTest(990, new DoWest(), ExitDirectionEnum.WEST,
            "The locked stone door prevents your exit in that direction.",
            "Your pearl key unlocks the stone door and allows you to pass through." + 
            "&YYou're in the town sewers.\n",
            989,
            "slay hydra",
            "While searching the area, you notice pearl key, which you add to your\npossessions.",
            1); 
   }

   @Test(groups = { "Trap", "Sewers" })
   public void testTrap() {
      testTrap(new DoWest(), 989, 
            "&RSeveral crossbow bolts fire from holes in the walls, striking you!");
   }

   public void treasureTest() {
      getTreasure(1088, new DoDown(),
            "&YYou find a chest of coins and gemstones worth");
   }
}
