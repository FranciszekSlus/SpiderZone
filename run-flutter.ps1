# Uruchom aplikacje Flutter (nie Kotlin z folderu app/)
Set-Location $PSScriptRoot\flutter_app
Write-Host "Instaluje SpiderZone Flutter na emulatorze..." -ForegroundColor Green
Write-Host "Stara Kotlin ma 6 zakladek. Flutter ma 4: Home, Hodowla, Rozmnazanie, Profil." -ForegroundColor Yellow
flutter run
