package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.AABBUtils;
import HxCKDMS.HxCShards.utils.ModRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import java.util.List;

@SuppressWarnings("all")
public class EventSoulRitual {
    @SubscribeEvent
    public void itemDropInWorld(ItemTossEvent event) {
        if ((event.entityItem.getEntityItem().getItem() == ModRegistry.ItemMaterials && event.entityItem.getEntityItem().getItemDamage() == 3) || getTier(event.entityItem.getEntityItem().getItem()) < 2) {
            int x = (int) Math.round(event.entityItem.posX);
            int y = (int) Math.round(event.entityItem.posY - 2);
            int z = (int) Math.round(event.entityItem.posZ);
            int a = event.entityItem.getEntityItem().stackSize;

            World world = event.entityItem.worldObj;
            List<EntityItem> l = world.getEntitiesWithinAABB(EntityItem.class, AABBUtils.getAreaBoundingBox(x, y, z, 5));

            final boolean[] hasStars = {false};
            final boolean[] hasBars = {false};
            final EntityItem[] items = {null};
            l.forEach(i -> {
                if (i.getEntityItem().getItem() == Items.nether_star && i.getEntityItem().stackSize >= a * (world.difficultySetting.getDifficultyId() * 4)) {
                    hasStars[0] = true;
                    items[0] = i;
                }
                if (i.getEntityItem().getItem() == ModRegistry.ItemMaterials && (i.getEntityItem().getItemDamage() == 3 || i.getEntityItem().getItemDamage() == 4) && i.getEntityItem().stackSize >= 4) {
                    hasBars[0] = true;
                    items[0] = i;
                }
            });

            if (hasStars[0] && event.entityItem.getEntityItem().getItemDamage() == 3) {
                for (int i = y - 5; i <= y; i++) {
                    for (int j = x - 1; j <= x + 1; j++) {
                        for (int k = z - 1; k <= z + 1; k++) {
                            if (world.getBlock(j, i, k) instanceof BlockDragonEgg && canCreate(world, j, i, k)) {
                                event.entityItem.setDead();
                                items[0].setDead();
                                clearStructure(world, j, i, k);
                                world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemMaterials, a, 5)));
                            }
                        }
                    }
                }
            }
            if (hasBars[0]) {
                for (int i = y - 5; i <= y; i++) {
                    for (int j = x - 1; j <= x + 1; j++) {
                        for (int k = z - 1; k <= z + 1; k++) {
                            if (world.getBlock(j, i, k) == Blocks.crafting_table && canCreate(world, j, i, k)) {
                                event.entityItem.setDead();
                                items[0].setDead();
                                clearStructure(world, j, i, k);
                                if (items[0].getEntityItem().getItemDamage() == 4) {
                                    if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSwordSoul2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSwordSoul3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemAxeSoul2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemAxeSoul3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemPickaxeSoul2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemPickaxeSoul3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSpadeSoul2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSpadeSoul3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemBowSoul2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemBowSoul3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulHelm2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulHelm3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulChest2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulChest3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulLegs2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulLegs3)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulBoots2) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulBoots2)));
                                    }
                                }

                                if (items[0].getEntityItem().getItemDamage() == 3) {
                                    if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSwordSoul) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSwordSoul2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemAxeSoul) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemAxeSoul2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemPickaxeSoul) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemPickaxeSoul2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSpadeSoul) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSpadeSoul2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemBowSoul) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemBowSoul2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulHelm) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulHelm2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulChest) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulChest2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulLegs) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulLegs2)));
                                    } else if (event.entityItem.getEntityItem().getItem() == ModRegistry.ItemSoulBoots) {
                                        world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModRegistry.ItemSoulBoots2)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private byte getTier(Item item) {
        if (item == ModRegistry.ItemSwordSoul2
         || item == ModRegistry.ItemPickaxeSoul2
         || item == ModRegistry.ItemAxeSoul2
         || item == ModRegistry.ItemSpadeSoul2
         || item == ModRegistry.ItemBowSoul2
         || item == ModRegistry.ItemSoulHelm2
         || item == ModRegistry.ItemSoulChest2
         || item == ModRegistry.ItemSoulLegs2
         || item == ModRegistry.ItemSoulBoots2)
            return 1;

        if (item == ModRegistry.ItemSwordSoul
                || item == ModRegistry.ItemPickaxeSoul
                || item == ModRegistry.ItemAxeSoul
                || item == ModRegistry.ItemSpadeSoul
                || item == ModRegistry.ItemBowSoul
                || item == ModRegistry.ItemSoulHelm
                || item == ModRegistry.ItemSoulChest
                || item == ModRegistry.ItemSoulLegs
                || item == ModRegistry.ItemSoulBoots)
            return 0;
        return 2;
    }

    public void clearStructure(World world, int x, int y, int z) {
        world.setBlockToAir(x, y, z);
        world.setBlockToAir(x + 1, y, z);
        world.setBlockToAir(x - 1, y, z);
        world.setBlockToAir(x, y, z + 1);
        world.setBlockToAir(x, y, z - 1);
        world.setBlockToAir(x + 1, y, z + 1);
        world.setBlockToAir(x - 1, y, z - 1);
        world.setBlockToAir(x - 1, y, z + 1);
        world.setBlockToAir(x + 1, y, z - 1);
        world.setBlockToAir(x + 2, y, z + 2);
        world.setBlockToAir(x + 2, y, z + 1);
        world.setBlockToAir(x + 1, y, z + 2);
        world.setBlockToAir(x - 2, y, z + 2);
        world.setBlockToAir(x - 2, y, z + 1);
        world.setBlockToAir(x - 1, y, z + 2);
        world.setBlockToAir(x + 2, y, z - 2);
        world.setBlockToAir(x + 2, y, z - 1);
        world.setBlockToAir(x + 1, y, z - 2);
        world.setBlockToAir(x - 2, y, z - 2);
        world.setBlockToAir(x - 2, y, z - 1);
        world.setBlockToAir(x - 1, y, z - 2);
        world.setBlockToAir(x + 2, y, z);
        world.setBlockToAir(x - 2, y, z);
        world.setBlockToAir(x, y, z + 2);
        world.setBlockToAir(x, y, z - 2);
        world.setBlockToAir(x + 2, y + 1, z);
        world.setBlockToAir(x - 2, y + 1, z);
        world.setBlockToAir(x, y + 1, z + 2);
        world.setBlockToAir(x, y + 1, z - 2);
        world.setBlockToAir(x + 3, y, z + 3);
        world.setBlockToAir(x + 3, y, z + 2);
        world.setBlockToAir(x + 2, y, z + 3);
        world.setBlockToAir(x - 3, y, z + 3);
        world.setBlockToAir(x - 3, y, z + 2);
        world.setBlockToAir(x - 2, y, z + 3);
        world.setBlockToAir(x + 3, y, z - 3);
        world.setBlockToAir(x + 3, y, z - 2);
        world.setBlockToAir(x + 2, y, z - 3);
        world.setBlockToAir(x - 3, y, z - 3);
        world.setBlockToAir(x - 3, y, z - 2);
        world.setBlockToAir(x - 2, y, z - 3);
        world.setBlockToAir(x + 3, y + 1, z + 3);
        world.setBlockToAir(x - 3, y + 1, z - 3);
        world.setBlockToAir(x - 3, y + 1, z + 3);
        world.setBlockToAir(x + 3, y + 1, z - 3);
        world.setBlockToAir(x + 3, y + 2, z + 3);
        world.setBlockToAir(x - 3, y + 2, z - 3);
        world.setBlockToAir(x - 3, y + 2, z + 3);
        world.setBlockToAir(x + 3, y + 2, z - 3);
        world.setBlockToAir(x + 3, y + 3, z + 3);
        world.setBlockToAir(x - 3, y + 3, z - 3);
        world.setBlockToAir(x - 3, y + 3, z + 3);
        world.setBlockToAir(x + 3, y + 3, z - 3);
        world.setBlockToAir(x + 3, y + 4, z + 3);
        world.setBlockToAir(x - 3, y + 4, z - 3);
        world.setBlockToAir(x - 3, y + 4, z + 3);
        world.setBlockToAir(x + 3, y + 4, z - 3);
    }

    public boolean canCreate(World world, int x, int y, int z) {
        boolean diamonds =
                world.getBlock(x + 1, y, z) == Blocks.diamond_block &&
                world.getBlock(x - 1, y, z) == Blocks.diamond_block &&
                world.getBlock(x, y, z + 1) == Blocks.diamond_block &&
                world.getBlock(x, y, z - 1) == Blocks.diamond_block;
        boolean quartz =
                world.getBlock(x + 1, y, z + 1) == Blocks.quartz_block &&
                world.getBlock(x - 1, y, z - 1) == Blocks.quartz_block &&
                world.getBlock(x - 1, y, z + 1) == Blocks.quartz_block &&
                world.getBlock(x + 1, y, z - 1) == Blocks.quartz_block;
        boolean obsidian =
                world.getBlock(x + 2, y, z + 2) == Blocks.obsidian &&
                world.getBlock(x + 2, y, z + 1) == Blocks.obsidian &&
                world.getBlock(x + 1, y, z + 2) == Blocks.obsidian &&
                world.getBlock(x - 2, y, z + 2) == Blocks.obsidian &&
                world.getBlock(x - 2, y, z + 1) == Blocks.obsidian &&
                world.getBlock(x - 1, y, z + 2) == Blocks.obsidian &&
                world.getBlock(x + 2, y, z - 2) == Blocks.obsidian &&
                world.getBlock(x + 2, y, z - 1) == Blocks.obsidian &&
                world.getBlock(x + 1, y, z - 2) == Blocks.obsidian &&
                world.getBlock(x - 2, y, z - 2) == Blocks.obsidian &&
                world.getBlock(x - 2, y, z - 1) == Blocks.obsidian &&
                world.getBlock(x - 1, y, z - 2) == Blocks.obsidian;
        boolean netherrack =
                world.getBlock(x + 2, y, z) == Blocks.netherrack &&
                world.getBlock(x - 2, y, z) == Blocks.netherrack &&
                world.getBlock(x, y, z + 2) == Blocks.netherrack &&
                world.getBlock(x, y, z - 2) == Blocks.netherrack;
        boolean fire =
                world.getBlock(x + 2, y + 1, z) == Blocks.fire &&
                world.getBlock(x - 2, y + 1, z) == Blocks.fire &&
                world.getBlock(x, y + 1, z + 2) == Blocks.fire &&
                world.getBlock(x, y + 1, z - 2) == Blocks.fire;
        boolean endstone =
                world.getBlock(x + 3, y, z + 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y, z + 2) == Blocks.end_stone &&
                world.getBlock(x + 2, y, z + 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y, z + 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y, z + 2) == Blocks.end_stone &&
                world.getBlock(x - 2, y, z + 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y, z - 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y, z - 2) == Blocks.end_stone &&
                world.getBlock(x + 2, y, z - 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y, z - 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y, z - 2) == Blocks.end_stone &&
                world.getBlock(x - 2, y, z - 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 1, z + 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 1, z - 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 1, z + 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 1, z - 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 2, z + 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 2, z - 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 2, z + 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 2, z - 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 3, z + 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 3, z - 3) == Blocks.end_stone &&
                world.getBlock(x - 3, y + 3, z + 3) == Blocks.end_stone &&
                world.getBlock(x + 3, y + 3, z - 3) == Blocks.end_stone;
        boolean glowstone =
                world.getBlock(x + 3, y + 4, z + 3) == Blocks.glowstone &&
                world.getBlock(x - 3, y + 4, z - 3) == Blocks.glowstone &&
                world.getBlock(x - 3, y + 4, z + 3) == Blocks.glowstone &&
                world.getBlock(x + 3, y + 4, z - 3) == Blocks.glowstone;
        return diamonds && quartz && obsidian && netherrack && fire && endstone && glowstone;
    }
}
