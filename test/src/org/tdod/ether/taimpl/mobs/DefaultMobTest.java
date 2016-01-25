package org.tdod.ether.taimpl.mobs;

import java.util.ArrayList;

import org.tdod.ether.AbstractTest;
import org.tdod.ether.output.MockOutput;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.cosmos.DefaultRoom;
import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.taimpl.mobs.enums.SubType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.taimpl.player.DefaultPlayer;
import org.tdod.ether.taimpl.player.DefaultVitality;
import org.tdod.ether.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DefaultMobTest extends AbstractTest {

   private Mob _mob;
   
   @BeforeClass
   public void setUp() {
      super.baseSetUp();      

      _mob = new DefaultMob();
   }

   @Test(groups = { "unit" })
   public void testCase() {
      Player player = TestUtil.createDefaultPlayer(new MockOutput());
      
      for (Gender gender:Gender.values()) {
         _mob.setGender(gender);
      }

      for (Status status:Status.values()) {
         _mob.setStatus(status);
      }
      
      for (SubType subType:SubType.values()) {
         _mob.setSubType(subType);
      }
      
      for (Terrain terrain:Terrain.values()) {
         _mob.setTerrain(terrain);
      }
      
      _mob.setGroupLeader(player);
      _mob.setGroupLeader(null);
      
      ArrayList<Entity> groupList = new ArrayList<Entity>();
      groupList.add(new DefaultMob());
      groupList.add(new DefaultPlayer());
      _mob.setGroupList(groupList);
      
      
      for (int index = TestUtil.MIN_INT_TEST; index <= TestUtil.MAX_INT_TEST; index++) {
         _mob.setActivityTicker(index);
         _mob.setArmorRating(index);
         _mob.setCombatSkill(index);
         _mob.setCombatTicker(index);
         _mob.setGold(index);
         _mob.setHitDice(index);
         _mob.setInvisibiltyTimer(index);
         _mob.setLastMove(index);
         _mob.setLevel(index);
         _mob.setMagicProtection(index);
         _mob.setMagicProtectionTimer(index);
         _mob.setMentalExhaustionTicker(index);
         _mob.setMorale(index);
         _mob.setParalysisTicker(index);
         _mob.setPoisonDamage(index);
         _mob.setRegeneration(index);
         _mob.setRestTicker(index);
         _mob.setRoom(index);
         _mob.setSpecialAttackPercent(index);
         _mob.setTreasure(index);
         _mob.setVnum(index);
      }

      _mob.setChasing(player);
      _mob.setChasing(null);
      
      _mob.setDescription(null);
      _mob.setDescription("");
      _mob.setDescription("description");
      
      _mob.setName(null);
      _mob.setName("");
      _mob.setName("name");

      _mob.setPlural(null);
      _mob.setPlural("");
      _mob.setPlural("plural");
      
      _mob.setPrefix(null);
      _mob.setPrefix("");
      _mob.setPrefix("prefix");
      
      _mob.setDraggedBy(player);
      _mob.setDraggedBy(null);
      
      _mob.setFollowingGroup(true);
      _mob.setFollowingGroup(false);
      
      _mob.setCanTrack(true);
      _mob.setCanTrack(false);
      _mob.setIgnoreTrip(true);
      _mob.setIgnoreTrip(false);
      _mob.setIsFleeing(true);
      _mob.setIsFleeing(false);
      _mob.setIsFriendly(true);
      _mob.setIsFriendly(false);
      _mob.setIsTripping(true);
      _mob.setIsTripping(false);
      
      _mob.setMobSpells(new DefaultMobSpells());
      _mob.setMobWeapon(new DefaultMobWeapon());
      _mob.setRoom(new DefaultRoom());
      _mob.setSpecialAbility(new DefaultSpecialAbility());
      _mob.setVitality(new DefaultVitality());
      
      _mob.setSpecialAttack(new DefaultSpecialAttack());
      
      _mob.setGeneralAttack(new DefaultGeneralAttack());
      _mob.setGeneralAttack(null);
      _mob.setMessageHandler(new MobMessageHandler(_mob));

}

}
