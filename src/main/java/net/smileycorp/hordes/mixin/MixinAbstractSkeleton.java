package net.smileycorp.hordes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.smileycorp.hordes.common.ConfigHandler;

@Mixin(AbstractSkeleton.class)
public abstract class MixinAbstractSkeleton extends EntityMob implements IRangedAttackMob {

	public MixinAbstractSkeleton(World world) {
		super(world);
	}

	@Inject(at=@At("HEAD"), method = "onLivingUpdate()V", cancellable = true)
	public void onLivingUpdate(CallbackInfo callback) {
		if (!ConfigHandler.skeletonsBurn) {
			super.onLivingUpdate();
			callback.cancel();
		}
	}

}
