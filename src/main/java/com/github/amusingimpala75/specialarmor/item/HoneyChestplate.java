package com.github.amusingimpala75.specialarmor.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class HoneyChestplate extends Item implements Wearable {
    public HoneyChestplate() {
        super(new FabricItemSettings().equipmentSlot(item -> EquipmentSlot.CHEST).maxDamage(250).group(ItemGroup.COMBAT));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem().equals(Items.HONEY_BOTTLE) || super.canRepair(stack, ingredient);
    }

    public void damage(Entity entity) {
        if (entity instanceof LivingEntity) {
            if ((new Random()).nextInt(100) == 99) {
                ((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST).damage(1, ((LivingEntity) entity), p -> p.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
            }
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
            user.equipStack(EquipmentSlot.CHEST, user.getStackInHand(hand).copy());
            user.setStackInHand(hand, ItemStack.EMPTY);
        }
        return super.use(world, user, hand);
    }
}
