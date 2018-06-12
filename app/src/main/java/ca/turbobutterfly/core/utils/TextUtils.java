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
}
