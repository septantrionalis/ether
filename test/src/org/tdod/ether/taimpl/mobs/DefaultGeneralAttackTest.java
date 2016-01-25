package org.tdod.ether.taimpl.mobs;

import org.tdod.ether.AbstractTest;
import org.tdod.ether.ta.mobs.GeneralAttack;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.taimpl.mobs.enums.AttackEffectEnum;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DefaultGeneralAttackTest extends AbstractTest {

   GeneralAttack _generalAttack;

   @BeforeClass
   public void setUp() {
      super.baseSetUp();
      _generalAttack = new DefaultGeneralAttack();
   }

   @Test(groups = { "unit" })
   public void testCase() {
      for (AttackEffectEnum attackEffectEnum:AttackEffectEnum.values()) {
         _generalAttack.setAttackEffect(attackEffectEnum);         
      }
      
      for (int index = TestUtil.MIN_INT_TEST; index <= TestUtil.MAX_INT_TEST; index++) {
         _generalAttack.setMaxAttackEffect(index);
         _generalAttack.setMaxDamage(index);
         _generalAttack.setMinAttackEffect(index);
         _generalAttack.setMinDamage(index);
         _generalAttack.setNumAttacks(index);
      }
      
      for (MobWeaponType mobWeaponType:MobWeaponType.values()) {
         _generalAttack.setMobWeaponType(mobWeaponType);         
      }
      
      _generalAttack.setWeapon(null);
      _generalAttack.setWeapon("");
      _generalAttack.setWeapon("weapon");
      

   }
   
}
