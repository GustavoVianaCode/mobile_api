# 📖 Guia de Uso - PokéDex App

## 🎯 Como Usar a Aplicação

### 1️⃣ Tela Principal - PokéDex

Ao abrir o app, você verá a lista de Pokémon da primeira geração por padrão.

#### Filtrar por Geração
- Use os **chips** no topo da tela (Gen 1, Gen 2, Gen 3, etc.)
- Clique no chip da geração desejada
- A lista será atualizada automaticamente

#### Filtrar por Versão de Jogo
- Use o **Spinner** abaixo dos chips
- Selecione uma versão (Red/Blue, Gold/Silver, etc.)
- A lista mostrará os Pokémon disponíveis naquela versão

#### Interagir com a Lista
- **Clique no card** do Pokémon para ver detalhes completos
- **Clique no ícone de coração** (⭐) para adicionar ao seu time rapidamente

### 2️⃣ Tela de Detalhes

Aqui você vê todas as informações sobre um Pokémon específico.

#### Informações Disponíveis
- **Número e Nome** do Pokémon
- **Sprite oficial** em alta qualidade
- **Tipos** (Grass, Fire, Water, etc.)
- **Altura e Peso**

#### Ações Disponíveis
- **"Ver Versão Shiny"**: Alterna entre sprite normal e shiny
- **"Adicionar ao Time"**: Adiciona o Pokémon ao seu time (máximo 6)
- **Botão de voltar**: Retorna à lista

### 3️⃣ Meu Time

Acesse pela aba "Meu Time" no menu inferior.

#### O que você pode fazer
- Ver todos os Pokémon do seu time (máximo 6)
- Clicar em um Pokémon para ver seus detalhes
- Remover Pokémon do time (ícone de lixeira)
- Contador "X/6" mostra quantos Pokémon você tem

## 🔧 Funcionalidades Técnicas

### Cache Local (Offline)
- Todos os Pokémon buscados são salvos localmente
- Você pode ver Pokémon já carregados mesmo sem internet
- O banco de dados Room garante persistência dos dados

### Carregamento Inteligente
- Primeira vez: busca da API e salva no banco
- Próximas vezes: carrega do banco local (mais rápido)
- Loading indicator mostra quando está buscando da API

### Validações
- **Limite de 6 Pokémon no time**
- **Não permite duplicados** no time
- **Mensagens de erro e sucesso** para todas as ações

## ⚠️ Problemas Comuns e Soluções

### Erro: "Não foi possível carregar Pokémon"

**Causas possíveis:**
1. Sem conexão com internet
2. API fora do ar
3. Timeout de rede

**Soluções:**
- Verifique sua conexão com internet
- Tente novamente após alguns segundos
- Se persistir, os Pokémon em cache local ainda estarão disponíveis

### Erro: "Time completo! Máximo de 6 Pokémon"

**Causa:** Você já tem 6 Pokémon no seu time

**Solução:**
- Vá para a aba "Meu Time"
- Remova um Pokémon existente
- Tente adicionar o novo Pokémon novamente

### Erro: "Pokémon já está no time!"

**Causa:** O Pokémon que você está tentando adicionar já está no seu time

**Solução:**
- Este Pokémon já foi adicionado anteriormente
- Escolha outro Pokémon

### Imagens não carregam

**Causas possíveis:**
1. Problema com a URL da imagem na API
2. Conexão lenta
3. Erro no carregamento

**Soluções:**
- Aguarde alguns segundos (imagens são carregadas progressivamente)
- Verifique sua conexão
- Tente recarregar a lista

### App fica carregando indefinidamente

**Causas possíveis:**
1. Muitas requisições simultâneas à API
2. API com rate limit
3. Conexão muito lenta

**Soluções:**
- Aguarde alguns segundos
- Feche e reabra o app
- Tente carregar uma geração de cada vez

## 📊 Gerações e Versões Disponíveis

### Gerações
- **Gen 1** (1-151): Kanto - Red/Blue/Yellow
- **Gen 2** (152-251): Johto - Gold/Silver/Crystal
- **Gen 3** (252-386): Hoenn - Ruby/Sapphire/Emerald
- **Gen 4** (387-493): Sinnoh - Diamond/Pearl/Platinum
- **Gen 5** (494-649): Unova - Black/White

### Versões de Jogo Suportadas
- Red/Blue
- Yellow
- Gold/Silver
- Crystal
- Ruby/Sapphire
- Emerald
- FireRed/LeafGreen
- Diamond/Pearl
- Platinum
- HeartGold/SoulSilver
- Black/White
- Black 2/White 2
- X/Y
- Omega Ruby/Alpha Sapphire
- Sun/Moon
- Ultra Sun/Ultra Moon
- Sword/Shield
- Scarlet/Violet

## 💡 Dicas de Uso

### Performance
1. **Carregue por geração** em vez de todas de uma vez
2. **Use o cache local**: Pokémon já visualizados carregam instantaneamente
3. **Evite trocar de filtro rapidamente**: Aguarde o carregamento completar

### Montando seu Time
1. Explore diferentes gerações
2. Considere ter tipos variados no seu time
3. Use a versão shiny para personalizar

### Explorando Pokémon
1. Clique em qualquer Pokémon na lista para ver detalhes
2. Use o botão shiny para comparar as versões
3. Adicione seus favoritos ao time para acesso rápido

## 🎮 Fluxo de Uso Recomendado

1. **Início**: Abra o app e veja a Gen 1 carregada
2. **Explore**: Navegue pelas gerações usando os chips
3. **Descubra**: Clique nos Pokémon para ver detalhes
4. **Monte seu time**: Adicione até 6 Pokémon favoritos
5. **Gerencie**: Acesse "Meu Time" para ver e gerenciar sua coleção
6. **Experimente**: Use versões de jogo para ver Pokémon específicos

## 🔐 Privacidade e Dados

- **Dados armazenados localmente**: Nome, sprite, tipos, altura e peso dos Pokémon
- **Sem coleta de dados pessoais**
- **Sem necessidade de login ou cadastro**
- **Dados podem ser limpos desinstalando o app**

## 🆘 Suporte

Se encontrar problemas não listados aqui:

1. Verifique sua versão do Android (mínimo: Android 8.0)
2. Certifique-se de que tem permissão de internet
3. Tente limpar o cache do app nas configurações
4. Reinstale o aplicativo se necessário

---

**Desenvolvido com ❤️ usando Kotlin e Android Jetpack**
