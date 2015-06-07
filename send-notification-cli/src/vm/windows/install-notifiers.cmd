echo 'Installing 7zip'
choco install -y 7zip.commandline

echo 'Installing Java JDK'
choco install -y java.jdk

echo 'Installing .NET Framework (needed by Growl)'
choco install -y DotNet3.5

echo 'Installing Growl for Windows'
choco install -y Growl

echo 'Installing Wget'
choco install -y Wget

echo 'Installing Notifu'
wget -O %TEMP%\notifu.zip http://www.paralint.com/projects/notifu/dl/notifu-1.6.zip
if exist c:\notifu rd /s /q c:\notifu
mkdir c:\notifu
7za x %TEMP%\notifu.zip -oc:\notifu -r
setx PATH "%PATH%;c:\notifu"

echo 'Installing Toaster'
wget -O %TEMP%\toaster.zip https://github.com/nels-o/toaster/archive/master.zip --no-check-certificate
if exist c:\toaster rd /s /q c:\toaster
mkdir c:\toaster
7za x %TEMP%\toaster.zip -oc:\toaster -r
setx PATH "%PATH%;c:\toaster\toaster-master\toast\bin\Release"

echo 'Installing Snarl'
wget -O %TEMP%\melon.exe http://sourceforge.net/projects/snarlwin/files/Goodies/setup-minimal.exe/download
%TEMP%\melon.exe /S
wget -O %TEMP%\snarl.exe http://sourceforge.net/projects/snarlwin/files/latest/download?source=files

if NOT %ERRORLEVEL% == 0 exit /B %ERRORLEVEL%
%TEMP%\snarl.exe /S
if %ERRORLEVEL% == 1223 exit /B 0
