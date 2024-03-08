package com.rideh.schema.registry.eventbridge;

import com.rideh.schema.registry.config.SchemaRegistryConfig;
import com.rideh.schema.registry.schemas.SchemaDownloader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.schemas.SchemasClient;
import software.amazon.awssdk.services.schemas.model.DescribeSchemaRequest;
import software.amazon.awssdk.services.schemas.model.DescribeSchemaResponse;

@Slf4j
public class EventBridgeSchemaDownloader implements SchemaDownloader {

  private static final String SUPPORTED_SCHEMA_FORMAT = "JSONSchemaDraft4";
  private final SchemasClient schemasClient;

  public EventBridgeSchemaDownloader(SchemasClient schemasClient) {

    this.schemasClient = schemasClient;
  }

  private static boolean sourceDirectoryDoesNotExist(Path sourcesDirectory) {
    return Files.notExists(sourcesDirectory);
  }

  public void downloadSchemas(SchemaRegistryConfig schemaRegistryConfig) {
    Path sourcesDirectory = Paths.get(schemaRegistryConfig.getSourcesDirectory());

    if (sourceDirectoryDoesNotExist(sourcesDirectory)) {
      throw new SourcesDirectoryDoesNotExist(sourcesDirectory.toString());
    }

    schemaRegistryConfig.getSchemaNames().forEach(schemaName -> {
      schemaRegistryConfig.getVersions().forEach(version -> {
        DescribeSchemaResponse schemaResponse = getSchemaExport(
            schemaRegistryConfig.getRegistryName(),
            schemaName, version);

        if (!schemaResponse.type().equals(SUPPORTED_SCHEMA_FORMAT)) {
          log.warn("Schema format not supported: " + schemaResponse.type() + ". Skipping.");
          return;
        }
        log.info("Writing schema to file: " + schemaName);

        writeSchemaToFile(schemaName, schemaResponse.content(),
            schemaRegistryConfig.getSourcesDirectory());

        log.info("Schema written to file: " + schemaName);
      });
    });
  }

  private DescribeSchemaResponse getSchemaExport(String registryName, String schemaName,
      String version) {
    DescribeSchemaRequest schemaRequest = DescribeSchemaRequest.builder()
        .schemaName(schemaName)
        .schemaVersion(version)
        .registryName(registryName)
        .build();

    return schemasClient.describeSchema(schemaRequest);
  }

  private void writeSchemaToFile(String schemaName, String schemaContent, String directory) {
    try {
      String fileName = directory + "/" + schemaName + ".json";
      Files.write(Paths.get(fileName), schemaContent.getBytes());
    } catch (IOException e) {
      log.error("Error while writing schema to file", e);
      e.printStackTrace();
    }
  }
}
