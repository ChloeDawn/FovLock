/*
 * Copyright (C) 2018 InsomniaKitten
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
import io.github.insomniakitten.fovlock.mixin.hook.ScreenAccessor;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.SettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SettingsScreen.class)
@SuppressWarnings("ConstantConditions")
final class OptionsScreenMixin {
  private OptionsScreenMixin() {}

  @Inject(
    method = "onInitialized",
    at = @At(
      value = "NEW",
      target = "net/minecraft/client/gui/widget/OptionSliderWidget"
    ),
    locals = LocalCapture.CAPTURE_FAILHARD,
    allow = 1
  )
  private void addFovLockButton(final CallbackInfo ci, final int buttonIndex) {
    final int offset = FovLock.SLIDER_WIDTH - FovLock.BUTTON_WIDTH;
    final int x = ((Screen) (Object) this).width / 2 - 155 + buttonIndex % 2 * 160 + offset;
    final int y = ((Screen) (Object) this).height / 6 - 12 + 24 * (buttonIndex >> 1);
    ((ScreenAccessor) (Object) this).callAddButton(new FovLockButtonWidget(x, y));
  }
}
