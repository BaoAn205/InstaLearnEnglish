package com.example.instalearnenglish.feature.station23.model;

import com.example.instalearnenglish.feature.station23.R;

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

    public static List<TravelTip> getStation3Tips() {
        List<TravelTip> tips = new ArrayList<>();

        tips.add(new TravelTip(
                R.drawable.ic_tip_metro_card, // You need to add this icon
                "Buy a Metro Card",
                "Most major cities have automated ticket machines. Have some small cash or an international credit card ready. Don't forget to keep your receipt!"
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_ticket_validation, // You need to add this icon
                "Validate Your Ticket",
                "In many European countries, you must validate your train or bus ticket in a small machine before boarding. Forgetting to do this can result in a fine."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_rideshare, // You need to add this icon
                "Use Ride-Sharing Apps",
                "Apps like Uber or Grab are available in many cities and can be cheaper than taxis. Always check the license plate before getting in the car."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_map, // You need to add this icon
                "Download Offline Maps",
                "Download the local public transport map on your phone. This helps you navigate even without an internet connection."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_pickpocket, // You need to add this icon
                "Beware of Pickpockets",
                "Crowded buses and trains are prime spots for pickpockets. Keep your wallet and phone in a front pocket or a secure bag."
        ));

        return tips;
    }
}
