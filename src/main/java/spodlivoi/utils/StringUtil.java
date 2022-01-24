package spodlivoi.utils;

public class StringUtil {

    public static final boolean isNullOrWhiteSpace(String s) {
        return s == null || s.isEmpty() || s.isBlank();
    }

}
