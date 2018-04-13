package HxCKDMS.HxCShards.commands;

import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.TierHandler;
import HxCKDMS.HxCShards.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class CommandSoulShards extends CommandBase {
	@Override
	public String getCommandName() {
		return "soulshards";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/soulshards help";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@SuppressWarnings({"unchecked", "unused"})
	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		if ((params.length > 0) && (params.length <= 7)) {
            switch (params[0]) {
                case "killall":
                    int killCounter = 0;

                    for (Entity ent : (List<Entity>) sender.getEntityWorld().loadedEntityList)
                        if (ent.getEntityData().getBoolean("SoulShard")) {
                            ent.setDead();
                            killCounter++;
                        }

                    if (killCounter == 0)
                        sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.notfound")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    else sender.addChatMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.command.killed", "" + killCounter)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                    break;
                case "settier":
                    if (params.length == 2) {
                        byte tier = Byte.parseByte(params[1]);
                        int minKills = TierHandler.getMinKills(tier);
                        if (((EntityPlayerMP) sender).getHeldItem() != null
                                && ((EntityPlayerMP) sender).getHeldItem().getItem() == ModRegistry.ItemSoulShard) {
                            ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();

                            Utils.setShardTier(shard, tier);
                            Utils.setShardKillCount(shard, minKills);
                        }
                    } else sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.setwrong")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    break;
                case "addkills":
                    byte killAmount = 100;

                    if (params.length >= 2)
                        killAmount = Byte.parseByte(params[1]);
                    else sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.addkillwrong")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

                    if (((EntityPlayerMP) sender).getHeldItem() != null
                            && ((EntityPlayerMP) sender).getHeldItem().getItem() == ModRegistry.ItemSoulShard) {
                        ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();
                        Utils.increaseShardKillCount(shard, killAmount);
                    }
                    break;
                case "removekills":
                    if (params.length == 2) {
                        killAmount = Byte.parseByte(params[1]);
                        if (((EntityPlayerMP) sender).getHeldItem() != null
                                && ((EntityPlayerMP) sender).getHeldItem().getItem() == ModRegistry.ItemSoulShard) {
                            ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();
                            Utils.increaseShardKillCount(shard, (byte) -killAmount);
                        }
                    } else sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.remkillwrong")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    break;
                case "xp":
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You have " + ((EntityPlayerMP) sender).experienceTotal + " of xp"));
                    break;
                case "stop":
                    if (params.length == 2) {
                        HxCShards.spawners.values().forEach(b -> b.disableTimer = Integer.parseInt(params[1]));
                    } else {
                        HxCShards.spawners.forEach((creator, spawner) -> {
                            if (params[1].equalsIgnoreCase(creator))
                                spawner.disableTimer = Integer.parseInt(params [2]);
                        });
                    }
                    break;
                case "purge":
                    if (params.length == 2) {
                        HxCShards.spawners.forEach((creator, spawner) -> {
                            if (params[1].equalsIgnoreCase(creator))
                                spawner.getWorldObj().func_147480_a(spawner.xCoord, spawner.yCoord, spawner.zCoord, true);
                        });
                    } else {
                        HxCShards.spawners.values().forEach(b -> b.getWorldObj().func_147480_a(b.xCoord, b.yCoord, b.zCoord, true));
                    }
                    break;
                case "logdumpclient":
                    HxCShards.logDump.values().forEach(b -> sender.addChatMessage(new ChatComponentText(b)));
                    break;
                case "spawnaltar":
                    if (params.length == 4)
                        spawnAltar(sender.getEntityWorld(), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                    else
                        spawnAltar(sender.getEntityWorld(), sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY - 1, sender.getPlayerCoordinates().posZ);
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.killall")));
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.settier")));
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.addkills")));
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.removekills")));
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.stop")));
                    sender.addChatMessage(new ChatComponentText(Utils.localize("chat.hxcshards.command.purge")));
                    break;
            }
		}
	}

    private void spawnAltar(World world, int x, int y, int z) {
        world.setBlock(x + 1, y, z, Blocks.diamond_block);
        world.setBlock(x - 1, y, z, Blocks.diamond_block);
        world.setBlock(x, y, z + 1, Blocks.diamond_block);
        world.setBlock(x, y, z - 1, Blocks.diamond_block);
        world.setBlock(x + 1, y, z + 1, Blocks.quartz_block);
        world.setBlock(x - 1, y, z - 1, Blocks.quartz_block);
        world.setBlock(x - 1, y, z + 1, Blocks.quartz_block);
        world.setBlock(x + 1, y, z - 1, Blocks.quartz_block);
        world.setBlock(x + 2, y, z + 2, Blocks.obsidian);
        world.setBlock(x + 2, y, z + 1, Blocks.obsidian);
        world.setBlock(x + 1, y, z + 2, Blocks.obsidian);
        world.setBlock(x - 2, y, z + 2, Blocks.obsidian);
        world.setBlock(x - 2, y, z + 1, Blocks.obsidian);
        world.setBlock(x - 1, y, z + 2, Blocks.obsidian);
        world.setBlock(x + 2, y, z - 2, Blocks.obsidian);
        world.setBlock(x + 2, y, z - 1, Blocks.obsidian);
        world.setBlock(x + 1, y, z - 2, Blocks.obsidian);
        world.setBlock(x - 2, y, z - 2, Blocks.obsidian);
        world.setBlock(x - 2, y, z - 1, Blocks.obsidian);
        world.setBlock(x - 1, y, z - 2, Blocks.obsidian);
        world.setBlock(x + 2, y, z, Blocks.netherrack);
        world.setBlock(x - 2, y, z, Blocks.netherrack);
        world.setBlock(x, y, z + 2, Blocks.netherrack);
        world.setBlock(x, y, z - 2, Blocks.netherrack);
        world.setBlock(x + 2, y + 1, z, Blocks.fire);
        world.setBlock(x - 2, y + 1, z, Blocks.fire);
        world.setBlock(x, y + 1, z + 2, Blocks.fire);
        world.setBlock(x, y + 1, z - 2, Blocks.fire);
        world.setBlock(x + 3, y, z + 3, Blocks.end_stone);
        world.setBlock(x + 3, y, z + 2, Blocks.end_stone);
        world.setBlock(x + 2, y, z + 3, Blocks.end_stone);
        world.setBlock(x - 3, y, z + 3, Blocks.end_stone);
        world.setBlock(x - 3, y, z + 2, Blocks.end_stone);
        world.setBlock(x - 2, y, z + 3, Blocks.end_stone);
        world.setBlock(x + 3, y, z - 3, Blocks.end_stone);
        world.setBlock(x + 3, y, z - 2, Blocks.end_stone);
        world.setBlock(x + 2, y, z - 3, Blocks.end_stone);
        world.setBlock(x - 3, y, z - 3, Blocks.end_stone);
        world.setBlock(x - 3, y, z - 2, Blocks.end_stone);
        world.setBlock(x - 2, y, z - 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 1, z + 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 1, z - 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 1, z + 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 1, z - 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 2, z + 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 2, z - 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 2, z + 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 2, z - 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 3, z + 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 3, z - 3, Blocks.end_stone);
        world.setBlock(x - 3, y + 3, z + 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 3, z - 3, Blocks.end_stone);
        world.setBlock(x + 3, y + 4, z + 3, Blocks.glowstone);
        world.setBlock(x - 3, y + 4, z - 3, Blocks.glowstone);
        world.setBlock(x - 3, y + 4, z + 3, Blocks.glowstone);
        world.setBlock(x + 3, y + 4, z - 3, Blocks.glowstone);
    }
}