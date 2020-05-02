package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import java.io.IOException;

public class TermIdResolver extends TypeIdResolverBase {
    private JavaType superType;

    @Override
    public void init(JavaType baseType) {
        superType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().replace("Immutable", "");
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return suggestedType.getSimpleName().replace("Immutable", "");
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.MINIMAL_CLASS;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        try {
            return context.constructSpecializedType(
                    superType,
                    Class.forName(TermIdResolver.class.getPackageName() + ".Immutable" + id));
        } catch (ClassNotFoundException e) {
            try {
                return context.constructSpecializedType(
                        superType,
                        Class.forName(TermIdResolver.class.getPackageName() + ".Immutable" + id));
            } catch (ClassNotFoundException e2) {
                var excp = new IOException("No type mapping for " + id);
                excp.addSuppressed(e);
                excp.addSuppressed(e2);

                throw excp;
            }
        }
    }
}
