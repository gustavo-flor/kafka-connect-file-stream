package com.github.gustavoflor.kafkaconnectfilestream;

import com.github.gustavoflor.kafkaconnectfilestream.errors.RequiredConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static com.github.gustavoflor.kafkaconnectfilestream.FileStreamSinkConnector.FILENAME_CONFIG;
import static com.github.gustavoflor.kafkaconnectfilestream.util.VersionUtil.getVersion;
import static java.util.Optional.ofNullable;

public class FileStreamSinkTask extends SinkTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStreamSinkTask.class);

    private Map<String, String> properties;
    private String filename;
    private BufferedWriter bufferedFileWriter;

    @Override
    public void start(final Map<String, String> props) {
        LOGGER.info("Starting {}", getClass().getSimpleName());
        properties = props;
        filename = getFilename();
        bufferedFileWriter = getBufferedFileWriter();
    }

    @Override
    public synchronized void put(final Collection<SinkRecord> records) {
        records.stream()
                .map(SinkRecord::value)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .forEachOrdered(this::writeNewLineOnFile);
        saveFile();
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping {}", getClass().getSimpleName());
        try {
            bufferedFileWriter.close();
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }

    @Override
    public String version() {
        return getVersion();
    }

    private BufferedWriter getBufferedFileWriter() {
        try {
            return new BufferedWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }

    private void writeNewLineOnFile(final String value) {
        try {
            bufferedFileWriter.newLine();
            bufferedFileWriter.write(value);
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }

    private void saveFile() {
        try {
            bufferedFileWriter.flush();
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }

    private String getFilename() {
        final var value = properties.get(FILENAME_CONFIG);
        return ofNullable(value).orElseThrow(() -> new RequiredConfigException(FILENAME_CONFIG));
    }
}
