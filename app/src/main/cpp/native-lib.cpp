#include <jni.h>
#include <string>
#include <array>
#include <cstdio>
#include <android/log.h>

#define LOG_TAG "localinter"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Run llama-cli as root with LD_LIBRARY_PATH set
std::string runSystemCommandAsRoot(const std::string &cmd) {
  std::string envFix  = "export LD_LIBRARY_PATH=/data/local/tmp/llama && ";
  std::string fullCmd = "su -c \"" + envFix + cmd + " 2>&1\"";

  std::string result;
  std::array<char, 128> buffer{};
  FILE *pipe = popen(fullCmd.c_str(), "r");
  if (!pipe) {
    return "Failed to run root command";
  }

  while (fgets(buffer.data(), buffer.size(), pipe) != nullptr) {
    result += buffer.data();
  }

  int status = pclose(pipe);
  if (status != 0) {
    result += "\n[llama-cli exited with code " + std::to_string(status) + "]";
  }

  return result;
}

// JNI bridge for: public native String runShellCommand(String cmd, boolean usePython);
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xirion_localinter_MainActivity_runShellCommand(
  JNIEnv *env,
  jobject thiz,
  jstring cmd,
  jboolean usePython
) {
  // Convert Java String -> std::string
  const char *nativeCmd = env->GetStringUTFChars(cmd, nullptr);
  std::string userCmd(nativeCmd ? nativeCmd : "");
  env->ReleaseStringUTFChars(cmd, nativeCmd);

  std::string output;

  if (usePython) {
    // Call static Java method: MainActivity.runPython(String): String
    jclass clazz = env->GetObjectClass(thiz);
    if (clazz == nullptr) {
      LOGE("Failed to get MainActivity class");
      output = "JNI error: MainActivity class not found";
    } else {
      jmethodID method = env->GetStaticMethodID(
        clazz,
        "runPython",
        "(Ljava/lang/String;)Ljava/lang/String;"
      );

      if (method == nullptr) {
        LOGE("Failed to find static method runPython");
        output = "JNI error: runPython not found";
      } else {
        jstring input  = env->NewStringUTF(userCmd.c_str());
        jstring result = (jstring) env->CallStaticObjectMethod(clazz, method, input);

        if (result != nullptr) {
          const char *resultStr = env->GetStringUTFChars(result, nullptr);
          output = resultStr ? resultStr : "";
          env->ReleaseStringUTFChars(result, resultStr);
        } else {
          output = "Python returned null";
        }
      }
    }
  } else {
    // Run llama-cli with the user prompt
    std::string llamaCmd =
      "/data/local/tmp/llama/llama-cli "
      "-m /data/local/tmp/llama/mistral-7b-instruct-v0.2.Q4_K_S.gguf "
      "-p \"" + userCmd + "\"";

    LOGI("Running llama command: %s", llamaCmd.c_str());
    output = runSystemCommandAsRoot(llamaCmd);
  }

  return env->NewStringUTF(output.c_str());
}
