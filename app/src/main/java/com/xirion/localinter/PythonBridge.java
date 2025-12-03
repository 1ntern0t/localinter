package com.xirion.localinter;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class PythonBridge {
    public static String execute(String code) {
        try {
            Python py = Python.getInstance();
            PyObject pycore = py.getModule("pycore");
            PyObject result = pycore.callAttr("run_interpreter", code);
            return result.toString();
        } catch (Throwable e) {
            return "Python exception: " + e.toString();
        }
    }
}
