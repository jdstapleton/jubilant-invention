package com.jamesstapleton.com.bems.service;


import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoredQueryService {
    Map<String, StoredQuery>  queries = new HashMap<>();

    public StoredQuery save(String expression) {
        return save(StoredQuery.parseFromString(expression));
    }

    public StoredQuery save(StoredQuery query) {
        queries.put(query.getId(), query);

        return query;
    }

    public Optional<StoredQuery> findById(String id) {
        return Optional.ofNullable(queries.get(id));
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext) {
        var context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());

        return queries.values().stream()
                .filter(x -> x.getExpression().getValue(context, documentContext.getCtx(), Boolean.class))
                .collect(Collectors.toList());
    }
}
