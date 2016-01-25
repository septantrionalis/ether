package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.tdod.ether.util.TaMessageManager;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the add command.
 * @author Ron Kinney
 */
public class DoAddTest extends AbstractCommandTest {

   /**
    * Sets up the unit test.
    */
   @BeforeClass
   public void setUp() {
      super.commandSetUp();
      setCommand(new DoAdd());
   }

   /**
    * Runs the test case.
    */
   @Test(groups = { "unit" })
   public void testCase() {
      getPlayer().teleportToRoom(TEST_ROOM_1);
      getPlayerA().teleportToRoom(TEST_ROOM_2);
      getPlayerB().teleportToRoom(TEST_ROOM_1);
      clearAllOutput();
      getCommand().execute(getPlayer(), "add " + getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(),
            MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), getPlayerA().getName().toLowerCase()));
      TestUtil.assertOutput(getPlayerAOutput(), "");
      TestUtil.assertOutput(getPlayerBOutput(), "");

      getPlayer().teleportToRoom(TEST_ROOM_1);
      getPlayerA().teleportToRoom(TEST_ROOM_1);
      getPlayerB().teleportToRoom(TEST_ROOM_1);
      clearAllOutput();

      getCommand().execute(getPlayer(), "add " + getPlayerA().getName());
      String expectedOutput = MessageFormat.format(TaMessageManager.NASGRP.getMessage(), getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.NASGRP2.getMessage(), getPlayer().getName(), "his");
      TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.OTHAGR2.getMessage(), getPlayer().getName(), getPlayerA().getName(), "his");
      TestUtil.assertOutput(getPlayerBOutput(), expectedOutput);

      new DoJoin().execute(getPlayerA(), "join " + getPlayer().getName());
      expectedOutput = MessageFormat.format(TaMessageManager.ASKGRP2.getMessage(), getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.ASKGRP.getMessage(), getPlayer().getName());
      TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.OTHAGR1.getMessage(), getPlayerA().getName(), getPlayer().getName());
      TestUtil.assertOutput(getPlayerBOutput(), expectedOutput);

      getCommand().execute(getPlayer(), "add " + getPlayerA().getName());
      expectedOutput = MessageFormat.format(TaMessageManager.JOIGRP.getMessage(), getPlayerA().getName());
      TestUtil.assertOutput(getPlayerOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.ACCGRP.getMessage(), getPlayer().getName());      
      TestUtil.assertOutput(getPlayerAOutput(), expectedOutput);
      expectedOutput = MessageFormat.format(TaMessageManager.OTHACG.getMessage(), getPlayerA().getName(), getPlayer().getName());      
      TestUtil.assertOutput(getPlayerBOutput(), expectedOutput);

      if (getPlayer().getGroupList().size() != 1) {
         Assert.fail("Group size is " + getPlayer().getGroupList().size());
      }
   }

}
