package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.Translator;
import mods.vintage.homes.utils.CommonTeleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandHome extends VintageCommand {

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return Translator.GOLD.format("message.command.home.usage", Translator.AQUA.literal("/home"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof EntityPlayerMP)) {
                sender.sendChatToPlayer(Translator.RED.format("message.command.home.allowed"));
                return;
            }

            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer world = (WorldServer) playerMP.worldObj;
            int dimID = world.provider.dimensionId;
            EntityPlayer playerSP = playerMP;

            VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(server);
            String playerName = playerSP.username;

            NBTTagCompound playerHome = storage.homesTag.getCompoundTag(playerName);

            if (playerHome == null) {
                sender.sendChatToPlayer(Translator.RED.format("message.command.home.set.home"));
            } else {
                int destDimId = playerHome.getInteger("dim");
                double xPos = playerHome.getDouble("posX");
                double yPos = playerHome.getDouble("posY");
                double zPos = playerHome.getDouble("posZ");

                WorldServer destWorld = server.worldServerForDimension(destDimId);
                if (!(destWorld == server.worldServerForDimension(dimID))) {
                    server.getConfigurationManager().transferPlayerToDimension(playerMP, destDimId, new CommonTeleporter(destWorld, CommonTeleporter.Type.HOME, true));
                }
                playerMP.setPositionAndUpdate(xPos, yPos, zPos);
                playerSP.setPositionAndUpdate(xPos, yPos, zPos);

                sender.sendChatToPlayer(Translator.GREEN.format("message.command.home.success"));
            }
        } else {
            sender.sendChatToPlayer(Translator.GOLD.format("message.command.home.usage", Translator.AQUA.literal("/home")));
        }
    }
}
