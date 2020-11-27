package com.github.amusingimpala75.specialarmor.client;

import com.github.amusingimpala75.specialarmor.SpecialArmor;
import com.github.amusingimpala75.specialarmor.item.HoneyChestplate;
import com.github.amusingimpala75.specialarmor.item.SlimeBoots;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class HoneyChestplateFeatureRenderer<T extends LivingEntity, A extends BipedEntityModel<T>> extends FeatureRenderer<T, A> {
    private final A bodyModel;

    public HoneyChestplateFeatureRenderer(FeatureRendererContext<T, A> context, A bodyModel) {
        super(context);
        this.bodyModel = bodyModel;
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() instanceof HoneyChestplate) {
            (this.getContextModel()).setAttributes(this.bodyModel);
            this.bodyModel.setVisible(false);
            this.bodyModel.torso.visible = true;
            this.bodyModel.rightArm.visible = true;
            this.bodyModel.leftArm.visible = true;
            boolean bl2 = itemStack.hasGlint();
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(SpecialArmor.id("textures/models/armor/honey_chestplate.png")), false, bl2);
            this.bodyModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}