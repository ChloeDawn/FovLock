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
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Screen.class)
abstract class ScreenMixin extends AbstractParentElement implements TickableElement, Drawable {
  /**
   * Adjusts the width of the FOV option slider button to account for the FOV lock button
   *
   * <pre>{@code
   * // Pseudo implementation
   * if (FovLock.isFovSlider(button)) {
   *   button.setWidth(button.getWidth() - FovLock.BUTTON_WIDTH);
   * }
   * this.buttons.add(button);
   * return this.addChild(button);
   * }</pre>
   */
  @ModifyVariable(method = "addButton", at = @At("HEAD"), require = 1)
  private <T extends AbstractButtonWidget> T setFovSliderWidth(final T button) {
    if (FovLock.isFovSlider(button)) {
      button.setWidth(button.getWidth() - FovLock.BUTTON_WIDTH);
    }
    return button;
  }
}
