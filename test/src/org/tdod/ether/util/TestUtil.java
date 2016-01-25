package org.tdod.ether.util;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.World;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.Agility;
import org.tdod.ether.ta.player.Charisma;
import org.tdod.ether.ta.player.Intellect;
import org.tdod.ether.ta.player.Knowledge;
import org.tdod.ether.ta.player.Physique;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Stamina;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.RaceEnum;
import org.tdod.ether.taimpl.commands.debug.DoReset;
import org.tdod.ether.taimpl.player.DefaultAgility;
import org.tdod.ether.taimpl.player.DefaultCharisma;
import org.tdod.ether.taimpl.player.DefaultIntellect;
import org.tdod.ether.taimpl.player.DefaultKnowledge;
import org.tdod.ether.taimpl.player.DefaultPhysique;
import org.tdod.ether.taimpl.player.DefaultPlayer;
import org.tdod.ether.taimpl.player.DefaultStamina;

public class TestUtil {

   private static Boolean _isWorldInitialized = Boolean.FALSE;
   
   private static Log _log = LogFactory.getLog(TestUtil.class);

   public static final int NORTH_PLAZA_ROOM = -99;

   public static final int MIN_INT_TEST = -1000;
   public static final int MAX_INT_TEST = 1000;
   
   public static Player createDefaultPlayer(GameOutput output) {
      Player player = new DefaultPlayer();
      
      player.setIgnoreTrip(true);
      player.setName("Minex");
      player.setLevel(20);
      player.setOutput(output);
      player.setPlayerClass(PlayerClass.WARRIOR);
      player.setRace(RaceEnum.HUMAN);
      player.setWeapon(WorldManager.getDefaultWeapon());
      player.setArmor(WorldManager.getDefaultArmor());

      Agility agility = new DefaultAgility();
      agility.setValue(18);
      player.getStats().setAgility(agility);

      Charisma charisma = new DefaultCharisma();
      charisma.setValue(18);
      player.getStats().setCharisma(charisma);

      Intellect intellect = new DefaultIntellect();
      intellect.setValue(18);
      player.getStats().setIntellect(intellect);

      Knowledge knowledge = new DefaultKnowledge();
      knowledge.setValue(18);
      player.getStats().setKnowledge(knowledge);

      Physique physique = new DefaultPhysique();
      physique.setValue(18);
      player.getStats().setPhysique(physique);

      Stamina stamina = new DefaultStamina();
      stamina.setValue(18);
      player.getStats().setStamina(stamina);

      player.getVitality().setCurVitality(1000);
      player.getVitality().setMaxVitality(1000);

      player.placeItemInInventory(WorldManager.getItem(25), false);

      player.setRoom(NORTH_PLAZA_ROOM);

      Room room = WorldManager.getRoom(NORTH_PLAZA_ROOM);
      room.addPlayer(player);

      return player;
   }
   
   public static void assertDoesNotContains(MockOutput output, String string) {
      if (output.getBuffer().contains(string)) {
         Assert.fail("Output contained the string : " + string);         
      }
      output.clearBuffer();

   }

   
   public static void assertContains(MockOutput output, String expectedString) {
      if (!output.getBuffer().contains(expectedString)) {
         Assert.fail("Output did not contain : " + expectedString);
      }
      output.clearBuffer();
   }
   
   public static void assertOutput(MockOutput output, String expectedString) {
      if (!output.getBuffer().equals(expectedString)) {
         _log.error("Expected: " + expectedString);
         _log.error("Got: " + output.getBuffer());            
         Assert.fail("Expected output did not match.");
      }
      output.clearBuffer();
   }
   
   public synchronized static void initializeWorld() {
      if (!_isWorldInitialized) {
         try {
            System.setProperty("TaConfigFile", "config/ta.properties");
            World world = DefaultAppFactory.createWorld();
            WorldManager.setWorld(world);
            WorldManager.setGameMechanics(DefaultAppFactory.createGameMechanics());
            world.bigBang();
            _isWorldInitialized = Boolean.TRUE;
         } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
         }                     
      }
   }
   
   public static void reset(Player player) {
      player.setIsSysop(true);
      new DoReset().execute(player, "");
      player.setIsSysop(false);
   }
}
