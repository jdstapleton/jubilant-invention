package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.utils.ModelValidator;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;

import java.net.URI;

import static com.jamesstapleton.com.bems.utils.AssertThat.assertThat;

@JsonDeserialize(builder = ImmutableQuerySubject.Builder.class)
@Value.Immutable
@Model
public interface QuerySubject {
    static ImmutableQuerySubject.Builder builder() {
        return ImmutableQuerySubject.builder();
    }

    /**
     * Its initializes to empty string, but when preserved will have a filled
     * out id.
     *
     * @return generated id that should never change, used for logging
     */
    @Value.Default
    default String getId() {
        return "";
    }

    /**
     * @return the friendly full title to show the user
     */
    @NonNull
    String getTitle();

    /**
     * @return a friendly description to show the user
     */
    @NonNull
    String getDescription();

    /**
     * @return the target uri to redirect the user to
     */
    @NonNull
    URI getTargetUri();

    QuerySubject withId(@NonNull String id);

    @Value.Check
    default void check() {
        try (ModelValidator validator = new ModelValidator("QuerySubject")) {
            validator.validate("title", assertThat(getTitle()).isNotBlank().maxLength(64));
            validator.validate("description", assertThat(getDescription()).isNotBlank());
            validator.validate("targetUri", assertThat(getTargetUri())
                    .isNotNull()
                    .isHttpOrHttps()
                    .hostIsSpecified()
                    .hasNoUserInfo());
        }
    }
}
