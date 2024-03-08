package com.rideh.schema.registry.codegen;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CodeGenConfig {

  String sourceDirectory;
  String outputDirectory;
  String targetPackage;
}
