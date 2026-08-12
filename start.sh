#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_CMD="$(command -v java || true)"
JAVAC_CMD="$(command -v javac || true)"

if [ -z "$JAVA_CMD" ] && [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

if [ -z "$JAVAC_CMD" ] && [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
  JAVAC_CMD="$JAVA_HOME/bin/javac"
fi

if [ -z "$JAVA_CMD" ] && [ -f "/c/Program Files/Java/jdk/bin/java.exe" ]; then
  JAVA_CMD="/c/Program Files/Java/jdk/bin/java.exe"
fi

if [ -z "$JAVAC_CMD" ] && [ -f "/c/Program Files/Java/jdk/bin/javac.exe" ]; then
  JAVAC_CMD="/c/Program Files/Java/jdk/bin/javac.exe"
fi

if [ -z "$JAVA_CMD" ] || [ -z "$JAVAC_CMD" ]; then
  echo "Error: Java runtime and compiler are required."
  echo "Please install JDK and ensure 'java' and 'javac' are available in PATH or JAVA_HOME is set."
  exit 1
fi

cd "$SCRIPT_DIR" || exit 1

if [ ! -d "java" ]; then
  echo "Error: java source directory not found."
  exit 1
fi

mkdir -p "$SCRIPT_DIR/java/classes"

echo "Compiling Java sources..."
"$JAVAC_CMD" -d "$SCRIPT_DIR/java/classes" "$SCRIPT_DIR/java"/*.java
if [ $? -ne 0 ]; then
  echo "Error: Java compilation failed."
  exit 1
fi

echo "Starting Canvas application..."
"$JAVA_CMD" -cp "$SCRIPT_DIR/java/classes" Main
