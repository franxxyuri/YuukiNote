package com.yukinoa.domain.model

data class UserPreferences(
    val titleFontSize: Float = 24f,
    val contentFontSize: Float = 16f,
    val titleColor: Int = 0, // 0表示使用主题默认颜色
    val contentColor: Int = 0 // 0表示使用主题默认颜色
) {
    companion object {
        val DEFAULT = UserPreferences()
    }
}