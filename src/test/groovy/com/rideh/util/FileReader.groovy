package com.rideh.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

import java.nio.file.Files
import java.nio.file.Paths

class FileReader {

    static def readFile(String path) {
        return new String(Files.readAllBytes(Paths.get(path)))
    }

    static def readFilePretty(String path) {
        String unPrettyFile = readFile(path)
        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode jsonNode = objectMapper.readValue(unPrettyFile, JsonNode.class)
        return objectMapper.writeValueAsString(jsonNode)
    }
}
