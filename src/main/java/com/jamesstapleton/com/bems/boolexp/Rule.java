package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonDeserialize(builder = ImmutableRule.Builder.class)
@Value.Immutable
@Model
public abstract class Rule {
    public enum Mode {
        /**
         * Ands of Ors
         */
        CNF,
        /**
         * Ors of Ands
         */
        DNF
    }

    @SafeVarargs
    public static Rule createCNF(List<Term>... terms) {
        return ImmutableRule.of(Mode.CNF, Arrays.asList(terms));
    }

    @SafeVarargs
    public static Rule createDNF(List<Term>... terms) {
        return ImmutableRule.of(Mode.DNF, Arrays.asList(terms));
    }

    @Value.Parameter
    public abstract Mode getMode();

    private static boolean intersects(List<Term> terms, DocumentContext context) {
        return terms.stream().anyMatch(i -> i.matches(context));
    }

    private static boolean allMatches(List<Term> terms, DocumentContext context) {
        return terms.stream().allMatch(i -> i.matches(context));
    }

    @Value.Parameter
    public abstract List<List<Term>> getClauses();

    public final boolean matches(DocumentContext context) {
        if (getMode() == Mode.CNF) {
            return getClauses().stream()
                    .allMatch(inner -> intersects(inner, context));
        } else {
            return getClauses().stream()
                    .anyMatch(inner -> allMatches(inner, context));
        }
    }

    @Override
    public String toString() {
        if (getMode() == Mode.CNF) {
            return getClauses().stream()
                    .map(c -> "(" + c.stream().map(Object::toString).collect(Collectors.joining(" OR ")) + ")")
                    .collect(Collectors.joining(" AND "));
        } else {
            return getClauses().stream()
                    .map(c -> "(" + c.stream().map(Object::toString).collect(Collectors.joining(" AND ")) + ")")
                    .collect(Collectors.joining(" OR "));
        }
    }
}
