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
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GameOptionSliderWidget;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
abstract class ScreenMixin extends AbstractParentElement implements Drawable {
  @Inject(method = "addButton", at = @At("HEAD"))
  private void fovlock$setFovSliderWidth(final AbstractButtonWidget button, final CallbackInfoReturnable<ButtonWidget> cir) {
    if (button instanceof GameOptionSliderWidget && Option.FOV == ((SliderButtonOption) button).fovlock$getOption()) {
      button.setWidth(button.getWidth() - FovLock.BUTTON_WIDTH);
    }
  }
}
