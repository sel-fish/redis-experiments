package mobing.fql.stackoverflow;

import mobing.fql.RedisExperimentConfig;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.Jedis;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fenqi on 16/6/7.
 */
public class Gopall {
    private static final Logger log = LoggerFactory.getLogger(Gopall.class);
    private static final String REDIS_EXPERIMENT_CONFIG_FILE = "re.yml";

    private Jedis jedis = null;
    private RedisExperimentConfig config = null;
    private FileWriter writer = null;

    public boolean init() {
        try {
            config = new Yaml().loadAs(new FileInputStream(REDIS_EXPERIMENT_CONFIG_FILE),
                    RedisExperimentConfig.class);
            jedis = new Jedis(config.getRedisHost(), config.getRedisPort());
            if (null != config.getOutputFile()) {
                writer = new FileWriter(config.getOutputFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("init fail, config file is {}", REDIS_EXPERIMENT_CONFIG_FILE);
            return false;

//            InterruptedIOException interruptedIOException = null;
        }

        log.info("init ok...");
        return true;
    }

    public static void main(String[] args) throws IOException {
        Gopall gopall = new Gopall();

        if (!gopall.init()) {
            log.error("init failed...");
            System.exit(-1);
        }

        gopall.runDifferentSize();
        gopall.close();

        System.exit(0);
    }

    private void close() throws IOException {
        if (null != jedis) {
            jedis.close();
        }
        if (null != writer) {
            writer.close();
        }
        log.info("close ok...");
    }

    public static long getUsed(String infoResult) {
        return getSectionValue(infoResult, "used_memory");
    }

    public static long getRss(String infoResult) {
        return getSectionValue(infoResult, "used_memory_rss");
    }

    public static long getSectionValue(String infoResult, String keyword) {
        String[] lines = infoResult.split("\\r?\\n");
        String strResult = null;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(keyword)) {
                strResult = lines[i].split(":")[1];
                break;
            }
        }

        if (null == strResult) {
            return -1;
        } else {
            return Long.valueOf(strResult);
        }
    }

    private void runDifferentSize() throws IOException {
        log.info("run different size start...");

        String value = String.valueOf(new char[config.getValLen()]);

        for (int i = config.getStartEntryNums(); i <= config.getMaxEntryNums(); i += config.getEntryNumInterval()) {
            for (int j = 0; j < config.getHashNums(); j++) {
                if (i == config.getStartEntryNums()) {
                    for (int k = 0; k < i; k++) {
                        log.debug("hset {} {} value length : {}",
                                j, k, value.length());
                        jedis.hset(String.valueOf(j), String.valueOf(k), value);
                    }
                } else {
                    for (int k = 0; k < config.getEntryNumInterval(); k++) {
                        log.debug("hset {} {} value length : {}",
                                j, i - config.getEntryNumInterval() + k, value.length());
                        jedis.hset(String.valueOf(j), String.valueOf(i - config.getEntryNumInterval() + k), value);
                    }
                }
            }
            String infoResult = jedis.info("Memory");
            String record = i + " " + getRss(infoResult) + " " + getUsed(infoResult);
            if (null == writer) {
                System.out.println(record);
            } else {
                writer.write(record);
                writer.write("\n");
            }
        }

        log.info("run different size over...");
    }
}
