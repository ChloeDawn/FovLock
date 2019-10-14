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

package io.github.chloedawn.fovlock.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.chloedawn.fovlock.FovLock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.Contract;

import java.util.Locale;

public final class FovLockButton extends ButtonWidget {
  private boolean locked = FovLock.isEnabled();

  public FovLockButton(final int x, final int y) {
    super(x, y, 20, 20, I18n.translate("narrator.button.fovlock"), FovLockButton::pressed);
  }

  private static void pressed(final ButtonWidget button) {
    final FovLockButton lock = (FovLockButton) button;
    lock.setLocked(!lock.locked);
  }

  @Contract(pure = true)
  public boolean isLocked() {
    return this.locked;
  }

  @Contract(mutates = "this")
  public void setLocked(final boolean locked) {
    this.locked = locked;
    FovLock.setEnabled(locked);
  }

  @Override
  public String toString() {
    return String.format(Locale.ROOT, "FovLockButton(locked=%s)", this.locked);
  }

  @Override
  protected String getNarrationMessage() {
    return super.getNarrationMessage() + ". " + I18n.translate("narrator.button.fovlock." + (this.locked ? "locked" : "unlocked"));
  }

  @Override
  public void renderButton(final int x, final int y, final float delta) {
    MinecraftClient.getInstance().getTextureManager().bindTexture(ButtonWidget.WIDGETS_LOCATION);
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    final Icon icon;
    if (!this.active) {
      icon = this.locked ? Icon.LOCKED_DISABLED : Icon.UNLOCKED_DISABLED;
    } else if (this.isHovered()) {
      icon = this.locked ? Icon.LOCKED_HOVER : Icon.UNLOCKED_HOVER;
    } else {
      icon = this.locked ? Icon.LOCKED : Icon.UNLOCKED;
    }
    this.blit(this.x, this.y, icon.getU(), icon.getV(), this.width, this.height);
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

    @Contract(pure = true)
    Icon(final int u, final int v) {
      this.u = u;
      this.v = v;
    }

    @Contract(pure = true)
    public final int getU() {
      return this.u;
    }

    @Contract(pure = true)
    public final int getV() {
      return this.v;
    }
  }
}
