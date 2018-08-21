package butterknife.internal;

import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import butterknife.ViewController;

public class ClickSession {

  public final Object target;
  public final View view;
  private final Condition[] conditions;
  private final MethodExecutor executor;

  public ClickSession(Object target, View view,
                      @Nullable Condition[] conditions, MethodExecutor executor) {
    this.target = target;
    this.view = view;
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

  public final Object getInvokeReturned() {
    return executor.getReturned();
  }

}
