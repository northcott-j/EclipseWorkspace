package cs3500.hw01.duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Abstract base class for implementations of {@link Duration}.
 */
abstract class AbstractDuration implements Duration {
  /**
   * Constructs a {@link Duration} in a manner selected by concrete
   * subclasses of this class.
   *
   * @param inSeconds the length in seconds
   * @return the new {@code Duration}
   */
  protected abstract Duration fromSeconds(long inSeconds);

  @Override
  public String toString() {
    return asHms();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (! (that instanceof Duration)) {
      return false;
    }

    return ((Duration) that).inSeconds() == this.inSeconds();
  }

  @Override
  public int hashCode() {
    return Long.hashCode(inSeconds());
  }

  @Override
  public int compareTo(Duration that) {
    return Long.compare(this.inSeconds(), that.inSeconds());
  }

  @Override
  public Duration plus(Duration that) {
    return fromSeconds(this.inSeconds() + that.inSeconds());
  }

  @Override
  public String format(String template) {
      long totalSeconds = this.inSeconds();
      long hours = this.hoursOf(totalSeconds);
      long minutes = this.minutesOf(totalSeconds);
      long seconds = totalSeconds % 60;
      /**
       * TEMPLATE SYMBOLS
       * %t - time in seconds
       * %% - literal % symbol
       * %H - padded hours
       * %h - only hours
       * %M - padded minutes
       * %m - only minutes
       * %S - padded seconds
       * %s - only seconds
       * else malformed
       */
     String output = "";
     for (int i = 0 ; i < template.length() ; i += 1) {
         if (String.valueOf(template.charAt(i)).equals("%")) {
             String nextChar = String.valueOf(template.charAt(i + 1));
             i += 1;
             if (nextChar.equals("t")) {
                 output += totalSeconds;
             }
             else if (nextChar.equals("%")) {
                 output += "%";
             }
             else if (nextChar.equals("H")) {
                 if (hours < 10) {
                     output += "0" + Long.toString(hours);
                 }
                 else {
                     output = output + Long.toString(hours);
                 }
             }
             else if (nextChar.equals("h")) {
                 output += Long.toString(hours);
             }
             else if (nextChar.equals("M")) {
                 if (minutes < 10) {
                     output += "0" + Long.toString(minutes);
                 }
                 else {
                     output += Long.toString(minutes);
                 }
             }
             else if (nextChar.equals("m")) {
                 output += Long.toString(minutes);
             }
             else if (nextChar.equals("S")) {
                 if (seconds < 10) {
                     output += "0" + Long.toString(seconds);
                 }
                 else {
                     output = output + Long.toString(seconds);
                 }
             }
             else if (nextChar.equals("s")) {
                 output += Long.toString(seconds);
             }
             else {
                 throw new IllegalArgumentException("Malformed Template");
             }
         }
         else {
             output = output + template.charAt(i);
         }
     }

      return output;
  }
  /**
   * Converts an unpacked hours-minutes-seconds duration to its length
   * in seconds.
   *
   * @param hours the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @return the duration in seconds
   */
  protected static long inSeconds(int hours, int minutes, int seconds) {
    return 3600 * hours + 60 * minutes + seconds;
  }

  /**
   * Formats an unpacked hours-minutes-seconds duration in the same
   * {@code H:MM:SS} format that {@link Duration#asHms()} returns.
   * Assumes that
   *
   * @param hours the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @return formatted duration
   * @throws IllegalArgumentException if any argument is negative
   */
  protected static String asHms(int hours, int minutes, int seconds) {
    return String.format("%d:%02d:%02d", hours, minutes, seconds);
  }

  /**
   * Ensures that the hours, minutes, and seconds are all non-negative.
   * Is factoring this out overkill? Or should we also factor out the
   * {@code inSeconds < 0} check in the two unary constructors? Discuss.
   *
   * @param hours the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @throws IllegalArgumentException if any argument is negative
   */
  protected static void ensureHms(int hours, int minutes, int seconds) {
    if (hours < 0 || minutes < 0 || seconds < 0) {
      throw new IllegalArgumentException("must be non-negative");
    }
  }

  /**
   * Returns the number of whole hours in the given number of seconds.
   *
   * @param inSeconds the total number of seconds
   * @return the number of hours
   * @throws ArithmeticException if the correct result cannot fit in an
   *          {@code int}.
   */
  protected static int hoursOf(long inSeconds) {
    if (inSeconds / 3600 > Integer.MAX_VALUE) {
      throw new ArithmeticException("result cannot fit in type");
    }

    return (int) (inSeconds / 3600);
  }

  /**
   * Returns the number of whole minutes in the given number of seconds, less
   * the number of whole hours.
   *
   * @param inSeconds the total number of seconds
   * @return the number of remaining minutes
   */
  protected static int minutesOf(long inSeconds) {
    return (int) (inSeconds / 60 % 60);
  }

  /**
   * Returns the number of seconds remaining after all full minutes are
   * removed from the given number of seconds.
   *
   * @param inSeconds the total number of seconds
   * @return the number of remaining seconds
   */
  protected static int secondsOf(long inSeconds) {
    return (int) (inSeconds % 60);
  }
}

