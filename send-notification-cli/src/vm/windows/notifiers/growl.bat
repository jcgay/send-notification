@echo off
call kill-all.bat
start "Growl" "c:\Program Files (x86)\Growl for Windows\Growl.exe"
call notify.bat growl
