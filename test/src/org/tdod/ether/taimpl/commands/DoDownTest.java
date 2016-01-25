package org.tdod.ether.taimpl.commands;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoDownTest extends AbstractMovementCommandTest {

   /**
    * Sets up the unit  test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoDown());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      executeMovementTest(NORTH_PLAZA, "d", ARENA, TEST_ROOM_1, "You are inside the dungeon entrance.");
   }

}
