package com.xirion.localinter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("localinter");
    }

    public native String runShellCommand(String cmd, boolean usePython);
    public static String runPython(String code) {
        return PythonBridge.execute(code);
    }

    private boolean isPythonMode = true;  // Default to Python interpreter

    public boolean hasRootAccess() {
        try {
            Process process = Runtime.getRuntime().exec("su -c echo rooted");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            return result != null && result.contains("rooted");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        setContentView(R.layout.activity_main);

        EditText inputField = findViewById(R.id.inputField);
        Button runButton = findViewById(R.id.runButton);
        Button modeButton = findViewById(R.id.modeButton);
        TextView outputField = findViewById(R.id.outputField);
        TextView modeLabel = findViewById(R.id.modeLabel);

        updateModeLabel(modeLabel);

        modeButton.setOnClickListener(v -> {
            isPythonMode = !isPythonMode;
            updateModeLabel(modeLabel);
        });

        runButton.setOnClickListener(v -> {
            String userInput = inputField.getText().toString().trim();

            new Thread(() -> {
                String result = runShellCommand(userInput, isPythonMode);
                runOnUiThread(() -> outputField.setText(result));
            }).start();
        });

        if (hasRootAccess()) {
            Log.d("ShellZapp", "Root access confirmed.");
        } else {
            Log.d("ShellZapp", "Root not available.");
        }
    }

    private void updateModeLabel(TextView label) {
        runOnUiThread(() -> {
            String modeText = isPythonMode ? "Interpreter: Python" : "Interpreter: AI (LLaMA)";
            label.setText(modeText);
        });
    }
}