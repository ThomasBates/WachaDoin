package ca.turbobutterfly.core.utils;

public class TextUtils
{
    public static boolean equals(String a, String b)
    {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static boolean isEmpty(String a)
    {
        return (a == null) || a.isEmpty();
    }

    public static int compare(String a, String b)
    {
        if ((a == null) & (b == null))
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
