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

package io.github.chloedawn.fovlock;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class FovLock {
  public static final int BUTTON_WIDTH = 20;
  public static final int SLIDER_WIDTH = 150;
  public static final int BUTTON_OFFSET = SLIDER_WIDTH - BUTTON_WIDTH;

  private static final Logger LOGGER = LogManager.getLogger("FovLock");
  private static final Path FOVLOCK_TXT = Paths.get("fovlock.txt");
  private static final String ENABLED = "enabled";

  private static boolean loaded = false;
  private static boolean enabled = true;

  private FovLock() {
  }

  @Contract(pure = true)
  public static boolean isEnabled() {
    Preconditions.checkState(loaded, "Not loaded");
    return enabled;
  }

  public static void setEnabled(final boolean value) {
    Preconditions.checkState(loaded, "Not loaded");
    Preconditions.checkState(enabled != value, value ? "Already enabled" : "Already disabled");
    enabled = value;
    writeProperties(FOVLOCK_TXT);
  }

  @Deprecated
  public static void load() {
    Preconditions.checkState(!loaded, "Already loaded");
    readProperties(FOVLOCK_TXT);
    loaded = true;
  }

  private static void readProperties(final Path file) {
    LOGGER.debug("Reading properties from {}", file);

    final Properties properties = new Properties();

    try (final Reader reader = Files.newBufferedReader(file)) {
      properties.load(reader);
    } catch (final NoSuchFileException e) {
      writeProperties(file);
    } catch (final IOException e) {
      throw new RuntimeException("Reading properties from " + file, e);
    } catch (final IllegalArgumentException e) {
      LOGGER.error("Malformed properties in {}", file, e);
      writeProperties(file);
    }

    enabled = Boolean.parseBoolean(properties.getProperty(ENABLED, "true"));
  }

  private static void writeProperties(final Path file) {
    LOGGER.debug("Writing properties to {}", file);

    final Properties properties = new Properties();

    properties.setProperty(ENABLED, Boolean.toString(enabled));

    try (final Writer writer = Files.newBufferedWriter(file)) {
      properties.store(writer, null);
    } catch (final IOException e) {
      throw new RuntimeException("Writing properties to " + file, e);
    }
  }
}
