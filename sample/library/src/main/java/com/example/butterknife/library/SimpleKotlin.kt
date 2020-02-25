package com.example.butterknife.library

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import butterknife.*

class SimpleKotlin : Activity() {

  @BindView(R2.id.title)
  lateinit var title: TextView
  @Bind(R2.id.subtitle) lateinit var subtitle: TextView
  @BindView(R2.id.hello) lateinit var hello: Button
  @BindView(R2.id.list_of_things)
  lateinit var listOfThings: ListView
  @BindView(R2.id.footer)
  lateinit var footer: TextView
  @BindViews(R2.id.title, R2.id.subtitle)
  lateinit var headerViews: Array<View>

  private var adapter: SimpleAdapter? = null

  @OnClick(R2.id.hello)
  fun sayHello() {
    Toast.makeText(this, "Hello, views!", Toast.LENGTH_SHORT).show()
    ViewCollections.run(headerViews, ALPHA_FADE)
  }
  /*@OnLongClick(R2.id.hello) fun sayGetOffMe(): Boolean {
    Toast.makeText(this, "Let go of me!", Toast.LENGTH_SHORT).show()
    return true
  }*/

  /*@OnItemClick(R2.id.list_of_things)
  fun onItemClick(position: Int) {
    Toast.makeText(this, "You clicked: " + adapter!!.getItem(position), Toast.LENGTH_SHORT).show()
  }*/

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.simple_activity)
    ButterKnife.bind(this)
    // Contrived code to use the bound fields.
    title.text = "Butter Knife"
    subtitle.setText("Field and method binding for Android views.");
    footer.text = "by Jake Wharton"
    adapter = SimpleAdapter(this)
    listOfThings.adapter = adapter
  }

  companion object {
    private val ALPHA_FADE = Action<View> { view, index ->
      val alphaAnimation = AlphaAnimation(0f, 1f)
      alphaAnimation.fillBefore = true
      alphaAnimation.duration = 500
      alphaAnimation.startOffset = index * 100.toLong()
      view.startAnimation(alphaAnimation)
    }
  }
}