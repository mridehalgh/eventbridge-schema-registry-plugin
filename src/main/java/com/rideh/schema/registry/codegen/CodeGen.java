package com.rideh.schema.registry.codegen;

import com.sun.codemodel.JCodeModel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.WordUtils;
import org.jsonschema2pojo.SchemaMapper;

@Slf4j
public class CodeGen {

  private final JCodeModel codeModel;

  private final SchemaMapper mapper;

  public CodeGen(JCodeModel codeModel, SchemaMapper mapper) {
    this.codeModel = codeModel;
    this.mapper = mapper;
  }

  private static Set<URL> getFiles(String sourceDirectory) {
    return Stream.of(Objects.requireNonNull(new File(sourceDirectory).listFiles()))
        .filter(file -> !file.isDirectory())
        .map(file1 -> {
          try {
            return file1.toURI().toURL();
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toSet());
  }

  public void convertToPojo(CodeGenConfig codeGenConfig) throws IOException {
    String sourceDirectory = FilenameUtils.normalize(codeGenConfig.getSourceDirectory());
    String targetDirectory = FilenameUtils.normalize(codeGenConfig.getOutputDirectory());

    createDirectoryIfDoesNotExist(targetDirectory);

    Set<URL> files = getFiles(sourceDirectory);

    files.forEach(url -> {
      try {
        generatePojo(url, codeGenConfig);
      } catch (IOException e) {
        log.error("Unable to generate POJO for {}", url.getFile());
        throw new RuntimeException(e);
      }
    });

  }

  private void createDirectoryIfDoesNotExist(String targetDirectory) throws IOException {
    Path path = Path.of(targetDirectory);

    if (Files.notExists(path)) {
      Files.createDirectories(path);
    }
  }

  private void generatePojo(URL url, CodeGenConfig codeGenConfig) throws IOException {
    String fileName = FilenameUtils.removeExtension(FilenameUtils.getName(url.getFile()));
    String className = WordUtils.capitalizeFully(fileName, new char[]{'/', '.', '-', '_'})
        .replaceAll("[^A-Za-z0-9 ]", "");
    String lowerClassName = className.toLowerCase();
    String packageName = codeGenConfig.getTargetPackage() + "." + lowerClassName;

    mapper.generate(codeModel, className, packageName, url);
    File destination = new File(codeGenConfig.getOutputDirectory());
    codeModel.build(destination);
  }
}
