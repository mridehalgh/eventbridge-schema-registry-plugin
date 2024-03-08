package com.rideh.schema.registry.schemas;

import com.rideh.schema.registry.config.SchemaRegistryConfig;

public interface SchemaDownloader {

  void downloadSchemas(SchemaRegistryConfig schemaRegistryConfig);
}
