package bdk.qr.lib.camera;

import android.graphics.Bitmap;
import com.google.zxing.LuminanceSource;


/**
 * user-defined Bitmap LuminanceSource
 */
public class BitmapLuminanceSource extends LuminanceSource {

    private byte bitmapPixels[];

    public BitmapLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        // First, to get the content of the pixel array of the picture
        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
        this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

        // Converting the int array into an byte array, that is, to take the blue value of the pixel value as a discriminating content
        for (int i = 0; i < data.length; i++) {
            this.bitmapPixels[i] = (byte) data[i];
        }
    }

    @Override
    public byte[] getMatrix() {
        // Return us to generate good pixel data
        return bitmapPixels;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        // To get the pixel data of the specified row
        System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());
        return row;
    }
}
