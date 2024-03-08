package com.rideh.schema.registry.eventbridge;

class SchemaDoesNotExist extends RuntimeException {

  public SchemaDoesNotExist(String directory) {
    super("Schema directory does not exist: " + directory);
  }
}
