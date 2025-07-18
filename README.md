# Kids Calculator 🧮

![Build Status](https://github.com/skippdot/kids-calculator/workflows/Android%20Build/badge.svg)

A simple, child-friendly calculator app for Android designed specifically for 4-year-olds. The app features large buttons, colorful design, and text-to-speech functionality to help children learn numbers and basic math operations.

## 📱 Download

### Latest Release
🎉 **[Download the latest version](https://github.com/skippdot/kids-calculator/releases/latest)** 🎉

Choose between:
- **Debug APK** - For testing and development
- **Release APK** - Optimized for production use

### Development Builds
For the bleeding-edge version, you can also:
- Check [GitHub Actions](https://github.com/skippdot/kids-calculator/actions/workflows/android-build.yml) for recent builds
- Build locally using the instructions below

### Installation
1. Download the APK file from the releases page
2. Enable "Install from unknown sources" in your Android settings
3. Install the APK file on your device

## Features

- **Large, Touch-Friendly Buttons**: Easy for small fingers to press
- **Russian Text-to-Speech**: Speaks numbers and operations in Russian language ("плюс", "минус", "умножить", "разделить")
- **Kid-Friendly Design**: Bright colors and simple interface
- **Basic Math Operations**: Addition, subtraction, multiplication, and division
- **Voice Feedback**: Announces results and operations audibly
- **Multilingual Support**: Primary support for Russian, with fallback to system language

## Technical Details

- **Platform**: Android (API 21+)
- **Language**: Kotlin
- **UI Framework**: Android Views with Material Design
- **Text-to-Speech**: Android TTS API
- **Build System**: Gradle with Kotlin DSL

## Building the App

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 17 or higher
- Android SDK API 34

### Local Build

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device or emulator

### GitHub Actions Build

The project includes automated building via GitHub Actions:

- **Triggers**: Push to main/develop branches, Pull requests to main
- **Outputs**: Debug and release APK files
- **Tests**: Automated unit test execution

## Project Structure

```
app/
├── src/main/
│   ├── java/com/kidscalculator/
│   │   └── MainActivity.kt          # Main calculator logic
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # UI layout
│   │   ├── values/
│   │   │   ├── colors.xml           # Color definitions
│   │   │   ├── strings.xml          # String resources
│   │   │   └── themes.xml           # App theme
│   │   └── xml/                     # Backup and data extraction rules
│   └── AndroidManifest.xml
├── build.gradle                     # Module build configuration
└── proguard-rules.pro              # ProGuard configuration
```

## Key Features Implementation

### Text-to-Speech Integration

The app uses Android's built-in TextToSpeech API to provide audio feedback:

- Numbers are announced when pressed
- Operations are spoken ("plus", "minus", "times", "divided by")
- Results are read aloud
- Speech rate is adjusted for children (slower pace)

### Child-Friendly UI

- **Large Buttons**: 32sp text size with generous padding
- **High Contrast**: Bright colors for easy visibility
- **Grid Layout**: Organized button arrangement
- **Visual Feedback**: Different colors for different button types

### Calculator Logic

- Basic arithmetic operations (+, -, ×, ÷)
- Error handling (division by zero)
- Result formatting (integers vs decimals)
- Clear functionality

## Installation

1. Download the APK from the GitHub Actions artifacts
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK file
4. Grant microphone permissions if prompted (for TTS)

## Usage

1. Launch the app
2. Tap number buttons to input numbers (app will speak each number)
3. Tap operation buttons for math operations
4. Press equals (=) to calculate result
5. Use clear (C) to reset the calculator

## Development

### Adding New Features

The app is designed to be easily extensible:

- Add new operations in `MainActivity.kt`
- Modify UI in `activity_main.xml`
- Update colors and themes in `res/values/`

### Testing

Run tests with:
```bash
./gradlew test
```

### Creating a Release

There are two ways to create a new release with APK files:

#### 🚀 Automatic Releases (Recommended)

Releases are now created automatically when pull requests are merged to the main branch:

1. Create a pull request with your changes
2. Once the PR is reviewed and merged to main, the system will:
   - Automatically determine the next version number (increments patch version)
   - Create a git tag (e.g., v1.0.0 → v1.0.1)
   - Build debug and release APK files
   - Create a GitHub release with detailed notes
   - Upload APK files to the release

#### 📋 Manual Releases

You can still create releases manually using git tags:

1. Create and push a version tag:
```bash
git tag v1.0.0
git push origin v1.0.0
```

2. The GitHub Actions workflow will automatically:
   - Build debug and release APK files
   - Create a GitHub release
   - Upload APK files to the release

3. Users can then download the APK files from the [releases page](https://github.com/skippdot/kids-calculator/releases)

#### 🏷️ Versioning Strategy

- **Automatic releases**: Increment the patch version (e.g., v1.0.5 → v1.0.6)
- **Manual releases**: Use any valid semantic version tag (e.g., v1.1.0, v2.0.0)
- **First release**: Starts at v1.0.0 if no previous tags exist
- **Version format**: Must follow `vX.Y.Z` pattern (semantic versioning with 'v' prefix)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.

---

# Детский Калькулятор 🧮

![Статус сборки](https://github.com/skippdot/kids-calculator/workflows/Android%20Build/badge.svg)

Простое, детское приложение-калькулятор для Android, специально разработанное для детей 4 лет. Приложение имеет крупные кнопки, красочный дизайн и функцию озвучивания текста, которая помогает детям изучать числа и основные математические операции.

## 📱 Скачать

### Последняя версия
🎉 **[Скачать последнюю версию](https://github.com/skippdot/kids-calculator/releases/latest)** 🎉

Выберите между:
- **Debug APK** - Для тестирования и разработки
- **Release APK** - Оптимизированная для использования

### Сборки разработки
Для самой свежей версии вы также можете:
- Проверить [GitHub Actions](https://github.com/skippdot/kids-calculator/actions/workflows/android-build.yml) для последних сборок
- Собрать локально, используя инструкции ниже

### Установка
1. Скачайте APK файл со страницы релизов
2. Включите "Установку из неизвестных источников" в настройках Android
3. Установите APK файл на ваше устройство

## Возможности

- **Крупные, удобные кнопки**: Легко нажимать маленькими пальчиками
- **Русская озвучка**: Произносит числа и операции на русском языке ("плюс", "минус", "умножить", "разделить")
- **Детский дизайн**: Яркие цвета и простой интерфейс
- **Основные математические операции**: Сложение, вычитание, умножение и деление
- **Голосовая обратная связь**: Озвучивает результаты и операции
- **Многоязычная поддержка**: Основная поддержка русского языка с возвратом к системному языку

## Технические детали

- **Платформа**: Android (API 21+)
- **Язык**: Kotlin
- **UI Framework**: Android Views с Material Design
- **Озвучивание**: Android TTS API
- **Система сборки**: Gradle с Kotlin DSL

## Локальная сборка

### Требования

- Android Studio Arctic Fox или новее
- JDK 17 или выше
- Android SDK API 34

### Локальная сборка

1. Клонируйте репозиторий
2. Откройте в Android Studio
3. Синхронизируйте файлы Gradle
4. Соберите и запустите на устройстве или эмуляторе

### Сборка GitHub Actions

Проект включает автоматическую сборку через GitHub Actions:

- **Триггеры**: Push в ветки main/develop, Pull requests в main
- **Выходы**: Debug и release APK файлы
- **Тесты**: Автоматическое выполнение unit тестов

## Структура проекта

```
app/
├── src/main/
│   ├── java/com/kidscalculator/
│   │   └── MainActivity.kt          # Основная логика калькулятора
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # UI макет
│   │   ├── values/
│   │   │   ├── colors.xml           # Определения цветов
│   │   │   ├── strings.xml          # Строковые ресурсы
│   │   │   └── themes.xml           # Тема приложения
│   │   └── xml/                     # Правила резервного копирования
│   └── AndroidManifest.xml
├── build.gradle                     # Конфигурация сборки модуля
└── proguard-rules.pro              # Конфигурация ProGuard
```

## Ключевые особенности реализации

### Интеграция озвучивания текста

Приложение использует встроенный в Android API TextToSpeech для обеспечения аудио обратной связи:

- Числа озвучиваются при нажатии
- Операции произносятся ("плюс", "минус", "умножить", "разделить")
- Результаты читаются вслух
- Скорость речи адаптирована для детей (более медленный темп)

### Детский UI

- **Крупные кнопки**: Размер текста 32sp с большими отступами
- **Высокая контрастность**: Яркие цвета для легкой видимости
- **Сеточная раскладка**: Организованное расположение кнопок
- **Визуальная обратная связь**: Разные цвета для разных типов кнопок

### Логика калькулятора

- Основные арифметические операции (+, -, ×, ÷)
- Обработка ошибок (деление на ноль)
- Форматирование результатов (целые числа vs десятичные)
- Функция очистки

## Использование

1. Запустите приложение
2. Нажимайте кнопки с числами для ввода чисел (приложение будет произносить каждое число)
3. Нажимайте кнопки операций для математических операций
4. Нажмите равно (=) для вычисления результата
5. Используйте очистку (C) для сброса калькулятора

## Разработка

### Добавление новых функций

Приложение разработано для легкого расширения:

- Добавляйте новые операции в `MainActivity.kt`
- Изменяйте UI в `activity_main.xml`
- Обновляйте цвета и темы в `res/values/`

### Тестирование

Запустите тесты с помощью:
```bash
./gradlew test
```

### Создание релиза

Для создания нового релиза с APK файлами:

1. Создайте и отправьте тег версии:
```bash
git tag v1.0.0
git push origin v1.0.0
```

2. Workflow GitHub Actions автоматически:
   - Соберет debug и release APK файлы
   - Создаст GitHub релиз
   - Загрузит APK файлы в релиз

3. Пользователи смогут скачать APK файлы со [страницы релизов](https://github.com/skippdot/kids-calculator/releases)

## Участие в разработке

1. Сделайте форк репозитория
2. Создайте ветку для функции
3. Внесите изменения
4. Тщательно протестируйте
5. Отправьте pull request

## Лицензия

Этот проект имеет открытый исходный код и доступен под лицензией MIT.

---

# Детски Калкулатор 🧮

![Статус на компилацията](https://github.com/skippdot/kids-calculator/workflows/Android%20Build/badge.svg)

Простo, детско приложение-калкулатор за Android, специално проектирано за деца на 4 години. Приложението има големи бутони, цветен дизайн и функция за озвучаване на текст, която помага на децата да изучават числа и основни математически операции.

## 📱 Изтегляне

### Последна версия
🎉 **[Изтеглете последната версия](https://github.com/skippdot/kids-calculator/releases/latest)** 🎉

Изберете между:
- **Debug APK** - За тестване и разработка
- **Release APK** - Оптимизирана за използване

### Версии за разработка
За най-новата версия можете също да:
- Проверите [GitHub Actions](https://github.com/skippdot/kids-calculator/actions/workflows/android-build.yml) за последни компилации
- Компилирате локално, използвайки инструкциите по-долу

### Инсталация
1. Изтеглете APK файла от страницата с версии
2. Включете "Инсталация от неизвестни източници" в настройките на Android
3. Инсталирайте APK файла на вашето устройство

## Функции

- **Големи, удобни бутони**: Лесни за натискане с малки пръстчета
- **Руско озвучаване**: Произнася числа и операции на руски език ("плюс", "минус", "умножи", "раздели")
- **Детски дизайн**: Ярки цветове и прост интерфейс
- **Основни математически операции**: Събиране, изваждане, умножение и деление
- **Гласова обратна връзка**: Озвучава резултати и операции
- **Многоезична поддръжка**: Основна поддръжка на руски език с връщане към системния език

## Технически подробности

- **Платформа**: Android (API 21+)
- **Език**: Kotlin
- **UI Framework**: Android Views с Material Design
- **Озвучаване**: Android TTS API
- **Система за компилация**: Gradle с Kotlin DSL

## Локална компилация

### Изисквания

- Android Studio Arctic Fox или по-нова
- JDK 17 или по-висока
- Android SDK API 34

### Локална компилация

1. Клонирайте хранилището
2. Отворете в Android Studio
3. Синхронизирайте Gradle файловете
4. Компилирайте и стартирайте на устройство или емулатор

### Компилация GitHub Actions

Проектът включва автоматична компилация чрез GitHub Actions:

- **Тригери**: Push в клонове main/develop, Pull requests в main
- **Изходи**: Debug и release APK файлове
- **Тестове**: Автоматично изпълнение на unit тестове

## Структура на проекта

```
app/
├── src/main/
│   ├── java/com/kidscalculator/
│   │   └── MainActivity.kt          # Основна логика на калкулатора
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # UI макет
│   │   ├── values/
│   │   │   ├── colors.xml           # Дефиниции на цветове
│   │   │   ├── strings.xml          # Низови ресурси
│   │   │   └── themes.xml           # Тема на приложението
│   │   └── xml/                     # Правила за резервно копиране
│   └── AndroidManifest.xml
├── build.gradle                     # Конфигурация за компилация на модула
└── proguard-rules.pro              # Конфигурация ProGuard
```

## Ключови особености на реализацията

### Интеграция на озвучаване на текст

Приложението използва вградения в Android API TextToSpeech за осигуряване на аудио обратна връзка:

- Числата се озвучават при натискане
- Операциите се произнасят ("плюс", "минус", "умножи", "раздели")
- Резултатите се четат на глас
- Скоростта на речта е адаптирана за деца (по-бавно темпо)

### Детски UI

- **Големи бутони**: Размер на текста 32sp с големи отстъпи
- **Висока контрастност**: Ярки цветове за лесна видимост
- **Мрежова подредба**: Организирано разположение на бутоните
- **Визуална обратна връзка**: Различни цветове за различни типове бутони

### Логика на калкулатора

- Основни аритметични операции (+, -, ×, ÷)
- Обработка на грешки (деление на нула)
- Форматиране на резултати (цели числа vs десетични)
- Функция за изчистване

## Използване

1. Стартирайте приложението
2. Натиснете бутоните с числа за въвеждане на числа (приложението ще произнесе всяко число)
3. Натиснете бутоните за операции за математически операции
4. Натиснете равно (=) за изчисляване на резултата
5. Използвайте изчистване (C) за нулиране на калкулатора

## Разработка

### Добавяне на нови функции

Приложението е проектирано за лесно разширение:

- Добавяйте нови операции в `MainActivity.kt`
- Променяйте UI в `activity_main.xml`
- Актуализирайте цветове и теми в `res/values/`

### Тестване

Стартирайте тестовете с:
```bash
./gradlew test
```

### Създаване на версия

За създаване на нова версия с APK файлове:

1. Създайте и изпратете етикет на версия:
```bash
git tag v1.0.0
git push origin v1.0.0
```

2. Workflow GitHub Actions автоматично:
   - Ще компилира debug и release APK файлове
   - Ще създаде GitHub версия
   - Ще качи APK файлове във версията

3. Потребителите ще могат да изтеглят APK файловете от [страницата с версии](https://github.com/skippdot/kids-calculator/releases)

## Участие в разработката

1. Направете форк на хранилището
2. Създайте клон за функция
3. Направете промените си
4. Тествайте внимателно
5. Изпратете pull request

## Лиценз

Този проект има отворен код и е достъпен под MIT лиценз.