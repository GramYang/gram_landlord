package com.gram.gram_landlord.event;

import android.net.Uri;

public class PictureCompressEvent {
    private Uri pictureUri;

    public PictureCompressEvent(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }
}
