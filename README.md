# SwingArc

2D golf game (Java 17 / JavaFX) for Vanier College — *Program Development in a Graphical Environment*.

**Team:** Kian Dehghani, Yoonchan Rhie, Liam Bohdjalian

---

## Physics engine (Kian) — `physics-engine` branch

This branch completes Kian’s physics lead work: motion **and** collisions.

### What was added

| Piece | Location | Purpose |
| --- | --- | --- |
| `Vector2D` (expanded) | `swingarc.environment.Vector2D` | Math helpers for physics: `copy`, `set`, `dot`, `normalize` / `normalized`, `addInPlace`, `scaleInPlace`, `lengthSquared`, `nearlyEquals`, etc. |
| `Ball` | `swingarc.physics.Ball` | Ball state: position, velocity, radius, moving/grounded/`inWater` flags, surface friction, `launch()` / `stop()` |
| `PhysicsEngine` | `swingarc.physics.PhysicsEngine` | Per-frame update: gravity, wind, friction, stop speed, then collision resolve |
| `CollisionDetector` | `swingarc.physics.CollisionDetector` | Terrain floor contact/bounce, sand friction, water sink, wall bounce, cup check |
| `TerrainType` values | `swingarc.environment.TerrainType` | Friction and bounce for fairway, rough, sand, water, and wall |
| Module wiring | `module-info.java` | Exports / opens `swingarc.physics` (and exports `swingarc.environment`) |
| Build / tests | `pom.xml` | JUnit Jupiter + Surefire so tests run from NetBeans or Maven |
| Unit tests | `src/test/java/physics/` | `Vector2DTest`, `BallTest`, `PhysicsEngineTest`, `CollisionDetectorTest` |

### How the simulation works

- Coordinates match the level JSON: **+X right, +Y down** (1920×1080 canvas).
- While the ball is **airborne**, gravity increases downward velocity and wind applies a horizontal push.
- While the ball is **grounded**, surface friction damps velocity (set from terrain / sand by collisions).
- After each move, `CollisionDetector`:
  - snaps / bounces the ball on `TerrainSegment` floors
  - applies sand friction, sinks the ball in water, bounces off walls
  - can report when the ball is in the cup (`isInCup`)
- When speed drops below `PhysicsEngine.STOP_SPEED`, the ball stops.
- Preferred gameplay call: `physicsEngine.update(ball, dt, level)`.

### Not part of this physics package

- Drag-to-aim input, gameplay canvas, HUD, menus (team / other leads)
- Wiring the game loop / stroke scoring UI (shared integration)

### Running in NetBeans

1. Open this Maven project in NetBeans.
2. Check out the `physics-engine` branch.
3. Run the app as usual (`App`), or right-click tests under **Test Packages** → **Test**.

```text
mvn test          # optional CLI
mvn javafx:run    # optional CLI
```

### Related team areas

- **Yoonchan** — screens / rendering (menu, gameplay, HUD, scorecard)
- **Liam** — level data (`Level`, `LevelLoader`, hole JSON)
