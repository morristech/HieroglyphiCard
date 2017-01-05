package com.elegantsolutions.hieroglyphic.gift.service.internal;

import com.elegantsolutions.hieroglyphic.gift.service.BitmapManager;
import com.elegantsolutions.hieroglyphic.gift.service.GalleryManager;
import com.elegantsolutions.hieroglyphic.gift.service.HieroManager;
import com.elegantsolutions.hieroglyphic.gift.service.ImageManager;
import com.elegantsolutions.hieroglyphic.gift.service.MeasurementManager;
import com.elegantsolutions.hieroglyphic.gift.service.ProgressManager;
import com.elegantsolutions.hieroglyphic.gift.service.ShareManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    BitmapManager provideBitmapManager() {
        return new BitmapManagerImpl();
    }

    @Provides
    @Singleton
    GalleryManager provideGalleryManager() {
        return new GalleryManagerImpl();
    }

    @Provides
    @Singleton
    HieroManager provideHieroManager() {
        return new HieroManagerImpl();
    }

    @Provides
    @Singleton
    ImageManager provideImageManager(BitmapManager bitmapManager, MeasurementManager measurementManager) {
        return new ImageManagerImpl(bitmapManager, measurementManager);
    }

    @Provides
    @Singleton
    MeasurementManager provideMeasurementManager() {
        return new MeasurementManagerImpl();
    }

    @Provides
    @Singleton
    ProgressManager provideProgressManager() {
        return new ProgressManagerImpl();
    }

    @Provides
    @Singleton
    ShareManager provideShareManager() {
        return new ShareManagerImpl();
    }
}
