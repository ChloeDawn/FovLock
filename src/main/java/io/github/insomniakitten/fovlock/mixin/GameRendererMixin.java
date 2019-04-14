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
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
final class GameRendererMixin {
  private GameRendererMixin() {
    throw new UnsupportedOperationException();
  }

  @ModifyVariable(
    method = "updateMovementFovMultiplier",
    at = @At(
      value = "INVOKE_ASSIGN",
      target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;method_3118()F"
    ),
    ordinal = 0,
    allow = 1
  )
  private float fovlock$modifyFovModifier(final float fovModifier) {
    return FovLock.isEnabled() ? FovLock.NULL_MODIFIER : fovModifier;
  }
}
