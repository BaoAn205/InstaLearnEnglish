# InstaLearnEnglish - Timeline Journey Project

Dự án này được thiết kế theo cấu trúc **Multi-module (Modularization)** để hỗ trợ làm việc nhóm (Developer A, B, C) và tối ưu hóa cho AI Assistant.

## ⚠️ QUY TẮC QUAN TRỌNG (IMPORTANT RULE)
**TUYỆT ĐỐI KHÔNG làm việc trực tiếp trong module `:app`.** 
Module `:app` chỉ đóng vai trò là "vỏ bọc" để khởi chạy và tích hợp các module khác. Toàn bộ logic, giao diện và tài nguyên **PHẢI** được đặt trong các module `:feature` hoặc `:core` tương ứng.

---

## 📌 Phân chia nhiệm vụ (Team Roles)

### 👤 Developer A (Home & Station 1)
- **Module chính:** `:feature:home`
- **Nhiệm vụ:** 
    - Phát triển giao diện bản đồ hành trình, Hệ thống Xác thực (Login/Register), Hồ sơ (Profile), và các công cụ học tập (Từ điển, Thẻ từ).
- **Module nội dung:** `:feature:station1` (Trạm 1: Chuẩn bị hành lý).

### 👤 Developer B (Station 2 & 3)
- **Module chính:** `:feature:station23`
- **Nhiệm vụ:** Hoàn thiện toàn bộ nội dung, logic bài học cho Trạm 2 (Sân bay) và Trạm 3 (Di chuyển).

### 👤 Developer C (Station 4 & 5)
- **Module chính:** `:feature:station45`
- **Nhiệm vụ:** Hoàn thiện toàn bộ nội dung, logic bài học cho Trạm 4 (Khách sạn) và Trạm 5 (Ăn uống & Mua sắm).

---

## 🛠 Cấu trúc hệ thống (System Architecture)

- **`:feature:home`**: Trung tâm điều khiển (Map, Auth, Profile, Tools).
- **`:feature:stationX`**: Các module bài học độc lập (Nơi A, B, C làm việc chính).
- **`:core:ui`**: Chứa các Custom View và Adapter dùng chung (ví dụ: `LessonPagerAdapter`).
- **`:core:data`**: Chứa Models (Word, Station) và API Services (Retrofit).
- **`:core:common`**: Chứa các lớp Tiện ích (Utils), Constants.

---

## 🤖 Hướng dẫn cho AI Assistant
Khi bạn (AI) hỗ trợ lập trình viên trong dự án này, hãy tuân thủ:
1. **Scope Limit:** Chỉ chỉnh sửa code trong module được phân công. Tuyệt đối không thêm Activity/Layout vào `:app`.
2. **Resource Prefix:** Đặt tên resource có tiền tố module (ví dụ: `st45_iv_hotel` thay vì `iv_hotel`) để tránh xung đột khi merge Git.
3. **Logic Reuse:** Luôn kiểm tra `:core:ui` và `:core:data` để sử dụng lại code có sẵn trước khi viết mới.
4. **Binding Identity:** Sử dụng đúng lớp Binding của module (ví dụ: `FeatureStation1MainBinding`) để tránh lỗi nạp sai layout.

---

## 🚀 Cách bắt đầu
1. **Clone** dự án từ GitHub.
2. Mở bằng Android Studio và thực hiện **Gradle Sync**.
3. **Build -> Clean Project** để làm sạch cache cũ.
4. Chọn đúng module phụ trách để bắt đầu code.
