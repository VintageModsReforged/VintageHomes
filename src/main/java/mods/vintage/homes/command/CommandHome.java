package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.FormattedTranslator;
import mods.vintage.homes.utils.CommonTeleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandHome extends VintageCommand {

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return FormattedTranslator.GOLD.format("message.command.home.usage", FormattedTranslator.AQUA.literal("/home"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof EntityPlayerMP)) {
                sender.sendChatToPlayer(FormattedTranslator.RED.format("message.command.home.allowed"));
                return;
            }

            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer world = (WorldServer) playerMP.worldObj;
            int dimID = world.provider.dimensionId;
            EntityPlayer playerSP = playerMP;

            VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(server);
            String playerName = playerSP.username;

            int[] homeData = storage.homesTag.getIntArray(playerName);
            if (homeData.length <= 0) {
                sender.sendChatToPlayer(FormattedTranslator.RED.format("message.command.home.set.home"));
            } else {
                int destDimId = homeData[0];
                int xPos = homeData[1];
                int yPos = homeData[2];
                int zPos = homeData[3];

                WorldServer destWorld = server.worldServerForDimension(destDimId);
                if (!(destWorld == server.worldServerForDimension(dimID))) {
                    server.getConfigurationManager().transferPlayerToDimension(playerMP, destDimId, new CommonTeleporter(destWorld, CommonTeleporter.Type.HOME, true));
                }
                playerMP.setPositionAndUpdate(xPos + 0.5, yPos, zPos + 0.5);
                playerSP.setPositionAndUpdate(xPos + 0.5, yPos, zPos + 0.5);

                sender.sendChatToPlayer(FormattedTranslator.GREEN.format("message.command.home.success"));
            }
        } else {
            sender.sendChatToPlayer(FormattedTranslator.GOLD.format("message.command.home.usage", FormattedTranslator.AQUA.literal("/home")));
        }
    }
}
