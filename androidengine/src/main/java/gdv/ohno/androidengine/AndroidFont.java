package gdv.ohno.androidengine;
import android.graphics.Typeface;
import android.content.Context;
import gdv.ohno.engine.Font;

public class AndroidFont implements Font {
    public AndroidFont(String filename,float size, boolean isBold, Context context) {
        try {
            f = Typeface.createFromAsset(context.getAssets(), filename);
        } catch(Exception e) {
            e.printStackTrace();
        }
        _size = size;
        _isBold = isBold;
    }
    public Typeface f;
    public float _size;
    public boolean _isBold;
}
