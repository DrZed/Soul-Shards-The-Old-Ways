package HxCKDMS.HxCShards.item.armor;

import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemSoulArmor3 extends ItemArmor {
	@SideOnly(Side.CLIENT)
	private IIcon[] armorIcons = new IIcon[12];

	public ItemSoulArmor3(ArmorMaterial amat, int type) {
		super(amat, 0, type);
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		String name = Reference.modName;
		switch (getTier(stack.getItem())) {
			case 1: name += ".soularmor2";
				break;
			case 2: name += ".soularmor3";
				break;
			default: name += ".soularmor";
		}

		switch (((ItemArmor)stack.getItem()).armorType) {
			case 0: name += ".helm";
				break;
			case 2: name += ".leggings";
				break;
			case 3: name += ".boots";
				break;
			default: name += ".chest";
		}
		return name;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean boo) {
		if (getTier(stack.getItem()) == 2 /*tier 3*/ && stack.hasTagCompound() && stack.getTagCompound().hasKey("Souls"))
			list.add("\u00a76Souls : " + stack.getTagCompound().getInteger("Souls"));
		super.addInformation(stack, player, list, boo);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":armor/chestSoulium");
		armorIcons[0] = iconRegister.registerIcon(Reference.modID + ":armor/helmSoulium");
		armorIcons[1] = iconRegister.registerIcon(Reference.modID + ":armor/chestSoulium");
		armorIcons[2] = iconRegister.registerIcon(Reference.modID + ":armor/legsSoulium");
		armorIcons[3] = iconRegister.registerIcon(Reference.modID + ":armor/bootsSoulium");

		armorIcons[4] = iconRegister.registerIcon(Reference.modID + ":armor/helmSoulium2");
		armorIcons[5] = iconRegister.registerIcon(Reference.modID + ":armor/chestSoulium2");
		armorIcons[6] = iconRegister.registerIcon(Reference.modID + ":armor/legsSoulium2");
		armorIcons[7] = iconRegister.registerIcon(Reference.modID + ":armor/bootsSoulium2");

		armorIcons[8] = iconRegister.registerIcon(Reference.modID + ":armor/helmSoulium3");
		armorIcons[9] = iconRegister.registerIcon(Reference.modID + ":armor/chestSoulium3");
		armorIcons[10] = iconRegister.registerIcon(Reference.modID + ":armor/legsSoulium3");
		armorIcons[11] = iconRegister.registerIcon(Reference.modID + ":armor/bootsSoulium3");
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (getTier(stack.getItem()) == 0)
			return Reference.modID + ":textures/models/armor/soulium_layer_" + (stack.getItem() == ModRegistry.ItemSoulLegs ? "2" : "1") + ".png";
		if (getTier(stack.getItem()) == 1)
			return Reference.modID + ":textures/models/armor/improved_soulium_layer_" + (stack.getItem() == ModRegistry.ItemSoulLegs2 ? "2" : "1") + ".png";
		if (getTier(stack.getItem()) == 2)
			return Reference.modID + ":textures/models/armor/draconic_soulium_layer_" + (stack.getItem() == ModRegistry.ItemSoulLegs3 ? "2" : "1") + ".png";
		return super.getArmorTexture(stack, entity, slot, type);
	}

	@Override
    public IIcon getIconIndex(ItemStack stack) {
		return armorIcons[getTier(stack.getItem()) * 4 + ((ItemArmor)stack.getItem()).armorType];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }

    private byte getTier(Item item) {
		if (item == ModRegistry.ItemSoulHelm2 || item == ModRegistry.ItemSoulChest2 || item == ModRegistry.ItemSoulLegs2 || item == ModRegistry.ItemSoulBoots2)
			return 1;
		if (item == ModRegistry.ItemSoulHelm3 || item == ModRegistry.ItemSoulChest3 || item == ModRegistry.ItemSoulLegs3 || item == ModRegistry.ItemSoulBoots3)
			return 2;
		return 0;
	}
}
