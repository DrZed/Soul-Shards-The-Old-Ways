package HxCKDMS.HxCShards.block;

import HxCKDMS.HxCShards.utils.Reference;
import HxCKDMS.HxCShards.utils.ModRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockMaterials extends Block {
	public BlockMaterials() {
		super(Material.iron);
		this.setUnlocalizedName(Reference.modID + ".block.material");
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	public static final String[] names = new String[] { 
		"Soulium",		// 0
	};
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(item, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.modID + ":block" + names[0]);
	}

	@Override
    public int damageDropped(int meta) {
        return meta;
    }

	@Override
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}
}
