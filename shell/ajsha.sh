#!/bin/sh

echo "Please input your java code.(Press Ctrl+D to exit)"
cat > file.java
echo "exit"

adb shell am start --activity-clear-top -n org.ligi.ajsha/.EditActivity --es "org.ligi.ajsha.EditActivity.EXTRA_CODE" `echo \'\`cat file.java\`\'`

