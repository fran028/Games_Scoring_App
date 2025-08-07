# Games Scoring App

## Overview

Games Scoring App is an Android application built with Jetpack Compose that allows users to easily keep track of scores for various popular games. The app provides a user-friendly interface for setting up games, adding players, recording scores, and viewing game history. It features dynamic themes based on game types and supports different scoring mechanics for different games.

## Features

*   **Multiple Game Types:** Supports various games like Truco, Points-based games, etc. (Extendable)
*   **Player Management:** Add and name multiple players for each game.
*   **Dynamic Scoring:** Different UI and logic for scoring based on the selected game type.
*   **Game Setup:** Configure game-specific options (e.g., target score, player names).
*   **Intuitive Score Input:** Easy-to-use interface for adding and editing scores.
*   **Real-time Totals:** Automatically calculates and displays total scores for each player.
*   **Dynamic Theming:** App theme and colors can change based on the selected game.
*   **Saved Games (Planned/In-Progress):** Functionality to save and resume ongoing games.
*   **Settings (Planned/In-Progress):** Options to customize app behavior.
*   **Roll Dice (Utility):** A simple dice rolling utility.
*   **Modern UI:** Built entirely with Jetpack Compose for a declarative and modern UI.
*   **Database Integration:** Uses Room persistence library for storing game data (like game types, potentially saved games and settings).

## Technologies Used

*   **Kotlin:** Primary programming language.
*   **Jetpack Compose:** For building the entire UI declaratively.
*   **Android Jetpack:**
    *   **Navigation Compose:** For handling in-app navigation.
    *   **ViewModel:** For managing UI-related data in a lifecycle-conscious way.
    *   **Room Persistence Library:** For local data storage.
    *   **Lifecycle:** For managing lifecycle states.
    *   **DataStore (Potentially for Settings):** For simple key-value storage.
*   **Coroutines:** For managing background threads and asynchronous operations.
*   **Material 3 Design:** Implementing modern Material Design components and theming.
*   **Accompanist (Potentially for System UI Controller):** For easier control over system bars.

## Setup and Installation

1.  **Clone the repository:** 
2.  **Open in Android Studio:**
    *   Open Android Studio
    *   Select "Open an Existing Project".
    *   Navigate to the cloned directory and select it.
3.  **Build the project:**
    *   Android Studio should automatically sync the Gradle files. If not, click "Sync Project with Gradle Files".
    *   Once synced, click "Build" > "Make Project" or run the app directly on an emulator or physical device.

## How to Use

1.  **Launch the App:** Open the Games Scoring App on your Android device or emulator.
2.  **Select Game Type:** From the home screen, tap on the desired game type (e.g., "Truco", "Points").
3.  **Setup Game:**
    *   You'll be taken to a setup screen (e.g., `SetupPage`).
    *   Enter player names.
4.  **Start Scoring:**
    *   Once setup is complete, you'll proceed to the game screen (`GamePage`).
    *   Use the provided UI elements (buttons, input fields) to add scores for each player or team.
    *   The total scores will update automatically.


## Future Enhancements (Ideas)

*   [ ] More game types with custom scoring rules.
*   [ ] Detailed game history and statistics.
*   [ ] Cloud synchronization for saved games (e.g., Firebase).
*   [ ] Player profiles and stats across games.
*   [ ] More advanced customization options in Settings.
*   [ ] UI/UX improvements and animations.

## Contact

https://scarpettini.es
Project Link: [https://github.com/your-username/games-scoring-app](https://github.com/your-username/games-scoring-app)
