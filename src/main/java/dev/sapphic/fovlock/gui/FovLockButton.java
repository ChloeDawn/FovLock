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

package dev.sapphic.fovlock.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sapphic.fovlock.FovLock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FovLockButton extends ButtonWidget {
  private boolean locked = FovLock.isEnabled();

  public FovLockButton(final int x, final int y) {
    super(x, y, 20, 20, I18n.translate("narrator.button.fovlock"), FovLockButton::pressed);
  }

  private static void pressed(final ButtonWidget button) {
    final boolean locked = !((FovLockButton) button).locked;
    ((FovLockButton) button).locked = locked;
    FovLock.setEnabled(locked);
  }

  @Override
  protected String getNarrationMessage() {
    return super.getNarrationMessage() + ". " + I18n.translate("narrator.button.fovlock." + (this.locked ? "locked" : "unlocked"));
  }

  @Override
  public void renderButton(final int x, final int y, final float delta) {
    MinecraftClient.getInstance().getTextureManager().bindTexture(ButtonWidget.WIDGETS_LOCATION);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    final Icon icon;
    if (!this.active) {
      icon = this.locked ? Icon.LOCKED_DISABLED : Icon.UNLOCKED_DISABLED;
    } else if (this.isHovered()) {
      icon = this.locked ? Icon.LOCKED_HOVER : Icon.UNLOCKED_HOVER;
    } else {
      icon = this.locked ? Icon.LOCKED : Icon.UNLOCKED;
    }
    this.blit(this.x, this.y, icon.u, icon.v, this.width, this.height);
  }

  @Override
  public String toString() {
    return "FovLockButton(locked=" + this.locked + ')';
  }

  private enum Icon {
    LOCKED(0, 146),
    LOCKED_HOVER(0, 166),
    LOCKED_DISABLED(0, 186),
    UNLOCKED(20, 146),
    UNLOCKED_HOVER(20, 166),
    UNLOCKED_DISABLED(20, 186);

    private final int u;
    private final int v;

    Icon(final int u, final int v) {
      this.u = u;
      this.v = v;
    }
  }
}
