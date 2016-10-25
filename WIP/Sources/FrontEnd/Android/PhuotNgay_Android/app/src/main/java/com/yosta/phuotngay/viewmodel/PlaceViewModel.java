package com.yosta.phuotngay.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.yosta.phuotngay.BR;

public class PlaceViewModel extends BaseObservable {

    private String name = null;
    private String imageUrl = null;

    private String description = null;

    public PlaceViewModel(String name, String imageUrl, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Bindable
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
}
