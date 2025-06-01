package mods.vintage.homes.utils;

import cpw.mods.fml.common.IPlayerTracker;
import mods.vintage.core.platform.lang.FormattedTranslator;
import mods.vintage.homes.References;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerTracker implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        World world = player.worldObj;
        if (world != null) {
            player.addChatMessage(FormattedTranslator.WHITE.format("message.player.login", FormattedTranslator.GREEN.literal(References.NAME)));
            player.addChatMessage(FormattedTranslator.GOLD.format("message.player.usage", FormattedTranslator.AQUA.literal("/sethome"), FormattedTranslator.AQUA.literal("/home")));
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer entityPlayer) {}

    @Override
    public void onPlayerChangedDimension(EntityPlayer entityPlayer) {}

    @Override
    public void onPlayerRespawn(EntityPlayer entityPlayer) {}
}
