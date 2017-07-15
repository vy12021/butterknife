package butterknife.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class MethodViewBinding implements MemberViewBinding {
  private final String name;
  private final List<Parameter> parameters;
  private final boolean required;
  private final String[] conditions;
  private final String key;

  MethodViewBinding(String name, List<Parameter> parameters, boolean required) {
    this.name = name;
    this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
    this.required = required;
    this.conditions = null;
    this.key = null;
  }

  MethodViewBinding(String name, List<Parameter> parameters,
                    String[] conditions, String key, boolean required) {
    this.name = name;
    this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
    this.required = required;
    this.conditions = conditions;
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public String[] getConditions() {
    return conditions;
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
