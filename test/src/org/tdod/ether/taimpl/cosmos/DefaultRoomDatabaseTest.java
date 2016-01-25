package org.tdod.ether.taimpl.cosmos;

import java.util.Set;

import junit.framework.Assert;

import org.tdod.ether.AbstractTest;
import org.tdod.ether.ta.cosmos.Area;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.RoomDatabase;
import org.tdod.ether.taimpl.cosmos.DefaultRoomDatabase;
import org.tdod.ether.util.InvalidFileException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DefaultRoomDatabaseTest extends AbstractTest {

   private RoomDatabase _roomDatabase;
   
   @BeforeClass
   public void setUp() {
      super.baseSetUp();      
      _roomDatabase = new DefaultRoomDatabase();
      try {
         _roomDatabase.initialize();         
      } catch (InvalidFileException e) {
         Assert.fail("Error reading file.  Message was " + e.getMessage());
      }
   }

   @Test(groups = { "unit" })
   public void testCase() {
      Area area = _roomDatabase.getArea();
      area.initialize();
      
      Set<Integer> keys = area.getRoomMap().keySet();

      for (Integer key:keys) {
         Room room1 = area.getEntry(key);
         Room room2 = area.getRoomMap().get(key);
         if (!room1.equals(room2)) {
            Assert.fail("Room1 does not equal room2.");
         }
      }
   }

}
