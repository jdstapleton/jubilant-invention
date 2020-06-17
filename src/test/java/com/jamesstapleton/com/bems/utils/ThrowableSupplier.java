package com.jamesstapleton.com.bems.utils;

public interface ThrowableSupplier<T> {
    T get() throws Exception;
}
