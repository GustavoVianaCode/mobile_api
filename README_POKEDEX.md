# 🎮 PokéDex - Android App

Uma aplicação Android completa de Pokédex desenvolvida em Kotlin, utilizando a [PokéAPI](https://pokeapi.co/) para buscar informações sobre Pokémon e Room Database para cache local.

## 📱 Funcionalidades

### ✨ Principais Features

- **Listagem de Pokémon**
  - Visualizar Pokémon por geração (Gen 1-5)
  - Filtrar por versão de jogo (Red/Blue, Gold/Silver, etc.)
  - Exibir sprite, nome, número e tipos de cada Pokémon
  
- **Detalhes do Pokémon**
  - Visualizar informações completas (altura, peso, tipos)
  - Alternar entre sprite normal e shiny
  - Adicionar/remover Pokémon do time
  
- **Meu Time**
  - Criar um time personalizado com até 6 Pokémon
  - Visualizar todos os Pokémon do seu time
  - Remover Pokémon do time facilmente

## 🏗️ Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)** e utiliza as melhores práticas de desenvolvimento Android:

### Estrutura de Pacotes

```
com.devmasterteam.mybooks/
├── api/                    # Retrofit e modelos de API
│   ├── model/             # Data classes para respostas da API
│   ├── PokeApiService.kt  # Interface do Retrofit
│   └── RetrofitClient.kt  # Configuração do Retrofit
├── entity/                # Entidades do Room Database
│   ├── PokemonEntity.kt
│   └── TeamPokemonEntity.kt
├── repository/            # Camada de dados
│   ├── PokemonDAO.kt
│   ├── TeamPokemonDAO.kt
│   ├── PokemonDatabase.kt
│   └── PokemonRepository.kt
├── viewmodel/             # ViewModels
│   ├── PokemonListViewModel.kt
│   ├── PokemonDetailsViewModel.kt
│   └── TeamViewModel.kt
├── ui/                    # Camada de apresentação
│   ├── adapter/
│   ├── viewholder/
│   ├── listener/
│   ├── PokemonListFragment.kt
│   ├── PokemonDetailsFragment.kt
│   └── TeamFragment.kt
└── helper/               # Classes auxiliares e constantes
    └── PokemonConstants.kt
```

## 🛠️ Tecnologias Utilizadas

### Principais Bibliotecas

- **Kotlin** - Linguagem de programação
- **Android Jetpack**
  - Room Database - Persistência local de dados
  - LiveData - Observação de dados
  - ViewModel - Gerenciamento de estado
  - Navigation Component - Navegação entre telas
  - View Binding - Ligação de views
- **Retrofit** - Cliente HTTP para consumo de API REST
- **Gson** - Serialização/deserialização JSON
- **OkHttp** - Cliente HTTP e interceptadores
- **Coil** - Carregamento eficiente de imagens
- **Material Design Components** - UI components

### Dependências (build.gradle.kts)

```kotlin
dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    
    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    // Retrofit & Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    
    // Coil para imagens
    implementation(libs.coil)
}
```

## 🎯 Como Funciona

### 1. Busca de Pokémon

A aplicação busca dados da PokéAPI e salva no banco de dados local (Room) para permitir acesso offline:

```kotlin
// Buscar por geração
suspend fun fetchPokemonByGeneration(generationId: Int): Result<List<PokemonEntity>>

// Buscar por versão de jogo
suspend fun fetchPokemonByVersion(versionName: String): Result<List<PokemonEntity>>

// Buscar detalhes de um Pokémon
suspend fun getPokemonDetails(pokemonId: Int): Result<PokemonEntity>
```

### 2. Gerenciamento de Time

O usuário pode criar um time com até 6 Pokémon:

```kotlin
// Adicionar ao time (máximo 6)
suspend fun addToTeam(pokemon: PokemonEntity): Result<Long>

// Remover do time
suspend fun removeFromTeam(pokemonId: Int): Result<Unit>

// Verificar se está no time
suspend fun isPokemonInTeam(pokemonId: Int): Boolean
```

### 3. Cache Local

Todos os Pokémon buscados são armazenados localmente usando Room Database, permitindo:
- Acesso offline aos dados já visualizados
- Carregamento rápido de informações
- Redução de chamadas à API

## 📊 Banco de Dados (Room)

### Tabela Pokemon
```kotlin
@Entity(tableName = "Pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val spriteUrl: String,
    val shinySpriteUrl: String,
    val types: String,
    val height: Int,
    val weight: Int,
    val generation: Int
)
```

### Tabela TeamPokemon
```kotlin
@Entity(tableName = "TeamPokemon")
data class TeamPokemonEntity(
    @PrimaryKey(autoGenerate = true) val teamId: Int = 0,
    val pokemonId: Int,
    val pokemonName: String,
    val spriteUrl: String,
    val types: String,
    val addedDate: Long
)
```

## 🌐 API Utilizada

**PokéAPI** - https://pokeapi.co/

Endpoints principais:
- `GET /pokemon` - Lista de Pokémon
- `GET /pokemon/{id}` - Detalhes de um Pokémon
- `GET /generation/{id}` - Pokémon por geração
- `GET /version/{name}` - Pokémon por versão de jogo
- `GET /pokedex/{name}` - Pokédex específica

## 🚀 Como Executar

1. **Clone o repositório**
   ```bash
   git clone <url-do-repositorio>
   ```

2. **Abra no Android Studio**
   - Abra o Android Studio
   - File > Open > Selecione a pasta do projeto

3. **Sincronize as dependências**
   - O Android Studio irá automaticamente sincronizar as dependências do Gradle

4. **Execute o app**
   - Conecte um dispositivo Android ou use um emulador
   - Clique em "Run" (▶️) no Android Studio

## 📝 Requisitos

- Android Studio Hedgehog ou superior
- JDK 21
- Android SDK 26 (mínimo) - 35 (target)
- Conexão com internet (para buscar dados da API)

## 🎨 UI/UX

- Material Design 3 components
- Bottom Navigation para navegação principal
- RecyclerView com CardViews para listagem
- Chips para filtros de geração
- Spinner para seleção de versão de jogo
- FloatingActionButton para ações principais
- Estados de loading, erro e vazio
- Transições suaves entre telas

## 🔮 Futuras Melhorias

- [ ] Adicionar busca por nome de Pokémon
- [ ] Implementar paginação infinita
- [ ] Adicionar filtro por tipo
- [ ] Exibir estatísticas de batalha (HP, Attack, Defense, etc.)
- [ ] Adicionar evolução chain
- [ ] Implementar modo escuro
- [ ] Adicionar animações entre telas
- [ ] Comparar Pokémon
- [ ] Exportar/compartilhar time

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👨‍💻 Desenvolvedor

Desenvolvido como exemplo de implementação de uma aplicação Android moderna utilizando:
- Arquitetura MVVM
- Room Database
- Retrofit para API REST
- Navigation Component
- Coroutines para operações assíncronas

---

**PokéAPI** - https://pokeapi.co/  
Todos os dados de Pokémon são fornecidos pela PokéAPI.
