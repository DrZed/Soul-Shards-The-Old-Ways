package HxCKDMS.HxCShards.block;

import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

@SuppressWarnings("all")
public class RGBFluid extends BlockFluidFinite {
    @SideOnly(Side.CLIENT)
    private IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    private IIcon flowingIcon;

    public int colour = 0xffffffff;

    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
        return colour;
    }

    public RGBFluid(Fluid fluid, int color) {
        super(fluid, Material.lava);
        setCreativeTab(CreativeTabs.tabMisc);
        colour = color;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(Reference.modID + ":fluidStill");
        flowingIcon = register.registerIcon(Reference.modID + ":fluidFlowing");
        System.out.println(stillIcon.getIconName());
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
    }

    @Override
    public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z) == Blocks.air)
            return 0;

        if (world.getBlock(x, y, z) != this)
            return -1;

        return quantaPerBlock - world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean canCollideCheck(int meta, boolean fullHit) {
        return fullHit && meta == quantaPerBlock - 1;
    }

    @Override
    public int getMaxRenderHeightMeta() {
        return quantaPerBlock + 1;
    }

    public boolean isValid(World world, int x, int y, int z, int meta) {
        if (world.getBlock(x + 1, y, z) == this && world.getBlockMetadata(x + 1, y, z) == meta - 1) {
            return true;
        }
        if (world.getBlock(x - 1, y, z) == this && world.getBlockMetadata(x - 1, y, z) == meta - 1) {
            return true;
        }
        if (world.getBlock(x, y, z + 1) == this && world.getBlockMetadata(x, y, z + 1) == meta - 1) {
            return true;
        }
        if (world.getBlock(x, y, z - 1) == this && world.getBlockMetadata(x, y, z - 1) == meta - 1) {
            return true;
        }
        if (world.getBlock(x, y + 1, z) == this) {
            return true;
        }
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta > 8) {
            world.setBlockToAir(x, y, z);
        }
        //fluid evaporation
        if (meta != 0 && !isValid(world, x, y, z, meta)) {
            if (meta + 1 >= 8)
                world.setBlock(x, y, z, Blocks.air, 0, 3);
            else world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
            return;
        }

        if (meta != 8 && world.getBlock(x + 1, y, z) == Blocks.air) {
            world.setBlock(x + 1, y, z, this, meta + 1, 2);
        }

        if (meta != 8 && world.getBlock(x - 1, y, z) == Blocks.air) {
            world.setBlock(x - 1, y, z, this, meta + 1, 2);
        }

        if (meta != 8 && world.getBlock(x, y, z + 1) == Blocks.air) {
            world.setBlock(x, y, z + 1, this, meta + 1, 2);
        }

        if (meta != 8 && world.getBlock(x, y, z - 1) == Blocks.air) {
            world.setBlock(x, y, z - 1, this, meta + 1, 2);
        }

        if (world.getBlock(x, y - 1, z) == Blocks.air) {
            world.setBlock(x, y - 1, z, this, meta, 3);
            if (meta >= 8) world.setBlockToAir(x, y, z);
            else world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
        }
    }

    @Override
    public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
        return doDrain ? new FluidStack(ModRegistry.fluids.get(getUnlocalizedName().substring(5)), 1000) : null;
    }

    @Override
    public boolean canDrain(World world, int x, int y, int z) {
        return true;
    }
}