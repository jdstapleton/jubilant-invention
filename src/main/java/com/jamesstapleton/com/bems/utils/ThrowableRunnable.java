package com.jamesstapleton.com.bems.utils;

@FunctionalInterface
public interface ThrowableRunnable {
    void run() throws Exception;
}
