package com.github.amusingimpala75.specialarmor.mixin;

import com.github.amusingimpala75.specialarmor.item.HoneyChestplate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class MixinEntity implements Nameable, CommandOutput {

    @Shadow
    public abstract Box getBoundingBox();

    @ModifyVariable(
            method = "checkBlockCollision",
            at = @At(value = "STORE", target = "Lnet/minecraft/util/math/BlockPos;<init>(DDD)V"),
            ordinal = 0
    )
    private BlockPos setBlockPos(BlockPos original) {
        if ((Entity)(Object)this instanceof LivingEntity) {
            if (((LivingEntity) (Object) this).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HoneyChestplate) {
                Box box = this.getBoundingBox();
                return new BlockPos(box.minX - 0.001D, box.minY + 0.001D, box.minZ - 0.001D);
            }
        }
        return original;
    }

    @ModifyVariable(
            method = "checkBlockCollision",
            at = @At(value = "STORE", target = "Lnet/minecraft/util/math/BlockPos;<init>(DDD)V"),
            ordinal = 1
    )
    private BlockPos setBlockPos2(BlockPos original) {
        if ((Entity)(Object)this instanceof LivingEntity) {
            if (((LivingEntity) (Object) this).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HoneyChestplate) {
                Box box = this.getBoundingBox();
                return new BlockPos(box.maxX + 0.001D, box.maxY - 0.001D, box.maxZ + 0.001D);
            }
        }
        return original;
    }
}
