# InstaLearnEnglish - Timeline Journey Project

Dự án này được thiết kế theo cấu trúc Multi-module để hỗ trợ làm việc nhóm (Developer A, B, C) và tối ưu hóa cho AI Assistant.

## 📌 Phân chia nhiệm vụ (Team Roles)

### 👤 Developer A (Home & Station 1)
- **Module:** `:feature:home`, `:feature:station1`
- **Nhiệm vụ:**
    - Phát triển giao diện bản đồ hành trình (Home Map).
    - Quản lý trạng thái mở khóa trạm (`SharedPreferences`).
    - Hoàn thiện nội dung Trạm 1: Chuẩn bị hành lý (Packing).

### 👤 Developer B (Station 2 & 3)
- **Module:** `:feature:station23`
- **Nhiệm vụ:**
    - Hoàn thiện nội dung Trạm 2: Tại Sân bay (At the Airport).
    - Hoàn thiện nội dung Trạm 3: Di chuyển (Transportation).

### 👤 Developer C (Station 4 & 5)
- **Module:** `:feature:station45`
- **Nhiệm vụ:**
    - Hoàn thiện nội dung Trạm 4: Khách sạn (Accommodation).
    - Hoàn thiện nội dung Trạm 5: Ăn uống & Mua sắm (Dining & Shopping).

---

## 🛠 Cấu trúc Module Hệ thống (Core Modules)
*Dành cho tất cả mọi người cùng đóng góp và sử dụng chung:*

- **`:core:ui`**: Chứa các Custom View dùng chung (FlashcardView, ChatLayout, v.v.).
- **`:core:data`**: Chứa Data Models (Station, VocabularyItem) và Repository.
- **`:core:common`**: Chứa các Utils (TextToSpeech, MediaRecorder, Constants).
- **`:app`**: Module chính dùng để khởi chạy và tích hợp các trạm lại với nhau.

---

## 🤖 Hướng dẫn cho AI Assistant
Khi làm việc trong dự án này, hãy tuân thủ các quy tắc sau:
1. **Scope:** Chỉ chỉnh sửa code trong module được phân công (ví dụ: nếu bạn là AI của Developer B, chỉ tập trung vào `:feature:station23`).
2. **Reuse:** Kiểm tra các thành phần UI trong `:core:ui` trước khi tạo View mới để đảm bảo tính đồng nhất.
3. **Data:** Sử dụng các model trong `:core:data` để đồng bộ dữ liệu giữa các trạm.
4. **Resources:** Đặt tên tài nguyên có tiền tố module (ví dụ: `st1_iv_backpack` thay vì `iv_backpack`) để tránh xung đột khi merge code trên GitHub.

---

## 🚀 Bắt đầu
1. Clone dự án.
2. Thực hiện `Gradle Sync`.
3. Mở module tương ứng với vai trò của mình để bắt đầu code.
