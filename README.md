# Kids Calculator 🧮

A simple, child-friendly calculator app for Android designed specifically for 4-year-olds. The app features large buttons, colorful design, and text-to-speech functionality to help children learn numbers and basic math operations.

## Features

- **Large, Touch-Friendly Buttons**: Easy for small fingers to press
- **Text-to-Speech**: Speaks numbers and operations when buttons are pressed
- **Kid-Friendly Design**: Bright colors and simple interface
- **Basic Math Operations**: Addition, subtraction, multiplication, and division
- **Voice Feedback**: Announces results and operations audibly

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

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.