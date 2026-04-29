# VitalityOrbs 🧪

A lightweight Paper plugin that adds a "looter" mechanic to mob drops. Mobs have a chance to drop **Vitality Orbs** that provide immediate healing and temporary buffs upon collection.

---

## ✨ Features

- **Dynamic Drops**: Mobs drop glowing Nether Stars with a customizable chance.
- **Immediate Feedback**: Heart particles and level-up sounds when picked up.
- **Healing & Buffs**: Restores 1 heart (+2 HP) and grants Speed I for 5 seconds.
- **Configurable**: Change drop rates, despawn timers, and pickup messages.
- **Performant**: Minimal overhead with optimized task scheduling.

## ⚙️ Configuration

The `config.yml` allows full control over the orb mechanics:

```yaml
drop-chance: 0.05 # 5% chance

messages:
  enabled: true
  pickup: "&aYou picked up a &6&l{orb}&a!"

orb:
  display-name: "&6&lVitality Orb"
  despawn-timer: 30 # Seconds until orb disappears
```

## 🚀 Installation

1. Download the latest `VitalityOrbs.jar`.
2. Drop it into your server's `plugins` folder.
3. Restart the server or load the plugin.
4. Edit `plugins/VitalityOrbs/config.yml` to your liking and reload.

## 🛠️ Building from Source

Requirements:
- JDK 21
- Gradle

```bash
git clone https://github.com/ranawise/VitalityOrbs.git
cd VitalityOrbs
gradle build
```
The compiled JAR will be in `build/libs/`.

---
*Created by ranawise*
