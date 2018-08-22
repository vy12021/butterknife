package butterknife;

import butterknife.compiler.ButterKnifeProcessor;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/** This augments {@link OnClickTest} with tests that exercise callbacks with return types. */
public class OnLongClickTest {
  @Test public void onLongClickBinding() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import android.view.View;\n"
        + "import butterknife.OnLongClick;\n"
        + "import butterknife.ViewBinder;\n"
        + "public class Test implements ViewBinder {\n"
        + "  @OnLongClick(value = {1}, required = {\"condition\"}, key = \"Key\") boolean doStuff() {\n"
        + "    return true;\n"
        + "  }\n"

        + "public boolean condition() {return true;}\n"
        + "@Override public void postAction(View view, String clazz, String method, String key) {}\n"
        + "@Override public View getView() {return null;}\n"

            + "}"
    );

    JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ViewBinding", ""
        + "package test;\n"
        + "import android.support.annotation.CallSuper;\n"
        + "import android.support.annotation.UiThread;\n"
        + "import android.view.View;\n"
        + "import butterknife.Unbinder;\n"
        + "import butterknife.ViewBinder;\n"
        + "import butterknife.internal.Utils;\n"
        + "import java.lang.IllegalStateException;\n"
        + "import java.lang.Override;\n"
        + "public class Test_ViewBinding implements Unbinder {\n"
        + "  private Test target;\n"
        + "  private View view1;\n"
        + "  @UiThread\n"
        + "  public Test_ViewBinding(final Test target, View source) {\n"
        + "    this.target = target;\n"
        + "    View view;\n"
        + "    view = Utils.findRequiredView(source, 1, \"method 'doStuff'\");\n"
        + "    view1 = view;\n"
        + "    view.setOnLongClickListener(new View.OnLongClickListener() {\n"
        + "      @Override\n"
        + "      public boolean onLongClick(View p0) {\n"
        + "        if (!ViewBinder.class.isInstance(target)) {\n"
        + "          throw new RuntimeException(\"Target must be implements from ViewBinder\");\n"
        + "        }\n"
        + "        if (!target.condition()) {\n"
        + "          return false;\n"
        + "        }\n"
        + "        target.postAction(p0, \"test.Test\", \"doStuff\", \"Key\");\n"
        + "        return target.doStuff();\n"
        + "      }\n"
        + "    });\n"
        + "  }\n"
        + "  @Override\n"
        + "  @CallSuper\n"
        + "  public void unbind() {\n"
        + "    if (target == null) throw new IllegalStateException(\"Bindings already cleared.\");\n"
        + "    target = null;\n"
        + "    view1.setOnLongClickListener(null);\n"
        + "    view1 = null;\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .withCompilerOptions("-Xlint:-processing")
        .processedWith(new ButterKnifeProcessor())
        .compilesWithoutWarnings()
        .and()
        .generatesSources(bindingSource);
  }

  @Test public void failsIfMissingReturnType() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
        + "package test;\n"
        + "import butterknife.OnLongClick;\n"
        + "public class Test {\n"
        + "  @OnLongClick(1)\n"
        + "  public void doStuff() {\n"
        + "  }\n"
        + "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new ButterKnifeProcessor())
        .failsToCompile()
        .withErrorContaining(
            "@OnLongClick methods must have a 'boolean' return type. (test.Test.doStuff)")
        .in(source).onLine(5);
  }
}
