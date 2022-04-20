package gdv.ohno.pcengine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gdv.ohno.engine.Image;

public class PCImage implements Image {

    public PCImage(String name) {
        _image = null;
        try {
            _image = ImageIO.read(new File("data/"+name));
        } catch (IOException e) {
            System.err.println("Error cargando la imagen: " + e);
            return;
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

    public BufferedImage getImage() {
        return _image;
    }

    private BufferedImage _image;
}
