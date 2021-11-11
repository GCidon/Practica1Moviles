package gdv.ohno.androidengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import gdv.ohno.engine.Image;

public class AndroidImage implements Image {

    public AndroidImage(String name, AssetManager manager) {
        try {
            _image = BitmapFactory.decodeStream(manager.open(name));
        } catch(Exception e) {
            e.printStackTrace();
        }
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
