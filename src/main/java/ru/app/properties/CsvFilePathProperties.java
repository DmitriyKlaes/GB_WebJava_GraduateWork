package ru.app.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application.csv")
public class CsvFilePathProperties {

    private String path;

}
