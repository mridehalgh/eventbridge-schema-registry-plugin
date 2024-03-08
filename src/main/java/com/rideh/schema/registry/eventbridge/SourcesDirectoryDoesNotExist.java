package com.rideh.schema.registry.eventbridge;

class SourcesDirectoryDoesNotExist extends RuntimeException {

  public SourcesDirectoryDoesNotExist(String directory) {
    super("Sources directory does not exist: " + directory);
  }
}
