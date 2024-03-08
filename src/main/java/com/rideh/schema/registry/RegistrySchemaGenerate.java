package com.rideh.schema.registry;

import com.rideh.schema.registry.codegen.CodeGen;
import com.rideh.schema.registry.codegen.CodeGenConfig;
import com.rideh.schema.registry.codegen.CodeGenProvider;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
    name = "generate",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE,
    threadSafe = true
)
@Slf4j
public class RegistrySchemaGenerate extends AbstractMojo {

  @Parameter(property = "sourceDirectory")
  String sourceDirectory;
  @Parameter(property = "targetPackage")
  String targetPackage;
  CodeGenProvider codeGenProvider = new CodeGenProvider();
  CodeGen codeGen = new CodeGen(codeGenProvider.getCodeModel(), codeGenProvider.schemaMapper());
  @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/schemas")
  private File outputDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    CodeGenConfig config = CodeGenConfig.builder()
        .sourceDirectory(sourceDirectory)
        .outputDirectory(outputDirectory.getAbsolutePath())
        .targetPackage(targetPackage)
        .build();

    try {
      codeGen.convertToPojo(config);
    } catch (IOException e) {
      log.error("Error generating POJOs", e);
      throw new RuntimeException(e);
    }
  }
}
