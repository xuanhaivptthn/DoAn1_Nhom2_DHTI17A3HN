#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

if ! command -v javac >/dev/null 2>&1; then
  VSCODE_JRE=$(ls -d "$HOME"/.vscode/extensions/redhat.java-*/jre/*/bin 2>/dev/null | head -1 || true)
  if [ -n "${VSCODE_JRE:-}" ] && [ -x "$VSCODE_JRE/javac" ]; then
    export PATH="$VSCODE_JRE:$PATH"
  fi
fi

if ! command -v javac >/dev/null 2>&1; then
  echo "❌ Không tìm thấy javac. Cài JDK 17+ rồi chạy lại."
  echo "   Ubuntu/Debian: sudo apt install openjdk-17-jdk"
  exit 1
fi

echo "▶ Biên dịch với: $(javac -version 2>&1)"
mkdir -p out
# shellcheck disable=SC2046
javac -d out -encoding UTF-8 $(find src -name "*.java")
echo "✅ Xong. Class files trong thư mục out/"
