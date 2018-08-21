package butterknife.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class MethodViewBinding implements MemberViewBinding {
  private final String name;
  private final List<Parameter> parameters;
  private final boolean required;
  private final String[] requireds;
  private final String key;
  private final boolean handle;

  MethodViewBinding(String name, List<Parameter> parameters, boolean required) {
    this.name = name;
    this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
    this.required = required;
    this.requireds = null;
    this.key = null;
    this.handle = false;
  }

  MethodViewBinding(String name, List<Parameter> parameters,
                    String[] requireds, boolean handle, String key, boolean required) {
    this.name = name;
    this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
    this.required = required;
    this.requireds = requireds;
    this.handle = handle;
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public String[] getRequireds() {
    return null == requireds ? new String[0] : requireds;
  }

  public boolean isHandle() {
    return handle;
  }

  public String getKey() {
    return key;
  }

  @Override public String getDescription() {
    return "method '" + name + "'";
  }

  public boolean isRequired() {
    return required;
  }
}
