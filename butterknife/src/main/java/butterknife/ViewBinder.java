package butterknife;

import android.view.View;

import androidx.annotation.NonNull;

import butterknife.internal.ClickSession;

/**
 * The target interface to view Binding.
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

}
