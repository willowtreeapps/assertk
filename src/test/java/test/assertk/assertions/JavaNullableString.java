package test.assertk.assertions;

import javax.annotation.Nullable;

public class JavaNullableString {

    public static String string() {
        return null;
    }

    @Nullable
    public static String stringWithNullableAnnotation() {
        return null;
    }

    public static String nonNullableString() {
        return "nonNullableString";
    }
}
