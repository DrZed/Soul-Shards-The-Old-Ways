package HxCKDMS.HxCShards.item.tools;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.ModRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import HxCKDMS.HxCShards.utils.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemPickaxeSoul extends ItemPickaxe {
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon2;
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon3;

	public ItemPickaxeSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul)
			return Reference.modName + ".soultool.pickaxe";
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul2)
			return Reference.modName + ".soultool2.pickaxe";
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul3)
			return Reference.modName + ".soultool3.pickaxe";
		return Reference.modName + ".soultool.pickaxe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.modID + ":tools/pickaxeSoul" + (Configs.useSSTOWTextures ? "_old" : ""));
		itemIcon2 = iconRegister.registerIcon(Reference.modID + ":tools/pickaxeImprovedSoul");
		itemIcon3 = iconRegister.registerIcon(Reference.modID + ":tools/pickaxeDraconicSoul");
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul)
			return itemIcon;
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul2)
			return itemIcon2;
		if (stack.getItem() == ModRegistry.ItemPickaxeSoul3)
			return itemIcon3;
		return itemIcon;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
}
