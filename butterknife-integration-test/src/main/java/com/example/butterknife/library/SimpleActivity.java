package com.example.butterknife.library;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.butterknife.R;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;
import butterknife.ViewBinder;
import butterknife.internal.ClickSession;

import static android.widget.Toast.LENGTH_SHORT;

public class SimpleActivity extends Activity implements ViewBinder, Condition {

  private final static String TAG = SimpleActivity.class.getSimpleName();

  private static final ButterKnife.Action<View> ALPHA_FADE = new ButterKnife.Action<View>() {
    @Override public void apply(@NonNull View view, int index) {
      AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
      alphaAnimation.setFillBefore(true);
      alphaAnimation.setDuration(500);
      alphaAnimation.setStartOffset(index * 100);
      view.startAnimation(alphaAnimation);
    }
  };

  @Bind(R.id.title) TextView title;
  @BindView(R.id.subtitle) TextView subtitle;
  @BindView(R.id.hello) Button hello;
  @BindView(R.id.list_of_things) ListView listOfThings;
  @BindView(R.id.footer) TextView footer;
  @BindString(R.string.app_name) String butterKnife;
  @BindString(R.string.field_method) String fieldMethod;
  @BindString(R.string.by_jake_wharton) String byJakeWharton;
  @BindString(R.string.say_hello) String sayHello;

  @BindViews({ R.id.title, R.id.subtitle, R.id.hello }) List<View> headerViews;

  private boolean retryFlag;
  private SimpleAdapter adapter;

  @OnClick(value = {R.id.hello}, required = {"condition"}, handle = true, key = "hello") void sayHello() {
    Toast.makeText(this, "Hello, views!", LENGTH_SHORT).show();
    ButterKnife.apply(headerViews, ALPHA_FADE);
  }

  @OnLongClick(value = R.id.hello, required = {"condition"}, handle = true, key = "hello Long") boolean sayGetOffMe() {
    Toast.makeText(this, "Let go of me!", LENGTH_SHORT).show();
    return true;
  }

  @OnItemClick(R.id.list_of_things) void onItemClick(int position) {
    Toast.makeText(this, "You clicked: " + adapter.getItem(position), LENGTH_SHORT).show();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    ButterKnife.bind((ViewBinder) this);

    // Contrived code to use the bound fields.
    title.setText(butterKnife);
    subtitle.setText(fieldMethod);
    footer.setText(byJakeWharton);
    hello.setText(sayHello);

    adapter = new SimpleAdapter(this);
    listOfThings.setAdapter(adapter);
  }

  @Override
  public void postAction(View view, String clazz, String method, String key) {
    Log.e(TAG, clazz + "." + method + ": " + key);
  }

  @Override
  public void onPreClick(@NonNull ClickSession session) {
    Log.e(TAG, "onPreClick--->" + session.executor.toString());
  }

  @Override
  public void onPostClick(@NonNull ClickSession session) {
    Log.e(TAG, "onPostClick--->" + session.executor.toString());
  }

  @NonNull
  @Override
  public View getView() {
    return getWindow().getDecorView();
  }

  @Override
  public boolean condition() {
    Log.e(TAG, "Click to test condition: ");
    return retryFlag;
  }

  @Override
  public boolean condition(final ClickSession session) {
    Log.e(TAG, "Click to test condition session: " + retryFlag);
    if (!retryFlag) {
      getView().postDelayed(new Runnable() {
        @Override
        public void run() {
          retryFlag = !retryFlag;
          session.execute(true);
        }
      }, 3000);
    }
    return retryFlag;
  }

}
