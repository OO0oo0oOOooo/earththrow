package com.oo0oo0oooooo;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;

public class Dirt extends EarthAbility implements AddonAbility {

    private static final long COOLDOWN = 0;
    private Listener _dirtListener;

    public Dirt(Player player) {
        super(player);
        // Yes
    }

    @Override
    public void progress() {

    }

    public void onClick() {

    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public String getName() {
        return "Dirt";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public void load() {
        _dirtListener = new DirtListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(_dirtListener, ProjectKorra.plugin);
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(_dirtListener);
        super.remove();
    }

    @Override
    public String getAuthor() {
        return "Jahko";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}