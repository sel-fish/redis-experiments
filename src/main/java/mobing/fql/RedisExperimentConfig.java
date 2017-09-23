package mobing.fql;

/**
 * Created by fenqi on 16/6/8.
 */
public class RedisExperimentConfig {
    private String redisHost;
    private int redisPort;
    private int valLen;
    private String outputFile;
    private int startEntryNums;
    private int maxEntryNums;
    private int entryNumInterval;
    private int hashNums;

    public int getValLen() {
        return valLen;
    }

    public void setValLen(int valLen) {
        this.valLen = valLen;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public int getHashNums() {
        return hashNums;
    }

    public void setHashNums(int hashNums) {
        this.hashNums = hashNums;
    }

    public int getStartEntryNums() {
        return startEntryNums;
    }

    public void setStartEntryNums(int startEntryNums) {
        this.startEntryNums = startEntryNums;
    }

    public int getMaxEntryNums() {
        return maxEntryNums;
    }

    public void setMaxEntryNums(int maxEntryNums) {
        this.maxEntryNums = maxEntryNums;
    }

    public int getEntryNumInterval() {
        return entryNumInterval;
    }

    public void setEntryNumInterval(int entryNumInterval) {
        this.entryNumInterval = entryNumInterval;
    }
}
