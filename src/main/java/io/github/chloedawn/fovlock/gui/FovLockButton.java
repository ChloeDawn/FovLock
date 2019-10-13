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

import io.github.chloedawn.fovlock.FovLock;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.LockButtonWidget;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.Locale;

public final class FovLockButton extends LockButtonWidget {
  public FovLockButton(final int x, final int y) {
    super(x, y, FovLockButton::pressed);
    super.setLocked(FovLock.isEnabled());
  }

  @Contract(mutates = "param")
  private static void pressed(final ButtonWidget button) {
    final LockButtonWidget lock = (LockButtonWidget) button;
    lock.setLocked(!lock.isLocked());
  }

  @Override
  @Contract(mutates = "this")
  public void setLocked(final boolean locked) {
    super.setLocked(locked);
    FovLock.setEnabled(locked);
  }

  @Override
  @Nonnull
  public String toString() {
    return String.format(Locale.ROOT, "FovLockButton(locked=%s)", this.isLocked());
  }
}
