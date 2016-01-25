package org.tdod.ether.taimpl.commands;

import org.tdod.ether.AbstractTest;
import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TestUtil;

/**
 * The abstract class for testing commands.
 * @author Ron Kinney
 */
public abstract class AbstractCommandTest extends AbstractTest {

   protected static final int TEST_ROOM_1 = 1;
   protected static final int TEST_ROOM_2 = 2;
   protected static final int NORTH_PLAZA = -99;
   protected static final int TEMPLE = -96;
   protected static final int ARENA = -98;

   private Command    _command;
   private Player     _player;
   private MockOutput _playerOutput;
   private Player     _playerA;
   private MockOutput _playerAOutput;
   private Player     _playerB;
   private MockOutput _playerBOutput;
   private Player     _playerC;
   private MockOutput _playerCOutput;

   /**
    * Sets up the command abstract class.
    */
   public void commandSetUp() {
      super.baseSetUp();

      _playerOutput = new MockOutput();
      _player = TestUtil.createDefaultPlayer(_playerOutput);
      _player.setName("M" + getRandomName());
      _playerAOutput = new MockOutput();
      _playerA = TestUtil.createDefaultPlayer(_playerAOutput);
      _playerA.setName("A" + getRandomName());
      _playerBOutput = new MockOutput();
      _playerB = TestUtil.createDefaultPlayer(_playerBOutput);
      _playerB.setName("B" + getRandomName());
      _playerCOutput = new MockOutput();
      _playerC = TestUtil.createDefaultPlayer(_playerCOutput);
      _playerC.setName("C" + getRandomName());

   }

   /**
    * Gets the command.
    * @return the command.
    */
   protected Command getCommand() {
      return _command;
   }

   /**
    * Sets the command.
    * @param command the command.
    */
   protected void setCommand(Command command) {
      _command = command;
   }

   /**
    * Gets the player.
    * @return the player.
    */
   protected Player getPlayer() {
      return _player;
   }

   /**
    * Gets the player output.
    * @return the player output.
    */
   protected MockOutput getPlayerOutput() {
      return _playerOutput;
   }

   /**
    * Gets player A.
    * @return player A.
    */
   protected Player getPlayerA() {
      return _playerA;
   }

   /**
    * Gets the output for player A.
    * @return the output for player A.
    */
   protected MockOutput getPlayerAOutput() {
      return _playerAOutput;
   }

   /**
    * Gets player B.
    * @return player B.
    */
   protected Player getPlayerB() {
      return _playerB;
   }

   /**
    * Gets the output for player B.
    * @return the output for player B.
    */
   protected MockOutput getPlayerBOutput() {
      return _playerBOutput;
   }

   /**
    * Gets player C.
    * @return player C.
    */
   protected Player getPlayerC() {
      return _playerC;
   }

   /**
    * Gets the output for player C.
    * @return the output for player C.
    */
   protected MockOutput getPlayerCOutput() {
      return _playerCOutput;
   }

   /**
    * Clears the buffer for all players.
    */
   protected void clearAllOutput() {
      _playerOutput.clearBuffer();
      _playerAOutput.clearBuffer();
      _playerBOutput.clearBuffer();
   }

   /**
    * Creates a "random" name.
    * @return a "random" name.
    */
   private String getRandomName() {
      String[] split = this.toString().split("@");
      return split[1];
   }
}
