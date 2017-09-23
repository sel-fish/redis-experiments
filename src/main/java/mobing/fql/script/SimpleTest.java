package mobing.fql.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fenqi on 16/6/12.
 */

public class SimpleTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleTest.class);

    private Jedis jedis = new Jedis("localhost", 6379);
    private static final String timeSliceScript = "/Users/fenqi/d/TimeSliceCalculate.lua";
    private static final String userDataScript = "/Users/fenqi/d/CalculateUserDataHandle.lua";

    public static void main(String[] args) throws IOException {
        SimpleTest test = new SimpleTest();
//        test.runTimeSliceScript();
        test.runUserDataScript();
    }

    private String loadScript(String path) {
        try {
            String scriptContent = new String(Files.readAllBytes(Paths.get(path)));
            String strSHA = jedis.scriptLoad(scriptContent);
            return strSHA;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void runTimeSliceScript() throws IOException {
        String timeSliceSHA = loadScript(timeSliceScript);
        System.out.println(timeSliceSHA);

        Map<String, Integer> businessKeys = new HashMap<>();
        businessKeys.put("foo", 2);
        businessKeys.put("bar", 3);

        List<String> keys = new ArrayList<>();

        List<String> args = new ArrayList<>();
        Integer businessKeyNum = businessKeys.size();
        args.add(businessKeyNum.toString());

        // init it
        for (Map.Entry<String, Integer> entry: businessKeys.entrySet()) {
            String prefix = entry.getKey();
            Integer count = entry.getValue();

            for (int i = 0; i < count; i++) {
                String cacheKey = prefix + i;
                String cacheVal = String.valueOf(new Random().nextInt(100));
                jedis.set(cacheKey, cacheVal);
                log.info("jedis set {} {}", cacheKey, cacheVal);
                keys.add(cacheKey);
            }

            args.add(count.toString());
        }

        // run job
        Object o = jedis.evalsha(timeSliceSHA, keys, args);
        System.out.println(o);
    }

    private void runUserDataScript() throws IOException {
        String userDataSHA = loadScript(userDataScript);
        System.out.println(userDataSHA);

        List<String> keys = new ArrayList<>();


        List<String> args = new ArrayList<>();
        args.add("xxx");


        Object o = jedis.evalsha(userDataSHA, keys, args);
        System.out.println(o);
    }

}