package com.tahhu.id;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.Calendar;

public class TodayDecorator implements DayViewDecorator {
    private final CalendarDay today;
    private final Drawable backgroundDrawable;

    public TodayDecorator(Drawable backgroundDrawable) {
        this.today = CalendarDay.today();
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        boolean isToday = day.equals(today); // Hanya mendekorasi tanggal hari ini
        Log.d("TodayDecorator", "Checking decoration for: " + day + " -> isToday: " + isToday);
        return isToday;
    }

    @Override
    public void decorate(DayViewFacade view) {
        Log.d("TodayDecorator", "Decorating today: " + today);
        view.setBackgroundDrawable(backgroundDrawable);// Atur latar belakang

        // Tambahkan warna teks
        view.addSpan(new ForegroundColorSpan(Color.BLUE));

        // Tambahkan font khusus
//      view.addSpan(new CustomTypefaceSpan(boldFont));
    }// Gaya teks
}

