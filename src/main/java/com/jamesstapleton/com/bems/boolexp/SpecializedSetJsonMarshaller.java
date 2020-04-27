package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SpecializedSetJsonMarshaller {
    public static class Serializer extends JsonSerializer<Set<String>>{
        @Override
        public void serialize(Set<String> v, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (v.size() == 1) {
                gen.writeString(v.stream().findFirst().get());
            } else {
                provider.defaultSerializeValue(v.stream().sorted().collect(Collectors.toList()), gen);
            }
        }
    }

    public static class Deserializer extends  JsonDeserializer<Set<String>> {
        @Override
        public Set<String> deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            if (node.isTextual()) {
                return Set.of(node.asText());
            } else if (node.isArray()) {
                Iterable<JsonNode> iterable = node::elements;
                return StreamSupport.stream(iterable.spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(Collectors.toSet());
            } else {
                throw new RuntimeException("Unable to parse field: not one of String or Array<String>.");
            }
        }
    }
}
