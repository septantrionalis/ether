package org.tdod.ether.taimpl.commands;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoUpTest extends AbstractMovementCommandTest {

   /**
    * Sets up the unit  test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoUp());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      executeMovementTest(NORTH_PLAZA, "u", TEST_ROOM_1, ARENA, "You're in the arena.");
   }

}
