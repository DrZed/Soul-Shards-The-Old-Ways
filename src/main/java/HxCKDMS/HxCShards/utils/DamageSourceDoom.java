package HxCKDMS.HxCShards.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class DamageSourceDoom extends DamageSource {
    public EntityPlayer player;
    public DamageSourceDoom(String source, EntityPlayer player) {
        super(source);
        setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
        this.player = player;
    }
}
