package net.awired.ajsl.core.lang;

public interface EnumInterface<E extends Enum<E>> {
    public Class<E> getDeclaringClass();

}
