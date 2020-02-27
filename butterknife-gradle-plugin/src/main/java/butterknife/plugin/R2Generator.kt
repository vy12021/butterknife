package butterknife.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
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

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.NONE)
  var rCacheDir: File? = null

  @Suppress("unused") // Invoked by Gradle.
  @TaskAction
  fun brewJava() {
    brewJava(rFile!!.singleFile, rCacheDir!!, outputDir!!, packageName!!, className!!)
  }
}

fun brewJava(
  rFile: File,
  rCacheDir: File,
  outputDir: File,
  packageName: String,
  className: String
) {
  FinalRClassBuilder(packageName, className)
      .also {
        ResourceSymbolListReader(it).apply {
          process(rCacheDir, rFile)
        }
      }
      .build()
      .writeTo(outputDir)
}
