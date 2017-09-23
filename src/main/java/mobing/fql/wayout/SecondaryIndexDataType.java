package mobing.fql.wayout;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.xml.stream.FactoryConfigurationError;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SecondaryIndexDataType {

    private static final Logger log = LoggerFactory.getLogger(SecondaryIndexDataType.class);
    private Jedis jedis;
    private static final String MAIN_KEY = "WEATHER-INFO-MAIN-INDEX";
    private static final String CITY_KEY = "WEATHER-INFO-CITY-INDEX-";
    private static final String WEATHER_KEY = "WEATHER-INFO-WEATHER-INDEX-";
    private static final String LEVEL_KEY = "WEATHER-INFO-LEVEL-INDEX-";

    public SecondaryIndexDataType() {
        jedis = new Jedis("localhost");
    }

    public String flush() {
        return jedis.flushAll();
    }

    protected String loadScript(String fileName) {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource(fileName).getFile();
            log.debug("filePath: {}", filePath);
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            String script = new String(encoded, Charset.defaultCharset());
            log.debug("script: {}", script);
            return script;
        } catch (IOException e) {
            log.error("", e);
        }

        return null;
    }

    public boolean insert(WeatherInfo weatherInfo) {
        String insertScript = loadScript("insert.lua");
        return genericTransaction(insertScript, weatherInfo);
    }

    public boolean delete(WeatherInfo weatherInfo) {
        String deleteScript = loadScript("delete.lua");
        return genericTransaction(deleteScript, weatherInfo);
    }

    public boolean exist(WeatherInfo weatherInfo) {
        return jedis.sismember(MAIN_KEY, JSON.toJSONString(weatherInfo));
    }

    public long size() {
       return jedis.scard(MAIN_KEY);
    }

    private boolean genericTransaction(String scriptContent, WeatherInfo weatherInfo) {
        List<String> keys = new ArrayList<String>();
        keys.add(weatherInfo.getCity());
        keys.add(weatherInfo.getWeather());
        keys.add(weatherInfo.getLevel().name());

        List<String> args = new ArrayList<String>();
        args.add(JSON.toJSONString(weatherInfo));

        Long ret = (Long) jedis.eval(scriptContent, keys, args);
        return ret > 0;
    }

    private List<WeatherInfo> genericQuery(String key) {
        Set<String> jsonStrings = jedis.smembers(key);
        List<WeatherInfo> result = new ArrayList<>();
        for (String jsonString : jsonStrings) {
            WeatherInfo weatherInfo = JSON.parseObject(jsonString, WeatherInfo.class);
            result.add(weatherInfo);
        }
        return result;
    }

    public List<WeatherInfo> queryByCity(String city) {
        String key = CITY_KEY + city;
        return genericQuery(key);
    }

    public List<WeatherInfo> queryByWeather(String weather) {
        String key = WEATHER_KEY + weather;
        return genericQuery(key);
    }

    public List<WeatherInfo> queryByLevel(WeatherLevel level) {
        String key = LEVEL_KEY + level.name();
        return genericQuery(key);
    }

}
