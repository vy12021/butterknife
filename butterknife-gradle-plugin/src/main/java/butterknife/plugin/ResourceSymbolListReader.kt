package butterknife.plugin

import com.squareup.javapoet.CodeBlock
import java.io.File
import kotlin.math.max

internal class ResourceSymbolListReader(private val builder: FinalRClassBuilder) {
  private var idLastValue = 0
  private var idValue = 0
  private val cacheSymbols = mutableMapOf<String, Int>()
  private val changedSymbols = mutableMapOf<String, ArrayList<String>>()

  fun process(symbolCacheDir: File, symbolTable: File) {
    readSymbolCache(symbolCacheDir)
    readSymbolTable(symbolTable)
  }

  private fun readSymbolCache(symbolCacheDir: File) {
    symbolCacheDir.resolve("index").apply {
      if (exists()) {
        forEachLine { processCacheLine(it) }
      }
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
    changedSymbols.forEach { (type, names) ->
      names.forEach { name ->
        builder.addResourceField(type, name, CodeBlock.of("\$L", ++idLastValue))
      }
    }
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
    val idValue = if (cacheSymbols.isEmpty()) { ++idValue } else { cache }
    idLastValue = max(idLastValue, idValue)
    if (0 == idValue) {
      var values = changedSymbols[symbolType]
      if (null == values) {
        values = java.util.ArrayList()
        changedSymbols[symbolType] = values
      }
      values.add(name)
    } else {
      builder.addResourceField(symbolType, name, CodeBlock.of("\$L", idValue))
    }
  }
}
