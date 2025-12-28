package com.example.instalearnenglish.feature.station4;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.station4.adapter.ST4_TipsPagerAdapter;
import com.example.instalearnenglish.feature.station4.model.Tip;
import com.example.instalearnenglish.feature.station4.utils.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ST4_LessonTipsFragment extends Fragment {

    private ViewPager2 viewPager;
    private TextView tvTipProgress;
    private List<Tip> tipList;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!");
                }
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        });
        return inflater.inflate(R.layout.st4_fragment_lesson_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.tips_view_pager);
        tvTipProgress = view.findViewById(R.id.tv_tip_progress);

        loadTips();

        ST4_TipsPagerAdapter adapter = new ST4_TipsPagerAdapter(tipList, tts);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(new DepthPageTransformer());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvTipProgress.setText((position + 1) + " / " + tipList.size());
            }
        });

        tvTipProgress.setText("1 / " + tipList.size());
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void loadTips() {
        tipList = new ArrayList<>();
        
        // 1. Language: Polite Requests
        tipList.add(new Tip(
            "Polite Requests", 
            "Instead of saying 'I want a towel', use 'Could I have an extra towel, please?'. It sounds much more polite to hotel staff.", 
            "Thay vì nói 'I want...' (Tôi muốn), hãy dùng 'Could I have...' (Tôi có thể có... không?). Cách này nghe lịch sự hơn nhiều đối với nhân viên khách sạn.", 
            R.drawable.st4_tip_polite));

        // 2. Language: Double vs Twin
        tipList.add(new Tip(
            "Double vs. Twin Room", 
            "Be careful! A 'Double Room' has one large bed for two people, while a 'Twin Room' has two separate single beds.", 
            "Hãy cẩn thận! 'Double Room' là phòng có một giường lớn cho 2 người, trong khi 'Twin Room' có hai giường đơn tách biệt.", 
            R.drawable.st4_tip_beds));

        // 3. Language: Complimentary
        tipList.add(new Tip(
            "Meaning of 'Complimentary'", 
            "When you see 'Complimentary' on water or snacks in your room, it means they are FREE of charge.", 
            "Khi bạn thấy từ 'Complimentary' trên nước uống hoặc đồ ăn nhẹ trong phòng, nó có nghĩa là chúng MIỄN PHÍ.", 
            R.drawable.st4_tip_free));

        // 4. Language: Describing Issues
        tipList.add(new Tip(
            "Describing Room Issues", 
            "To complain politely, use: 'There seems to be a problem with the AC' instead of 'The AC is broken'.", 
            "Để phàn nàn một cách lịch sự, hãy dùng: 'There seems to be a problem with...' (Có vẻ như có vấn đề với...) thay vì nói thẳng là nó bị hỏng.", 
            R.drawable.st4_tip_issues));

        // 5. Language: Checking in
        tipList.add(new Tip(
            "Checking In", 
            "When arriving, say: 'I have a reservation under the name of [Your Name]'. This is the standard way to start your check-in.", 
            "Khi đến nơi, hãy nói: 'I have a reservation under the name of...' (Tôi có lời đặt phòng dưới tên là...). đây là cách tiêu chuẩn để bắt đầu thủ tục nhận phòng.", 
            R.drawable.st4_tip_reserve));

        // 6. Language: Wake-up Call
        tipList.add(new Tip(
            "The Wake-up Call", 
            "If you have an early flight, ask the reception: 'Could I schedule a wake-up call for 6 AM, please?'.", 
            "Nếu bạn có chuyến bay sớm, hãy hỏi lễ tân: 'Could I schedule a wake-up call for...?' (Tôi có thể đặt dịch vụ gọi báo thức lúc... giờ không?).", 
            R.drawable.st4_tip_wakeup));

        // 7. General: Using the Safe
        tipList.add(new Tip(
            "Store Your Valuables", 
            "Always use the in-room safe for your 'valuables' (passport, cash, jewelry). Don't leave them out in the open.", 
            "Luôn sử dụng két sắt trong phòng cho 'valuables' (đồ giá trị) như hộ chiếu, tiền mặt. Đừng để chúng ở ngoài.", 
            R.drawable.st4_tip_safe));

        // 8. Language: Late Check-out
        tipList.add(new Tip(
            "Asking for Late Check-out", 
            "If you need more time, ask: 'Is it possible to have a late check-out?'. Some hotels allow this for free or a small fee.", 
            "Nếu bạn cần thêm thời gian, hãy hỏi: 'Is it possible to have a late check-out?' (Liệu có thể trả phòng muộn không?). Một số khách sạn cho phép miễn phí hoặc thu phí nhỏ.", 
            R.drawable.st4_tip_late));

        // 9. Language: Directions inside
        tipList.add(new Tip(
            "Finding Facilities", 
            "To find things inside, ask: 'Where is the [gym/pool/restaurant] located?'. The word 'located' makes you sound more fluent.", 
            "Để tìm các khu vực bên trong, hãy hỏi: 'Where is the... located?' (Cái... nằm ở đâu?). Dùng từ 'located' sẽ giúp bạn nghe trôi chảy hơn.", 
            R.drawable.st4_tip_directions));

        // 10. General: Review the Bill
        tipList.add(new Tip(
            "Review Your Bill", 
            "At check-out, always 'review your bill' carefully to ensure there are no extra charges from the mini-bar you didn't use.", 
            "Khi trả phòng, hãy luôn 'review your bill' (kiểm tra hóa đơn) cẩn thận để đảm bảo không có phí phát sinh từ mini-bar mà bạn không dùng.", 
            R.drawable.st4_tip_bill));
    }
}
