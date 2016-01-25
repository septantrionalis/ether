package org.tdod.ether.tools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.Genesis;
import org.tdod.ether.ta.cosmos.RoomDatabase;
import org.tdod.ether.taimpl.cosmos.DefaultRoomDatabase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class ValidateAreaFiles {

   private static Log _log = LogFactory.getLog(Genesis.class);

   @BeforeClass
   public void setUp() {
      System.setProperty("TaConfigFile", "config/ta.properties");
   }

   @Test(groups = { "integration" })
   public void areaFileTestCase() {
      _log.info("Testing area files...") ;
      try {
         initialize();         
      } catch (Exception e) {
         Assert.fail("Error reading file.", e) ;
         e.printStackTrace();
         return ;
      }
      _log.info("Looks good!") ;   
   }
   
   private void initialize() throws Exception {
      RoomDatabase roomDatabase = new DefaultRoomDatabase() ;
      roomDatabase.initialize();
   }   
   
}
