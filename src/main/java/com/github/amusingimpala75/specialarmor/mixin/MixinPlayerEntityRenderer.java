package com.github.amusingimpala75.specialarmor.mixin;

import com.github.amusingimpala75.specialarmor.client.HoneyChestplateFeatureRenderer;
import com.github.amusingimpala75.specialarmor.client.IceBootsFeatureRenderer;
import com.github.amusingimpala75.specialarmor.client.SlimeBootsFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
@SuppressWarnings({"unchecked", "rawtypes"})
@Environment(EnvType.CLIENT)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public MixinPlayerEntityRenderer(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(dispatcher, model, shadowRadius);
        System.out.println("This should never be called");
    }

    //TODO: Figure out how to lower to more reasonable amt. w/o disappearing entirely
    @Inject(at=@At("TAIL"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V")
    public void addCustomRenderers(CallbackInfo info) {
        this.addFeature(new SlimeBootsFeatureRenderer(((PlayerEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
        this.addFeature(new HoneyChestplateFeatureRenderer(((PlayerEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
        this.addFeature(new IceBootsFeatureRenderer(((PlayerEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
    }
}
