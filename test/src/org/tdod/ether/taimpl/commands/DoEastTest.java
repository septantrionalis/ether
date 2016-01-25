package org.tdod.ether.taimpl.commands;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DoEastTest extends AbstractMovementCommandTest {

   /**
    * Sets up the unit  test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoEast());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      executeMovementTest(ARENA, "e", NORTH_PLAZA, ARENA, "You're in the arena.");
   }

}
