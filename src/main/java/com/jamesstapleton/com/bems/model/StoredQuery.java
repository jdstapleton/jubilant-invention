package com.jamesstapleton.com.bems.model;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.UUID;

public class StoredQuery {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private final String id;
    private final Expression expression;

    public StoredQuery(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getExpressionString() {
        return expression.getExpressionString();
    }

    public static StoredQuery parseFromString(String expression) {
        return parseFromString(UUID.randomUUID().toString(), expression);
    }

    public static StoredQuery parseFromString(String id, String expression) {
        return new StoredQuery(id, parser.parseExpression(expression));
    }
}
