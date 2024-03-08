package com.rideh.schema.registry.eventbridge


import com.rideh.fixtures.CommonFixtures
import com.rideh.fixtures.SchemaRegistryConfigProvider
import com.rideh.util.FileReader
import software.amazon.awssdk.services.schemas.SchemasClient
import software.amazon.awssdk.services.schemas.model.DescribeSchemaResponse
import spock.lang.Specification

class EventBridgeSchemaDownloaderSpec extends Specification {

    private final SchemasClient schemasClient = Mock(SchemasClient)
    private final EventBridgeSchemaDownloader schemaDownloader = new EventBridgeSchemaDownloader(schemasClient);


    def "download schemas from a list"() {
        given: "a schema exists in eventbridge"
        def schemaConfig = SchemaRegistryConfigProvider.make()
        def expected = FileReader.readFilePretty(CommonFixtures.TEST_DATA_SCHEMA_FILE)

        1 * schemasClient.describeSchema(_) >> {
            return buildSuccessfulDescribeSchemaResponse(expected)
        }

        when: "the schema is downloaded"
        schemaDownloader.downloadSchemas(schemaConfig);

        then: "the file is exported to the correct location"
        def result = FileReader.readFilePretty(CommonFixtures.OUTPUT_FILE)
        expected == result
    }

    def "throw error if schema directory doesn't exist"() {
        when: "a directory to export the schemas doesn't exist"
        def schemaConfig = SchemaRegistryConfigProvider.make(sourcesDirectory: CommonFixtures.BAD_DIRECTORY)
        schemaDownloader.downloadSchemas(schemaConfig)

        then: "an error is thrown"
        thrown(SourcesDirectoryDoesNotExist)
    }

    def buildSuccessfulDescribeSchemaResponse(String expected) {
        return DescribeSchemaResponse.builder()
                .content(expected)
                .type(CommonFixtures.SUPPORTED_SCHEMA_FORMAT)
                .build()
    }
}
