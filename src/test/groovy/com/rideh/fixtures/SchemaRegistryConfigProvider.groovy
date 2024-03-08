package com.rideh.fixtures

import com.rideh.schema.registry.config.SchemaRegistryConfig

import static com.rideh.fixtures.CommonFixtures.*

class SchemaRegistryConfigProvider {

    static def make(Map<String, Object> map = [:]) {
        def props = SAMPLE_CONFIG + map

        return SchemaRegistryConfig.builder()
                .schemaNames(props.schemaNames as List)
                .registryName(props.registryName as String)
                .versions(props.versions as List)
                .sourcesDirectory(props.sourcesDirectory as String)
                .build()
    }

    private static Map SAMPLE_CONFIG = [
            schemaNames     : [SCHEMA_NAME],
            registryName    : REGISTRY_NAME,
            versions        : VERSIONS,
            sourcesDirectory: SOURCE_DIR
    ]

}
