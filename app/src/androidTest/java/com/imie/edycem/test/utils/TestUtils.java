/*
 * TestUtils.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.test.utils;

import java.util.Random;

import org.joda.time.DateTime;

/**
 * Utils class for data generation.
 */
public abstract class TestUtils {

    private static final Random RANDOM = new Random();

    /**
     * <p>Creates a random string based on a variety of options, using supplied source of randomness.</p>.
     *
     * <p>If start and end are both <code>0</code>, start and end are set
     * to <code>' '</code> and <code>'z'</code>, the ASCII printable
     * characters, will be used, unless letters and numbers are both
     * <code>false</code>, in which case, start and end are set to
     * <code>0</code> and <code>Integer.MAX_VALUE</code>.
     *
     * <p>If set is not <code>null</code>, characters between start and
     * end are chosen.</p>
     *
     * <p>This method accepts a user-supplied {@link Random}
     * instance to use as a source of randomness. By seeding a single
     * {@link Random} instance with a fixed seed and using it for each call,
     * the same random sequence of strings can be generated repeatedly
     * and predictably.</p>.
     *
     * @param count  the length of random string to create
     * @param start  the position in set of chars to start at
     * @param end  the position in set of chars to end before
     * @param letters  only allow letters?
     * @param numbers  only allow numbers?
     * @param chars  the set of chars to choose randoms from.
     *  If <code>null</code>, then it will use the set of all chars.
     * @param random  a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *  <code>(end - start) + 1</code> characters in the set array.
     * @throws IllegalArgumentException if <code>count</code> &lt; 0.
     * @since 2.0
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers,
                                char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }

        if ((start == 0) && (end == 0)) {
            end = 'z' + 1;
            start = ' ';

            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch;

            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }

            if ((letters && Character.isLetter(ch))
                    || (numbers && Character.isDigit(ch))
                    || (!letters && !numbers)) {
                if (ch >= 56320 && ch <= 57343) {
                    if (count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if (ch >= 55296 && ch <= 56191) {
                    if (count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if (ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }

        return new String(buffer);
    }

    /**
     * Generate a random String.
     *
     * @param length Length of the generated String
     *
     * @return The random String
     */
    public static String generateRandomString(int length) {
        return random(length, 0, 0, true, true, null, RANDOM);
    }

    /**
     * Generate a random int.
     *
     * @param min Min value for the generated int
     * @param max Max value for the generated int
     *
     * @return The random int
     */
    public static int generateRandomInt(int min, int max) {
        return (int) generateRandomDouble(min, max);
    }

    /**
     * Generate a random double.
     *
     * @param min Min value for the generated double
     * @param max Max value for the generated double
     *
     * @return The random double
     */
    public static double generateRandomDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    /**
     * Generate a random float.
     *
     * @param min Min value for the generated float
     * @param max Max value for the generated float
     *
     * @return The random float
     */
    public static float generateRandomFloat(float min, float max) {
        return (float) generateRandomDouble(min, max);
    }

    /*
     * Generate a random short.
     *
     * @return The random short
     */
    public static short generateRandomShort() {
        return (short) (Math.random() * (Short.MAX_VALUE));
    }

    /**
     * Generate a random byte.
     *
     * @return The random byte
     */
    public static byte generateRandomByte() {
        return (byte) (Math.random() * (Byte.MAX_VALUE));
    }

    /**
     * Generate a random char.
     *
     * @return The random char
     */
    public static char generateRandomChar() {
        return (char) (Math.random() * (Character.MAX_VALUE));
    }

    /**
     * Generate a random bool.
     *
     * @return The random bool
     */
    public static boolean generateRandomBool() {
        return Math.random() > 0.5;
    }

    /**
     * Generate a random DateTime.
     *
     * @return The random DateTime
     */
    public static DateTime generateRandomDateTime() {
        DateTime date = generateRandomDate();
        DateTime time = generateRandomTime();

        return new DateTime(
                date.getYear(),
                date.getMonthOfYear(),
                date.getDayOfWeek(),
                time.getHourOfDay(),
                time.getMinuteOfHour());
    }

    /**
     * Generate a random Date.
     *
     * @return The random Date
     */
    public static DateTime generateRandomDate() {
        int year;
        int month;
        int day;

        year = (int) (Math.random() * 200) + 1900;
        month = (int) (Math.random() * 11) + 1;
        day = (int) (Math.random() * 27) + 1;

        return new DateTime(year, month, day, 0, 0);
    }

    /**
     * Generate a random Time.
     *
     * @return The random Time
     */
    public static DateTime generateRandomTime() {
        DateTime dt = new DateTime();
        int hours;
        int minutes;

        hours = (int) (Math.random() * 23);
        minutes = (int) (Math.random() * 59);

        return new DateTime(
                dt.getYear(),
                dt.getMonthOfYear(),
                dt.getDayOfMonth(),
                hours,
                minutes);
    }
}
