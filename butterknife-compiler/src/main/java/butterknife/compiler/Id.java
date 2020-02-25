package butterknife.compiler;

import androidx.annotation.Nullable;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.sun.tools.javac.code.Symbol;

import java.util.regex.Pattern;

/**
 * Represents an ID of an Android resource.
 */
final class Id {
  private static final ClassName ANDROID_R = ClassName.get("android", "R");
  private static final String R = "R";

  final int value;
  final CodeBlock code;
  final boolean qualifed;
  final String qualifiedName;

  Id(int value) {
    this(value, (Symbol) null);
  }

  Id(int value, String qualifiedName) {
    this.value = value;
    if (qualifiedName != null) {
      String ResClass = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
      String RClass = ResClass.substring(0, ResClass.lastIndexOf("."));
      ClassName RClassName = ClassName.bestGuess(RClass);
      ClassName className = ClassName.get(RClassName.packageName(), R,
              ResClass.substring(ResClass.lastIndexOf(".") + 1));
      String resourceName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);

      this.code = className.topLevelClassName().equals(ANDROID_R)
              ? CodeBlock.of("$L.$N", className, resourceName)
              : CodeBlock.of("$T.$N", className, resourceName);
      this.qualifiedName = qualifiedName;
      this.qualifed = true;
    } else {
      this.code = CodeBlock.of("$L", value);
      this.qualifiedName = null;
      this.qualifed = false;
    }
  }

  Id(int value, @Nullable Symbol rSymbol) {
    this.value = value;
    if (rSymbol != null) {
      ClassName className = ClassName.get(rSymbol.packge().getQualifiedName().toString(), R,
          rSymbol.enclClass().name.toString());
      String resourceName = rSymbol.name.toString();

      this.code = className.topLevelClassName().equals(ANDROID_R)
        ? CodeBlock.of("$L.$N", className, resourceName)
        : CodeBlock.of("$T.$N", className, resourceName);
      this.qualifiedName = code.toString();
      this.qualifed = true;
    } else {
      this.code = CodeBlock.of("$L", value);
      this.qualifiedName = null;
      this.qualifed = false;
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof Id && value == ((Id) o).value;
  }

  @Override public int hashCode() {
    return value;
  }

  @Override public String toString() {
    throw new UnsupportedOperationException("Please use value or code explicitly");
  }

  boolean isLiteral() {
    Pattern pattern = Pattern.compile("[\\d]*");
    return pattern.matcher(code.toString()).matches();
  }

}
