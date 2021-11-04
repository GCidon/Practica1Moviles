package gdv.ohno.androidengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;

import gdv.ohno.engine.Image;

public class AndroidImage implements Image {

    public AndroidImage(String name) {
        _image = null;
        _image = BitmapFactory.decodeFile(name);
    }


    @Override
    public int getWidth() {
        return _image.getWidth();
    }

    @Override
    public int getHeight() {
        return _image.getHeight();
    }

    public Bitmap getImage() {
        return _image;
    }

    Bitmap _image;
}
