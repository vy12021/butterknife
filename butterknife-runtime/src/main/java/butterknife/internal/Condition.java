package butterknife.internal;

import butterknife.OnClick;

/**
 * The class wrapper for {@link OnClick#required()}...
 */
public abstract class Condition {

  /**
   * {@link OnClick#required()}
   */
  public final String required;

  public Condition(String required) {
    this.required = required;
  }

  /**
   * checkRequired
   * @return  condition check result
   */
  protected abstract boolean require();

}
