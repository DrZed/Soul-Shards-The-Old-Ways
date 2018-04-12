package HxCKDMS.HxCShards.item.tools;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemSwordSoul extends ItemSword {
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon2;
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon3;

	public ItemSwordSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemSwordSoul)
			return Reference.modName + ".soultool.sword";
		if (stack.getItem() == ModRegistry.ItemSwordSoul2)
			return Reference.modName + ".soultool2.sword";
		if (stack.getItem() == ModRegistry.ItemSwordSoul3)
			return Reference.modName + ".soultool3.sword";
		return Reference.modName + ".soultool.sword";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":swordSoul" + (Configs.useSSTOWTextures ? "_old" : ""));
		itemIcon2 = iconRegister.registerIcon(Reference.modID + ":swordImprovedSoul");
		itemIcon3 = iconRegister.registerIcon(Reference.modID + ":swordDraconicSoul");
	}

    @Override
    public IIcon getIconIndex(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemSwordSoul)
			return itemIcon;
		if (stack.getItem() == ModRegistry.ItemSwordSoul2)
			return itemIcon2;
		if (stack.getItem() == ModRegistry.ItemSwordSoul3)
			return itemIcon3;
        return itemIcon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }
}
