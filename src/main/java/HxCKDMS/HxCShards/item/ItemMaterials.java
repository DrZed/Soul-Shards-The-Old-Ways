package HxCKDMS.HxCShards.item;

import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMaterials extends Item {

    private static String[] names = {
    	"nugget.soulium", 		    // 0
    	"ingot.soulium", 		    // 1
    	"dust.vile", 			    // 2
        "ingot.improved_soulium",    // 3
        "ingot.draconic_soulium",   // 4
    	
    };
    
    private IIcon[] icon = new IIcon[names.length];

    public ItemMaterials() {
        super();

        setUnlocalizedName(Reference.modID + ".material");
        setCreativeTab(ModRegistry.CREATIVE_TAB);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < names.length; i++)
            icon[i] = iconRegister.registerIcon(Reference.modID + ":" + names[i]);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }
}
