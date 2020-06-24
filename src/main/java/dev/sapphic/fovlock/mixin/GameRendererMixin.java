/*
 * Copyright (C) 2020 Chloe Dawn
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

package dev.sapphic.fovlock.mixin;

import dev.sapphic.fovlock.FovLock;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin implements SynchronousResourceReloadListener {
  @Shadow private float lastMovementFovMultiplier;
  @Shadow private float movementFovMultiplier;

  /**
   * Negates movement based FOV multiplication when FOV is locked
   *
   * <pre>{@code
   * // Pseudo implementation
   * fov *= MathHelper.lerp(tickDelta, this.lastMovementFovMultiplier, this.movementFovMultiplier);
   * if (FovLock.isEnabled()) {
   *   fov /= MathHelper.lerp(tickDelta, this.lastMovementFovMultiplier, this.movementFovMultiplier);
   * }
   * }</pre>
   */
  @ModifyVariable(method = "getFov", at = @At(
    value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F",
    shift = Shift.BY, by = 4 // INVOKESTATIC, F2D, DMUL, DSTORE 4
  ), allow = 1, require = 1)
  private double negateMultiplier(final double fov, final Camera camera, final float tickDelta) {
    if (FovLock.isEnabled()) {
      return fov / MathHelper.lerp(tickDelta, this.lastMovementFovMultiplier, this.movementFovMultiplier);
    }
    return fov;
  }

  /**
   * Prevents FOV reduction in non-empty fluids when FOV is locked
   *
   * <pre>{@code
   * // Pseudo implementation
   * FluidState state = camera.getSubmergedFluidState();
   * if (FovLock.isEnabled() && !state.isEmpty()) {
   *   state = Fluids.EMPTY.getDefaultState();
   * }
   * if (!state.isEmpty())
   * }</pre>
   */
  @ModifyVariable(method = "getFov", at = @At(
    value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isEmpty()Z", shift = Shift.BEFORE
  ), allow = 1, require = 1)
  private FluidState emptyFluidState(final FluidState state) {
    return (FovLock.isEnabled() && !state.isEmpty()) ? Fluids.EMPTY.getDefaultState() : state;
  }
}
