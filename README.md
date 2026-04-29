# VitalityOrbs

A lightweight, high-performance Paper plugin designed for Minecraft 1.21.1. VitalityOrbs introduces a new gameplay mechanic where mobs have a configurable chance to drop Vitality Orbs, providing players with immediate health restoration and tactical movement buffs.

## Core Features

* **Procedural Drops**: Mobs drop unique orb entities upon death based on a customizable probability.
* **Instant Interaction**: Players collect orbs by walking over them, triggering immediate effects without inventory clutter.
* **Healing Mechanics**: Each orb restores 2 health points (1 full heart) by default.
* **Tactical Buffs**: Grants a Speed I potion effect for 5 seconds to reward aggressive play.
* **Visual Feedback**: Utilizes heart and happy-villager particles combined with spatial audio cues for a satisfying collection experience.
* **Configurable Lifespan**: Orbs automatically despawn after a set period (default 30 seconds) to prevent entity lag.

## Configuration

The plugin generates a `config.yml` on first run. All values can be modified without a server restart (requires a plugin reload).

```yaml
drop-chance: 0.05

messages:
  enabled: true
  pickup: "&aYou picked up a &6&l{orb}&a!"

orb:
  display-name: "&6&lVitality Orb"
  despawn-timer: 30
```

## Installation

1. Place the `VitalityOrbs.jar` file into your server's `plugins` directory.
2. Start the server to generate the default configuration.
3. Modify `config.yml` in the `plugins/VitalityOrbs/` folder as needed.
4. Use a plugin manager or restart the server to apply changes.

## Development

Built using Java 21 and the PaperMC API.

### Compilation
To build the project from source, use the following commands:

```bash
./gradlew build
```

The resulting JAR will be located in the `build/libs/` directory.

---
**Author:** ranawise  
**Compatibility:** Paper 1.21.1+
