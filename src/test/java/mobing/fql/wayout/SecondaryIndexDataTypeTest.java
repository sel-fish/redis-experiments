package mobing.fql.wayout;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SecondaryIndexDataTypeTest {
    private static final Logger log = LoggerFactory.getLogger(SecondaryIndexDataTypeTest.class);
    private static SecondaryIndexDataType secondaryIndexDataType = null;
    private static WeatherInfo nanjingInfo, beijingInfo, shanghaiInfo, wuhanInfo;

    @BeforeClass
    public static void beforeAll() {
        log.info("before all");
        secondaryIndexDataType = new SecondaryIndexDataType();

        nanjingInfo = new WeatherInfo();
        nanjingInfo.setCity("Nanjing");
        nanjingInfo.setWeather("storm");
        nanjingInfo.setLevel(WeatherLevel.ORANGE);

        beijingInfo = new WeatherInfo();
        beijingInfo.setCity("Beijing");
        beijingInfo.setWeather("rain");
        beijingInfo.setLevel(WeatherLevel.RED);

        shanghaiInfo = new WeatherInfo();
        shanghaiInfo.setCity("Shanghai");
        shanghaiInfo.setWeather("snow");
        shanghaiInfo.setLevel(WeatherLevel.BLUE);

        wuhanInfo = new WeatherInfo();
        wuhanInfo.setCity("Wuhan");
        wuhanInfo.setWeather("rain");
        wuhanInfo.setLevel(WeatherLevel.RED);
    }

    @AfterClass
    public static void afterAll() {
        log.info("after all");
    }

    @Before
    public void setUp() throws Exception {
        log.debug("set up");
        secondaryIndexDataType.flush();
    }

    @After
    public void tearDown() throws Exception {
        log.debug("tear down");
    }

    @Test
    public void flush() throws Exception {
        Assert.assertEquals("OK", secondaryIndexDataType.flush());
    }

    @Test
    public void loadScript() throws IOException {
        secondaryIndexDataType.loadScript("insert.lua");
    }

    @Test
    public void insert() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(beijingInfo));
        Assert.assertFalse(secondaryIndexDataType.insert(beijingInfo));

        Assert.assertTrue(secondaryIndexDataType.insert(wuhanInfo));
        Assert.assertEquals(2, secondaryIndexDataType.queryByWeather("rain").size());

    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(beijingInfo));
        Assert.assertTrue(secondaryIndexDataType.insert(wuhanInfo));
        Assert.assertEquals(2, secondaryIndexDataType.queryByWeather("rain").size());

        Assert.assertTrue(secondaryIndexDataType.delete(beijingInfo));
        Assert.assertFalse(secondaryIndexDataType.delete(beijingInfo));
        Assert.assertEquals(1, secondaryIndexDataType.queryByWeather("rain").size());

        Assert.assertTrue(secondaryIndexDataType.delete(wuhanInfo));
        Assert.assertFalse(secondaryIndexDataType.delete(wuhanInfo));
        Assert.assertEquals(0, secondaryIndexDataType.queryByWeather("rain").size());
    }

    @Test
    public void exist() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(beijingInfo));
        Assert.assertTrue(secondaryIndexDataType.exist(beijingInfo));
        Assert.assertFalse(secondaryIndexDataType.exist(wuhanInfo));
    }

    @Test
    public void size() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(beijingInfo));
        Assert.assertTrue(secondaryIndexDataType.insert(wuhanInfo));
        Assert.assertTrue(secondaryIndexDataType.insert(nanjingInfo));
        Assert.assertTrue(secondaryIndexDataType.insert(shanghaiInfo));
        Assert.assertEquals(4, secondaryIndexDataType.size());

        Assert.assertTrue(secondaryIndexDataType.delete(beijingInfo));
        Assert.assertTrue(secondaryIndexDataType.delete(nanjingInfo));
        Assert.assertTrue(secondaryIndexDataType.delete(shanghaiInfo));
        Assert.assertEquals(1, secondaryIndexDataType.size());
    }

    @Test
    public void queryByCity() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(nanjingInfo));
        List<WeatherInfo> matchedInfos = null;
        matchedInfos = secondaryIndexDataType.queryByCity("Nanjing");
        Assert.assertEquals(1, matchedInfos.size());

        matchedInfos = secondaryIndexDataType.queryByCity("CityNotExist");
        Assert.assertEquals(0, matchedInfos.size());
    }

    @Test
    public void queryByWeather() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(nanjingInfo));
        List<WeatherInfo> matchedInfos = null;
        matchedInfos = secondaryIndexDataType.queryByWeather("storm");
        Assert.assertEquals(1, matchedInfos.size());

        matchedInfos = secondaryIndexDataType.queryByWeather("WeatherNotExist");
        Assert.assertEquals(0, matchedInfos.size());
    }

    @Test
    public void queryByLevel() throws Exception {
        Assert.assertTrue(secondaryIndexDataType.insert(nanjingInfo));
        List<WeatherInfo> matchedInfos = null;
        matchedInfos = secondaryIndexDataType.queryByLevel(WeatherLevel.ORANGE);
        Assert.assertEquals(1, matchedInfos.size());

        matchedInfos = secondaryIndexDataType.queryByLevel(WeatherLevel.BLUE);
        Assert.assertEquals(0, matchedInfos.size());
    }

}