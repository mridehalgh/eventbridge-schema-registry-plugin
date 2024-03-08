# AWS EventBridge Schema Registry Maven Plugin

The AWS EventBridge Schema Registry Maven Plugin is a versatile tool that simplifies working with
AWS EventBridge schemas in your Java applications. This plugin provides two main commands:

1. `eventbridge-schema-registry:download`: Fetches schemas from the AWS EventBridge Schema Registry and caches
   them locally.
2. `eventbridge-schema-registry:generate`: Generates Java POJOs from the cached schemas.

## Features

- Pulls schemas from the AWS EventBridge Schema Registry and caches them locally.
- Generates Java POJOs based on the cached schemas.
- Supports customization of generated POJOs and schema caching through configuration.

## Prerequisites

Before using this plugin, make sure you have the following prerequisites installed and configured:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- AWS credentials and AWS CLI configured with access to the AWS EventBridge Schema Registry.

## Installation

You can easily add the AWS EventBridge Schema Registry Maven Plugin to your Maven project by
including it in your `pom.xml` file:

### Local install
First use `mvn install` to compiles, test, package, and install the Maven project's artifacts into the local Maven repository, this will make them available to other projects on the same machine.

```bash
mvn install
```

### Add dependency
Then add the following dependency to your application/services `pom.xml` file:

```xml
<build>
   <plugins>
      <plugin>
         <groupId>com.rideh</groupId>
         <artifactId>eventbridge-schema-registry-plugin</artifactId>
         <version>1.0-SNAPSHOT</version>
      </plugin>
   </plugins>
</build>

```

## Usage

### Pull and Cache Schemas

To pull and cache schemas from the AWS EventBridge Schema Registry, use the following command:

```shell
 mvn eventbridge-schema-registry:download
```

This command will fetch schemas from the registry and store them locally for offline usage.

### Generate POJOs from Cached Schemas

To generate Java POJOs from the cached schemas, use the following command:

```shell
 mvn eventbridge-schema-registry:generate
```

This command will generate Java POJOs based on the schemas that were previously cached.

### Configuration Options

You can configure the plugin in your pom.xml to specify the AWS EventBridge Schema Registry
location, output directory for cached schemas, and other options:

```xml

<build>
   <plugins>
      <plugin>
         <groupId>com.rideh</groupId>
         <artifactId>eventbridge-schema-registry-plugin</artifactId>
         <version>1.0-SNAPSHOT</version>
         <configuration>
            <registryName>demo-registry</registryName>
            <schemaNames>
               <schemaName>demo-schema</schemaName>
            </schemaNames>
            <versions>
               <version>1</version>
            </versions>
            <targetPackage>com.rideh.schemas</targetPackage>
            <sourceDirectory>${project.basedir}/src/main/resources/schemas</sourceDirectory>
         </configuration>
      </plugin>
   </plugins>
</build>

```
