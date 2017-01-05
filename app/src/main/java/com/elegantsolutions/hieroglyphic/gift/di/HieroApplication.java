package com.elegantsolutions.hieroglyphic.gift.di;

import android.app.Application;
import android.content.Context;

import com.elegantsolutions.hieroglyphic.gift.di.component.AppComponent;
import com.elegantsolutions.hieroglyphic.gift.di.component.DaggerAppComponent;
import com.elegantsolutions.hieroglyphic.gift.di.module.AppModule;

public class HieroApplication extends Application {
    private static AppComponent appComponent;
    private static HieroApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAppComponents();
    }

    public static HieroApplication get(Context context) {
        return (HieroApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void initAppComponents() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }
}
