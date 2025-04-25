package com.oo0oo0oooooo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class EarthThrowListener implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (player == null) return;
        if (bPlayer == null) return;
        if (event.isCancelled()) return;
        if (player.isSneaking()) return;

        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("EarthThrow")) {
            EarthThrow earthThrow = CoreAbility.getAbility(player, EarthThrow.class);
            if (earthThrow != null) {
                earthThrow.remove();
                earthThrow = null;
            }

            new EarthThrow(player);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR
         && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (player == null) return;
        if (bPlayer == null) return;

        EarthThrow earthThrow = CoreAbility.getAbility(player, EarthThrow.class);
        if (earthThrow != null) {
            earthThrow.onClick();
        }
    }
}
