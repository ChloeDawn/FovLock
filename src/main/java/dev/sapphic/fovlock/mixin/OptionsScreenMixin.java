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
import dev.sapphic.fovlock.gui.FovLockButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
abstract class OptionsScreenMixin extends Screen {
  OptionsScreenMixin() {
    super(null);
  }

  /**
   * Adds the FOV lock button next to the FOV option slider button
   *
   * <pre>{@code
   * // Pseudo implementation
   * this.addButton(option.createButton(this.client.options, x, y, 150));
   * if (FovLock.isFovOption(option) {
   *   this.addButton(new FovLockButton(x + FovLock.BUTTON_OFFSET, y));
   * }
   * }</pre>
   */
  @Inject(
    method = "init",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/options/Option;createButton(Lnet/minecraft/client/options/GameOptions;III)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;",
      shift = At.Shift.BY, by = 2),
    locals = LocalCapture.CAPTURE_FAILHARD,
    require = 1, allow = 1)
  private void addFovLockButton(
    final CallbackInfo ci, final int buttonIndex, final Option[] options,
    final int optionsCount, final int optionIndex, final Option option,
    final int x, final int y
  ) {
    if (FovLock.isFovOption(option)) {
      this.addButton(new FovLockButton(x + FovLock.BUTTON_OFFSET, y));
    }
  }
}
