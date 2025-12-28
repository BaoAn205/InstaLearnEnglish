package com.example.instalearnenglish.feature.station2.model;

import com.example.instalearnenglish.feature.station2.R;

import java.util.ArrayList;
import java.util.List;

public class TravelTipProvider {

    public static List<TravelTip> getStation2Tips() {
        List<TravelTip> tips = new ArrayList<>();

        tips.add(new TravelTip(
                R.drawable.ic_tip_liquids,
                "Liquids Rule (100ml)",
                "All liquids, aerosols, and gels in your carry-on must be in containers of 100ml or less, and all containers must fit into a single, transparent, resealable plastic bag."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_electronics,
                "Electronics Out",
                "Be prepared to take large electronics, like laptops and tablets, out of your bag for separate screening. Keep them easily accessible to save time."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_boarding_pass,
                "Have Documents Ready",
                "Keep your boarding pass and passport or ID card in your hand when you are in the security line. You will need to show them to the security officer."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_announcement,
                "Listen for Announcements",
                "Pay close attention to airport announcements. They provide crucial information about gate changes, boarding times, and delays. The gate number is very important."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_shoes,
                "Easy-to-Remove Shoes",
                "In many countries, you are required to remove your shoes for security screening. Wearing slip-on shoes can make the process much faster."
        ));
        
        tips.add(new TravelTip(
                R.drawable.ic_tip_empty_pockets,
                "Empty Your Pockets",
                "Before you reach the scanner, empty your pockets of all items, including keys, coins, and your phone. Place them in your carry-on bag or in the tray provided."
        ));

        return tips;
    }
}
