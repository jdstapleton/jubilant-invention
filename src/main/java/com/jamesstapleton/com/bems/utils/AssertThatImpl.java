package com.jamesstapleton.com.bems.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jamesstapleton.com.bems.utils.AssertThat.assertThat;

class AssertThatImpl {
    @FunctionalInterface
    public interface AssertionObjectFunc<T> {
        AssertionMessagesSupplier call(T t);
    }

    @FunctionalInterface
    public interface AssertionMessagesSupplier {
        List<String> getMessages();
    }

    public interface IAssertObject<T, F> extends ThrowableRunnable, AssertionMessagesSupplier {
        AssertionSubject<T> getAssertionSubject();

        default T getSubject() {
            return getAssertionSubject().subject;
        }

        default boolean isInvalid() {
            return !getMessages().isEmpty();
        }

        default List<String> getMessages() {
            return getAssertionSubject().messages;
        }

        default void addMessage(String msg) {
            getAssertionSubject().messages.add(msg);
        }

        default void addMessages(IAssertObject<?, ?> other) {
            getAssertionSubject().messages.addAll(other.getMessages());
        }

        default void addAllMessages(Stream<String> messages) {
            getAssertionSubject().messages
                    .addAll(messages.collect(Collectors.toList()));
        }

        default F isNotNull() {
            if (getSubject() == null) {
                addMessage("Subject is null.");
            }

            return self();
        }

        default void run() throws IllegalStateException {
            if (!getMessages().isEmpty()) {
                throw new IllegalStateException("Assertions failed:\n" +
                        String.join("\n", getMessages()));
            }
        }

        F self();
    }

    public interface IAssertString<T extends String, F> extends IAssertObject<T, F> {
        default F isBlank() {
            if (getSubject() != null && !getSubject().isBlank()) {
                addMessage("Subject is not blank.");
            }

            return self();
        }

        default F isNotBlank() {
            if (getSubject() == null || getSubject().isBlank()) {
                addMessage("Subject is blank.");
            }

            return self();
        }

        default F maxLength(int maxLength) {
            if (getSubject() == null) {
                return self();
            }

            if (getSubject().length() > maxLength) {
                addMessage("Subject must contain fewer than " + maxLength + " characters.");
            }

            return self();
        }

        default F isOneOf(String... str) {
            var lst = List.of(str);
            if (!lst.contains(getSubject())) {
                addMessage("Subject must be one of " + lst + ".");
            }
            return self();
        }
    }

    public interface IAssertURI<T extends URI, F> extends IAssertObject<T, F> {
        default F hostIsSpecified() {
            isNotNull();

            if (getSubject() == null) {
                return self();
            }

            if (assertThat(getSubject().getHost()).isNotBlank().isInvalid()) {
                addMessage("Subject does not have a host specified.");
            }

            return self();
        }

        default F hasNoUserInfo() {
            if (getSubject() == null) {
                return self();
            }

            if (assertThat(getSubject().getUserInfo()).isBlank().isInvalid()) {
                addMessage("Subject has an authority specified.");
            }

            return self();
        }

        default F isHttpOrHttps() {
            isNotNull();

            if (getSubject() == null) {
                return self();
            }

            var schemeVal = assertThat(getSubject().getScheme())
                    .isOneOf("http", "https");
            if (schemeVal.isInvalid()) {
                addMessage("Scheme: " + schemeVal.getMessages().get(0));
            }

            return self();
        }
    }

    public interface IAssertCollection<U, T extends Collection<U>, F> extends IAssertObject<T, F> {
        default F isNotEmpty() {
            if (getSubject() == null || getSubject().isEmpty()) {
                addMessage("Subject is empty.");
            }

            return self();
        }

        default F assertThatAll(AssertionObjectFunc<U> iterAssertion) {
            var subj = getSubject();
            if (subj == null) {
                return self();
            }

            var subjIter = subj.iterator();
            for (int i = 0; i < subj.size(); i++) {
                var idx = i;
                var u = subjIter.next();

                addAllMessages(iterAssertion.call(u).getMessages().stream()
                        .map(x -> "[" + idx + "] " + x));
            }

            return self();
        }
    }

    static class AssertionSubject<T> {
        private final List<String> messages = new ArrayList<>();
        private final T subject;

        AssertionSubject(T subject) {
            this.subject = subject;
        }
    }

    public static final class AssertString implements IAssertString<String, AssertString> {
        private final AssertionSubject<String> subject;

        public AssertString(String subject) {
            this.subject = new AssertionSubject<>(subject);
        }

        @Override
        public AssertionSubject<String> getAssertionSubject() {
            return subject;
        }

        @Override
        public AssertString self() {
            return this;
        }
    }


    public static final class AssertUri implements IAssertURI<URI, AssertUri> {
        private final AssertionSubject<URI> subject;

        public AssertUri(URI subject) {
            this.subject = new AssertionSubject<>(subject);
        }

        @Override
        public AssertionSubject<URI> getAssertionSubject() {
            return subject;
        }

        @Override
        public AssertUri self() {
            return this;
        }
    }

    public static final class AssertCollection<U>
            implements IAssertCollection<U, Collection<U>, AssertCollection<U>> {
        private final AssertionSubject<Collection<U>> subject;

        public AssertCollection(Collection<U> subject) {
            this.subject = new AssertionSubject<>(subject);
        }

        @Override
        public AssertionSubject<Collection<U>> getAssertionSubject() {
            return subject;
        }

        @Override
        public AssertCollection<U> self() {
            return this;
        }
    }
}
