# Retro64 Minecraft Mod repository
This is the Minecraft side of the Retro64 mod - It also requires a native library, and an executable for audio support.
Be aware that the code is currently messy, and needs a lot of polishing.

For instructions on how to install the mod, watch this video [here](https://www.youtube.com/watch?v=2yWKqc2rmHI).

# Status
Mario's complete moveset including the BLJ is implemented.
Mario can attack enemies, swim, travel between dimensions, take damage and heal with XP or water and more, he also has a first person mode making some of these easier.
Currently, the only implemented cap is the Wing Cap, which can be accessed by equiping an elytra.

# Compilation instructions for Windows
!! First ensure you have installed the AdoptOpenJDK or as it is now known as, Temurin [here](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot)
The compilation process has been tested with Temurin 17, it is unknown if it will work on earlier versions.

To compile Retro64 on Windows, first download MrCrayFish's Controllable mod version 0.15.1 and rename it to "controllable-0.15.1.jar"
After you've renamed the file, in the root of this project, create a folder called "libs" and drag the Controllable jar in.
Once you have setup the Controllable jar, open Powershell/CMD and run ./gradlew.bat build
It will begin compiling the mod and if it successfully compiles, you can find the output at build/reobfJar/output.jar

# Compilation instructions for Linux
You should be able to follow the same steps for compiling on windows except instead of running ./gradew.bat build, instead run ./gradlew build 