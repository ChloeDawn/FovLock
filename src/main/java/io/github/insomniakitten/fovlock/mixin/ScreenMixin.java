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
import io.github.insomniakitten.fovlock.mixin.hook.SliderWidgetAccessor;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.options.GameOptions.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
final class ScreenMixin {
  private ScreenMixin() {}

  @Inject(method = "addButton", at = @At("HEAD"))
  private void setFovSliderWidth(final ButtonWidget widget, final CallbackInfoReturnable<ButtonWidget> cir) {
    if (widget instanceof OptionSliderWidget && Option.FOV == ((SliderWidgetAccessor) widget).getOption()) {
      widget.setWidth(widget.getWidth() - FovLock.BUTTON_WIDTH);
    }
  }
}
