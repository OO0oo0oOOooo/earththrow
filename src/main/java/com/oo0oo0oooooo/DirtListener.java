package com.oo0oo0oooooo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class DirtListener implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if(bPlayer == null) return;
        if(event.isCancelled()) return;
        if(player.isSneaking()) return;

        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Dirt")) {
            if(CoreAbility.getAbility(player, Dirt.class) == null) {
                new Dirt(player);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.LEFT_CLICK_AIR 
        && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        
        if(bPlayer == null) return;

        Dirt dirt = CoreAbility.getAbility(player, Dirt.class);
        if (dirt != null) {
            dirt.onClick();
        }
    }    
         
}        
         