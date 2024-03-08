package com.rideh.schema.registry.codegen;

import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;

public class CodeGenProvider {

  public JCodeModel getCodeModel() {
    return new JCodeModel();
  }

  public SchemaMapper schemaMapper() {
    return new SchemaMapper(
        new RuleFactory(getConfig(), new Jackson2Annotator(getConfig()), new SchemaStore()),
        new SchemaGenerator());

  }

  private GenerationConfig getConfig() {
    return new DefaultGenerationConfig() {
      @Override
      public boolean isGenerateBuilders() { // set config option by overriding method
        return true;
      }
    };
  }
}
