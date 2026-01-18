# 📦 KitsX - Hytale Kit Manager

> **Nota:** Este plugin foi desenvolvido para a API de servidor do Hytale (Hytale Server API).

**KitsX** é um gerenciador de kits leve, eficiente e totalmente configurável para servidores de Hytale. Ele permite que administradores criem pacotes de itens personalizados, definam permissões específicas e configurem tempos de recarga (cooldowns) através de arquivos JSON simples e intuitivos.

## ✨ Funcionalidades

* **📄 Configuração via JSON:** Adicione, remova ou edite kits rapidamente sem precisar recompilar o plugin.
* **⏱️ Sistema de Cooldown:** Defina tempos de espera (em segundos) individuais para evitar o uso excessivo de kits.
* **🔐 Permissões:** Controle total sobre quem pode usar cada kit (ex: kits exclusivos para VIPs ou Staff).
* **🎒 Gestão Inteligente de Inventário:** Os itens são enviados para o melhor slot disponível (Hotbar ou Mochila).
* **🎨 Feedback Visual:** Mensagens coloridas e formatadas para sucesso, erros e listagem de kits.

## 🚀 Instalação

1. Baixe o arquivo `.jar` do **KitsX**.
2. Coloque o arquivo na pasta `mods/` do diretório do seu servidor Hytale.
3. Inicie o servidor. O plugin criará automaticamente a pasta de configuração.
4. O arquivo de configuração será gerado em: `mods/KitsX/kits.json`.

## ⚙️ Configuração (`kits.json`)

A configuração é feita através de um arquivo JSON. Você pode criar quantos kits quiser seguindo este modelo.

**Localização:** `mods/KitsX/kits.json`

```json
[
  {
    "name": "iniciante",
    "permission": "",
    "icon": "sword_stone",
    "cooldown": 60,
    "items": [
      {
        "id": "Weapon_Spear_Thorium",
        "amount": 1
      },
      {
        "id": "bread",
        "amount": 5
      }
    ]
  },
  {
    "name": "vip",
    "permission": "kits.vip",
    "icon": "sword_diamond",
    "cooldown": 3600,
    "items": [
      {
        "id": "Weapon_Sword_Diamond",
        "amount": 1
      },
      {
        "id": "potion_healing",
        "amount": 3
      }
    ]
  }
]
