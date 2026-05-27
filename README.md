# Love Kiss Installer

APK-мастер, который помогает установить связку для запуска userscript-а Love Kiss на Android:

1. **Firefox** (через Google Play / mozilla.org)
2. **Tampermonkey** (через addons.mozilla.org в Firefox)
3. **Скрипт Love Kiss** (через download.lovekiss.you в Firefox)

Каждый шаг открывает нужную страницу/магазин. Установку подтверждает пользователь — это требование Android для apk без root.

## Как собрать APK

Локально ничего ставить не нужно. Сборка идёт в GitHub Actions.

### Один раз

1. Создай пустой репозиторий на GitHub (публичный или приватный — без разницы).
2. В этой папке выполни в PowerShell:

   ```powershell
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/<your-username>/<repo-name>.git
   git push -u origin main
   ```

3. Открой репозиторий на GitHub → вкладка **Actions** → дождись зелёной галочки (~3–5 минут).
4. Кликни на завершившийся workflow run → внизу раздел **Artifacts** → скачай `LoveKissInstaller-debug`.
5. Внутри ZIP — `app-debug.apk`. Это и есть APK. Кидай на телефон, ставь.

### Каждый раз, когда меняешь код

`git push` — workflow запустится автоматически, новый APK появится в Actions.

## Структура проекта

```
LoveKissInstaller/
├── .github/workflows/build.yml   # CI: сборка APK
├── app/
│   ├── build.gradle.kts          # настройки модуля app
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/lovekiss/installer/MainActivity.kt
│       └── res/
│           ├── drawable/ic_launcher.xml
│           ├── layout/activity_main.xml
│           └── values/{strings,themes}.xml
├── build.gradle.kts              # настройки проекта
├── settings.gradle.kts
└── gradle.properties
```

## Что менять

- **URL скрипта/расширения** — в `MainActivity.kt` сверху (`LOVEKISS_URL`, `TAMPERMONKEY_URL`).
- **Тексты UI** — в `res/values/strings.xml`.
- **Цвета / тема** — в `res/values/themes.xml` и `layout/activity_main.xml`.
- **Иконка** — `res/drawable/ic_launcher.xml` (векторная).

## Известные ограничения

- Tampermonkey работает на **Firefox для Android 113+** (актуальные сборки последних лет). На очень старых Firefox / Firefox Focus / Firefox Lite не работает.
- Если у пользователя нет Google Play, шаг 1 откроет mozilla.org для прямого скачивания APK Firefox.
- APK подписан debug-ключом (стандартный для CI без секретов). Для публикации в Play Store нужна release-подпись — отдельная история.
