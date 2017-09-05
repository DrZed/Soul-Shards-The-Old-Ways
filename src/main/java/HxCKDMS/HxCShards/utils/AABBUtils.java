package HxCKDMS.HxCShards.utils;

import net.minecraft.util.AxisAlignedBB;

public class AABBUtils {
    public static AxisAlignedBB getAreaBoundingBox(int x, int y, int z, int mod) {
        return AxisAlignedBB.getBoundingBox(x - mod, y - mod, z - mod,
                x + 0.99 + mod, y + 0.99 + mod, z + 0.99 + mod);
    }
}
