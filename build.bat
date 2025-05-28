@echo off
echo Building MotorPH Employee Management System...

REM Create build directories
if not exist build mkdir build
if not exist build\classes mkdir build\classes

REM Create package directories
if not exist "build\classes\model" mkdir "build\classes\model"
if not exist "build\classes\view" mkdir "build\classes\view"
if not exist "build\classes\service" mkdir "build\classes\service"
if not exist "build\classes\view\assets" mkdir "build\classes\view\assets"

REM Copy resources
echo Copying resources...
copy "src\main\java\view\assets\logo.png" "build\classes\view\assets\" > nul
copy "src\main\resources\employees.csv" "build\classes\" > nul

REM Set source path
set SOURCEPATH=src

REM Compile Java files
echo Compiling Java files...
javac -sourcepath %SOURCEPATH% -d build\classes %SOURCEPATH%\Main.java %SOURCEPATH%\model\*.java %SOURCEPATH%\service\*.java %SOURCEPATH%\view\*.java

if errorlevel 1 (
    echo Build failed!
) else (
    echo Build completed successfully!
    echo.
    echo To run the application, use: java -cp build\classes Main
) 