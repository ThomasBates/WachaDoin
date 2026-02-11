package ca.turbobutterfly.core.utils;

import java.util.Objects;

public class TextUtils
{
    /**
     * Null-safe check for equality.
     */
    public static boolean equals(String a, String b)
    {
        return Objects.equals(a, b);
    }

    /**
     * Checks if a string is null or empty.
     */
    public static boolean isEmpty(String a)
    {
        return (a == null) || a.isEmpty();
    }

    /**
     * Null-safe natural order comparison. 
     * Treats null as "less than" any non-null string.
     */
    public static int compare(String a, String b)
    {
        if (Objects.equals(a, b))
        {
            return 0;
        }
        if (a == null)
        {
            return -1;
        }
        if (b == null)
        {
            return 1;
        }

        return a.compareTo(b);
    }
}
