package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.Translator;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

                int[] arrayToStore = {0, 0, 0, 0};
                arrayToStore[0] = senderP.worldObj.provider.dimensionId;
                arrayToStore[1] = x;
                arrayToStore[2] = y;
                arrayToStore[3] = z;

                sd.homesTag.setIntArray(senderP.username, arrayToStore);
                sd.markDirty();
                ((EntityPlayer) sender).addChatMessage(Translator.GREEN.format("message.command.sethome.success"));
            } else {
                sender.sendChatToPlayer(Translator.GOLD.format("message.command.sethome.warning", Translator.AQUA.literal("/sethome")));
            }
        }
    }
}
