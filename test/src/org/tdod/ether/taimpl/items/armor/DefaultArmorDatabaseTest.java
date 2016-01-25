package org.tdod.ether.taimpl.items.armor;

import junit.framework.Assert;

import org.tdod.ether.AbstractTest;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.armor.ArmorDatabase;
import org.tdod.ether.taimpl.items.armor.DefaultArmorDatabase;
import org.tdod.ether.util.InvalidFileException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DefaultArmorDatabaseTest extends AbstractTest {

   private ArmorDatabase _armorDatabase;
   private static final int DEFAULT_ARMOR = 12;
   
   @BeforeClass
   public void setUp() {
      super.baseSetUp();      
      _armorDatabase = new DefaultArmorDatabase();
      try {
         _armorDatabase.initialize();         
      } catch (InvalidFileException e) {
         Assert.fail("Error reading file.  Message was " + e.getMessage());
      }
   }

   @Test(groups = { "unit" })
   public void testCase() {
      Armor armor = _armorDatabase.getArmor(DEFAULT_ARMOR);
      Armor defaultArmor = _armorDatabase.getDefaultArmor();
      if (!armor.equals(defaultArmor)) {
         Assert.fail("Default armor is not correct.");
      }
      
      Armor armorString = _armorDatabase.getArmor(armor.getName());
      if (!armor.equals(armorString)) {
         Assert.fail("Getting armor by string did not work.");
      }
   }
   
}
