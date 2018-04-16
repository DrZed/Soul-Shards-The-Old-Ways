package HxCKDMS.HxCShards.item.tools;

import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemBowSoul extends ItemBow {
    private ToolMaterial toolMaterial = ToolMaterial.IRON;
    private IIcon[] icons = new IIcon[12];

    public ItemBowSoul(ToolMaterial material) {
        super();
        toolMaterial = material;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getItem() == ModRegistry.ItemBowSoul)
            return Reference.modName + ".soultool.bow";
        if (stack.getItem() == ModRegistry.ItemBowSoul2)
            return Reference.modName + ".soultool2.bow";
        if (stack.getItem() == ModRegistry.ItemBowSoul3)
            return Reference.modName + ".soultool3.bow";
        return Reference.modName + ".soultool.bow";
    }

    @Override
    public int getMaxDamage() {
        return toolMaterial.getMaxUses();
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return getIcon(stack, 0);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icons[0];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (useRemaining > 0) {
            int draw = stack.getMaxItemUseDuration() - useRemaining;

            if (draw > 17)
                return icons[3 + (getTier(stack.getItem()) * 4)];
            else if (draw > 13)
                return icons[2 + (getTier(stack.getItem()) * 4)];
            else if (draw > 0)
                return icons[1 + (getTier(stack.getItem()) * 4)];
        }
        return icons[getTier(stack.getItem()) * 4];
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean boo) {
        if (stack.getItem() == ModRegistry.ItemBowSoul3 && stack.hasTagCompound() && stack.getTagCompound().hasKey("Killstreak"))
            list.add("\u00a74Bonus Damage : " + stack.getTagCompound().getInteger("Killstreak"));
        super.addInformation(stack, player, list, boo);
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        for (int i = 0; i < 12; i++)
            icons[i] = ir.registerIcon(Reference.modID + ":tools/bow" + (i > 3 ? (i > 7 ? "Draconic" : "Improved") : "") + "Soul_" + (i - (i > 3 ? (i > 7 ? 8 : 4) : 0)));
    }

    private byte getTier(Item item) {
        if (item == ModRegistry.ItemBowSoul2)
            return 1;
        if (item == ModRegistry.ItemBowSoul3)
            return 2;
        return 0;
    }
}
