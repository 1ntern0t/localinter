import sys
import io

def run_interpreter(command):
    buffer = io.StringIO()
    try:
        sys.stdout = buffer
        try:
            # Try eval first (e.g., "2 + 2")
            result = eval(command, globals())
            if result is not None:
                print(result)
        except:
            # Fall back to exec for statements (e.g., "for x in range(3): ...")
            exec(command, globals())
        return buffer.getvalue()
    except Exception as e:
        return str(e)
    finally:
        sys.stdout = sys.__stdout__
'''
import sys
import io

def run_interpreter(command):
    buffer = io.StringIO()
    try:
        sys.stdout = buffer
        exec(command, globals())
        return buffer.getvalue()
    except Exception as e:
        return str(e)
    finally:
        sys.stdout = sys.__stdout__'''
