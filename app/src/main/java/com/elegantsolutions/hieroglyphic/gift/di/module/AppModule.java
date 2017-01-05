package com.elegantsolutions.hieroglyphic.gift.di.module;

import android.app.Application;
import android.content.res.Resources;

import com.elegantsolutions.hieroglyphic.gift.di.HieroApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    HieroApplication app;

    public AppModule(HieroApplication application) {
        app = application;
    }

    @Provides
    @Singleton
    protected Application provideApplication() {
        return app;
    }


    @Provides
    @Singleton
    protected Resources provideResources() {
        return app.getResources();
    }
}
