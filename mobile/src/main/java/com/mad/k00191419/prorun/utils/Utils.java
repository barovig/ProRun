package com.mad.k00191419.prorun.utils;


import java.util.concurrent.TimeUnit;

public class Utils {

    public static String formatDistance(float distance) {
        int m = (int)distance % 1000;
        int km = (int)distance / 1000;
        String out;

        if (km < 0)
            out = String.format(" %d m", m);
        else
            out = String.format("%d km %d m", km, m);

        return out;
    }

    public static String formatInterval(final long interval)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(interval);
        final long min = TimeUnit.MILLISECONDS.toMinutes(interval - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(interval - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }
}
