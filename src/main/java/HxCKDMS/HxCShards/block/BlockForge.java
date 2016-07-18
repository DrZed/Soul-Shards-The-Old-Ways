package HxCKDMS.HxCShards.block;

import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.tileentity.TileEntityForge;
import HxCKDMS.HxCShards.utils.HolidayHelper;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockForge extends BlockContainer {

	private IIcon top;
	private IIcon[] front;
	private IIcon bottom;
    
    private boolean active;

	public BlockForge() {
		super(Material.rock);
		setBlockName(Reference.modID + ".block.forge");
		setHardness(3.5F);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int xCoord, int yCoord, int zCoord, int side) {
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(xCoord, yCoord, zCoord));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
        front = new IIcon[2];
		if (HolidayHelper.isHalloween()) {
			this.blockIcon = iconRegister.registerIcon("minecraft:pumpkin_side");
            this.front[0] = iconRegister.registerIcon(Reference.modID + ":soulforge/soulForge_idle_halloween");
			this.front[1] = iconRegister.registerIcon(Reference.modID + ":soulforge/soulForge_active_halloween");
			this.top = iconRegister.registerIcon("minecraft:pumpkin_top");
			this.bottom = iconRegister.registerIcon("minecraft:pumpkin_top");
		} else {
			this.blockIcon = iconRegister.registerIcon(Reference.modID + ":soulforge/soulForge_side");
            this.front[0] = iconRegister.registerIcon(Reference.modID + ":soulforge/soulForge_idle");
            this.front[1] = iconRegister.registerIcon(Reference.modID + ":soulforge/soulForge_active");
			this.top = iconRegister.registerIcon("furnace_top");
			this.bottom = iconRegister.registerIcon("obsidian");
		}
	}



	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 1) {
			return this.top;
		} else if (side == 0) {
			return this.bottom;
		} else if (side != meta) {
			return this.blockIcon;
		} else {
			return this.active ? front[1] : front[0];
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		player.openGui(HxCShards.modInstance, 0, world, x, y, z);
		return true;
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(ModRegistry.BlockForge);
	}

	@Override
	public Item getItem(World world, int par2, int par3, int par4) {
		return Item.getItemFromBlock(ModRegistry.BlockForge);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var1) {
		return new TileEntityForge();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
		int dir = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (dir) {
            case 0:
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
                break;
            case 1:
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
                break;
            case 2:
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
                break;
            case 3:
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
                break;
        }
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (!active) {
			TileEntityForge forgeTile = (TileEntityForge) world.getTileEntity(x, y, z);
			if (forgeTile != null) {
				for (int i = 0; i < forgeTile.getSizeInventory(); ++i) {
					ItemStack itemstack = forgeTile.getStackInSlot(i);
					if (itemstack != null) {
						float f = world.rand.nextFloat() * 0.6F + 0.1F;
						float f1 = world.rand.nextFloat() * 0.6F + 0.1F;
						float f2 = world.rand.nextFloat() * 0.6F + 0.1F;

						while (itemstack.stackSize > 0) {
							int j = world.rand.nextInt(21) + 10;
							if (j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}
							itemstack.stackSize -= j;
							EntityItem itemEntity = new EntityItem(world,
									(double) ((float) x + f),
									(double) ((float) y + f1),
									(double) ((float) z + f2), new ItemStack(
											itemstack.getItem(), j,
											itemstack.getItemDamage()));

							if (itemstack.hasTagCompound()) {
								itemEntity.getEntityItem().setTagCompound(
										((NBTTagCompound) itemstack
												.getTagCompound().copy()));
							}
							float f3 = 0.025F;
							itemEntity.motionX = (double) ((float) world.rand
									.nextGaussian() * f3);
							itemEntity.motionY = (double) ((float) world.rand
									.nextGaussian() * f3 + 0.2F);
							itemEntity.motionZ = (double) ((float) world.rand
									.nextGaussian() * f3);
							world.spawnEntityInWorld(itemEntity);
						}
					}
				}
				world.func_147453_f(x, y, z, block);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        active = ((TileEntityForge)world.getTileEntity(x, y, z)).isBurning();
		if (active) {
			int direction = world.getBlockMetadata(x, y, z);

			float xx = (float) x + 0.5F;
			float yy = (float) y + random.nextFloat() * 6.0F / 16.0F;
			float zz = (float) z + 0.5F;
			float zz2 = random.nextFloat() * 0.3F - 0.2F;
			float xx2 = 0.5F;

			if (direction == 4) {
				world.spawnParticle("smoke", (double) (xx - xx2), (double) yy, (double) (zz + zz2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx - xx2), (double) yy, (double) (zz + zz2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 5) {
				world.spawnParticle("smoke", (double) (xx + xx2), (double) yy, (double) (zz + zz2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + xx2), (double) yy, (double) (zz + zz2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 3) {
				world.spawnParticle("smoke", (double) (xx + zz2), (double) yy, (double) (zz + xx2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz2), (double) yy, (double) (zz + xx2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 2) {
				world.spawnParticle("smoke", (double) (xx + zz2), (double) yy, (double) (zz - xx2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz2), (double) yy, (double) (zz - xx2), 0.0F, 0.0F, 0.0F);
			}
		}
	}
}
