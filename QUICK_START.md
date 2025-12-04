
# 🚀 QUICK START - PokéDex App

## ⚡ Início Rápido em 3 Passos

### 1️⃣ Abrir o Projeto (30 segundos)

```
1. Abra o Android Studio
2. File > Open
3. Navegue até: C:\Users\Gusta\Desktop\mobile_api
4. Clique em OK
5. Aguarde o Gradle Sync automático
```

### 2️⃣ Executar o App (1 minuto)

**Opção A: Com Emulador**
```
1. Tools > Device Manager
2. Clique no ▶️ de um emulador existente
   (ou crie um novo: Pixel 6 Pro, Android 14)
3. Aguarde o emulador iniciar
4. Clique em Run (▶️) na barra superior
```

**Opção B: Com Dispositivo Físico**
```
1. Ative USB Debugging no celular
2. Conecte via USB
3. Aceite a autorização no celular
4. Clique em Run (▶️) na barra superior
```

### 3️⃣ Usar o App

```
1. App abre na tela PokéDex
2. Aguarde ~5 segundos (carregando Gen 1 da API)
3. Explore os Pokémon!
4. Clique em um Pokémon para ver detalhes
5. Adicione ao seu time
6. Vá para aba "Meu Time"
```

---

## 🎯 Funcionalidades Principais

### Tela PokéDex
- **Filtrar por Geração**: Chips (Gen 1-5)
- **Filtrar por Versão**: Spinner (Red, Gold, etc.)
- **Ver Detalhes**: Click no card
- **Adicionar ao Time**: Click no ⭐

### Tela Detalhes
- **Ver Shiny**: Botão "Ver Versão Shiny"
- **Gerenciar Time**: Botão "Adicionar/Remover"
- **Informações**: Tipos, altura, peso

### Tela Meu Time
- **Ver Time Completo**: Até 6 Pokémon
- **Remover**: Click no 🗑️
- **Ver Detalhes**: Click no card

---

## 🔧 Troubleshooting Rápido

### Erro ao sincronizar Gradle
```
File > Invalidate Caches > Invalidate and Restart
```

### Dispositivo não aparece
```powershell
# No terminal
adb kill-server
adb start-server
adb devices
```

### App não carrega Pokémon
```
1. Verifique conexão com internet
2. Aguarde mais alguns segundos
3. Tente trocar de geração (força recarregar)
```

### Imagens não aparecem
```
1. Conexão lenta - aguarde
2. Problema com Coil - reinstale app
3. URL inválida - tente outro Pokémon
```

---

## 📁 Estrutura do Projeto

```
mobile_api/
├── app/src/main/
│   ├── java/.../mybooks/
│   │   ├── api/              ← Retrofit & API
│   │   ├── entity/           ← Room Entities
│   │   ├── repository/       ← DAOs & Repository
│   │   ├── viewmodel/        ← ViewModels
│   │   ├── ui/               ← Fragments & Adapters
│   │   └── helper/           ← Constants
│   └── res/
│       ├── layout/           ← XMLs de tela
│       ├── navigation/       ← Navigation graph
│       └── values/           ← Strings, colors
├── build.gradle.kts          ← Dependências
├── gradle/libs.versions.toml ← Versões
└── docs/                     ← Documentação
```

---

## 🎓 Documentação Completa

### Para Usuários
- 📖 **GUIA_USO.md** - Como usar o app

### Para Desenvolvedores
- 📋 **README_POKEDEX.md** - Visão geral técnica
- ✅ **CHECKLIST_IMPLEMENTACAO.md** - O que foi feito
- 💻 **EXEMPLOS_CODIGO.md** - Snippets úteis
- 🔧 **COMANDOS_UTEIS.md** - Comandos e configs
- 📝 **RESUMO_FINAL.md** - Resumo completo

---

## 🌐 Links Úteis

- **PokéAPI**: https://pokeapi.co/
- **Android Docs**: https://developer.android.com
- **Retrofit**: https://square.github.io/retrofit/
- **Room**: https://developer.android.com/training/data-storage/room
- **Coil**: https://coil-kt.github.io/coil/

---

## ✅ Checklist Antes de Executar

- [ ] Android Studio instalado
- [ ] JDK 21 configurado
- [ ] Android SDK 26-35 instalado
- [ ] Emulador criado OU dispositivo conectado
- [ ] Conexão com internet ativa
- [ ] Projeto aberto no Android Studio
- [ ] Gradle sync completo

---

## 💡 Dicas Rápidas

### Performance
- Primeira execução demora mais (downloads)
- Use cache local após primeira busca
- Carregue uma geração por vez

### Explorando
- Teste diferentes gerações
- Compare sprites normal e shiny
- Monte seu time favorito
- Experimente versões de jogo

### Debug
- Use Logcat para ver mensagens
- Database Inspector para ver Room
- Profiler para performance

---

## 🎮 Primeiros Passos Recomendados

### 1. Primeira Execução
```
1. Execute o app
2. Aguarde carregar Gen 1
3. Scroll pela lista
4. Click em Pikachu (#025)
5. Veja os detalhes
6. Click "Ver Versão Shiny"
7. Click "Adicionar ao Time"
8. Vá para aba "Meu Time"
9. Veja Pikachu no seu time!
```

### 2. Explorando Gerações
```
1. Volte para PokéDex
2. Click no chip "Gen 2"
3. Aguarde carregar
4. Explore Johto!
5. Adicione mais Pokémon ao time
```

### 3. Testando Versões
```
1. Abra o Spinner de versões
2. Selecione "FireRed/LeafGreen"
3. Veja Pokémon específicos
4. Compare com outras versões
```

---

## 🆘 Problemas Comuns

### "Erro ao carregar Pokémon"
**Causa**: Sem internet ou API fora  
**Solução**: Verifique conexão, tente novamente

### "Time completo! Máximo de 6"
**Causa**: Já tem 6 no time  
**Solução**: Remova um para adicionar outro

### "Pokémon já está no time"
**Causa**: Duplicata  
**Solução**: Escolha outro Pokémon

### App trava ao carregar
**Causa**: Muitas requisições  
**Solução**: Aguarde, force stop se necessário

---

## 📱 Requisitos do Sistema

### Mínimo
- Android 8.0 (API 26)
- 50 MB de espaço
- Conexão com internet

### Recomendado
- Android 14.0 (API 35)
- 100 MB de espaço
- Wi-Fi

---

## 🎯 Próximo Passo

### Execute Agora! 🚀

```bash
1. Abra Android Studio
2. Click em Run (▶️)
3. Aguarde instalar
4. Explore sua PokéDex!
```

---

**Pronto! Em menos de 2 minutos você terá sua PokéDex rodando! 🎮**
