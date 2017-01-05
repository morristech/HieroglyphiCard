package com.elegantsolutions.hieroglyphic.gift.di.component;

import com.elegantsolutions.hieroglyphic.gift.di.module.AppModule;
import com.elegantsolutions.hieroglyphic.gift.service.internal.ServiceModule;
import com.elegantsolutions.hieroglyphic.gift.ui.activities.BaseActivity;
import com.elegantsolutions.hieroglyphic.gift.ui.activities.CardActivity;
import com.elegantsolutions.hieroglyphic.gift.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(CardActivity cardActivity);
}
