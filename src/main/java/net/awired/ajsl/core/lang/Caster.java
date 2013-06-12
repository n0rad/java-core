package net.awired.ajsl.core.lang;

import java.util.List;

public class Caster {

    @SuppressWarnings("unchecked")
    public static <U, T extends U> List<U> cast(List<T> p, Class<U> t) {
        return (List<U>) p;
    }
}
