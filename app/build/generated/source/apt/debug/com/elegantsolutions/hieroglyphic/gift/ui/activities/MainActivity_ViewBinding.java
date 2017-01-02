// Generated code from Butter Knife. Do not modify!
package com.elegantsolutions.hieroglyphic.gift.ui.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.elegantsolutions.hieroglyphic.gift.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> implements Unbinder {
  protected T target;

  private View view2131427453;

  private View view2131427451;

  @UiThread
  public MainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.createCard, "field 'createCard' and method 'onCreateCard'");
    target.createCard = Utils.castView(view, R.id.createCard, "field 'createCard'", Button.class);
    view2131427453 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCreateCard();
      }
    });
    view = Utils.findRequiredView(source, R.id.cardPhoto, "field 'cardPhoto' and method 'onSelectCardPhoto'");
    target.cardPhoto = Utils.castView(view, R.id.cardPhoto, "field 'cardPhoto'", ImageView.class);
    view2131427451 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectCardPhoto();
      }
    });
    target.userNameField = Utils.findRequiredViewAsType(source, R.id.userName, "field 'userNameField'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.createCard = null;
    target.cardPhoto = null;
    target.userNameField = null;

    view2131427453.setOnClickListener(null);
    view2131427453 = null;
    view2131427451.setOnClickListener(null);
    view2131427451 = null;

    this.target = null;
  }
}
