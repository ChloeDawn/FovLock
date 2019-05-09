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
import io.github.insomniakitten.fovlock.gui.widget.FovLockButtonWidget;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.SettingsScreen;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SettingsScreen.class)
abstract class OptionsScreenMixin extends Screen {
  private OptionsScreenMixin() {
    super(null);
    throw new AssertionError();
  }

  @Inject(
    method = "init",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/options/Option;createButton(Lnet/minecraft/client/options/GameOptions;III)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;"
    ),
    locals = LocalCapture.CAPTURE_FAILHARD,
    allow = 1
  )
  private void fovlock$addFovLockButton(final CallbackInfo ci, final int buttonIndex, final Option[] options, final int optionsCount, final int optionIndex, final Option option) {
    if (Option.FOV == option) {
      final int offset = FovLock.SLIDER_WIDTH - FovLock.BUTTON_WIDTH;
      final int x = this.width / 2 - 155 + buttonIndex % 2 * 160 + offset;
      final int y = this.height / 6 - 12 + 24 * (buttonIndex >> 1);
      this.addButton(new FovLockButtonWidget(x, y));
    }
  }
}
