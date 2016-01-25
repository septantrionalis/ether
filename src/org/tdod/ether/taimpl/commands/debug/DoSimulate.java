package org.tdod.ether.taimpl.commands.debug;

import java.util.Formatter;
import java.util.Locale;

import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.spells.Spell;

/**
 * This class simulates combat against a mob using current in game parameters.
 * @author rkinney
 */
public class DoSimulate extends SysopCommand {

   private static final int PARAM_NUM = 4;

   private static final String MELEE_KEYWORD = "melee";
   
   // private static Log _log = LogFactory.getLog(DoSimulate.class);

   /**
    * Executes the sysop command.
    * @param player the player issuing the command.
    * @param input the player input.
    * @return true if the command was successful.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ", PARAM_NUM);

      if (split.length != PARAM_NUM) {
         displayHelp(player);
         return true;
      }

      String simulateType = split[1].toLowerCase();
      
      int rounds = Integer.valueOf(split[2]);
      if (rounds < 1) {
         player.println("Number of rounds needs to be a positive number.");
         return true;
      }
      
      int mobVnum = Integer.valueOf(split[3]);
      
      if (MELEE_KEYWORD.equals(simulateType)) {
         return handleMeleeSimulation(player, rounds, mobVnum);
      } else {
         return handleSpellSimulation(player, simulateType, rounds, mobVnum);
      }
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: simulate <melee|spell> <rounds> <mob vnum>");
   }

   /**
    * Handles a simple melee attack.
    * @param player the player issuing the attack.
    * @param rounds the number of rounds to simulate.
    * @param mobVnum the mob vnum to attack.
    * @return true if the command was successful.
    */
   private boolean handleMeleeSimulation(Player player, int rounds, int mobVnum) {
      Mob mob = WorldManager.getMob(mobVnum);
      if (mob == null) {
         player.println("Mob does not exist.");
         return true;
      }

      player.println("Simulating combat against a level " + mob.getLevel() + " " + mob.getName());
      Vitality vitality = WorldManager.getGameMechanics().calculateMobHealth(mob.getCombatSkill(), mob.getLevel(), mob.getVariance());
      for (int count = 0; count < rounds; count++) {
         MeleeResult result = WorldManager.getGameMechanics().doPlayerMeleeAttack(player, mob);
         int expGain = (int)(result.getDamage() * mob.getExpPerPointOfDamage(player.getLevel()));
         vitality.setCurVitality(vitality.getCurVitality() - result.getDamage());
         
         StringBuilder sb = new StringBuilder();
         Formatter formatter = new Formatter(sb, Locale.US);
         formatter.format("Result:%-7s  Damage:%-3d(%-4d/%-4d)  Exp:%-5d", result.getMeleeResultEnum(),
               result.getDamage(), vitality.getCurVitality(), vitality.getMaxVitality(), expGain);
         player.println(sb.toString());
      }
      
      return true;
   }

   /**
    * Handles a simple spell result.
    * @param player the player casting the spell.
    * @param rounds the number of rounds to simulate.
    * @param mobVnum the mob vnum to cast against.
    * @return true if the command was successful.
    */
   private boolean handleSpellSimulation(Player player, String str, int rounds, int mobVnum) {
      Spell spell = WorldManager.getSpell(str);
      if (spell == null) {
         player.println("Spell does not exist.");
         return true;
      }
      
      Mob mob = WorldManager.getMob(mobVnum);
      if (mob == null) {
         player.println("Mob does not exist.");
         return true;
      }
      
      player.println("Simulating spell against a level " + mob.getLevel() + " " + mob.getName());
      for (int count = 0; count < rounds; count++) {
         if (!WorldManager.getGameMechanics().isSpellSuccess(player, spell)) {
            player.println("Spell Failed");
            continue;
         }
         
         SpellResult result = WorldManager.getGameMechanics().calculateOffensiveSpellResult(player, mob, spell);
         StringBuilder sb = new StringBuilder();
         Formatter formatter = new Formatter(sb, Locale.US);
         formatter.format("Result:%-7s", result.getSpellResultEnum());
         player.println(sb.toString());
      }
      return true;
   }

}
