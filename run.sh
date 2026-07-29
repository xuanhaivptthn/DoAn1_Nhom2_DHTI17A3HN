#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

if ! command -v java >/dev/null 2>&1; then
  VSCODE_JRE=$(ls -d "$HOME"/.vscode/extensions/redhat.java-*/jre/*/bin 2>/dev/null | head -1 || true)
  if [ -n "${VSCODE_JRE:-}" ] && [ -x "$VSCODE_JRE/java" ]; then
    export PATH="$VSCODE_JRE:$PATH"
  fi
fi

if [ ! -d out ] || [ -z "$(ls -A out 2>/dev/null)" ]; then
  echo "Chưa biên dịch. Đang chạy compile.sh..."
  ./compile.sh
fi

if ! command -v java >/dev/null 2>&1; then
  echo "❌ Không tìm thấy java. Cài JDK 17+ rồi chạy lại."
  exit 1
fi

echo "▶ Khởi chạy Quản lý tài khoản..."
java -Dfile.encoding=UTF-8 -cp "out" QuanLySanBong.DoAn1_Nhom2_DHTI17A3HN
