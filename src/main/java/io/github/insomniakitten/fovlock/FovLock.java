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

package io.github.insomniakitten.fovlock;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


@Environment(EnvType.CLIENT)
public final class FovLock {
  public static final int BUTTON_WIDTH = 20;
  public static final int SLIDER_WIDTH = 150;
  public static final float NULL_MODIFIER = 1.0F;

  private static final String STATE = "fovlock.txt";
  private static final String KEY = "enabled";

  private static boolean enabled = true;
  private static boolean loaded = false;

  private FovLock() {
    throw new UnsupportedOperationException();
  }

  public static boolean isEnabled() {
    return enabled;
  }

  public static synchronized void setEnabled(final boolean value) {
    enabled = value;
    saveState();
  }

  @Deprecated
  @SneakyThrows(IOException.class)
  public static synchronized void loadState() {
    if (loaded) {
      throw new UnsupportedOperationException("Already loaded");
    }
    final Path stateFile = Paths.get(STATE);
    if (Files.notExists(stateFile)) {
      Files.createFile(stateFile);
    }
    final Properties properties = new Properties();
    try (final Reader reader = Files.newBufferedReader(stateFile)) {
      properties.load(reader);
    }
    @Nullable final Object property = properties.getOrDefault(KEY, "true");
    Preconditions.checkState(property instanceof String, property);
    enabled = "true".equalsIgnoreCase((String) property);
    loaded = true;
  }

  @SneakyThrows(IOException.class)
  private static synchronized void saveState() {
    if (!loaded) {
      throw new IllegalStateException("Nothing to save");
    }
    final Path stateFile = Paths.get(STATE);
    if (Files.notExists(stateFile)) {
      Files.createFile(stateFile);
    }
    final Properties properties = new Properties();
    properties.put(KEY, Boolean.toString(enabled));
    try (final Writer writer = Files.newBufferedWriter(stateFile)) {
      properties.store(writer, null);
    }
  }
}
