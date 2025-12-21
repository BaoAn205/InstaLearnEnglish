package com.example.instalearnenglish.core.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LessonPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // 4 Tab: Tip, Vocab, Simulation, Game
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        // Tạm thời trả về Fragment trống, Developer A/B/C sẽ thay thế bằng Fragment thật sau
        return Fragment()
    }
}
