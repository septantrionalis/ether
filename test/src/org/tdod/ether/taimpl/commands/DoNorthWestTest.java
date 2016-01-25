package org.tdod.ether.taimpl.commands;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoNorthWestTest extends AbstractMovementCommandTest {

   /**
    * Sets up the unit  test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoNorthWest());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      executeMovementTest(-97, "nw", NORTH_PLAZA, -97, "You're in the equipment shop.");
   }

}
