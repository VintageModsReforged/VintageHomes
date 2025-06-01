package mods.vintage.homes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import mods.vintage.homes.command.CommandHome;
import mods.vintage.homes.command.CommandSetHome;
import mods.vintage.homes.command.CommandDimensionTP;
import mods.vintage.homes.utils.PlayerTracker;
import net.minecraftforge.common.Configuration;

import java.util.Arrays;
import java.util.List;

@Mod(modid = References.ID, name = References.NAME, dependencies = References.DEPS, useMetadata = true)
public class VintageHomes implements ILangProvider {

    public static Configuration config;
    public static String[] langs;

    public VintageHomes() {
        GameRegistry.registerPlayerTracker(new PlayerTracker());
        config = ConfigHelper.getConfigFor(References.ID);
        config.load();
        langs = ConfigHelper.getLocalizations(config, new String[]{"en_US", "ru_RU"}, References.ID);
        if (config.hasChanged()) config.save();
        LangManager.THIS.registerLangProvider(this);
    }

    @Mod.ServerStarting
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandHome());
        e.registerServerCommand(new CommandSetHome());
        e.registerServerCommand(new CommandDimensionTP());
    }

    @Override
    public String getModid() {
        return References.ID;
    }

    @Override
    public List<String> getLocalizationList() {
        return Arrays.asList(langs);
    }
}
