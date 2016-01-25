package org.tdod.ether.taimpl.mobs;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.AbstractTest;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.MobDatabase;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.InvalidFileException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DefaultMobDatabaseTest extends AbstractTest {

   private static Log _log = LogFactory.getLog(DefaultMobDatabaseTest.class);

   private MobDatabase _defaultMobDatabase;
   
   @BeforeClass
   public void setUp() {
      super.baseSetUp();      
      _defaultMobDatabase = new DefaultMobDatabase();
      
      try {
         _defaultMobDatabase.initialize();         
      } catch (InvalidFileException e) {
         Assert.fail("Error reading file.  Message was " + e.getMessage());
      }
   }

   @Test(groups = { "unit" })
   public void testGetMobByTerrain() {
      ArrayList<Mob> mobs = _defaultMobDatabase.getMobList();
      if (mobs.size() == 0) {
         Assert.fail("DefaultMobDatabase.getMobList() was empty.");
         return;
      }
      
      for(Terrain terrain:Terrain.values()) {
         ArrayList<Mob> terrainMobs = _defaultMobDatabase.getMobByTerrain(terrain);
         if (terrainMobs != null) {
            for (Mob terrainMob:terrainMobs) {
               boolean error = true;
               for (Mob mob:mobs) {
                  if (mob.isSimilar(terrainMob)) {
                     error = false;
                     break;
                  }
                  
                  Mob clonedMob = mob.clone(null);
                  if (mobs.contains(clonedMob)) {
                     error = true;
                  }
               }
               
               if (error) {
                  Assert.fail("Error when comparing mob " + terrainMob.getName());                  
               }
            }            
         } else {
            _log.info(terrain + " contained no mobs.");
         }
      }
   }

   @Test(groups = { "unit" })
   public void testGetMobs() {
      ArrayList<Mob> mobs = _defaultMobDatabase.getMobList();
      if (mobs.size() == 0) {
         Assert.fail("DefaultMobDatabase.getMobList() was empty.");
         return;
      }

      for (Mob mob:mobs) {
         if (!mobs.contains(_defaultMobDatabase.getMob(mob.getVnum()))) {
            Assert.fail("Mobs did not contain " + mob.getName());            
         }
      }
   }

}
