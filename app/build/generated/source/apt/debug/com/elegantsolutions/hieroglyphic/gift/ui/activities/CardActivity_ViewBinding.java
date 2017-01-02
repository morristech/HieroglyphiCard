// Generated code from Butter Knife. Do not modify!
package com.elegantsolutions.hieroglyphic.gift.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.elegantsolutions.hieroglyphic.gift.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardActivity_ViewBinding<T extends CardActivity> implements Unbinder {
  protected T target;

  @UiThread
  public CardActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.userNameField = Utils.findRequiredViewAsType(source, R.id.userNameView, "field 'userNameField'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.userNameField = null;

    this.target = null;
  }
}
