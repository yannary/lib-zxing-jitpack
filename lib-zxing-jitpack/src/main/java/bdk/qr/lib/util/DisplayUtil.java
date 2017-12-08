package bdk.qr.lib.util;

import android.content.Context;

public class DisplayUtil {

    /**
     * Turn from the unit of DP to PX (pixel) according to the resolution of the cell phone
     * @param context context
     * @param   dpValue   value
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Turn from a unit of PX (pixel) to DP based on the resolution of the cell phone
     * @param context context
     * @param   pxValue   value
     * @return dip dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Get the screen density float based on the resolution of the mobile phone
     *
     * @param context context
     * @return Density Density
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * Get the screen density int based on the resolution of the mobile phone
     *
     * @param context context
     * @return tDensityDPI tDensityDPI
     */
    public static int getDensityDPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }


    /**
     * Get the screen width PX based on the resolution of the mobile phone
     *
     * @param context context
     * @return ScreenWidth ScreenWidth
     */
    public static int getScreenWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Get the screen height of PX based on the resolution of the mobile phone
     *
     * @param context context
     * @return ScreenHeight ScreenHeight
     */
    public static int getScreenHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Get the screen width DP based on the resolution of the mobile phone
     *
     * @param context context
     * @return ScreenWidth ScreenWidth
     */
    public static float getScreenWidthDp(Context context) {
        float screenWidthPx = context.getResources().getDisplayMetrics().widthPixels;
        return px2dip(context, screenWidthPx);
    }

    /**
     * Get the screen height of DP based on the resolution of the mobile phone
     *
     * @param context context
     * @return ScreenHeight ScreenHeight
     */
    public static float getScreenHeightDp(Context context) {
        float screenHeightPx = context.getResources().getDisplayMetrics().heightPixels;
        return px2dip(context, screenHeightPx);
    }

}
