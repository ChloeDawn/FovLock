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
import io.github.chloedawn.fovlock.api.FovLockPlugin;
import io.github.chloedawn.fovlock.mixin.SliderButtonAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.GameOptionSliderWidget;
import net.minecraft.client.options.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  private static boolean loaded = false;
  private static boolean enabled = true;

  private FovLock() {
  }

  public static boolean isEnabled() {
    Preconditions.checkState(loaded, "Mod not loaded");
    return enabled;
  }

  public static void setEnabled(final boolean enabled) {
    Preconditions.checkState(loaded, "Mod not loaded");
    Preconditions.checkState(FovLock.enabled != enabled, enabled ? "Already locked" : "Already unlocked");
    FovLock.enabled = enabled;
    writeProperties(FILE);
  }

  public static boolean isFovSlider(final AbstractButtonWidget button) {
    if (button instanceof GameOptionSliderWidget) {
      return isFovOption(((SliderButtonAccessor) button).getOption());
    }
    return false;
  }

  public static boolean isFovOption(final Option option) {
    return option == Option.FOV;
  }

  @Deprecated
  public static void load() {
    Preconditions.checkState(!loaded, "Mod already loaded");
    readProperties(FILE);
    provideLockToPlugins();
    loaded = true;
  }

  private static void provideLockToPlugins() {
    FabricLoader.getInstance().getEntrypoints(NAMESPACE, FovLockPlugin.class)
      .forEach(fovLockPlugin -> fovLockPlugin.provide(Lock.INSTANCE));
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

    readProperties(properties);
  }

  private static void readProperties(final Properties properties) {
    enabled = Boolean.parseBoolean(properties.getProperty(ENABLED, "true"));
  }

  private static void writeProperties(final Path file) {
    LOGGER.debug("Writing properties to {}", file);

    final Properties properties = new Properties();

    writeProperties(properties);

    try (final Writer writer = Files.newBufferedWriter(file)) {
      properties.store(writer, null);
    } catch (final IOException e) {
      throw new RuntimeException("Writing properties to " + file, e);
    }
  }

  private static void writeProperties(final Properties properties) {
    properties.setProperty(ENABLED, Boolean.toString(enabled));
  }

  private static final class Lock implements io.github.chloedawn.fovlock.api.FovLock {
    private static final io.github.chloedawn.fovlock.api.FovLock INSTANCE = new Lock();

    @Override
    public boolean isLocked() {
      return FovLock.isEnabled();
    }

    @Override
    public void setLocked(final boolean locked) {
      FovLock.setEnabled(locked);
    }

    @Override
    public String toString() {
      return "FovLock";
    }
  }
}
