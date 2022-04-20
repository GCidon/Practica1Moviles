package gdv.ohno.pcengine;

import java.awt.*;
import java.io.File;
import gdv.ohno.engine.Font;

public class PCFont implements Font {
    public PCFont(String is, float _size, boolean _isBold) {
        java.awt.Font auxFont;
        try
        {
            auxFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/"+ is));
        }
        catch(Exception e)
        {
            System.err.println("Error cargando la fuente: " + e);
            return;
        }
        if (_isBold)
            f = auxFont.deriveFont(java.awt.Font.BOLD, _size);
        else
            f = auxFont.deriveFont(_size);
    }

    //Getter de la fuente
    public java.awt.Font getFont() { return f; }

    public java.awt.Font f;
}
