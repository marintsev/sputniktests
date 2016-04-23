First download the Sputnik tests, either by downloading the [zip](http://sputniktests.googlecode.com/files/sputniktests-v1.zip) file or by checking out the code from [subversion](http://code.google.com/p/sputniktests/source/checkout).  To run the tests you also need to install Python.

The test suite comes with a python test harness, `tools/sputnik.py`.  The easiest way to run the tests is to enter the sputnik root directory and run

```
python tools/sputnik.py --command <command>
```

The `<command>` parameter specifies how to run the JavaScript implementation.  To run the v8 engine on windows you would use

```
python tools/sputnik.py --command v8/shell.exe
```

Each test will be run by adding the test file path name after the specified command.  For more complex commands you can use the `{{path}}` placeholder which will be replaced with the file path to run:

```
python tools/sputnik.py --command "v8/shell.exe {{path}}"
```

The test harness prints the test outcomes as it runs the tests.  To run a particular test or set of tests you can specify the names on the command-line, for instance

```
python tools/sputnik.py --command <command> Unary_Operators
```

to run only the unary operator tests.  The test group names match the names of the specification sections they test.  You can also use section numbers, for instance `11.4.8` to run all tests of the bitwise NOT operator or individual test names, or individual test names like `S9.3.1_A1` to run individual tests.

The test case files that come with Sputnik are not run directly.  A little bit of processing takes place to include common code, set up time zone information, etc.  To see the source code that is run for a particular test use the `--cat` option:

```
python tools/sputnik.py --command <command> S9.3.1_A1 --cat
```

Note that since some of the tests are time zone dependent so the source code printed may only be meaningful in your time zone.