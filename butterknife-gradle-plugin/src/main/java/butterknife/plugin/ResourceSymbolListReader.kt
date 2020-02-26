package butterknife.plugin

import com.android.ide.common.util.multimapOf
import com.squareup.javapoet.CodeBlock
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

internal class ResourceSymbolListReader(private val builder: FinalRClassBuilder) {
  private var idValue = 0
  private val symbols = mutableMapOf<String, Int>()

  fun readSymbolCache(symbolTable: File) {
    symbolTable.forEachLine { processCacheLine(it) }
  }

  private fun processCacheLine(line: String) {
    val pair = line.split(",")
    val value = pair[0].toInt()
    val qualifiedName = pair[1]
    symbols[qualifiedName.substringAfterLast(".")] = value
  }

  fun readSymbolTable(symbolTable: File) {
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
    val value = CodeBlock.of("\$L", ++idValue)
    builder.addResourceField(symbolType, name, value)
  }
}
