package butterknife.internal;

import android.support.annotation.Nullable;

/**
 * method delegate
 */
public abstract class MethodExecutor {

  private boolean invoked;
  private Object returned;

  @Nullable
  protected abstract Object execute();

  final void invoke() {
    if (!invoked) {
      invoked = true;
      returned = execute();
    }
  }

  public final Object getReturned() {
    return returned;
  }

  public final boolean isInvoked() {
    return invoked;
  }

}
