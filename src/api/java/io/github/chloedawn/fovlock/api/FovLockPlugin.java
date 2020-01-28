package io.github.chloedawn.fovlock.api;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The entry-point plugin API for FovLock. During environment loading,
 * {@link FovLockPlugin#provide(FovLock)} is invoked with the field of
 * view lock instance for this runtime. The recommended approach to
 * creating an entry-point is to use a method with a signature that
 * accepts an {@link FovLock} and returns {@code void}.
 * <pre>{@code
 * public static void setLock(final FovLock lock) {}
 * }</pre>
 * This method would then be referenced in the {@code entrypoints} object
 * of the dependant's {@code fabric.mod.json}, with {@code ::} delimiting
 * the class and the method name.
 * <pre>{@code
 * "fovlock": [
 *   "package.to.MyFovLockPlugin::setLock"
 * ]
 * }</pre>
 * The method body would be used to cache the provided {@code lock} for
 * reference later at runtime.
 * <pre>{@code
 * private static FovLock lock = null;
 * 
 * public static void setLock(final FovLock lock) {
 *   MyFovLockPlugin.lock = lock;
 * }
 * }</pre>
 * In this design, getters and setters could be implemented for safely
 * referencing the provided lock. The example below assumes the field from
 * the above example is declared in the same class as these methods.
 * <pre>{@code
 * public static boolean isFovLocked() {
 *   return lock != null && lock.isLocked();
 * }
 * public static void setFovLocked(final boolean locked) {
 *   if (lock != null) lock.setLocked(locked);
 * }
 * }</pre>
 *
 * @author ChloeDawn
 * @since 4.2.0
 */
public interface FovLockPlugin {
  /**
   * Invoked during environment loading by {@code FovLock},
   * providing the field of view lock instance for this runtime.
   *
   * @param lock The field of view lock
   * @author Chloe Dawn
   * @since 4.2.0
   */
  void provide(final @NonNull FovLock lock);
}
