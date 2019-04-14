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

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Properties;

public final class FovLock {
  public static final int BUTTON_WIDTH = 20;
  public static final int SLIDER_WIDTH = 150;
  public static final float NULL_MODIFIER = 1.0F;

  private static final String FILE = "fovlock.txt";
  private static final String PROPERTY = "enabled";

  private static boolean enabled = true;
  private static boolean loaded = false;

  private FovLock() {
    throw new UnsupportedOperationException();
  }

  public static boolean isEnabled() {
    return enabled;
  }

  public static void setEnabled(final boolean value) {
    enabled = value;
    saveState();
  }

  public static void loadState() {
    if (loaded) throw new UnsupportedOperationException();
    final Properties properties = new Properties();
    try (final InputStream input = Files.newInputStream(Paths.get(FILE))) {
      properties.load(input);
    } catch (final NoSuchFileException ignored) {
      // first run, state has never been set at runtime
    } catch (final IOException exception) {
      throw new IllegalStateException("Unable to load state", exception);
    }
    @Nullable final String property = properties.getProperty(PROPERTY);
    enabled = property == null || "true".equalsIgnoreCase(property);
    loaded = true;
  }

  private static void saveState() {
    if (!loaded) throw new UnsupportedOperationException();
    final Properties properties = new Properties();
    properties.setProperty(PROPERTY, Boolean.toString(isEnabled()));
    try (final OutputStream output = Files.newOutputStream(Paths.get(FILE))) {
      properties.store(output, null);
    } catch (final IOException exception) {
      throw new IllegalStateException("Unable to save state", exception);
    }
  }
}
