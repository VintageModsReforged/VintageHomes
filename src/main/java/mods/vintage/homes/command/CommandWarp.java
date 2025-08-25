package mods.vintage.homes.command;

import mods.vintage.core.platform.lang.Translator;
import mods.vintage.homes.utils.CommonTeleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CommandWarp extends VintageCommand {

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/warp <add-remove-list-warpName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendChatToPlayer(Translator.RED.format("message.command.warp.allowed"));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (args.length == 0) {
            player.sendChatToPlayer(Translator.GOLD.format("message.command.warp.usage", Translator.AQUA.literal("/warp <add-remove-list-warpName>")));
            return;
        }

        String sub = args[0];
        VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(MinecraftServer.getServer());
        String subLow = sub.toLowerCase(Locale.ROOT);
        if ("add".equals(subLow)) {
            if (args.length < 2) {
                player.sendChatToPlayer(Translator.GOLD.format("message.command.warp.usage.add", Translator.AQUA.literal("/warp add <name>")));
                return;
            }
            String warpName = args[1];
            int[] data = {
                    player.worldObj.provider.dimensionId,
                    (int) player.posX,
                    (int) player.posY,
                    (int) player.posZ
            };
            storage.warpsTag.setIntArray(warpName, data);
            storage.markDirty();
            player.sendChatToPlayer(Translator.GREEN.format("message.command.warp.success.add", Translator.AQUA.literal(warpName)));
        } else if ("list".equals(subLow)) {
            Set<String> keys = VintageHomesWorldStorage.getWarpNames(storage.warpsTag);
            if (keys.isEmpty()) {
                player.sendChatToPlayer(Translator.RED.format("message.command.warp.list.none"));
            } else {
                StringBuilder builder = new StringBuilder();
                for (String key : keys) {
                    if (builder.length() > 0) builder.append(", ");
                    builder.append(key);
                }
                player.sendChatToPlayer(Translator.GOLD.format("message.command.warp.list", Translator.AQUA.literal(builder.toString())));
            }
        } else if ("remove".equals(subLow)) {
            if (args.length < 2) {
                player.sendChatToPlayer(Translator.GOLD.format("message.command.warp.usage.remove", Translator.AQUA.literal("/warp remove <name>")));
                return;
            }
            String toRemove = args[1];
            if (storage.warpsTag.hasKey(toRemove)) {
                storage.warpsTag.removeTag(toRemove);
                storage.markDirty();
                player.sendChatToPlayer(Translator.GREEN.format("message.command.warp.success.remove", Translator.AQUA.literal(toRemove)));
            } else {
                player.sendChatToPlayer(Translator.RED.format("message.command.warp.fail.remove", Translator.AQUA.literal(toRemove)));
            }
        } else {
            if (storage.warpsTag.hasKey(sub)) {

                MinecraftServer server = MinecraftServer.getServer();
                WorldServer world = (WorldServer) player.worldObj;
                int dimID = world.provider.dimensionId;
                EntityPlayer playerSP = player;

                int[] pos = storage.warpsTag.getIntArray(sub);
                if (pos.length == 4) {
                    int dim = pos[0];
                    int xPos = pos[1];
                    int yPos = pos[2];
                    int zPos = pos[3];
                    WorldServer destWorld = server.worldServerForDimension(dim);
                    if (!(destWorld == server.worldServerForDimension(dimID))) {
                        server.getConfigurationManager().transferPlayerToDimension(player, dim, new CommonTeleporter(destWorld, CommonTeleporter.Type.WARP, sub, true));
                    }
                    player.setPositionAndUpdate(xPos + 0.5, yPos, zPos + 0.5);
                    playerSP.setPositionAndUpdate(xPos + 0.5, yPos, zPos + 0.5);
                    player.sendChatToPlayer(Translator.GREEN.format("message.command.warp.success", Translator.AQUA.literal(sub)));
                } else {
                    player.sendChatToPlayer(Translator.RED.format("message.command.warp.fail"));
                }
            } else {
                player.sendChatToPlayer(Translator.RED.format("message.command.warp.none", Translator.AQUA.literal(sub)));
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        MinecraftServer server = MinecraftServer.getServer();
        VintageHomesWorldStorage storage = VintageHomesWorldStorage.get(server);
        Set<String> warps = VintageHomesWorldStorage.getWarpNames(storage.warpsTag);
        List warpList = getListOfStringsMatchingLastWord(args, warps.toArray(new String[0]));
        if (args.length == 1 && "warp".equalsIgnoreCase(getCommandName())) {
            return warpList;
        } else if (args.length == 2 && "remove".equalsIgnoreCase(args[0])) {
            return warpList;
        }
        return Collections.emptyList();
    }
}
