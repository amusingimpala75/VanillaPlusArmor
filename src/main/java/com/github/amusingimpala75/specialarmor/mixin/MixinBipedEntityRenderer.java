package com.github.amusingimpala75.specialarmor.mixin;

import com.github.amusingimpala75.specialarmor.client.HoneyChestplateFeatureRenderer;
import com.github.amusingimpala75.specialarmor.client.IceBootsFeatureRenderer;
import com.github.amusingimpala75.specialarmor.client.SlimeBootsFeatureRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityRenderer.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class MixinBipedEntityRenderer<T extends MobEntity, M extends BipedEntityModel<T>> extends MobEntityRenderer<T, M> {
    public MixinBipedEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, M entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
        System.out.println("This should never be printed!");
    }
    @Inject(at=@At("TAIL"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Lnet/minecraft/client/render/entity/model/BipedEntityModel;FFFF)V")
    public void addCustomRenderers(CallbackInfo info) {
        this.addFeature(new SlimeBootsFeatureRenderer(((BipedEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
        this.addFeature(new HoneyChestplateFeatureRenderer(((BipedEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
        this.addFeature(new IceBootsFeatureRenderer(((BipedEntityRenderer)(Object)this), new BipedEntityModel(1.4F)));
    }
}
