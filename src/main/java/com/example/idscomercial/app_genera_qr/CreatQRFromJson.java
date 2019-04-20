package com.example.idscomercial.app_genera_qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CreatQRFromJson {

    private static final String LOG_TAG = CreatQRFromJson.class.getSimpleName();

    public CreatQRFromJson() {
    }

    public String crearJson(String et_cantidad, String et_concepto, String et_nombre,
                            Boolean isTdc, String strTdc, String strCuentaClabe) {

        String saldo = et_cantidad;
        String concepto = et_concepto;
        String nombre = et_nombre.toUpperCase(); //quitar en caso de ingresar datos automaticamente
        // y cambiar por strNombre
        String json;

        if (isTdc)
            json = "{\"ot\": \"0001\",\"dOp\": [{\"alias\": \"" + concepto + "\"},{\"cl\": \"" +
                    strTdc + "\"},{\"type\": \"TC\"},{\"refn\": \"\"},{\"refa\": \"" +
                    nombre + "\"},{\"amount\": \"" +
                    saldo + "\"},{\"bank\": \"00012\"},{\"country\": \"MX\"},{\"currency\": \"MXN\"}]}";
        else
            json = "{\"ot\": \"0001\",\"dOp\": [{\"alias\": \"" + concepto + "\"},{\"cl\": \"" +
                    strCuentaClabe + "\"},{\"type\": \"CL\"},{\"refn\": \"\"},{\"refa\": \"" +
                    nombre + "\"},{\"amount\": \"" +
                    saldo + "\"},{\"bank\": \"00012\"},{\"country\": \"MX\"},{\"currency\": \"MXN\"}]}";

        Log.d(LOG_TAG + ": ", "JSON armado por el Generador --> " + json);

        return json;
    }


    public Bitmap createQR(String qrCodeData, String charset, int sideLength, String item, Context context, Boolean isLogo) {
        BitMatrix result;

        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

            if (item.contains("H")) {
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            } else if (item.contains("M")) {
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            } else if (item.contains("L")) {
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            } else {
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            }

            Log.d(LOG_TAG + ": ", "Se crea el QR con un factor " + item);

            hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */

            result = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, sideLength, sideLength, hints);

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ?
                            BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            Bitmap overlay = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bmovil);
            Bitmap overlay1 = getResizedBitmap(overlay, 150, 150);

            Bitmap biti = mergeBitmaps(overlay1, bitmap);

            if (isLogo) {
                return biti;
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG + ": ", "Falla al generar la imagen del QR ");

            return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }
}