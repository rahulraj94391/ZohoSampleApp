package com.example.mall

class MSharedPreferences {
    companion object {
        const val NAME = "sharedPrefs"
        const val IS_FIRST_TIME = "is_first_time"   // true/False
        const val IS_LOGGED_IN = "is_logged_in"     // true/false
        const val LOGGED_IN_USER_ID = "user_id"     // int
    }
}