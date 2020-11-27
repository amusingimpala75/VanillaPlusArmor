package com.github.amusingimpala75.specialarmor.mixin;

import com.github.amusingimpala75.specialarmor.item.IceBoots;
import com.github.amusingimpala75.specialarmor.item.SlimeBoots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
        System.out.println("This will never be called");
    }

    @Shadow
    protected abstract void playBlockFallSound();

    @Shadow
    protected abstract SoundEvent getFallSound(int distance);

    @Shadow
    protected abstract int computeFallDamage(float fallDistance, float damageMultiplier);

    @ModifyVariable(
            method = "travel",
            at = @At(value = "STORE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"),
            ordinal = 0
    )
    private float modifySlipperiness(float original) {
        if (((LivingEntity)(Object)this).getEquippedStack(EquipmentSlot.FEET).getItem() instanceof IceBoots) {
            //MOOOORRRRREEEE
            return 1.09F/*+ 0.0089010989F*/;
        }
        return original;
    }

    @Inject(at=@At("HEAD"), method = "handleFallDamage", cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> info) {
        if (((LivingEntity)(Object)this).getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SlimeBoots) {
            boolean bl = super.handleFallDamage(fallDistance, damageMultiplier);
            int i = this.computeFallDamage(fallDistance, damageMultiplier);
            if (i > 0) {
                this.playSound(this.getFallSound(i), 1.0F, 1.0F);
                this.playBlockFallSound();
                this.damage(DamageSource.FALL, (float)i);
                info.setReturnValue(true);
            } else {
                info.setReturnValue(bl);
            }
        }
    }
}
