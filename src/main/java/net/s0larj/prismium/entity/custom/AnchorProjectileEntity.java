package net.s0larj.prismium.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.entity.ModEntityTypes;
import org.joml.Vector2f;

public class AnchorProjectileEntity extends AbstractArrow {

    private Entity hookedEntity;
    public Vector2f groundedOffset;

    public AnchorProjectileEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public AnchorProjectileEntity(Level level, Player player, ItemStack itemStack) {
        super(ModEntityTypes.ANCHOR_PROJECTILE, player, level, itemStack, itemStack);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public ItemStack getWeaponItem() {
        return super.getWeaponItem();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;
    }

    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }

    public void tick() {
        super.tick();

        if (this.hookedEntity != null){
            this.setPos(this.hookedEntity.getX(), this.hookedEntity.getEyeY(), this.hookedEntity.getZ());
            this.setRot(this.hookedEntity.getYRot(), this.hookedEntity.getXRot());
        }

        if (this.getOwner() == null){
            this.discard();
            return;
        }

        if (this.distanceToSqr(this.getOwner()) > 1024.0){
            this.discard();
            return;
        }

        if (this.getOwner() instanceof Player && this.getWeaponItem() != null) {
            if ( ((Player) this.getOwner()).getInventory().findSlotMatchingItem(this.getWeaponItem()) == -1 ){
                this.discard();
                return;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();

        if(!this.level().isClientSide()) {
            //hurt entity on the server
            entity.hurtServer((ServerLevel) this.level(), this.damageSources().thrown(this, this.getPlayerOwner()), 4);
            //tell the client
            entity.hurtClient(this.damageSources().thrown(this, this.getPlayerOwner()));
            //stop the anchor
            this.setDeltaMovement(this.getDeltaMovement().scale(0));

            //set hooked to mob that was hit
            if(entity.isAlive()){
                this.hookedEntity = entity;
            }
        }
    }

    public void pull(){
        Entity player = this.getOwner();
        if (this.hookedEntity != null) {
            Vec3 vec3d = new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ()).scale(0.3);
            this.hookedEntity.setDeltaMovement(this.hookedEntity.getDeltaMovement().add(vec3d));
        }
    }

    public boolean isHooked(){
        return this.hookedEntity != null;
    }

}
