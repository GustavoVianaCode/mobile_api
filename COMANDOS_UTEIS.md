# 🚀 Comandos e Configurações - PokéDex App

## 📦 Build e Execução

### No Android Studio

1. **Abrir o Projeto**
   ```
   File > Open > Selecione a pasta mobile_api
   ```

2. **Sync Gradle**
   - O Android Studio fará automaticamente
   - Ou clique em "Sync Now" na barra superior
   - Ou: `File > Sync Project with Gradle Files`

3. **Build do Projeto**
   - `Build > Make Project` (Ctrl+F9)
   - Ou use o botão de build (🔨)

4. **Executar o App**
   - Conecte um dispositivo Android via USB (com USB Debugging ativado)
   - Ou inicie um emulador Android
   - Clique em Run (▶️) ou Shift+F10

### Via Terminal (Windows PowerShell)

```powershell
# Navegar até a pasta do projeto
cd C:\Users\Gusta\Desktop\mobile_api

# Build do projeto
.\gradlew build

# Instalar no dispositivo conectado
.\gradlew installDebug

# Executar testes
.\gradlew test

# Limpar build
.\gradlew clean

# Build + Instalar
.\gradlew clean build installDebug
```

## 🛠️ Configurações Necessárias

### Requisitos Mínimos

- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **JDK**: 21
- **Android SDK**: API 26 (Android 8.0) ou superior
- **Gradle**: 8.x (incluído no projeto)
- **Kotlin**: 2.0.21 (configurado no projeto)

### SDK Manager

Certifique-se de ter instalado:
```
Tools > SDK Manager

SDK Platforms:
- ✅ Android 14.0 (API 35) - Target SDK
- ✅ Android 8.0 (API 26) - Min SDK

SDK Tools:
- ✅ Android SDK Build-Tools 35
- ✅ Android Emulator
- ✅ Android SDK Platform-Tools
- ✅ Google Play Services
```

## 🔧 Configurações do Dispositivo

### Dispositivo Físico

1. **Ativar Modo Desenvolvedor**
   ```
   Configurações > Sobre o telefone > 
   Toque 7x em "Número da versão"
   ```

2. **Ativar USB Debugging**
   ```
   Configurações > Sistema > Opções do desenvolvedor >
   Ativar "Depuração USB"
   ```

3. **Conectar via USB**
   - Conecte o dispositivo ao computador
   - Aceite a autorização de depuração no dispositivo
   - Verifique com: `adb devices`

### Emulador Android

**Criar novo emulador:**
```
Tools > Device Manager > Create Device

Recomendado:
- Dispositivo: Pixel 6 Pro
- System Image: Android 14 (API 35)
- RAM: 2GB+
- Armazenamento: 2GB+
```

## 📱 Comandos ADB Úteis

```powershell
# Listar dispositivos conectados
adb devices

# Instalar APK manualmente
adb install app/build/outputs/apk/debug/app-debug.apk

# Desinstalar app
adb uninstall com.devmasterteam.mybooks

# Ver logs em tempo real
adb logcat | Select-String "Pokemon"

# Limpar dados do app
adb shell pm clear com.devmasterteam.mybooks

# Ver banco de dados (requer root ou emulador)
adb shell
cd /data/data/com.devmasterteam.mybooks/databases/
ls -la
```

## 🗃️ Comandos Room Database (Debug)

### Inspecionar banco no Android Studio

1. **Abrir Database Inspector**
   ```
   View > Tool Windows > App Inspection > Database Inspector
   ```

2. **Com app rodando**, você verá:
   - Tabela `Pokemon`
   - Tabela `TeamPokemon`
   - Pode executar queries SQL
   - Visualizar dados em tempo real

### Via ADB (Emulador/Root)

```bash
# Entrar no shell do dispositivo
adb shell

# Navegar até o banco
cd /data/data/com.devmasterteam.mybooks/databases/

# Abrir SQLite
sqlite3 pokemon_db

# Comandos SQL úteis
.tables                           # Listar tabelas
SELECT * FROM Pokemon LIMIT 5;    # Ver 5 Pokémon
SELECT * FROM TeamPokemon;        # Ver time
SELECT COUNT(*) FROM Pokemon;     # Contar Pokémon
.exit                             # Sair
```

## 🧪 Testes

### Executar Testes Unitários

```powershell
# Todos os testes
.\gradlew test

# Testes de um módulo específico
.\gradlew app:test

# Ver relatório HTML
start app\build\reports\tests\testDebugUnitTest\index.html
```

### Executar Testes de Instrumentação

```powershell
# Com dispositivo conectado
.\gradlew connectedAndroidTest

# Ver relatório
start app\build\reports\androidTests\connected\index.html
```

## 📊 Análise de Código

### Lint (Análise estática)

```powershell
# Executar Lint
.\gradlew lint

# Ver relatório
start app\build\reports\lint-results-debug.html
```

### Gerar APK

```powershell
# APK Debug
.\gradlew assembleDebug

# Localização:
# app\build\outputs\apk\debug\app-debug.apk

# APK Release (requer assinatura)
.\gradlew assembleRelease
```

## 🔍 Debug e Profiling

### Logcat Filtrado

```powershell
# Filtrar por TAG
adb logcat -s PokemonRepository

# Filtrar por package
adb logcat | Select-String "com.devmasterteam.mybooks"

# Ver apenas erros
adb logcat *:E

# Limpar e seguir
adb logcat -c; adb logcat
```

### Profiler no Android Studio

```
View > Tool Windows > Profiler

Disponível:
- CPU Profiler (performance)
- Memory Profiler (memory leaks)
- Network Profiler (API calls)
- Energy Profiler (battery usage)
```

## 🌐 Teste de API (Fora do App)

### PowerShell

```powershell
# Testar endpoint da PokéAPI
Invoke-RestMethod -Uri "https://pokeapi.co/api/v2/pokemon/1"

# Ver geração
Invoke-RestMethod -Uri "https://pokeapi.co/api/v2/generation/1"

# Salvar resposta em arquivo
Invoke-RestMethod -Uri "https://pokeapi.co/api/v2/pokemon/25" | ConvertTo-Json -Depth 10 > pikachu.json
```

### Postman/Insomnia

```
GET https://pokeapi.co/api/v2/pokemon/1
GET https://pokeapi.co/api/v2/generation/1
GET https://pokeapi.co/api/v2/pokedex/kanto
```

## 🧹 Limpeza e Manutenção

```powershell
# Limpar build completo
.\gradlew clean

# Limpar cache do Gradle
.\gradlew cleanBuildCache

# Invalidar caches do Android Studio
# File > Invalidate Caches > Invalidate and Restart

# Limpar dados do emulador
# AVD Manager > Actions > Wipe Data
```

## 📝 Variáveis de Ambiente (Opcional)

Adicionar ao PATH (caso necessário):

```powershell
# Android SDK
$env:ANDROID_HOME = "C:\Users\<USER>\AppData\Local\Android\Sdk"

# Platform Tools (adb)
$env:Path += ";$env:ANDROID_HOME\platform-tools"

# Emulator
$env:Path += ";$env:ANDROID_HOME\emulator"
```

## 🆘 Troubleshooting Comum

### Gradle Sync Failed

```powershell
# Limpar e retentar
.\gradlew clean
# Depois: File > Sync Project with Gradle Files
```

### Device Not Found

```powershell
# Reiniciar ADB
adb kill-server
adb start-server
adb devices
```

### Out of Memory

```
# Aumentar heap do Gradle em gradle.properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
```

### App não instala

```powershell
# Desinstalar versão antiga
adb uninstall com.devmasterteam.mybooks

# Reinstalar
.\gradlew installDebug
```

## 📚 Recursos Úteis

- **PokéAPI Docs**: https://pokeapi.co/docs/v2
- **Android Docs**: https://developer.android.com
- **Retrofit**: https://square.github.io/retrofit/
- **Room**: https://developer.android.com/training/data-storage/room
- **Coil**: https://coil-kt.github.io/coil/

---

**Dica**: Mantenha este arquivo como referência rápida durante o desenvolvimento!
