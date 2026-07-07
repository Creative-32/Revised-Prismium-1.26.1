package net.s0larj.prismium.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.s0larj.prismium.data.ModDataComponents;
import net.s0larj.prismium.entity.custom.AnchorProjectileEntity;
import org.jspecify.annotations.NonNull;

public class AnchorItem extends Item {

    public AnchorItem(Properties properties) {
        super(properties);
    }

    public AnchorProjectileEntity anchorProjectile;

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5f, 0.4f);

        if (!level.isClientSide()) {
            if (itemStack.get(ModDataComponents.ANC) != null) {
                AnchorProjectileEntity entity = (AnchorProjectileEntity) level.getEntity(itemStack.get(ModDataComponents.ANC));
                if(entity != null ) {
                    entity.pull();
                    entity.discard();
                    itemStack.set(ModDataComponents.ANC, null);
                    return InteractionResult.SUCCESS;
                }
            }
            anchorProjectile = new AnchorProjectileEntity(level, player, itemStack);
            itemStack.set(ModDataComponents.ANC, anchorProjectile.getUUID());
            anchorProjectile.getWeaponItem().set(ModDataComponents.ANC, anchorProjectile.getUUID());
            anchorProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0F, 1.0F);
            level.addFreshEntity(anchorProjectile);
        }
        return InteractionResult.SUCCESS;
    }


}
