package org.tdod.ether.taimpl.engine.jobs;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;

public class SustenanceJob implements Job {

   private static Log _log = LogFactory.getLog(SustenanceJob.class);

   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(
               PropertiesManager.SUSTENANCE_TIMER_WAKEUP)).intValue();

   public void execute(JobExecutionContext context) {
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         try {
            Player player = playerConn.getPlayer();

            if (!player.getState().equals(PlayerStateEnum.PLAYING)) {
               continue;
            }

            int sustenanceReduction = TIMER_WAKEUP + Dice.roll(0, player.getStats().getStamina().getSustenanceModifier());
            player.subtractSustenance(sustenanceReduction);

         } catch (Exception e) {
            _log.error(e);
         }
      }      
   }

   /**
    * Gets the job trigger.
    * @return the job trigger.
    */
   public static Trigger getTrigger() {
      SimpleTrigger trigger = new SimpleTrigger("Sustenance",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }

}
