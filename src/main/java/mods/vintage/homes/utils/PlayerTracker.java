package mods.vintage.homes.utils;

import cpw.mods.fml.common.IPlayerTracker;
import mods.vintage.core.platform.lang.Translator;
import mods.vintage.homes.References;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerTracker implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        World world = player.worldObj;
        if (world != null) {
            player.addChatMessage(Translator.WHITE.format("message.player.login", Translator.GREEN.literal(References.NAME)));
            player.addChatMessage(Translator.GOLD.format("message.player.usage", Translator.AQUA.literal("/sethome"), Translator.AQUA.literal("/home")));
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer entityPlayer) {}

    @Override
    public void onPlayerChangedDimension(EntityPlayer entityPlayer) {}

    @Override
    public void onPlayerRespawn(EntityPlayer entityPlayer) {}
}
