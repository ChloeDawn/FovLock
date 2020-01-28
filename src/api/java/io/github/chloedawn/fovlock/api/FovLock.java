package io.github.chloedawn.fovlock.api;

/**
 * Represents the lock state of the client's field of view
 *
 * @author Chloe Dawn
 * @since 4.2.0
 */
public interface FovLock {
  /**
   * Gets the state of the field of view lock
   *
   * @return True if the FOV is locked
   * @author Chloe Dawn
   * @since 4.2.0
   */
  boolean isLocked();

  /**
   * Sets the state of the field of view lock
   *
   * @param locked True if the FOV is to be locked
   * @author Chloe Dawn
   * @since 4.2.0
   */
  void setLocked(final boolean locked);
}
