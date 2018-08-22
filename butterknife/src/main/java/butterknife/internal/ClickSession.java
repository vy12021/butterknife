package butterknife.internal;

import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.ViewBinder;

/**
 * The Wrapper class of binding context for one click event.
 */
public class ClickSession {

  /**
   * The target for view binding
   * @see ViewBinder
   */
  public final Object target;
  /**
   * The view bind
   */
  public final View view;
  /**
   * @see OnClick#key()
   * @see OnItemClick#key()
   * ...
   */
  public final String key;
  /**
   * @see OnClick#required()
   * @see OnItemClick#required()
   * ...
   */
  private final Condition[] conditions;
  /**
   * A delegate for method binding invoke.
   */
  public final MethodExecutor executor;

  public ClickSession(Object target, View view, @Nullable String key,
                      @Nullable Condition[] conditions, MethodExecutor executor) {
    this.target = target;
    this.view = view;
    this.key = key;
    this.conditions = conditions;
    this.executor = executor;
  }

  /**
   * run Action
   * @param checkRequired true, check conditions of {@link OnClick#required()}
   */
  public final boolean execute(boolean checkRequired) {
    if (checkRequired && null != conditions && conditions.length > 0) {
      for (Condition condition : conditions) {
        if (!condition.require()) {
          return false;
        }
      }
    }
    executor.invoke();
    return true;
  }

}
