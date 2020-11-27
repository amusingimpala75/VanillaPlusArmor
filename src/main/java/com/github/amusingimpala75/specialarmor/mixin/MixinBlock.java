package com.github.amusingimpala75.specialarmor.mixin;

import com.github.amusingimpala75.specialarmor.item.HoneyChestplate;
import com.github.amusingimpala75.specialarmor.item.SlimeBoots;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(Block.class)
public abstract class MixinBlock extends AbstractBlock implements ItemConvertible {

    public MixinBlock(Settings settings) {
        super(settings);
        System.out.println("We should never see this");
    }

    @Inject(at=@At("HEAD"), method = "onLandedUpon", cancellable = true)
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance, CallbackInfo info) {
        boolean modified = false;
        if (!((Block)(Object)this instanceof AirBlock)) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity)entity).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HoneyChestplate) {
                    entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
                    if (!world.isClient) {
                        world.sendEntityStatus(entity, (byte)54);
                    }
                    if (entity.handleFallDamage(distance, 0.2F)) {
                        entity.playSound(this.soundGroup.getFallSound(), this.soundGroup.getVolume() * 0.5F, this.soundGroup.getPitch() * 0.75F);
                    }
                    modified = true;
                }
                if (((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SlimeBoots) {
                    entity.handleFallDamage(distance, 0.0F);
                    modified = true;
                }
            }
        }
        if (modified) {
            info.cancel();
        }

    }
    @Inject(at=@At("HEAD"), method = "onEntityLand", cancellable = true)
    public void onEntityLand(BlockView world, Entity entity, CallbackInfo info) {
        if (!((Block)(Object)this instanceof AirBlock)) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SlimeBoots) {
                    this.bounce(entity);
                    ((SlimeBoots)(((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).getItem())).damage(entity);
                    info.cancel();
                }
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onSteppedOn")
    public void onSteppedOn(World world, BlockPos pos, Entity entity, CallbackInfo info) {
        if (!((Block)(Object)this instanceof AirBlock)) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SlimeBoots) {
                    double d = Math.abs(entity.getVelocity().y);
                    if (d < 0.1D && !entity.bypassesSteppingEffects()) {
                        double e = 0.4D + d * 0.2D;
                        entity.setVelocity(entity.getVelocity().multiply(e, 1.0D, e));
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!((Block)(Object)this instanceof AirBlock)) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HoneyChestplate) {
                    if (this.isSliding(pos, entity)) {
                        this.triggerAdvancement(entity, pos);
                        this.updateSlidingVelocity(entity);
                        this.addCollisionEffects(world, entity);
                        ((HoneyChestplate)(((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST).getItem())).damage(entity);
                    }
                }
            }
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    //TODO: Fix QOL bounce remover
    private void bounce(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0D) {
            double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
            //if (entity.fallDistance > 3) {
                entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
            //}
        }
    }

    private boolean isSliding(BlockPos pos, Entity entity) {
        if (entity.isOnGround()) {
            return false;
        } else if (entity.getY() > (double)pos.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (entity.getVelocity().y >= -0.08D) {
            return false;
        } else {
            double d = Math.abs((double)pos.getX() + 0.5D - entity.getX());
            double e = Math.abs((double)pos.getZ() + 0.5D - entity.getZ());
            double f = 0.4375D + (double)(entity.getWidth() / 2.0F);
            return d + 1.0E-7D > f || e + 1.0E-7D > f;
        }
    }

    private void triggerAdvancement(Entity entity, BlockPos pos) {
        if (entity instanceof ServerPlayerEntity && entity.world.getTime() % 20L == 0L) {
            Criteria.SLIDE_DOWN_BLOCK.test((ServerPlayerEntity)entity, entity.world.getBlockState(pos));
        }

    }

    private void updateSlidingVelocity(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < -0.13D) {
            double d = -0.05D / vec3d.y;
            entity.setVelocity(new Vec3d(vec3d.x * d, -0.05D, vec3d.z * d));
        } else {
            entity.setVelocity(new Vec3d(vec3d.x, -0.05D, vec3d.z));
        }
        entity.fallDistance = 0.0F;
    }

    private void addCollisionEffects(World world, Entity entity) {
        if (hasHoneyBlockEffects(entity)) {
            if (world.random.nextInt(5) == 0) {
                entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (!world.isClient && world.random.nextInt(5) == 0) {
                world.sendEntityStatus(entity, (byte)53);
            }
        }
    }
    private static boolean hasHoneyBlockEffects(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof AbstractMinecartEntity || entity instanceof TntEntity || entity instanceof BoatEntity;
    }
}
