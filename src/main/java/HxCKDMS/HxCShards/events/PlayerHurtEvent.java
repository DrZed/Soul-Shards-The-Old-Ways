package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.ModRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PlayerHurtEvent {
    @SubscribeEvent
    public void playerHurtEvent(LivingHurtEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;

            for (int i = 0; i < 4; i++) {
                if (player.getCurrentArmor(i) != null && isTier3(player.getCurrentArmor(i).getItem()) && player.getCurrentArmor(i).hasTagCompound() && player.getCurrentArmor(i).getTagCompound().hasKey("Souls")) {
                    if (player.getCurrentArmor(i).getTagCompound().getInteger("Souls") > 0) {
                        player.heal(Configs.Tier3ArmorHeal);
                        player.getCurrentArmor(i).getTagCompound().setInteger("Souls", player.getCurrentArmor(i).getTagCompound().getInteger("Souls") - 1);
                    }
                }
            }
        }
    }

    private boolean isTier3(Item item) {
        return item == ModRegistry.ItemSoulHelm3 || item == ModRegistry.ItemSoulChest3 || item == ModRegistry.ItemSoulLegs3 || item == ModRegistry.ItemSoulBoots3;
    }
}
