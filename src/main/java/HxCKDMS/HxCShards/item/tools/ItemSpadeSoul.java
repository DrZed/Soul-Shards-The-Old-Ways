package HxCKDMS.HxCShards.item.tools;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.Reference;
import HxCKDMS.HxCShards.utils.ModRegistry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSpade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemSpadeSoul extends ItemSpade {
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon2;
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon3;

	public ItemSpadeSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemSpadeSoul)
			return Reference.modName + ".soultool.spade";
		if (stack.getItem() == ModRegistry.ItemSpadeSoul2)
			return Reference.modName + ".soultool2.spade";
		if (stack.getItem() == ModRegistry.ItemSpadeSoul3)
			return Reference.modName + ".soultool3.spade";
		return Reference.modName + ".soultool.spade";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":spadeSoul" + (Configs.useSSTOWTextures ? "_old" : ""));
		itemIcon2 = iconRegister.registerIcon(Reference.modID + ":spadeImprovedSoul");
		itemIcon3 = iconRegister.registerIcon(Reference.modID + ":spadeDraconicSoul");
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemSpadeSoul)
			return itemIcon;
		if (stack.getItem() == ModRegistry.ItemSpadeSoul2)
			return itemIcon2;
		if (stack.getItem() == ModRegistry.ItemSpadeSoul3)
			return itemIcon3;
		return itemIcon;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
}
