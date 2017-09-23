package mobing.fql.wayout;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class WeatherLevelTest {
    private static final Logger log = LoggerFactory.getLogger(WeatherLevelTest.class);
    @Test
    public void name() throws Exception {
        String name = WeatherLevel.BLUE.name();
        log.info("name: {}", name);
    }

}