package net.s0larj.prismium.entity.layers;

import com.google.common.collect.Collections2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.entity.ModCustomEntityClient;
import net.s0larj.prismium.mixin.ModelPartAccessorMixin;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public abstract class ProjectileEntityLayer<M extends LivingEntityRenderState, S> extends RenderLayer<M, EntityModel<EntityRenderState>> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;
    private final PlacementStyle placementStyle;

    public ProjectileEntityLayer(final LivingEntityRenderer<?, M, EntityModel<EntityRenderState>> renderer, final Model<S> model, final S modelState, final Identifier texture, final PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    protected abstract List<AnchorAttachment> numStuck(final LivingEntityRenderState state);

    private void submitStuckItem(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final float directionX, final float directionY, final float directionZ, final int outlineColor) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2((double)directionX, (double)directionZ) * (double)(180F / (float)Math.PI));
        float xRot = (float)(Math.atan2((double)directionY, (double)directionXZ) * (double)(180F / (float)Math.PI));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final LivingEntityRenderState state, final float yRot, final float xRot) {
        List<AnchorAttachment> projectiles = this.numStuck(state);
        if (!projectiles.isEmpty()) {
            RandomSource random = RandomSource.createThreadLocalInstance(Objects.requireNonNull(state.getData(ModCustomEntityClient.ENTITY_ID)));

            for (var projectile:projectiles) {
                /*
                poseStack.pushPose();
                Vec3 projPos = projectile.pos().scale(16);
                ModelPart closestPart = Util.getRandom(List.copyOf(Collections2.filter(this.getParentModel().allParts(), part -> !part.isEmpty())), random);
                ModelPart.Cube closestCube = closestPart.getRandomCube(random);
                Vec3 closestCubeVec3 = new Vec3(closestCube.minX , closestCube.minY, closestCube.minZ);
                Vec3 closestPartVec3 = new Vec3(closestPart.x, closestPart.y, closestPart.z);

                for (var modelPart:List.copyOf(Collections2.filter(this.getParentModel().allParts(), part -> !part.isEmpty()))) {
                    Vec3 modelVec3 = new Vec3(modelPart.x, modelPart.y, modelPart.z);
                    for (var cube:((ModelPartAccessorMixin) (Object) modelPart).prismium$getCubes()) {

                        Vec3 cubeVec3 = new Vec3(cube.minX, cube.minY, cube.minZ);

                        if(projPos.closerThan(cubeVec3.subtract(modelVec3), projPos.distanceToSqr(closestCubeVec3.add(closestPartVec3)))){
                            closestCube = cube;
                            closestCubeVec3 = cubeVec3;
                            closestPart = modelPart;
                            closestPartVec3 = modelVec3;
                        }
                    }

                }
                projPos = projPos.add(closestCubeVec3);
                projPos = new Vec3(projPos.x / 16.0F, projPos.y / 16.0F, projPos.z / 16.0F);
                closestPart.translateAndRotate(poseStack);
                poseStack.translate(Mth.lerp(projPos.x(), closestCube.minX, closestCube.maxX) / 16.0F, Mth.lerp(projPos.y(), closestCube.minY, closestCube.maxY) / 16.0F, Mth.lerp(projPos.z(), closestCube.minZ, closestCube.maxZ) / 16.0F);
                //poseStack.translate(projectile.pos().x() ,projectile.pos().y() , projectile.pos().z());
                this.submitStuckItem(poseStack, submitNodeCollector, lightCoords, 0, 0, 0, state.outlineColor);
                poseStack.popPose();

                 */

                poseStack.pushPose();
                ModelPart modelPart = Util.getRandom(List.copyOf(Collections2.filter(this.getParentModel().allParts(), part -> !part.isEmpty())), random);
                ModelPart.Cube cube = modelPart.getRandomCube(random);
                modelPart.translateAndRotate(poseStack);
                float midX = random.nextFloat();
                float midY = random.nextFloat();
                float midZ = random.nextFloat();
                if (this.placementStyle == ProjectileEntityLayer.PlacementStyle.ON_SURFACE) {
                    int plane = random.nextInt(3);
                    switch (plane) {
                        case 0:
                            midX = snapToFace(midX);
                            break;
                        case 1:
                            midY = snapToFace(midY);
                            break;
                        default:
                            midZ = snapToFace(midZ);
                    }
                }

                poseStack.translate(
                        Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F, Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F, Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F
                );
                this.submitStuckItem(poseStack, submitNodeCollector, lightCoords, -(midX * 2.0F - 1.0F), -(midY * 2.0F - 1.0F), -(midZ * 2.0F - 1.0F), state.outlineColor);
                poseStack.popPose();
            }
        }
    }

    private static float snapToFace(final float value) {
        return value > 0.5F ? 1.0F : 0.5F;
    }

    @Environment(EnvType.CLIENT)
    public static enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE;
    }
}
