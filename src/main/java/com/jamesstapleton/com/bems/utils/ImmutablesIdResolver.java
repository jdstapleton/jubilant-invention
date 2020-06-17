package com.jamesstapleton.com.bems.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import java.io.IOException;

public class ImmutablesIdResolver extends TypeIdResolverBase {
    private JavaType superType;
    private String packageName = this.getClass().getPackageName();

    @Override
    public final void init(JavaType baseType) {
        superType = baseType;
        packageName = superType.getRawClass().getPackageName();
    }

    @Override
    public final String idFromValue(Object value) {
        return value.getClass().getSimpleName().replace("Immutable", "");
    }

    @Override
    public final String idFromValueAndType(Object value, Class<?> suggestedType) {
        return suggestedType.getSimpleName().replace("Immutable", "");
    }

    @Override
    public final JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }

    @Override
    public final JavaType typeFromId(DatabindContext context, String id) throws IOException {
        try {
            return context.constructSpecializedType(
                    superType,
                    Class.forName(packageName + ".Immutable" + id));
        } catch (ClassNotFoundException e) {
            try {
                return context.constructSpecializedType(
                        superType,
                        Class.forName(packageName + ".Immutable" + id));
            } catch (ClassNotFoundException e2) {
                var excp = new IOException("No type mapping for " + id);
                excp.addSuppressed(e);
                excp.addSuppressed(e2);

                throw excp;
            }
        }
    }
}
