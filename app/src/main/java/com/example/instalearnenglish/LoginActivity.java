package com.example.instalearnenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword;
    private Button btnAction;
    private TextView tvSwitchModePrompt, tvSwitchModeLink;
    private ImageView btnGoogle, btnFacebook, btnInstagram;
    
    // Sửa đổi: Mặc định là Login Mode (true) thay vì Sign Up
    private boolean isLoginMode = true; 

    private static final String PREFS_NAME = "InstaLearnPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Kiểm tra xem đã đăng nhập chưa
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            navigateToHome();
            return;
        }

        setContentView(R.layout.activity_login);

        // Ánh xạ View
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnAction = findViewById(R.id.btn_get_started);
        tvSwitchModePrompt = findViewById(R.id.tv_sign_in_prompt);
        tvSwitchModeLink = findViewById(R.id.tv_sign_in_link);
        
        btnGoogle = findViewById(R.id.btn_google);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnInstagram = findViewById(R.id.btn_instagram);

        // Cập nhật giao diện ban đầu (Login)
        updateUI();

        // Xử lý nút chính (Sign In hoặc Get Started)
        btnAction.setOnClickListener(v -> handleAuthAction());

        // Xử lý nút chuyển đổi chế độ
        tvSwitchModeLink.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUI();
        });

        // Xử lý nút Social Media
        btnGoogle.setOnClickListener(v -> openWebUrl("https://accounts.google.com/signin"));
        btnFacebook.setOnClickListener(v -> openWebUrl("https://www.facebook.com/login"));
        btnInstagram.setOnClickListener(v -> openWebUrl("https://www.instagram.com/accounts/login/"));
    }

    private void openWebUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void updateUI() {
        if (isLoginMode) {
            // Giao diện ĐĂNG NHẬP
            etFullName.setVisibility(View.GONE);
            btnAction.setText(R.string.btn_sign_in); // Nút ghi "SIGN IN"
            tvSwitchModePrompt.setText(R.string.msg_dont_have_account);
            tvSwitchModeLink.setText(R.string.action_sign_up); // Link ghi "Sign Up"
        } else {
            // Giao diện ĐĂNG KÝ
            etFullName.setVisibility(View.VISIBLE);
            btnAction.setText(R.string.get_started); // Nút ghi "Get Started"
            tvSwitchModePrompt.setText(R.string.already_have_account);
            tvSwitchModeLink.setText(R.string.sign_in); // Link ghi "Sign In"
        }
    }

    private void handleAuthAction() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (isLoginMode) {
            // --- XỬ LÝ ĐĂNG NHẬP ---
            String savedEmail = prefs.getString(KEY_EMAIL, null);
            String savedPassword = prefs.getString(KEY_PASSWORD, null);

            if (savedEmail == null) {
                // Chưa có tài khoản nào được lưu
                Toast.makeText(this, "Account not found. Please Sign Up first.", Toast.LENGTH_SHORT).show();
            } else if (email.equals(savedEmail) && password.equals(savedPassword)) {
                // Đăng nhập thành công
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                Toast.makeText(this, R.string.success_login, Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                // Sai thông tin
                Toast.makeText(this, R.string.error_login_failed, Toast.LENGTH_SHORT).show();
            }

        } else {
            // --- XỬ LÝ ĐĂNG KÝ (Get Started) ---
            String fullName = etFullName.getText().toString().trim();
            if (TextUtils.isEmpty(fullName)) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Chỉ lưu thông tin, KHÔNG đăng nhập ngay
            editor.putString(KEY_FULL_NAME, fullName);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
            editor.apply();

            Toast.makeText(this, "Account created! Please Sign In.", Toast.LENGTH_LONG).show();

            // Chuyển người dùng về giao diện Đăng Nhập
            isLoginMode = true;
            updateUI();
            
            // Xóa mật khẩu để người dùng phải nhập lại, giữ email cho tiện
            etPassword.setText("");
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}