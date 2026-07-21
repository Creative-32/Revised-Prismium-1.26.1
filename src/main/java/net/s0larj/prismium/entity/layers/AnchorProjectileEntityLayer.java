package net.s0larj.prismium.entity.layers;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.entity.ModCustomEntityClient;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class AnchorProjectileEntityLayer<M extends LivingEntityRenderState> extends ProjectileEntityLayer<M, AnchorProjectileEntityRenderState> {

    public AnchorProjectileEntityLayer(final LivingEntityRenderer<?, M, EntityModel<EntityRenderState>> renderer, final EntityRendererProvider.Context context) {
        super(renderer, new AnchorProjectileEntityModel(context.bakeLayer(ModEntityModelLayers.ANCHOR_PROJECTILE)), new AnchorProjectileEntityRenderState(), Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "textures/entity/anchor_projectile.png"), PlacementStyle.ON_SURFACE);
    }

    @Override
    protected List<AnchorAttachment> numStuck(LivingEntityRenderState state) {
        return Optionull.mapOrDefault(state.getData(ModCustomEntityClient.ANCHOR_KEY), Function.identity(), List.of());
    }
}
