package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandSetHome extends VintageCommand {

    @Override
    public String getCommandName() {
        return "sethome";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return Translator.GOLD.format("message.command.sethome.usage", Translator.AQUA.literal("/sethome"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer | sender instanceof EntityPlayerMP) {
            if (args.length == 0) {
                int x = (int) ((EntityPlayer) sender).posX;
                int y = (int) ((EntityPlayer) sender).posY;
                int z = (int) ((EntityPlayer) sender).posZ;
                EntityPlayer senderP = (EntityPlayer) sender;
                MinecraftServer server = MinecraftServer.getServer();

                VintageHomesWorldStorage sd = VintageHomesWorldStorage.get(server);
                NBTTagCompound playerHome = sd.homesTag.getCompoundTag(senderP.username);
                playerHome.setInteger("dim", senderP.worldObj.provider.dimensionId);
                playerHome.setDouble("posX", x);
                playerHome.setDouble("posY", y);
                playerHome.setDouble("posZ", z);
                sd.homesTag.setTag(senderP.username, playerHome);
                sd.markDirty();
                ((EntityPlayer) sender).addChatMessage(Translator.GREEN.format("message.command.sethome.success"));
            } else {
                sender.sendChatToPlayer(Translator.GOLD.format("message.command.sethome.warning", Translator.AQUA.literal("/sethome")));
            }
        }
    }
}
