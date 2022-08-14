package com.github.gustavoflor.kafkaconnectfilestream;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.github.gustavoflor.kafkaconnectfilestream.util.VersionUtil.getVersion;
import static java.util.Collections.singletonList;
import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

public class FileStreamSinkConnector extends SinkConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStreamSinkConnector.class);
    public static final String FILENAME_CONFIG = "filename";
    private static final String FILENAME_DOC = "Name of the file to be written";

    private Map<String, String> properties;

    @Override
    public void start(final Map<String, String> props) {
        LOGGER.info("Starting {}", getClass().getSimpleName());
        properties = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileStreamSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(final int maxTasks) {
        return singletonList(properties);
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping {}", getClass().getSimpleName());
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define(FILENAME_CONFIG, STRING, HIGH, FILENAME_DOC);
    }

    @Override
    public String version() {
        return getVersion();
    }
}
