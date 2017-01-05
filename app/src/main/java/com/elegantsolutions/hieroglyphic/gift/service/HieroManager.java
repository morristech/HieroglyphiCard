package com.elegantsolutions.hieroglyphic.gift.service;

import java.util.List;

/**
 * Created by hazemsaleh on 1/5/17.
 */
public interface HieroManager {
    List<int[]> convertEnglishNameToHiero(String name);

    boolean isValidEnglishName(String name);
}
