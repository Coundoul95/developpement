package sn.afrilins.net.gestionEnquete.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Component
public class TestUtils
{

    public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private ObjectMapper mapper;

    public byte[] convertObjectToJsonBytes(Object object) throws IOException
    {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public <O extends Object> O convertJsonToObject(String json, Class<O> className) throws JsonParseException,
        JsonMappingException, IOException
    {
        return mapper.readValue(json, className);
    }

    public <O extends Object> List<O> convertJsonToObjectList(String json, Class<O> className)
        throws JsonParseException, JsonMappingException, IOException
    {
        CollectionLikeType type = mapper.getTypeFactory().constructCollectionLikeType(List.class, className);
        return mapper.readValue(json, type);
    }

    public <O, P extends Object> Map<O, P> convertJsonToObjectMap(String json, Class<O> keyClass, Class<P> valueClass)
        throws JsonParseException, JsonMappingException, IOException
    {
        MapLikeType type = mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return mapper.readValue(json, type);
    }
    
    public <O, P extends Object> Map<O, List<P>> convertJsonToObjectMapOfList(String json, Class<O> keyClass,
        Class<P> valueClass) throws JsonParseException, JsonMappingException, IOException
    {

        TypeFactory typeFactory = mapper.getTypeFactory();

        JavaType keyType = typeFactory.constructType(keyClass);
        CollectionLikeType valueType = mapper.getTypeFactory().constructCollectionLikeType(List.class, valueClass);
        MapLikeType type = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        return mapper.readValue(json, type);
    }

    public <O, P, Q extends Object> Map<O, Map<P, Q>> convertJsonToObjectMapOfMap(String json, Class<O> topKeyClass,
        Class<P> keyClass, Class<Q> valueClass) throws JsonParseException, JsonMappingException, IOException
    {

        TypeFactory typeFactory = mapper.getTypeFactory();

        JavaType topKeyType = typeFactory.constructType(topKeyClass);
        MapLikeType valueType = typeFactory.constructMapLikeType(Map.class, keyClass, valueClass);
        MapLikeType mapType = typeFactory.constructMapLikeType(Map.class, topKeyType, valueType);

        return mapper.readValue(json, mapType);
    }

}
