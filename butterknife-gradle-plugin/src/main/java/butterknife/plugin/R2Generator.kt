package butterknife.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class R2Generator : DefaultTask() {
  @get:OutputDirectory
  var outputDir: File? = null

  @get:InputFiles
  @get:PathSensitive(PathSensitivity.NONE)
  var rFile: FileCollection? = null

  @get:Input
  var packageName: String? = null

  @get:Input
  var className: String? = null

  @get:InputFiles
  @get:PathSensitive(PathSensitivity.NONE)
  var rCache: FileCollection? = null

  @Suppress("unused") // Invoked by Gradle.
  @TaskAction
  fun brewJava() {
    brewJava(rFile!!.singleFile, rCache!!.singleFile,  outputDir!!, packageName!!, className!!)
  }
}

fun brewJava(
  rFile: File,
  rCacheFile: File,
  outputDir: File,
  packageName: String,
  className: String
) {
  FinalRClassBuilder(packageName, className)
      .also {
        ResourceSymbolListReader(it).apply {
          readSymbolCache(rCacheFile)
          readSymbolTable(rFile)
        }
      }
      .build()
      .writeTo(outputDir)
}
