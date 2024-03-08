package com.rideh.schema.registry.codegen

import com.rideh.fixtures.CommonFixtures
import com.sun.codemodel.JCodeModel
import org.jsonschema2pojo.SchemaMapper
import org.mockito.Mock
import org.mockito.Mockito
import spock.lang.Specification

class CodeGenTest extends Specification {

    @Mock
    def mockJCodeModel = Mockito.mock(JCodeModel)
    def mockSchemaMapper = Mock(SchemaMapper);
    def codegen = new CodeGen(mockJCodeModel, mockSchemaMapper);

    def "generates pojos"() {
        given: "a schema exists and has been downloaded"
        def config = CodeGenConfig.builder()
                .sourceDirectory("src/test/resources/schemas")
                .targetPackage("com.rideh.schema.registry.codegen")
                .outputDirectory("src/test/java/com/rideh/codegen")
                .build()

        when: "the schema is converted to a pojo"
        def expectedUrl = new File(CommonFixtures.OUTPUT_FILE).toURI().toURL()
        1 * mockSchemaMapper.generate(mockJCodeModel, "DemoSchema", "com.rideh.schema.registry.codegen.demoschema", expectedUrl)
        codegen.convertToPojo(config)

        then: "the pojo is written to the correct directory"
        def expectedFile = new File(config.outputDirectory)
        Mockito.verify(mockJCodeModel, Mockito.times(1)).build(expectedFile)
    }
}
