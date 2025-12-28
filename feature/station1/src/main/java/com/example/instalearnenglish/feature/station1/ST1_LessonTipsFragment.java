package com.example.instalearnenglish.feature.station1;

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

import com.example.instalearnenglish.feature.station1.adapter.ST1_TipsPagerAdapter;
import com.example.instalearnenglish.feature.station1.model.Tip;
import com.example.instalearnenglish.feature.station1.utils.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ST1_LessonTipsFragment extends Fragment {

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
        return inflater.inflate(R.layout.st1_fragment_lesson_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.tips_view_pager);
        tvTipProgress = view.findViewById(R.id.tv_tip_progress);

        loadTips();

        // Pass the TTS instance to the adapter
        ST1_TipsPagerAdapter adapter = new ST1_TipsPagerAdapter(tipList, tts);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        // Apply the DepthPageTransformer for a cool 3D effect
        viewPager.setPageTransformer(new DepthPageTransformer());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvTipProgress.setText((position + 1) + " / " + tipList.size());
            }
        });

        // Set initial progress text
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
        tipList.add(new Tip("The Rolling Method", "Rolling your clothes instead of folding them saves a lot of space in your suitcase and helps reduce wrinkles.", "Cuộn quần áo thay vì gấp giúp tiết kiệm không gian và giảm nếp nhăn.", R.drawable.ic_rolling_clothes));
        tipList.add(new Tip("100ml Liquid Rule", "All liquids, aerosols, and gels must be in containers of no more than 100ml and placed in a clear plastic bag.", "Chất lỏng phải được chứa trong chai không quá 100ml và đặt trong túi nhựa trong suốt.", R.drawable.ic_liquid_rule));
        tipList.add(new Tip("Battery Safety", "Power banks and devices with lithium batteries must be in your hand luggage for flight safety.", "Pin dự phòng và thiết bị có pin lithium phải được mang trong hành lý xách tay.", R.drawable.ic_battery_safety));
        tipList.add(new Tip("Digital Documents", "Scan or take photos of your passport and important documents. Save them on your phone and in the cloud.", "Quét hoặc chụp ảnh hộ chiếu và các giấy tờ quan trọng, lưu trên điện thoại và đám mây.", R.drawable.ic_digital_docs));
        tipList.add(new Tip("Solid Toiletries", "Use solid shampoo, conditioner, and toothpaste bars to save space and avoid liquid restrictions.", "Sử dụng dầu gội, dầu xả và kem đánh răng dạng rắn để tiết kiệm không gian và tránh các hạn chế về chất lỏng.", R.drawable.ic_solid_toiletries));
        tipList.add(new Tip("Empty Water Bottle", "Bring an empty water bottle to refill after passing security to save money and stay hydrated.", "Mang theo một chai nước rỗng để đổ đầy lại sau khi qua cửa an ninh.", R.drawable.ic_water_bottle));
        tipList.add(new Tip("Snacks for the Flight", "Pack a few non-perishable snacks for the plane or long waiting times.", "Chuẩn bị một ít đồ ăn nhẹ không dễ hỏng cho chuyến bay.", R.drawable.ic_snacks));
        tipList.add(new Tip("Universal Adapter", "Instead of multiple chargers, a universal travel adapter is compact and works in most countries.", "Sử dụng một bộ chuyển đổi du lịch đa năng thay vì nhiều bộ sạc.", R.drawable.ic_adapter));
        tipList.add(new Tip("First-Aid Kit", "Prepare a small first-aid kit with essentials like band-aids and pain relievers.", "Chuẩn bị một bộ sơ cứu nhỏ với các vật dụng cần thiết.", R.drawable.ic_first_aid));
        tipList.add(new Tip("Use Shoe Space", "Stuff small items like socks, underwear, or chargers inside your shoes to maximize every inch of space.", "Nhét các vật dụng nhỏ như tất, đồ lót hoặc bộ sạc vào trong giày.", R.drawable.ic_shoe_space));
        tipList.add(new Tip("Wear Your Heaviest Items", "Wear your bulkiest items, like boots and heavy coats, on the plane to save significant space and weight in your luggage.", "Mặc những món đồ cồng kềnh nhất của bạn, như ủng và áo khoác dày, trên máy bay.", R.drawable.ic_heavy_items));
        tipList.add(new Tip("Prevent Liquid Spills", "Place a small piece of plastic wrap over the opening of liquid bottles before screwing the cap on to create a seal and prevent leaks.", "Đặt một miếng bọc nhựa nhỏ lên miệng chai chất lỏng trước khi vặn nắp.", R.drawable.ic_liquid_rule));
        tipList.add(new Tip("Leave Space for Souvenirs", "Don't pack to 100% capacity on the way there. Leave some extra space in your luggage for souvenirs and gifts.", "Đừng đóng gói hành lý đến 100% sức chứa. Hãy để lại một ít không gian trống.", R.drawable.ic_souvenirs));
        tipList.add(new Tip("Dryer Sheet Freshness", "Place a dryer sheet in your suitcase. It weighs nothing and helps keep your clothes smelling fresh throughout the trip.", "Đặt một tờ giấy sấy vào vali của bạn. Nó không nặng và giúp giữ cho quần áo của bạn có mùi thơm.", R.drawable.ic_dryer_sheet));
        tipList.add(new Tip("Check Baggage Allowance", "Before packing, always check your airline’s website for their specific baggage allowance to avoid expensive surprise fees at the airport.", "Trước khi đóng gói, hãy luôn kiểm tra trang web của hãng hàng không để biết các quy định về hành lý.", R.drawable.ic_baggge_allowance));
    }
}
