package butterknife;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * the target interface on bind Onclick
 *
 * @version 1.0
 *
 * Created by TeslaLiu on 2017/7/15.
 */
public interface ViewController {

    /**
     * post Action
     * @param view      View
     * @param clazz     target class
     * @param method    click method
     * @param key       key of annotation param
     */
    void postAction(View view, String clazz, String method, String key);

    /**
     * return the view to bind
     */
    @NonNull
    View getView();

}
