⚡ LocalInter — Offline AI Interface for Android (Root Required)

Local Interface

This project is the UI / front-end layer for the offline AI setup.

Before You Use This App

If you haven’t set up your offline AI model on your device yet, STOP here and follow the installation guide:

👉 Offline AI Setup (GGUF + llama.cpp on Android)
https://github.com/1ntern0t/android-offline-ai

That repo shows you how to:

cross-compile llama.cpp for your device
push the GGUF model to /data/local/tmp/llama/
enable root execution of llama-cli
test the model fully offline (no Wi-Fi/data)

Once that setup is complete and the model runs successfully through terminal, this app makes everything easier.

What This App Does
This interface lets you:
Run your offline AI model (via llama-cli)
Run Python code directly (via Chaquopy)
Toggle between AI mode and Python mode
Chat with the model without opening a shell
Avoid typing root commands manually
Automatically pipe inputs to root llama-cli behind the scenes
Everything is handled in the background:

JNI bridges
Root shell commands
LD_LIBRARY_PATH
Model path + prompt formatting
Python interpreter execution

You just type your prompt → the app handles the rest.

Why This Exists

Using su, terminal commands, and manual flags is great for testing —
but annoying for daily interaction.

This app:
removes all terminal complexity
replaces commands with a normal text field
shows clean output in a UI
allows fast toggling between Python + LLaMA
works fully offline like your root shell setup
It’s the front-end brain for your offline AI.

Requirements
Rooted Android device
Offline model installed (from the guide above)
GGUF file placed in /data/local/tmp/llama/
llama-cli compiled using your device's architecture
Android device running minSdk 26+
