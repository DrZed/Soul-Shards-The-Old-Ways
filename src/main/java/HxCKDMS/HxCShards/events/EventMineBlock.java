package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hxckdms.hxccore.utilities.Logger;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventMineBlock {
    @SubscribeEvent
    public void mineBlock(BlockEvent.BreakEvent event) {
        System.out.println(event.block.getUnlocalizedName());
        if (event.getPlayer().getHeldItem() != null && event.getPlayer().getHeldItem().getItem() == ModRegistry.ItemPickaxeSoul3 && event.getPlayer().isSneaking()) {
            long starttime = System.nanoTime();
            for (int x = event.x - 10; x <= event.x + 10; x++) {
                for (int y = event.y - 10; y <= event.y + 10; y++) {
                    for (int z = event.z - 10; z <= event.z + 10; z++) {
                        if (event.getPlayer().canHarvestBlock(event.world.getBlock(x, y, z)) && isBlockOre(event.world.getBlock(x, y, z))) {
                            for (ItemStack drop : getDrops(event.getPlayer(), event.world, x, y, z)) {
                                event.world.spawnEntityInWorld(new EntityItem(event.world, event.getPlayer().posX, event.getPlayer().posY + 1, event.getPlayer().posZ, drop));
                            }
                            event.world.setBlockToAir(x, y, z);
                            event.getPlayer().getHeldItem().damageItem(1, event.getPlayer());
                        }
                    }
                }
            }
            Logger.info(("Collected ores in : " + (System.nanoTime() - starttime)/1000000f + " milliseconds."), Reference.modName);
        }

        if (isBlockWood(event.world.getBlock(event.x, event.y, event.z)) && event.getPlayer().getHeldItem() != null && event.getPlayer().getHeldItem().getItem() == ModRegistry.ItemAxeSoul3 && event.getPlayer().isSneaking()) {
            long starttime = System.nanoTime();
            List<BlockPos> points = new ArrayList<>();
            points.addAll(getPointsOfWood(event.world, event.x, event.y, event.z, points));
            HashMap<ItemStack, Integer> drops = new HashMap<>();
            while (points.size() > 0) {
                BlockPos p = points.remove(0);
                points.addAll(getPointsOfWood(event.world, p.x, p.y, p.z, points));
                if (event.world.getBlock(p.x, p.y, p.z) != Blocks.air) {
                    for (ItemStack drop : getDrops(event.getPlayer(), event.world, p.x, p.y, p.z)) {
                        if (drops.containsKey(drop))
                            drops.put(drop, drops.get(drop) + drop.stackSize);
                        else
                            drops.put(drop, drop.stackSize);
                    }
                    event.world.setBlockToAir(p.x, p.y, p.z);
                    event.getPlayer().getHeldItem().damageItem(1, event.getPlayer());
                }
            }
            drops.forEach((drop, amount) -> {
                for (int i = 0; i < amount; i++) {
                    event.world.spawnEntityInWorld(new EntityItem(event.world, event.getPlayer().posX, event.getPlayer().posY + 1, event.getPlayer().posZ, drop));
                }
            });
            Logger.info(("Collected wood in : " + (System.nanoTime() - starttime)/1000000f + " milliseconds."), Reference.modName);
        }
    }

    private boolean isBlockWood(Block block) {
        for (int i : OreDictionary.getOreIDs(new ItemStack(block))) {
            if (OreDictionary.getOreName(i).startsWith("logWood")) {
                return true;
            }
        }
        return (block.getUnlocalizedName().equalsIgnoreCase("tile.mfr.rubberwood.log") || block.getUnlocalizedName().equalsIgnoreCase("tile.natura.redwood"));
    }

    private List<BlockPos> getPointsOfWood(World world, int x, int y, int z, List<BlockPos> e) {
        List<BlockPos> l = new ArrayList<>();
        for (int a = x - 1; a <= x + 1; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                for (int c = z - 1; c <= z + 1; c++) {
                    BlockPos p = new BlockPos(a, b, c);
                    if (isBlockWood(world.getBlock(a, b, c)) && !(x == a && y == b && z == c) && !e.contains(p)) {
                        l.add(p);
                    }
                }
            }
        }
        return l;
    }

    private boolean isBlockOre(Block block) {
        for (int i : OreDictionary.getOreIDs(new ItemStack(block))) {
            if (OreDictionary.getOreName(i).startsWith("ore")) {
                return true;
            }
        }
        return false;
    }

    private List<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        ItemStack stack = player.getHeldItem();
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack) > 0 && block.canSilkHarvest(world, player, x, y, z, meta)) {
            return Lists.newArrayList(new ItemStack(block, 1, meta));
        }
        return block.getDrops(world, x, y, z, meta, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack));
    }

    private class BlockPos {
        int x;
        int y;
        int z;
        public BlockPos(int xpos, int ypos, int zpos) {
            x = xpos;
            y = ypos;
            z = zpos;
        }
    }
}
