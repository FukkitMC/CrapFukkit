package io.github.fukkitmc.mixin.crash;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Entity.class)
public abstract class EntityRotationMixin {
	@Shadow public World world;
	@Shadow public abstract String getEntityName();

	@Shadow public float pitch;

	@ModifyArgs(method = "setRotation", at = @At("HEAD"))
	private void crapfukkit_crashRotation(Args args) {
		float yaw = args.get(0);
		if(Float.isNaN(yaw))
			args.set(0, 0);

		if(yaw == Float.POSITIVE_INFINITY || yaw == Float.NEGATIVE_INFINITY) {
			if(((Object)this) instanceof ServerPlayerEntity) {
				this.world.getServer().logError(this.getEntityName() + " was caught trying to crash the server with an invalid yaw");
				ServerPlayerEntity entity = (ServerPlayerEntity) (Object)this;
				entity.networkHandler.disconnect(new LiteralText("Infinite yaw (Hacking?)"));
			}
			args.set(0, 0);
		}

		float pitch = args.get(1);
		if(Float.isNaN(pitch))
			args.set(1, 0);
		if(pitch == Float.POSITIVE_INFINITY || pitch == Float.NEGATIVE_INFINITY) {
			if(((Object)this) instanceof ServerPlayerEntity) {
				this.world.getServer().logError(this.getEntityName() + " was caught trying to crash the server with an invalid pitch");
				ServerPlayerEntity entity = (ServerPlayerEntity) (Object)this;
				entity.networkHandler.disconnect(new LiteralText("Infinite pitch (Hacking?)"));
			}
			args.set(1, 0);
		}
	}
}
