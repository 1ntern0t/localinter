import sys
import io
import traceback

def run_interpreter(code: str) -> str:
  buffer = io.StringIO()

  old_stdout = sys.stdout
  old_stderr = sys.stderr
  sys.stdout = buffer
  sys.stderr = buffer

  try:
    try:
      compiled = compile(code, "<input>", "eval")
      result = eval(compiled, {})
      if result is not None:
        print(repr(result))
    except SyntaxError:
      exec(code, {})
  except Exception:
    traceback.print_exc()
  finally:
    sys.stdout = old_stdout
    sys.stderr = old_stderr

  output = buffer.getvalue()
  return output if output.strip() else "<no output>"

