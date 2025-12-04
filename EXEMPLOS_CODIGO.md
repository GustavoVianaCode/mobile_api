# 💻 Exemplos de Código - PokéDex App

## 🎯 Snippets Úteis

### Como usar o Repository

```kotlin
// Em um Fragment ou Activity
class MeuFragment : Fragment() {
    
    private lateinit var repository: PokemonRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Obter instância singleton do repository
        repository = PokemonRepository.getInstance(requireContext())
    }
    
    // Buscar Pokémon por geração
    lifecycleScope.launch {
        val result = repository.fetchPokemonByGeneration(1)
        
        result.onSuccess { pokemons ->
            // Fazer algo com a lista de Pokémon
            Log.d("TAG", "Carregados ${pokemons.size} Pokémon")
        }.onFailure { error ->
            Log.e("TAG", "Erro: ${error.message}")
        }
    }
}
```

### Observar LiveData do Room

```kotlin
// No Fragment
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val repository = PokemonRepository.getInstance(requireContext())
    
    // Observar mudanças no banco de dados
    repository.getAllPokemonFromDB().observe(viewLifecycleOwner) { pokemons ->
        // Atualizar UI automaticamente quando dados mudarem
        adapter.updateList(pokemons)
        Log.d("TAG", "Lista atualizada: ${pokemons.size} Pokémon")
    }
}
```

### Carregar imagem com Coil

```kotlin
import coil.load
import coil.transform.CircleCropTransformation

// Básico
imageView.load(pokemon.spriteUrl)

// Com opções
imageView.load(pokemon.spriteUrl) {
    crossfade(true)
    crossfade(300) // duração em ms
    placeholder(R.drawable.ic_launcher_foreground)
    error(R.drawable.ic_launcher_foreground)
    transformations(CircleCropTransformation())
}

// Com callback
imageView.load(pokemon.spriteUrl) {
    listener(
        onSuccess = { _, _ ->
            Log.d("TAG", "Imagem carregada")
        },
        onError = { _, result ->
            Log.e("TAG", "Erro ao carregar imagem: ${result.throwable}")
        }
    )
}
```

### Usar Navigation Component

```kotlin
// Em um Fragment
class ListFragment : Fragment() {
    
    private fun navigateToDetails(pokemonId: Int) {
        // Criar bundle com argumentos
        val bundle = Bundle().apply {
            putInt("pokemon_id", pokemonId)
        }
        
        // Navegar usando action do navigation graph
        findNavController().navigate(
            R.id.action_listFragment_to_detailsFragment,
            bundle
        )
    }
    
    // Ou usando NavDirections (se configurado)
    private fun navigateWithDirections(pokemonId: Int) {
        val action = ListFragmentDirections
            .actionListFragmentToDetailsFragment(pokemonId)
        findNavController().navigate(action)
    }
}

// No Fragment de destino
class DetailsFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Receber argumentos
        val pokemonId = arguments?.getInt("pokemon_id") ?: 0
        
        if (pokemonId > 0) {
            loadPokemonDetails(pokemonId)
        }
    }
}
```

### Trabalhar com ViewModels

```kotlin
class MeuFragment : Fragment() {
    
    // Lazy initialization do ViewModel
    private val viewModel: PokemonListViewModel by viewModels()
    
    // Ou usando ViewModelProvider
    private lateinit var viewModel: PokemonListViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]
    }
    
    // Observar LiveData
    private fun observeViewModel() {
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemons ->
            adapter.updateList(pokemons)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }
    
    // Chamar métodos do ViewModel
    private fun loadPokemon() {
        viewModel.loadPokemonByGeneration(1)
    }
}
```

### Usar Coroutines com ViewModel

```kotlin
class MeuViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PokemonRepository.getInstance(application)
    
    // LiveData para UI
    private val _pokemon = MutableLiveData<PokemonEntity>()
    val pokemon: LiveData<PokemonEntity> = _pokemon
    
    // Função suspensa
    fun loadPokemon(id: Int) {
        // Executar em background usando viewModelScope
        viewModelScope.launch {
            try {
                val result = repository.getPokemonDetails(id)
                
                result.onSuccess { pokemonEntity ->
                    // Atualizar LiveData (UI)
                    _pokemon.value = pokemonEntity
                }
            } catch (e: Exception) {
                Log.e("TAG", "Erro: ${e.message}")
            }
        }
    }
    
    // Função com callback
    fun loadPokemonWithCallback(id: Int, callback: (PokemonEntity?) -> Unit) {
        viewModelScope.launch {
            val result = repository.getPokemonDetails(id)
            
            result.onSuccess { pokemon ->
                callback(pokemon)
            }.onFailure {
                callback(null)
            }
        }
    }
}
```

## 🔧 Operações do Room Database

### Inserir dados

```kotlin
lifecycleScope.launch {
    val pokemon = PokemonEntity(
        id = 25,
        name = "pikachu",
        spriteUrl = "https://...",
        shinySpriteUrl = "https://...",
        types = "electric",
        height = 4,
        weight = 60,
        generation = 1
    )
    
    // Insert único
    pokemonDao.insert(pokemon)
    
    // Insert múltiplo
    val pokemons = listOf(pokemon1, pokemon2, pokemon3)
    pokemonDao.insertAll(pokemons)
}
```

### Consultar dados

```kotlin
// Com LiveData (observável)
pokemonDao.getAllPokemon().observe(viewLifecycleOwner) { pokemons ->
    // UI atualizada automaticamente
}

// Suspensa (uma vez)
lifecycleScope.launch {
    val pokemon = pokemonDao.getPokemonById(25)
    pokemon?.let {
        Log.d("TAG", "Encontrado: ${it.name}")
    }
}

// Com filtro
pokemonDao.getPokemonByGeneration(1).observe(viewLifecycleOwner) { pokemons ->
    // Apenas Pokémon da geração 1
}
```

### Atualizar e Deletar

```kotlin
lifecycleScope.launch {
    // Atualizar
    val pokemon = pokemonDao.getPokemonById(25)
    pokemon?.let {
        val updated = it.copy(name = "Pikachu Atualizado")
        pokemonDao.update(updated)
    }
    
    // Deletar
    pokemon?.let {
        pokemonDao.delete(it)
    }
    
    // Deletar todos
    pokemonDao.deleteAll()
}
```

## 🌐 Chamadas de API com Retrofit

### Básico

```kotlin
lifecycleScope.launch {
    try {
        val response = RetrofitClient.pokeApiService.getPokemonDetails(25)
        
        if (response.isSuccessful) {
            val pokemon = response.body()
            pokemon?.let {
                Log.d("TAG", "Pokémon: ${it.name}")
            }
        } else {
            Log.e("TAG", "Erro: ${response.code()}")
        }
    } catch (e: Exception) {
        Log.e("TAG", "Exceção: ${e.message}")
    }
}
```

### Com tratamento de erros

```kotlin
suspend fun fetchPokemon(id: Int): Result<PokemonDetails> {
    return try {
        val response = apiService.getPokemonDetails(id)
        
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Erro HTTP: ${response.code()}"))
        }
    } catch (e: IOException) {
        Result.failure(Exception("Erro de rede: ${e.message}"))
    } catch (e: Exception) {
        Result.failure(Exception("Erro desconhecido: ${e.message}"))
    }
}

// Usar
lifecycleScope.launch {
    fetchPokemon(25).onSuccess { pokemon ->
        Log.d("TAG", "Sucesso: ${pokemon.name}")
    }.onFailure { error ->
        Log.e("TAG", "Falha: ${error.message}")
    }
}
```

## 🎨 Customizações de UI

### Toast customizado

```kotlin
// Toast simples
Toast.makeText(context, "Mensagem", Toast.LENGTH_SHORT).show()

// Toast com duração longa
Toast.makeText(context, "Mensagem", Toast.LENGTH_LONG).show()

// Toast customizado
val toast = Toast.makeText(context, "Mensagem", Toast.LENGTH_SHORT)
toast.setGravity(Gravity.CENTER, 0, 0)
toast.show()
```

### Snackbar com ação

```kotlin
Snackbar.make(view, "Pokémon adicionado", Snackbar.LENGTH_LONG)
    .setAction("DESFAZER") {
        // Ação ao clicar
        viewModel.removePokemonFromTeam(pokemonId)
    }
    .show()
```

### Dialog de confirmação

```kotlin
AlertDialog.Builder(requireContext())
    .setTitle("Confirmar")
    .setMessage("Remover este Pokémon do time?")
    .setPositiveButton("Sim") { dialog, _ ->
        viewModel.removePokemonFromTeam(pokemonId)
        dialog.dismiss()
    }
    .setNegativeButton("Não") { dialog, _ ->
        dialog.dismiss()
    }
    .show()
```

### ProgressBar programático

```kotlin
// Mostrar
progressBar.visibility = View.VISIBLE

// Esconder
progressBar.visibility = View.GONE

// Toggle
progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
```

## 🧪 Exemplos de Teste

### Unit Test (ViewModel)

```kotlin
class PokemonViewModelTest {
    
    @Test
    fun `loadPokemon deve atualizar LiveData`() = runTest {
        // Arrange
        val viewModel = PokemonListViewModel(application)
        
        // Act
        viewModel.loadPokemonByGeneration(1)
        
        // Assert
        val pokemons = viewModel.pokemonList.getOrAwaitValue()
        assertTrue(pokemons.isNotEmpty())
    }
}
```

### Instrumentation Test (Database)

```kotlin
@RunWith(AndroidJUnit4::class)
class PokemonDaoTest {
    
    private lateinit var database: PokemonDatabase
    private lateinit var dao: PokemonDAO
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).build()
        
        dao = database.pokemonDAO()
    }
    
    @Test
    fun insertAndRetrievePokemon() = runBlocking {
        // Arrange
        val pokemon = PokemonEntity(
            id = 1,
            name = "bulbasaur",
            // ... outros campos
        )
        
        // Act
        dao.insert(pokemon)
        val retrieved = dao.getPokemonById(1)
        
        // Assert
        assertNotNull(retrieved)
        assertEquals("bulbasaur", retrieved?.name)
    }
    
    @After
    fun teardown() {
        database.close()
    }
}
```

## 📱 Lifecycle Observers

```kotlin
class MeuFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Observer customizado
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                Log.d("TAG", "Fragment started")
            }
            
            override fun onStop(owner: LifecycleOwner) {
                Log.d("TAG", "Fragment stopped")
            }
        })
    }
}
```

## 🔄 Flow e StateFlow (Alternativa ao LiveData)

```kotlin
// No ViewModel
class PokemonViewModel : ViewModel() {
    
    private val _pokemonList = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntity>> = _pokemonList
    
    fun loadPokemon() {
        viewModelScope.launch {
            repository.getPokemon()
                .collect { pokemons ->
                    _pokemonList.value = pokemons
                }
        }
    }
}

// No Fragment
lifecycleScope.launch {
    viewModel.pokemonList.collect { pokemons ->
        adapter.updateList(pokemons)
    }
}
```

---

**Dica**: Salve este arquivo como referência rápida para copiar e adaptar código!
