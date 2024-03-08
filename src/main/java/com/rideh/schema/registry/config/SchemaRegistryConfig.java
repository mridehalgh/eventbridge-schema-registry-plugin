package com.rideh.schema.registry.config;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SchemaRegistryConfig {

  String registryName;
  String sourcesDirectory;
  List<String> schemaNames;
  List<String> versions;
}
