package com.rideh.schema.registry;

import com.rideh.schema.registry.config.SchemaRegistryConfig;
import com.rideh.schema.registry.eventbridge.EventBridgeSchemaDownloader;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import software.amazon.awssdk.services.schemas.SchemasClient;

@Mojo(name = "download")
public class RegistrySchemaDownload extends AbstractMojo {

  private final EventBridgeSchemaDownloader schemaDownloader;

  @Parameter(property = "registryName")
  String registryName;

  @Parameter(property = "sourceDirectory")
  String sourceDirectory;

  @Parameter(property = "schemaNames.name")
  List<String> schemaNames;

  @Parameter(property = "versions")
  List<String> versions;

  public RegistrySchemaDownload() {
    this.schemaDownloader = new EventBridgeSchemaDownloader(SchemasClient.create());
  }

  public RegistrySchemaDownload(EventBridgeSchemaDownloader schemasClient) {
    this.schemaDownloader = schemasClient;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    SchemaRegistryConfig registryConfig = SchemaRegistryConfig.builder()
        .registryName(registryName)
        .sourcesDirectory(sourceDirectory)
        .schemaNames(schemaNames)
        .versions(versions)
        .build();

    schemaDownloader.downloadSchemas(registryConfig);
  }
}
