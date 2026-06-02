package com.lovekiss.installer

enum class Browser(
    val id: String,
    val displayName: String,
    val shortDesc: String,
    val packageName: String,
    val hasPlayStore: Boolean,
    val installFallbackUrl: String,
    val tampermonkeyUrl: String,
    val logoRes: Int
) {
    FIREFOX(
        id = "firefox",
        displayName = "Firefox",
        shortDesc = "Mozilla · Google Play",
        packageName = "org.mozilla.firefox",
        hasPlayStore = true,
        installFallbackUrl = "https://www.mozilla.org/ru/firefox/android/",
        tampermonkeyUrl = "https://addons.mozilla.org/ru/firefox/addon/tampermonkey/",
        logoRes = R.drawable.logo_firefox
    ),
    YANDEX(
        id = "yandex",
        displayName = "Яндекс.Браузер",
        shortDesc = "Популярный в РФ · Chrome Web Store",
        packageName = "com.yandex.browser",
        hasPlayStore = true,
        installFallbackUrl = "https://browser.yandex.ru/mobile/",
        tampermonkeyUrl = "https://chromewebstore.google.com/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo",
        logoRes = R.drawable.logo_yandex
    ),
    CROMITE(
        id = "cromite",
        displayName = "Cromite",
        shortDesc = "Privacy Chromium · GitHub",
        packageName = "org.cromite.cromite",
        hasPlayStore = false,
        installFallbackUrl = "https://github.com/uazo/cromite/releases/latest",
        tampermonkeyUrl = "https://chromewebstore.google.com/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo",
        logoRes = R.drawable.logo_cromite
    ),
    ICERAVEN(
        id = "iceraven",
        displayName = "Iceraven",
        shortDesc = "Firefox-fork · все расширения AMO · GitHub",
        packageName = "io.github.forkmaintainers.iceraven",
        hasPlayStore = false,
        installFallbackUrl = "https://github.com/fork-maintainers/iceraven-browser/releases/latest",
        tampermonkeyUrl = "https://addons.mozilla.org/ru/firefox/addon/tampermonkey/",
        logoRes = R.drawable.logo_iceraven
    );

    companion object {
        fun byId(id: String?): Browser =
            values().firstOrNull { it.id == id } ?: FIREFOX
    }
}
