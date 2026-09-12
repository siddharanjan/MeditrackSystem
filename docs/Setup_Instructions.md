# Setup Instructions

## 1. Install a JDK

Used JDK 17 (Temurin) but 11+ works fine, nothing here needs newer than
that.

- Mac: `brew install --cask temurin17`
- Windows: installer from https://adoptium.net/temurin/releases/ (tick
  "Set JAVA_HOME")
- Linux: `sudo apt install openjdk-17-jdk`

## 2. Check it worked

```bash
java -version
javac -version
```

> add a screenshot of your own output here for the submission

## 3. Open the project

No Maven/Gradle, just plain source files (IDE project files aren't checked
in, so everyone's IntelliJ sets itself up fresh).

1. Open IntelliJ, **Open** the project folder
2. Make sure the project SDK is set (File > Project Structure > Project)
3. Right-click `src/main/java` > Mark Directory as > Sources Root
4. Right-click `Main.java` > Run

> screenshot idea: the green run arrow next to `main()`

Or from the terminal:

```bash
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.airtribe.meditrack.Main
```

## 4. Running the tests

No JUnit, `TestRunner` is a plain class:

```bash
java -cp out com.airtribe.meditrack.test.TestRunner
```

or just pick option 8 from the app's menu.

## 5. Reloading saved data

After using "Save Data To CSV" from the menu once:

```bash
java -cp out com.airtribe.meditrack.Main --loadData
```
