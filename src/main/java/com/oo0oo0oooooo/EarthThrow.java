package com.oo0oo0oooooo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.earthbending.passive.DensityShift;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.TempBlock;

public class EarthThrow extends EarthAbility implements AddonAbility {

    private long COOLDOWN;
    private double HIT_RADIUS;
    private double SOURCE_RANGE;
    private double LAUNCH_STRENGTH;

    private Location _location = null;
    private Block _sourceBlock = null;
    private Material _sourceType = null;
    private boolean _launchCodesConfirmed = false;

    private Listener _earthThrowListener;

    public EarthThrow(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        EarthThrow existing = getAbility(player, getClass());
        if (existing != null) {
            existing.remove();
            return;
        }

        setField();

        if (prepare()) {
            focusBlock();
            start();
        }
    }

    @Override
    public void progress() {
        if (!bPlayer.canBend(this)) {
            remove();
            return;
        }

        if (_location.distance(player.getLocation()) > SOURCE_RANGE) {
            remove();
            return;
        }

        if (_launchCodesConfirmed) {
            launch();
            remove();
            return;
        }
    }

    public void setField() {
        COOLDOWN = ConfigManager.getConfig().getLong("ExtraAbilities.Jahko.Earth.EarthThrow.Cooldown");
        SOURCE_RANGE = ConfigManager.getConfig().getDouble("ExtraAbilities.Jahko.Earth.EarthThrow.SourceRange");
        LAUNCH_STRENGTH = ConfigManager.getConfig().getDouble("ExtraAbilities.Jahko.Earth.EarthThrow.LaunchStrength");
        HIT_RADIUS = ConfigManager.getConfig().getDouble("ExtraAbilities.Jahko.Earth.EarthThrow.HitRadius");
    }

    public void onClick() {
        _launchCodesConfirmed = true;
    }

    private boolean prepare() {
        final Block block = BlockSource.getEarthSourceBlock(player, SOURCE_RANGE, ClickType.SHIFT_DOWN);
        if (block == null || !this.isEarthbendable(block)) {
            return false;
        } else if (TempBlock.isTempBlock(block) && !EarthAbility.isBendableEarthTempBlock(block)) {
            return false;
        }

        _sourceBlock = block;
        _sourceType = _sourceBlock.getType();
        _location = _sourceBlock.getLocation();

        return true;
    }

    private void focusBlock() {
        if (DensityShift.isPassiveSand(_sourceBlock)) {
            DensityShift.revertSand(_sourceBlock);
        }

        switch (_sourceBlock.getType()) {
            case SAND:
                _sourceType = Material.SAND;
                _sourceBlock.setType(Material.SANDSTONE);
                break;
            case RED_SAND:
                _sourceType = Material.RED_SAND;
                _sourceBlock.setType(Material.RED_SANDSTONE);
                break;
            case STONE:
                _sourceBlock.setType(Material.COBBLESTONE);
                _sourceType = Material.STONE;
                break;
            default:
                _sourceType = _sourceBlock.getType();
                _sourceBlock.setType(Material.STONE);
                break;
        }
    }

    private void unfocusBlock() {
        if (_sourceBlock != null) {
            _sourceBlock.setType(_sourceType);
            _sourceBlock = null;
            _sourceType = null;
            _location = null;
        }
    }

    private void launch() {
        Vector dir = this.player.getEyeLocation().getDirection().clone().normalize().multiply(LAUNCH_STRENGTH);
        for (final Entity entity : GeneralMethods.getEntitiesAroundPoint(this._location, HIT_RADIUS)) {
            GeneralMethods.setVelocity(this, entity, dir);
        }

        playEarthbendingSound(_location);
        bPlayer.addCooldown(this);
    }

    @Override
    public void remove() {
        super.remove();
        unfocusBlock();
    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public String getName() {
        return "EarthThrow";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public void load() {
        _earthThrowListener = new EarthThrowListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(_earthThrowListener, ProjectKorra.plugin);

        ConfigManager.getConfig().addDefault("ExtraAbilities.Jahko.Earth.EarthThrow.Cooldown", 1000);
        ConfigManager.getConfig().addDefault("ExtraAbilities.Jahko.Earth.EarthThrow.SourceRange", 10);
        ConfigManager.getConfig().addDefault("ExtraAbilities.Jahko.Earth.EarthThrow.LaunchStrength", 2);
        ConfigManager.getConfig().addDefault("ExtraAbilities.Jahko.Earth.EarthThrow.HitRadius", 2);
        ConfigManager.defaultConfig.save();
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(_earthThrowListener);
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