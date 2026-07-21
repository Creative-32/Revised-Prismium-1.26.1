package net.s0larj.prismium.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.entity.ModEntityTypes;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import java.util.ArrayList;
import java.util.List;

import static net.s0larj.prismium.attachment.ModAttachments.HOOKED;

public class AnchorProjectileEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> IS_HOOKED =
            SynchedEntityData.defineId(AnchorProjectileEntity.class,EntityDataSerializers.BOOLEAN);

    private Entity hookedEntity;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HOOKED, false);
    }

        public AnchorProjectileEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public AnchorProjectileEntity(Level level, Player player, ItemStack itemStack) {
        super(ModEntityTypes.ANCHOR_PROJECTILE, player, level, itemStack, itemStack);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(2.0);
    }


    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    // Returns the owner only when the owner is a Player.
    public Player getPlayerOwner() {
        Entity owner = this.getOwner();
        if (owner instanceof Player player) {
            return player;
        }

        return null;
    }


    @Override
    public void tick() {
        super.tick();

        // If the anchor is attached to an entity, keep the projectile positioned on that entity.
        if (this.hookedEntity != null) {
            this.setPos(
                    this.hookedEntity.getX(),
                    this.hookedEntity.getY(),
                    this.hookedEntity.getZ()
            );
            this.setRot(
                    this.hookedEntity.getYRot(),
                    this.hookedEntity.getXRot()
            );
        }


         // Remove the anchor if its owner no longer exists.
        Entity owner = this.getOwner();

        if (owner == null) {
            this.removeAttachment();
            this.discard();
            return;
        }

        // Remove the anchor if it gets too far away from its owner.
        if (this.distanceToSqr(owner) > 2048.0D) {
            this.removeAttachment();
            this.discard();
            return;
        }


        // Remove the projectile if the player no longer has the anchor weapon in their inventory.
        if (owner instanceof Player player) {

            ItemStack weapon = this.getWeaponItem();

            // getWeaponItem() can return null, so check for null first.
            if (weapon != null
                    && !weapon.isEmpty()
                    && player.getInventory().findSlotMatchingItem(weapon) == -1) {

                this.removeAttachment();
                this.discard();
                return;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        Entity owner = this.getOwner();

        // Damage and attachment changes should happen on the server.
        if (this.level() instanceof ServerLevel serverLevel) {

            // Hurt the entity that the anchor hit.
            hitEntity.hurtServer(serverLevel,this.damageSources().thrown(this,this.getPlayerOwner()),4.0F);

            //Stop the anchor from continuing through the entity.
            this.setDeltaMovement(Vec3.ZERO);

            //Only attach the anchor if the entity survived the hit.
            if (hitEntity.isAlive()) {

                this.hookedEntity = hitEntity;

                // Synchronize the hooked state to every client.
                this.entityData.set(IS_HOOKED, true);

                // Convert the world hit position into a position relative to the hooked entity.
                Vec3 localHitPosition = this.getLocalHitPosition(hitEntity, hitResult.getLocation());

                // Copy the entity's current anchor attachment list.
                List<AnchorAttachment> attachments = new ArrayList<>(hitEntity.getAttachedOrElse(HOOKED,List.of()));

                // Add this anchor to the entity's attachment list.
                attachments.add(new AnchorAttachment(this.getUUID(),
                                // Hit position relative to the entity.
                                localHitPosition,
                                // Vertical rotation of the projectile.
                                -this.getXRot(),

                                /*
                                 * Horizontal rotation relative to the entity.
                                 *
                                 * Subtracting the entity yaw means the anchor
                                 * continues to face the correct direction when
                                 * the entity turns.
                                 */
                                this.getYRot() - hitEntity.getYRot()
                        )
                );

                // Save the new immutable attachment list.
                hitEntity.setAttached(HOOKED,List.copyOf(attachments));

                // The anchor should no longer fall after attaching.
                this.setNoGravity(true);
            }
        }

        //Tell Minecraft that the owner last attacked this entity.
        if (owner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(hitEntity);
        }
    }


    /*
     * Converts a world-space hit position into a position relative
     * to the entity.
     *
     * World coordinates stay fixed in the level.
     * Local coordinates rotate with the entity.
     */
    private Vec3 getLocalHitPosition(
            Entity entity,
            Vec3 worldHitPosition
    ) {

        // Find the distance from the entity's position to the point where the anchor hit.
        Vec3 worldOffset = worldHitPosition.subtract(entity.position());

        double yawRadians = Math.toRadians(entity.getYRot());

        double cosYaw = Math.cos(yawRadians);
        double sinYaw = Math.sin(yawRadians);

        //Rotate the horizontal X and Z coordinates into the entity's local coordinate system.
        double localX = worldOffset.x * cosYaw - worldOffset.z * sinYaw;
        double localZ = worldOffset.x * sinYaw + worldOffset.z * cosYaw;

        return new Vec3(localX, worldOffset.y, localZ);
    }


    // Pulls the hooked entity toward the owner.
    public void pull() {
        Entity owner = this.getOwner();
        if (owner == null || this.hookedEntity == null) {
            return;
        }

        // Remove this anchor from the hooked entity's rendered attachment list.
        this.removeAttachment();

        // Direction from the hooked entity toward the player.
        Vec3 pullDirection = owner.position().subtract(this.hookedEntity.position());

        // Reduce the strength so the entity moves toward the player instead of instantly reaching the player.
        Vec3 pullVelocity = pullDirection.scale(0.3D);

        // Add the pulling force to the entity's current movement.
        this.hookedEntity.setDeltaMovement(this.hookedEntity.getDeltaMovement().add(pullVelocity));
    }

    // Removes this projectile's attachment from the hooked entity.
    private void removeAttachment() {
        if (this.hookedEntity == null) {
            return;
        }

        List<AnchorAttachment> attachments = new ArrayList<>(this.hookedEntity.getAttachedOrElse(HOOKED,List.of()));

        // Remove the attachment that has this projectile's UUID.
        attachments.removeIf(attachment -> attachment.uuid().equals(this.getUUID()));

        this.hookedEntity.setAttached(HOOKED, List.copyOf(attachments));

        // Tell the client this projectile is no longer attached.
        this.entityData.set(IS_HOOKED, false);
    }

    // Returns true after the projectile has attached to an entity.
    public boolean isHooked() {
        return this.entityData.get(IS_HOOKED);
    }
}
