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

package dev.sapphic.fovlock;

import dev.sapphic.fovlock.mixin.SliderButtonAccessor;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class FovLock {
  public static final String NAMESPACE = "fovlock";

  public static final int BUTTON_WIDTH = 20;
  public static final int SLIDER_WIDTH = 150;
  public static final int BUTTON_OFFSET = SLIDER_WIDTH - BUTTON_WIDTH;

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Path FILE = Paths.get(NAMESPACE + ".txt");
  private static final String ENABLED = "enabled";

  private static boolean enabled = true;

  private FovLock() {
    throw new UnsupportedOperationException();
  }

  public static boolean isEnabled() {
    return enabled;
  }

  public static void setEnabled(final boolean enabled) {
    if (FovLock.enabled != enabled) {
      FovLock.enabled = enabled;
      writeProperties();
    }
  }

  @ApiStatus.Internal
  public static void init() {
    readProperties();
  }

  @ApiStatus.Internal
  public static boolean isFovSlider(final AbstractButtonWidget button) {
    return (button instanceof SliderButtonAccessor) && isFovOption(((SliderButtonAccessor) button).getOption());
  }

  @ApiStatus.Internal
  public static boolean isFovOption(final Option option) {
    return option == Option.FOV;
  }

  private static void readProperties() {
    LOGGER.debug("Reading properties from {}", FILE);

    final Properties properties = new Properties();

    try (final Reader reader = Files.newBufferedReader(FILE)) {
      properties.load(reader);
    } catch (final NoSuchFileException e) {
      writeProperties();
    } catch (final IOException e) {
      throw new IllegalStateException("Unable to read properties from " + FILE, e);
    } catch (final IllegalArgumentException e) {
      LOGGER.error("Malformed properties in {}", FILE, e);
      writeProperties();
    }

    parseProperties(properties);
  }

  private static void parseProperties(final Properties properties) {
    enabled = Boolean.parseBoolean(properties.getProperty(ENABLED, "true"));
  }

  private static void writeProperties() {
    LOGGER.debug("Writing properties to {}", FILE);

    final Properties properties = new Properties();

    storeProperties(properties);

    try (final Writer writer = Files.newBufferedWriter(FILE)) {
      properties.store(writer, null);
    } catch (final IOException e) {
      throw new IllegalStateException("Unable to write properties to " + FILE, e);
    }
  }

  private static void storeProperties(final Properties properties) {
    properties.setProperty(ENABLED, Boolean.toString(enabled));
  }
}
