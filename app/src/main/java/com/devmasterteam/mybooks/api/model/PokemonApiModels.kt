package com.devmasterteam.mybooks.api.model

import com.google.gson.annotations.SerializedName

// Resposta da lista de Pokémon
data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

// Detalhes completos do Pokémon
data class PokemonDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("sprites")
    val sprites: PokemonSprites,
    @SerializedName("types")
    val types: List<PokemonTypeSlot>
)

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("other")
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?
)

data class PokemonTypeSlot(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: PokemonType
)

data class PokemonType(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

// Geração de Pokémon
data class GenerationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonSpeciesBasic>
)

data class PokemonSpeciesBasic(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

// Versão de jogo
data class VersionResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("version_group")
    val versionGroup: VersionGroup
)

data class VersionGroup(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class VersionGroupResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("generation")
    val generation: GenerationBasic,
    @SerializedName("pokedexes")
    val pokedexes: List<PokedexBasic>
)

data class GenerationBasic(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PokedexBasic(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PokedexResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon_entries")
    val pokemonEntries: List<PokemonEntry>
)

data class PokemonEntry(
    @SerializedName("entry_number")
    val entryNumber: Int,
    @SerializedName("pokemon_species")
    val pokemonSpecies: PokemonSpeciesBasic
)
