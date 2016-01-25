package org.tdod.ether.taimpl.commands;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoNorthTest extends AbstractMovementCommandTest {

   /**
    * Sets up the unit  test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoNorth());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      executeMovementTest(ARENA, "n", NORTH_PLAZA, -92, "You're in the guild hall.");
   }

}
