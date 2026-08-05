#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

if ! command -v java >/dev/null 2>&1 || ! command -v javac >/dev/null 2>&1; then
  VSCODE_JRE=$(ls -d "$HOME"/.vscode/extensions/redhat.java-*/jre/*/bin 2>/dev/null | head -1 || true)
  if [ -n "${VSCODE_JRE:-}" ]; then
    if [ -x "$VSCODE_JRE/java" ] || [ -x "$VSCODE_JRE/javac" ]; then
      export PATH="$VSCODE_JRE:$PATH"
    fi
  fi
fi

if ! command -v java >/dev/null 2>&1 || ! command -v javac >/dev/null 2>&1; then
  echo "❌ Không tìm thấy java hoặc javac. Cài JDK 17+ rồi chạy lại."
  exit 1
fi

echo "▶ Biên dịch mã nguồn và kiểm thử..."
mkdir -p out

# Xây dựng classpath từ thư mục lib và lib/test
CP="out"
for jar in lib/*.jar lib/test/*.jar; do
  if [ -f "$jar" ]; then
    CP="$CP:$jar"
  fi
done

# Biên dịch cả thư mục src và test
# shellcheck disable=SC2046
javac -d out -encoding UTF-8 -cp "$CP" $(find src test -name "*.java")

echo "✅ Biên dịch thành công."
echo "▶ Chạy kiểm thử với JUnit 5 và AssertJ Swing..."

# Chạy JUnit Console Standalone Launcher để chạy toàn bộ test
java -Dfile.encoding=UTF-8 -jar lib/test/junit-platform-console-standalone-1.10.2.jar \
  --class-path "$CP" \
  --scan-class-path

echo "🎉 Hoàn tất chạy kiểm thử."
