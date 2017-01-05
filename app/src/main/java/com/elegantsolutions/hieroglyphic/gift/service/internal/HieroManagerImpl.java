package com.elegantsolutions.hieroglyphic.gift.service.internal;

import android.util.Log;

import com.elegantsolutions.hieroglyphic.gift.R;
import com.elegantsolutions.hieroglyphic.gift.service.HieroManager;

import java.util.ArrayList;
import java.util.List;

class HieroManagerImpl implements HieroManager {
    private static final String WORD_SEP = "^";
    private static final String CHAR_SEP = "|";
    private static final String TAG = HieroManagerImpl.class.getSimpleName();

    @Override
    public List<int[]> convertEnglishNameToHiero(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }

        name = name.toLowerCase();

        // Make pretty string
        name = name.replaceAll("\\s+", WORD_SEP).trim();

        // Replace special characters
        name = name.replaceAll("sh", R.drawable.sh + CHAR_SEP);
        name = name.replaceAll("ch", R.drawable.ch + CHAR_SEP);
        name = name.replaceAll("kh", R.drawable.kh + CHAR_SEP);
        name = name.replaceAll("ms", R.drawable.ms + CHAR_SEP);
        name = name.replaceAll("nh", R.drawable.nh + CHAR_SEP);

        // Replace normal characters with known indices.
        name = name.replaceAll("a", R.drawable.a + CHAR_SEP);
        name = name.replaceAll("b", R.drawable.b + CHAR_SEP);
        name = name.replaceAll("c", R.drawable.c + CHAR_SEP);
        name = name.replaceAll("d", R.drawable.d + CHAR_SEP);
        name = name.replaceAll("e", R.drawable.e + CHAR_SEP);
        name = name.replaceAll("f", R.drawable.f + CHAR_SEP);
        name = name.replaceAll("g", R.drawable.g + CHAR_SEP);
        name = name.replaceAll("h", R.drawable.h + CHAR_SEP);
        name = name.replaceAll("i", R.drawable.i + CHAR_SEP);
        name = name.replaceAll("j", R.drawable.j + CHAR_SEP);
        name = name.replaceAll("k", R.drawable.k + CHAR_SEP);
        name = name.replaceAll("l", R.drawable.l + CHAR_SEP);
        name = name.replaceAll("m", R.drawable.m + CHAR_SEP);
        name = name.replaceAll("n", R.drawable.n + CHAR_SEP);
        name = name.replaceAll("o", R.drawable.o + CHAR_SEP);
        name = name.replaceAll("p", R.drawable.p + CHAR_SEP);
        name = name.replaceAll("q", R.drawable.q + CHAR_SEP);
        name = name.replaceAll("r", R.drawable.r + CHAR_SEP);
        name = name.replaceAll("s", R.drawable.s + CHAR_SEP);
        name = name.replaceAll("t", R.drawable.t + CHAR_SEP);
        name = name.replaceAll("u", R.drawable.u + CHAR_SEP);
        name = name.replaceAll("v", R.drawable.v + CHAR_SEP);
        name = name.replaceAll("w", R.drawable.w + CHAR_SEP);
        name = name.replaceAll("x", R.drawable.x + CHAR_SEP);
        name = name.replaceAll("y", R.drawable.y + CHAR_SEP);
        name = name.replaceAll("z", R.drawable.z + CHAR_SEP);

        // Prepare the arrays
        String []words = name.split("\\" + WORD_SEP);
        List<int[]> wordList = new ArrayList<int[]>();


        for (String word:words) {
            Log.d(TAG, "Word = " + word);

            String[] chars = word.split("\\" + CHAR_SEP);

            Log.d(TAG, "Number of chars in word = " + chars.length);

            int[] partInts = new int[chars.length];

            for (int i = 0; i < chars.length; ++i) {
                Log.d(TAG, "chars["+i+"] = " + chars[i]);

                partInts[i] = Integer.parseInt(chars[i]);
            }

            wordList.add(partInts);
        }

        return wordList;
    }

    @Override
    public boolean isValidEnglishName(String name) {
        return name.matches("^([a-zA-Z]+\\s+)*[a-zA-Z]+$");
    }
}
