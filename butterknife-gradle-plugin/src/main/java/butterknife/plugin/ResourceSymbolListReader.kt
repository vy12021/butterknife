package butterknife.plugin

import com.squareup.javapoet.CodeBlock
import java.io.File

internal class ResourceSymbolListReader(private val builder: FinalRClassBuilder) {
  private var idValue = 0
  private var idChanged = 0
  private var cacheVersion = 0
  private val cacheSymbols = mutableMapOf<String, Int>()

  fun process(symbolCacheDir: File, symbolTable: File) {
    readSymbolCache(symbolCacheDir)
    readSymbolTable(symbolTable)
    val versionFile = symbolCacheDir.resolve("version")
    if (versionFile.exists()) {
      cacheVersion = versionFile.readText().toInt()
      if (idChanged > 0) {
        versionFile.writeText((++cacheVersion).toString())
      }
    }
  }

  private fun readSymbolCache(symbolCache: File) {
    if (symbolCache.exists()) {
      symbolCache.resolve("index").forEachLine { processCacheLine(it) }
    }
  }

  private fun processCacheLine(line: String) {
    val pair = line.split(",")
    val value = pair[0].toInt()
    val qualifiedName = pair[1]
    cacheSymbols[qualifiedName.substringAfterLast(".")] = value
  }

  private fun readSymbolTable(symbolTable: File) {
    symbolTable.forEachLine { processLine(it) }
  }

  private fun processLine(line: String) {
    val values = line.split(' ')
    if (values.size < 4) {
      return
    }
    val javaType = values[0]
    if (javaType != "int") {
      return
    }
    val symbolType = values[1]
    if (symbolType !in SUPPORTED_TYPES) {
      return
    }
    val name = values[2]
    val cache = cacheSymbols.getOrDefault(name, 0)
    val value = CodeBlock.of("\$L",
            if (cacheSymbols.isEmpty()) {
              ++idValue
            } else {
              if (cache == 0) {
                (cacheVersion + 1) * 10000 + ++idChanged
              } else {
                cache
              }
            }
    )
    builder.addResourceField(symbolType, name, value)
  }
}
