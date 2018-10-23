package butterknife.internal;

import androidx.annotation.Nullable;

/**
 * method delegate
 */
public abstract class MethodExecutor {

  private String methodName;
  private boolean invoked;
  private Object returnedValue;

  public MethodExecutor(String methodName) {
    this.methodName = methodName;
  }

  @Nullable
  protected abstract Object execute();

  final void invoke() {
    if (!invoked) {
      invoked = true;
      returnedValue = execute();
    }
  }

  public final String getMethodName() {
    return methodName;
  }

  public final Object getReturnedValue() {
    return returnedValue;
  }

  public final boolean isInvoked() {
    return invoked;
  }

  @Override
  public String toString() {
    return "MethodExecutor{" +
            "methodName='" + methodName + '\'' +
            ", invoked=" + invoked +
            '}';
  }
}
