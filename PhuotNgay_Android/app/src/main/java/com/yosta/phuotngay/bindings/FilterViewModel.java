package com.yosta.phuotngay.bindings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yosta.phuotngay.BR;

public class FilterViewModel extends BaseObservable {

    private String mContent = null;

    public FilterViewModel(String content) {
        this.mContent = content;
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    @Bindable
    public void setContent(String content) {
        this.mContent = content;
        notifyPropertyChanged(BR.name);
    }
}
