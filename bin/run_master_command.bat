@echo off
REM =========================================================
REM マスタ管理コマンド 起動用バッチファイル
REM
REM 前提:
REM   ・事前に Eclipse または mvn package でビルドしていること
REM     （target\classes 配下に .class ファイルが生成されていること）
REM   ・lib フォルダに ojdbc8.jar を配置していること
REM     （Oracle公式サイトからダウンロードして配置してください）
REM
REM 実行方法:
REM   プロジェクト直下で bin\run_master_command.bat をダブルクリック、
REM   またはコマンドプロンプトから実行してください。
REM =========================================================

setlocal
cd /d %~dp0\..

set CLASSPATH=target\classes;src\main\resources;lib\ojdbc8.jar

java -cp "%CLASSPATH%" jp.co.sample.orderentry.command.MasterManageCommand

pause
