package butterknife.internal;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import java.lang.reflect.Method;

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
  /**
   * Whether invoke executor when conditions valid.
   */
  public final boolean pendingRetry;

  public ClickSession(@Nullable Object target, @Nullable View view, @Nullable String key,
                      @Nullable Condition[] conditions, MethodExecutor executor,
                      boolean pendingRetry) {
    this.target = target;
    this.view = view;
    this.key = key;
    this.conditions = conditions;
    this.executor = executor;
    this.pendingRetry = pendingRetry;
  }

  /**
   * Run Action
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

  /**
   * Create a click session from an action.
   * @param action  runnable
   * @return        ClickSession
   */
  public static ClickSession create(final Runnable action) {
    return new ClickSession(null, null, null, null,
            new MethodExecutor(null) {
              @Nullable
              @Override
              protected Object execute() {
                action.run();
                return null;
              }
            }, true);
  }

  /**
   * Create a click session from an action.
   * @param action  runnable
   * @return        ClickSession
   */
  public static ClickSession create(@NonNull final ViewBinder binder,
                                    @NonNull final Runnable action,
                                    String... requireds) {
    return create((Object) binder, action, requireds);
  }

  /**
   * Create a click session from an action.
   * @param action  runnable
   * @return        ClickSession
   */
  public static ClickSession create(@NonNull final Object binder,
                                    @NonNull final Runnable action,
                                    String... requireds) {
    Condition[] conditions = new Condition[requireds.length];
    final ClickSession clickSession = new ClickSession(binder, null, null, conditions,
            new MethodExecutor(null) {
              @Nullable
              @Override
              protected Object execute() {
                action.run();
                return null;
              }
            }, true);
    for (int i = 0; i < requireds.length; i++) {
      conditions[i] = new Condition(requireds[i]) {
        @Override
        protected boolean require() {
          try {
            Method checkMethod = binder.getClass().getMethod(required, ClickSession.class);
            checkMethod.setAccessible(true);
            return (boolean) checkMethod.invoke(binder, clickSession);
          } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Resources.NotFoundException(
                    String.format("The method %s not found.", required));
          } catch (Exception e) {
            e.printStackTrace();
            return false;
          }
        }
      };
    }
    return clickSession;
  }

}
