/*
 * Copyright (C) 2019 Chloe Dawn
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

package io.github.chloedawn.fovlock.mixin;

import io.github.chloedawn.fovlock.FovLock;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.SynchronousResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin implements AutoCloseable, SynchronousResourceReloadListener {
  @Inject(
    method = "getFov",
    at = @At(
      value = "FIELD",
      target = "Lnet/minecraft/client/render/GameRenderer;lastMovementFovMultiplier:F"),
    locals = LocalCapture.CAPTURE_FAILHARD,
    cancellable = true,
    allow = 1)
  private void fovlock$skipFovMultiplication(final Camera camera, final float delta, final boolean viewOnly, final CallbackInfoReturnable<Double> cir, final double fov) {
    if (FovLock.isEnabled()) cir.setReturnValue(fov);
  }
}
