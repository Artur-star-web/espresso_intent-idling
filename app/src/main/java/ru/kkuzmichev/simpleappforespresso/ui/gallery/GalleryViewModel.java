package ru.kkuzmichev.simpleappforespresso.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<GalleryItem>> galleryData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
        isLoading.setValue(false);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<GalleryItem>> getGalleryData() {
        return galleryData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public void loadGalleryData() {
        isLoading.setValue(true);


        new Thread(() -> {
            try {

                Thread.sleep(1000); // Это НЕ в UI потоке, поэтому допустимо

                List<GalleryItem> data = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data.add(new GalleryItem("Gallery Item " + (i + 1),
                            "Description for item " + (i + 1),
                            i + 1));
                }


                galleryData.postValue(data);
                isLoading.postValue(false);

            } catch (InterruptedException e) {
                isLoading.postValue(false);
                e.printStackTrace();
            }
        }).start();
    }
}