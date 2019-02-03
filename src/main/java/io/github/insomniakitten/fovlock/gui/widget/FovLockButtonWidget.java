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

package io.github.insomniakitten.fovlock.gui.widget;

import io.github.insomniakitten.fovlock.FovLock;
import net.minecraft.client.gui.widget.LockButtonWidget;

public final class FovLockButtonWidget extends LockButtonWidget {
  public FovLockButtonWidget(final int x, final int y) {
    super(400, x, y);
    setLocked(FovLock.isEnabled());
  }

  @Override
  public void onPressed(final double mouseX, final double mouseY) {
    final boolean state = !isLocked();
    FovLock.setEnabled(state);
    setLocked(state);
  }
}
