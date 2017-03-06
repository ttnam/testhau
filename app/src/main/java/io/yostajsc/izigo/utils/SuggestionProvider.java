package io.yostajsc.izigo.utils;

import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * Created by Phuc-Hau Nguyen on 9/8/2016.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.yosta.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    public void Clear() {
        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(getContext(), AUTHORITY, MODE);
        suggestions.clearHistory();
    }
}
