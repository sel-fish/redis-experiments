import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class:%line >> %msg%n"
    }
}

appender("FILE", FileAppender) {
    file = "mst.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class:%line >> %msg%n"
    }
}

logger("com.mogujie.mst", INFO, ["FILE"], false)

root(ERROR, ["CONSOLE"])
