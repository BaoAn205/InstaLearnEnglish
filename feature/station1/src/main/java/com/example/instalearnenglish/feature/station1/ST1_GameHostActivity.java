package com.example.instalearnenglish.feature.station1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ST1_GameHostActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "EXTRA_GAME_TYPE";
    public static final String EXTRA_GAME_TITLE = "EXTRA_GAME_TITLE"; // nếu muốn hiển thị title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st1_activity_game);  // ← chính là file XML bạn vừa gửi

        if (savedInstanceState == null) {  // chỉ add fragment lần đầu
            String gameType = getIntent().getStringExtra(EXTRA_GAME_TYPE);

            Fragment fragment = null;
            if ("DRAG_AND_DROP_LUGGAGE".equals(gameType)) {
                fragment = new ST1_DragAndDropLuggageFragment();
                // Nếu muốn truyền title hoặc data khác
                // Bundle args = new Bundle();
                // args.putString("GAME_TITLE", getIntent().getStringExtra(EXTRA_GAME_TITLE));
                // fragment.setArguments(args);
            }
            // Sau này thêm các game khác ở đây: else if ("OTHER_GAME"...

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game_fragment_container, fragment)  // ← ID trong XML của bạn
                        .commit();
            } else {
                finish(); // không có game nào hợp lệ
            }
        }
    }
}