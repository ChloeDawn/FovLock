/*
 * Copyright (C) 2019 InsomniaKitten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.insomniakitten.fovlock.mixin;

import io.github.insomniakitten.fovlock.FovLock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.SynchronousResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
abstract class GameRendererMixin implements AutoCloseable, SynchronousResourceReloadListener {
  @Shadow private float movementFovMultiplier;
  @Shadow private float lastMovementFovMultiplier;

  private GameRendererMixin() {
    throw new AssertionError();
  }

  @Inject(
    method = "tick",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/render/GameRenderer;updateMovementFovMultiplier()V",
      shift = Shift.AFTER
    )
  )
  private void fovlock$resetFovMultipliers(final CallbackInfo ci) {
    if (FovLock.isEnabled()) {
      this.movementFovMultiplier = FovLock.NULL_MODIFIER;
      this.lastMovementFovMultiplier = FovLock.NULL_MODIFIER;
    }
  }
}
