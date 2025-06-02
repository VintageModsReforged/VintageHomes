package mods.vintage.homes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import mods.vintage.homes.command.*;
import mods.vintage.homes.utils.PlayerTracker;
import net.minecraftforge.common.Configuration;

import java.util.Arrays;
import java.util.List;

@Mod(modid = References.ID, name = References.NAME, dependencies = References.DEPS, useMetadata = true)
public class VintageHomes implements ILangProvider {

    public static Configuration config;
    public static String[] langs;
    public static int commandPermissionLevel;

    public VintageHomes() {
        GameRegistry.registerPlayerTracker(new PlayerTracker());
        config = ConfigHelper.getConfigFor(References.ID);
        config.load();
        langs = ConfigHelper.getLocalizations(config, new String[]{"en_US", "ru_RU"}, References.ID);
        commandPermissionLevel = ConfigHelper.getInt(config, "general", "commandPermissionLevel", 0, 4, 0, "Minimum required permission level to use commands (0 = all players, 4 = server owner)");
        if (config.hasChanged()) config.save();
        LangManager.THIS.registerLangProvider(this);
    }

    @Mod.ServerStarting
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandHome());
        e.registerServerCommand(new CommandSetHome());
        e.registerServerCommand(new CommandDimensionTP());
//        e.registerServerCommand(new CommandSetWarp());
        e.registerServerCommand(new CommandWarp());
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
