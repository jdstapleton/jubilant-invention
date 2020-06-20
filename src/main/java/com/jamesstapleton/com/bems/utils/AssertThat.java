package com.jamesstapleton.com.bems.utils;

import java.net.URI;
import java.util.Collection;

public final class AssertThat {
    private AssertThat() {
    }

    public static AssertThatImpl.AssertString assertThat(String subject) {
        return new AssertThatImpl.AssertString(subject);
    }

    public static AssertThatImpl.AssertUri assertThat(URI subject) {
        return new AssertThatImpl.AssertUri(subject);
    }

    public static <T> AssertThatImpl.AssertCollection<T> assertThat(Collection<T> subject) {
        return new AssertThatImpl.AssertCollection<>(subject);
    }
}
