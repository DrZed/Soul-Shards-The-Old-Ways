package HxCKDMS.HxCShards.block;

import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import HxCKDMS.HxCShards.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockCage extends BlockContainer {

	public BlockCage() {
		super(Material.iron);
		setBlockName(Reference.modID + ".cage");
		setCreativeTab(ModRegistry.CREATIVE_TAB);
		blockHardness = 3.0F;
		blockResistance = 3.0F;
	}

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

// Add pull item out of cage code
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3) {
        TileEntityCage cage = (TileEntityCage) world.getTileEntity(x, y, z);

        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModRegistry.ItemSoulShard &&
                player.getHeldItem().hasTagCompound() && Utils.getShardTier(player.getHeldItem()) > 0)
            cage.setInventorySlotContents(0, player.inventory.decrStackSize(player.inventory.currentItem, 1));
        else if (player.getHeldItem() == null && ((TileEntityCage) world.getTileEntity(x, y, z)).getStackInSlot(0) != null)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, cage.getStackInSlotOnClosing(0));
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile instanceof TileEntityCage)
				((TileEntityCage) tile).checkRedstone();
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile == null) {
				return;
			}

			ItemStack stack = ((IInventory) tile).decrStackSize(0, 1);

			if (stack != null) {
				world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityCage();
	}

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        super.onBlockPlacedBy(world, x, y, z, placer, itemIn);
        TileEntityCage cage = (TileEntityCage) world.getTileEntity(x ,y ,z);
        HxCShards.spawners.put(placer.getCommandSenderName(), cage);
    }

    @Override
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	@Override
	public int damageDropped(int par1) {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

    private IIcon[] icons = new IIcon[2];

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons[0] = iconRegister.registerIcon(Reference.modID + ":soulCage");
        icons[1] = iconRegister.registerIcon(Reference.modID + ":soulCageOn");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int sideInt, int meta) {
		return icons[meta];
	}

	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockMetadata(x, y, z) == 1) {
			double d0 = x + random.nextFloat();
			double d1 = y + random.nextFloat();
			double d2 = z + random.nextFloat();
            world.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}
