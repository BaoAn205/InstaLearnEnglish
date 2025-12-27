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

        // Pass the TTS instance to the adapter
        ST5_TipsPagerAdapter adapter = new ST5_TipsPagerAdapter(tipList, tts);
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
        tipList.add(new Tip("The Rolling Method", "Rolling your clothes instead of folding them saves a lot of space in your suitcase and helps reduce wrinkles.", R.drawable.ic_rolling_clothes));
        tipList.add(new Tip("100ml Liquid Rule", "All liquids, aerosols, and gels must be in containers of no more than 100ml and placed in a clear plastic bag.", R.drawable.ic_liquid_rule));
        tipList.add(new Tip("Battery Safety", "Power banks and devices with lithium batteries must be in your hand luggage for flight safety.", R.drawable.ic_battery_safety));
        tipList.add(new Tip("Digital Documents", "Scan or take photos of your passport and important documents. Save them on your phone and in the cloud.", R.drawable.ic_digital_docs));
        tipList.add(new Tip("Solid Toiletries", "Use solid shampoo, conditioner, and toothpaste bars to save space and avoid liquid restrictions.", R.drawable.ic_solid_toiletries));
        tipList.add(new Tip("Empty Water Bottle", "Bring an empty water bottle to refill after passing security to save money and stay hydrated.", R.drawable.ic_water_bottle));
        tipList.add(new Tip("Snacks for the Flight", "Pack a few non-perishable snacks for the plane or long waiting times.", R.drawable.ic_snacks));
        tipList.add(new Tip("Universal Adapter", "Instead of multiple chargers, a universal travel adapter is compact and works in most countries.", R.drawable.ic_adapter));
        tipList.add(new Tip("First-Aid Kit", "Prepare a small first-aid kit with essentials like band-aids and pain relievers.", R.drawable.ic_first_aid));
        tipList.add(new Tip("Use Shoe Space", "Stuff small items like socks, underwear, or chargers inside your shoes to maximize every inch of space.", R.drawable.ic_shoe_space));
        tipList.add(new Tip("Wear Your Heaviest Items", "Wear your bulkiest items, like boots and heavy coats, on the plane to save significant space and weight in your luggage.", R.drawable.ic_heavy_items));
        tipList.add(new Tip("Prevent Liquid Spills", "Place a small piece of plastic wrap over the opening of liquid bottles before screwing the cap on to create a seal and prevent leaks.", R.drawable.ic_liquid_rule));
        tipList.add(new Tip("Leave Space for Souvenirs", "Don't pack to 100% capacity on the way there. Leave some extra space in your luggage for souvenirs and gifts.", R.drawable.ic_souvenirs));
        tipList.add(new Tip("Dryer Sheet Freshness", "Place a dryer sheet in your suitcase. It weighs nothing and helps keep your clothes smelling fresh throughout the trip.", R.drawable.ic_dryer_sheet));
        tipList.add(new Tip("Check Baggage Allowance", "Before packing, always check your airline’s website for their specific baggage allowance to avoid expensive surprise fees at the airport.", R.drawable.ic_baggge_allowance));
    }
}
