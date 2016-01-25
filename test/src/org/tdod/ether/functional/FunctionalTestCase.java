package org.tdod.ether.functional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.AbstractTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FunctionalTestCase extends AbstractTest {

   private static Log _log = LogFactory.getLog(FunctionalTestCase.class);
   
   @BeforeClass
   public void setUp() {
      super.baseSetUp();
   }
   
   @Test(groups = { "functional", "White Rune" })
   public void whiteRuneTest() {
      _log.info("Running through the white rune obstacles...");
      WhiteRuneTests whiteRuneTests = new WhiteRuneTests();
      
      whiteRuneTests.northPlazaNoRuneTest();
      whiteRuneTests.copperKeyTest();
      whiteRuneTests.brassKeyTest();
      whiteRuneTests.ironKeyTest();
      whiteRuneTests.bronzeKeyTest();
      whiteRuneTests.silverKeyTest();
      whiteRuneTests.electrumKeyTest();
      whiteRuneTests.testSpikedTrap();
      whiteRuneTests.testCrossbowTrap();
      whiteRuneTests.testStonesTrap();
      whiteRuneTests.dungeon2PitTrapTest();
      whiteRuneTests.whiteRuneTest();
      whiteRuneTests.northPlazaRuneTest();
   }

   @Test(groups = { "functional", "Sewers" })   
   public void sewerTest() {
      _log.info("Running through the sewers...");
      SewerTests sewerTests = new SewerTests();
      
      sewerTests.treasureTest();
      sewerTests.rubyKeyTest();
      sewerTests.platinumKeyTest();
      sewerTests.onyxKeyTest();
      sewerTests.pitTrapTest();
      sewerTests.pearlKeyTest();
      sewerTests.testTrap();
   }
   
   @Test(groups = { "functional", "Yellow Rune" })
   public void yellowRuneTest() {
      _log.info("Running through the yellow rune obstacles...");
      YellowRuneTests yellowRuneTests = new YellowRuneTests();
      
      yellowRuneTests.sayKomiTest();
      yellowRuneTests.sayArokTest();
      yellowRuneTests.pushToTeleportTest1();
      yellowRuneTests.pushToTeleportTest2();
      yellowRuneTests.pushStoneToOpenPassageInStoneworks();
      yellowRuneTests.disableStonework1Trap();
      yellowRuneTests.disableStonework2Trap();
      yellowRuneTests.disableStonework3Trap();
      yellowRuneTests.disableStonework4Trap();
      yellowRuneTests.disableStonework5Trap();
      yellowRuneTests.testStonework6Trap1();
      yellowRuneTests.testStonework6Trap2();
      yellowRuneTests.openWestStoneworkPassage1();
      yellowRuneTests.openWestStoneworkPassage2();
      yellowRuneTests.yellowRuneTest();
   }
   
   @Test(groups = { "functional", "Blue Rune" })
   public void greenRuneTest() {
      _log.info("Running through the green rune obstacles...");
      GreenRuneTests greenRuneTests = new GreenRuneTests();
      
      greenRuneTests.testBarrier();
      greenRuneTests.teleport1();
      greenRuneTests.testTrap1();
      greenRuneTests.teleport2();
      greenRuneTests.testTrap2();
      greenRuneTests.pearlKeyTest();
      greenRuneTests.teleport3();
      greenRuneTests.movement1();
      greenRuneTests.teleport4();
      greenRuneTests.teleport5();
      greenRuneTests.teleport6();
      greenRuneTests.teleport7();
      greenRuneTests.teleport8();
      greenRuneTests.teleport9();
      greenRuneTests.teleport10();
      greenRuneTests.teleport11();
      greenRuneTests.teleport12();
      greenRuneTests.teleport13();
      greenRuneTests.teleport14();
      greenRuneTests.teleport15();
      greenRuneTests.teleport16();
      greenRuneTests.teleport17();
      greenRuneTests.testTrap3();
      greenRuneTests.testTrap4();
      greenRuneTests.testTrap5();
      greenRuneTests.testTrap6();
      greenRuneTests.testTrap7();
      greenRuneTests.testTrap8();
      greenRuneTests.testTrap9();
      greenRuneTests.testTrap10();
      greenRuneTests.testTrap11();
      
      greenRuneTests.testGreenRunes();
   }

   @Test(groups = { "functional", "Blue Rune" })
   public void blueRuneTest() {
      _log.info("Running through the blue rune obstacles...");
      BlueRuneTests blueRuneTests = new BlueRuneTests();
      
      blueRuneTests.testLever1();
      blueRuneTests.testLever2();
      blueRuneTests.testLever3();
      blueRuneTests.testDarkness1();
      blueRuneTests.testDarkness2();
      blueRuneTests.testSpring1();
      blueRuneTests.testSpring2();
      blueRuneTests.testSpring3();
      blueRuneTests.testSpring4();
      blueRuneTests.testSpring5();
      blueRuneTests.testSpring6();
      blueRuneTests.getRune();
   }
   
   @Test(groups = { "functional", "Violet Rune" })
   public void violetRuneTest() {
      _log.info("Running through the violet rune obstacles...");
      
      VioletRuneTests violetRuneTests = new VioletRuneTests();
      
      violetRuneTests.testRune();
      
   }
}
