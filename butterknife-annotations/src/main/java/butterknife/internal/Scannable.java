package butterknife.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a file for scannable.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {ElementType.TYPE})
public @interface Scannable {
}
