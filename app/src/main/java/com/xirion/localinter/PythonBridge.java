package com.xirion.localinter;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class PythonBridge {
    public static String execute(String code) {
        Python py = Python.getInstance();
        PyObject pycore = py.getModule("pycore");
        PyObject result = pycore.callAttr("run_interpreter", code);
        return result.toString();
    }
}