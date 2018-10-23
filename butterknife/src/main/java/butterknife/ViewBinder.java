package butterknife;

import androidx.annotation.NonNull;
import android.view.View;

import butterknife.internal.ClickSession;

/**
 * The target interface to view Binding.
 *
 * @version 1.0
 *
 * Created by TeslaLiu on 2017/7/15.
 */
public interface ViewBinder {

    /**
     * before session executed
     * @param session   The session about this click event
     */
    void onPreClick(@NonNull ClickSession session);

    /**
     * after session executed
     * @param session   The session about this click event
     */
    void onPostClick(@NonNull ClickSession session);

    /**
     * return the view to bind
     */
    @NonNull
    View getView();

    /**
     * post Action
     * @param view      View
     * @param clazz     target class
     * @param method    click method
     * @param key       value of the annotation field key
     * @deprecated instead of {@link #onPreClick(ClickSession)}
     * and {@link #onPostClick(ClickSession)}
     */
    @Deprecated
    void postAction(View view, String clazz, String method, String key);

}
