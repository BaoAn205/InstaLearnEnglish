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
                "All liquids, aerosols, and gels in your carry-on must be in containers of 100ml or less, and all containers must fit into a single, transparent, resealable plastic bag.",
                "Quy tắc chất lỏng (100ml): Tất cả chất lỏng, bình xịt và gel trong hành lý xách tay phải đựng trong bình dưới 100ml và để trong túi nhựa trong suốt."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_electronics,
                "Electronics Out",
                "Be prepared to take large electronics, like laptops and tablets, out of your bag for separate screening. Keep them easily accessible to save time.",
                "Bỏ đồ điện tử ra ngoài: Hãy sẵn sàng lấy các thiết bị điện tử lớn như laptop, máy tính bảng ra khỏi túi để kiểm tra riêng. Để chúng ở nơi dễ lấy để tiết kiệm thời gian."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_boarding_pass,
                "Have Documents Ready",
                "Keep your boarding pass and passport or ID card in your hand when you are in the security line. You will need to show them to the security officer.",
                "Chuẩn bị sẵn giấy tờ: Cầm sẵn thẻ lên máy bay và hộ chiếu hoặc CMND trên tay khi xếp hàng kiểm tra an ninh để xuất trình cho nhân viên."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_announcement,
                "Listen for Announcements",
                "Pay close attention to airport announcements. They provide crucial information about gate changes, boarding times, and delays. The gate number is very important.",
                "Lắng nghe thông báo: Chú ý các thông báo tại sân bay về thay đổi cổng, giờ lên máy bay hoặc hoãn chuyến. Số cổng (gate number) là thông tin cực kỳ quan trọng."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_shoes,
                "Easy-to-Remove Shoes",
                "In many countries, you are required to remove your shoes for security screening. Wearing slip-on shoes can make the process much faster.",
                "Giày dễ tháo: Ở nhiều nước, bạn phải tháo giày khi kiểm tra an ninh. Đi giày lười (slip-on) sẽ giúp quá trình này nhanh hơn nhiều."
        ));
        
        tips.add(new TravelTip(
                R.drawable.ic_tip_empty_pockets,
                "Empty Your Pockets",
                "Before you reach the scanner, empty your pockets of all items, including keys, coins, and your phone. Place them in your carry-on bag or in the tray provided.",
                "Lấy hết đồ trong túi: Trước khi qua máy quét, hãy lấy hết đồ trong túi ra như chìa khóa, tiền xu, điện thoại và để vào túi xách hoặc khay đựng."
        ));

        return tips;
    }

    public static List<TravelTip> getStation3Tips() {
        List<TravelTip> tips = new ArrayList<>();

        tips.add(new TravelTip(
                R.drawable.ic_tip_metro_card,
                "Buy a Metro Card",
                "Most major cities have automated ticket machines. Have some small cash or an international credit card ready. Don't forget to keep your receipt!",
                "Mua thẻ tàu điện: Hầu hết các thành phố lớn đều có máy bán vé tự động. Chuẩn bị sẵn tiền lẻ hoặc thẻ tín dụng quốc tế và đừng quên giữ lại biên lai!"
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_ticket_validation,
                "Validate Your Ticket",
                "In many European countries, you must validate your train or bus ticket in a small machine before boarding. Forgetting to do this can result in a fine.",
                "Xác thực vé: Ở nhiều nước châu Âu, bạn phải dập vé tàu hoặc xe buýt vào máy nhỏ trước khi lên xe. Quên việc này có thể khiến bạn bị phạt tiền."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_rideshare,
                "Use Ride-Sharing Apps",
                "Apps like Uber or Grab are available in many cities and can be cheaper than taxis. Always check the license plate before getting in the car.",
                "Sử dụng ứng dụng đặt xe: Các ứng dụng như Uber hay Grab thường rẻ hơn taxi truyền thống. Luôn kiểm tra biển số xe trước khi lên xe."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_map,
                "Download Offline Maps",
                "Download the local public transport map on your phone. This helps you navigate even without an internet connection.",
                "Tải bản đồ ngoại tuyến: Tải sơ đồ giao thông công cộng về điện thoại để có thể tìm đường ngay cả khi không có kết nối internet."
        ));

        tips.add(new TravelTip(
                R.drawable.ic_tip_pickpocket,
                "Beware of Pickpockets",
                "Crowded buses and trains are prime spots for pickpockets. Keep your wallet and phone in a front pocket or a secure bag.",
                "Cảnh giác móc túi: Xe buýt và tàu điện đông đúc là nơi dễ bị móc túi. Hãy để ví và điện thoại ở túi trước hoặc túi có khóa an toàn."
        ));

        return tips;
    }
}
