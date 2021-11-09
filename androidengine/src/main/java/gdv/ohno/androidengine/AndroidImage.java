package gdv.ohno.androidengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

import gdv.ohno.engine.Image;

public class AndroidImage implements Image {

    public AndroidImage(String name) {
        File image = new File(name);
        if(image.exists()) {
            _image = BitmapFactory.decodeFile(name);
        } else {
            System.out.println("Cagaste");
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
