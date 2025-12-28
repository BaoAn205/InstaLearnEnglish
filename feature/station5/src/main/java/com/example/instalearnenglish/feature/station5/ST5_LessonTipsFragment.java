package com.example.instalearnenglish.feature.station5;

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

import com.example.instalearnenglish.feature.station5.adapter.ST5_TipsPagerAdapter;
import com.example.instalearnenglish.feature.station5.model.Tip;
import com.example.instalearnenglish.feature.station5.utils.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ST5_LessonTipsFragment extends Fragment {

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
        return inflater.inflate(R.layout.st5_fragment_lesson_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.tips_view_pager);
        tvTipProgress = view.findViewById(R.id.tv_tip_progress);

        loadTips();

        ST5_TipsPagerAdapter adapter = new ST5_TipsPagerAdapter(tipList, tts);
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
        
        // 1. Language: Polite Ordering
        tipList.add(new Tip(
            "Polite Ordering", 
            "Instead of saying 'I want', use 'Could I have...?' or 'I'd like to order...'. It sounds much more natural and polite.", 
            "Thay vì nói 'Tôi muốn', hãy dùng 'Tôi có thể lấy...?' hoặc 'Tôi muốn gọi món...'. Cách này nghe tự nhiên và lịch sự hơn nhiều.", 
            R.drawable.st5_tip_order));

        // 2. Language: Asking for Sizes
        tipList.add(new Tip(
            "Asking for Sizes", 
            "To check for fit, ask: 'Do you have this in a larger/smaller size?' or 'Can I try this on in a medium?'.", 
            "Để hỏi về kích cỡ, hãy nói: 'Bạn có cái này size lớn hơn/nhỏ hơn không?' hoặc 'Tôi có thể thử cái này size M không?'.", 
            R.drawable.st5_tip_size));

        // 3. Language: Natural Price Inquiry
        tipList.add(new Tip(
            "Price Inquiry", 
            "Besides 'How much is it?', you can ask 'What's the price of this?' or 'How much does this cost?'.", 
            "Ngoài câu 'Cái này bao nhiêu tiền?', bạn có thể hỏi 'Giá của cái này là bao nhiêu?' hoặc 'Cái này có giá thế nào?'.", 
            R.drawable.st5_tip_price));

        // 4. Language: Food Issues
        tipList.add(new Tip(
            "Reporting Food Issues", 
            "If your food is not right, say politely: 'Excuse me, I think this isn't what I ordered' or 'This is a bit cold'.", 
            "Nếu món ăn có vấn đề, hãy nói lịch sự: 'Xin lỗi, tôi nghĩ đây không phải món tôi gọi' hoặc 'Món này hơi bị nguội'.", 
            R.drawable.st5_tip_food_issue));

        // 5. General: Receipt Importance
        tipList.add(new Tip(
            "Keep Your Receipt", 
            "Always keep your receipt. You will need it if you want to return an item or get a refund later.", 
            "Luôn giữ lại biên lai. Bạn sẽ cần nó nếu muốn trả lại hàng hoặc nhận lại tiền sau này.", 
            R.drawable.st5_tip_receipt));

        // 6. General: Local Flavors
        tipList.add(new Tip(
            "Ask for Recommendations", 
            "Not sure what to eat? Ask the waiter: 'What is the chef's special today?' or 'What do you recommend?'.", 
            "Bạn không biết nên ăn gì? Hãy hỏi bồi bàn: 'Món đặc biệt hôm nay là gì?' hoặc 'Bạn gợi ý món nào?'.", 
            R.drawable.st5_tip_recommend));
    }
}
