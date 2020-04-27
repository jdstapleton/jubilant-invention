package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jamesstapleton.com.bems.model.DocumentContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Rule {
    public enum Mode {
        /**
         * Ands of Ors
         */
        CNF,
        /**
         * Ors of Ands
         */
        BNF
    }
    @JsonProperty
    private final Mode mode;
    @JsonProperty
    private final List<List<Term>> clauses;

    public Rule(Mode mode, List<List<Term>> operands) {
        this.mode = mode;
        this.clauses = operands;
    }

    public boolean matches(DocumentContext context) {
        if (mode == Mode.CNF) {
            return clauses.stream()
                    .allMatch(inner -> intersects(inner, context));
        } else {
            return clauses.stream()
                    .anyMatch(inner -> allMatches(inner, context));
        }
    }

    private static boolean intersects(List<Term> terms, DocumentContext context) {
        return terms.stream().anyMatch(i -> i.matches(context));
    }

    private static boolean allMatches(List<Term> terms, DocumentContext context) {
        return terms.stream().allMatch(i -> i.matches(context));
    }

    @SafeVarargs
    public static Rule createCNF(List<Term>... terms) {
        return new Rule(Mode.CNF, Arrays.asList(terms));
    }

    @SafeVarargs
    public static Rule createBNF(List<Term>... terms) {
        return new Rule(Mode.BNF, Arrays.asList(terms));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return mode == rule.mode &&
                Objects.equals(clauses, rule.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, clauses);
    }

    @Override
    public String toString() {
        if (mode == Mode.CNF) {
            return clauses.stream()
                    .map(c -> "(" + c.stream().map(Object::toString).collect(Collectors.joining(" OR ")) + ")")
                    .collect(Collectors.joining(" AND "));
        } else {
            return clauses.stream()
                    .map(c -> "(" + c.stream().map(Object::toString).collect(Collectors.joining(" AND ")) + ")")
                    .collect(Collectors.joining(" OR "));
        }
    }
}
