# SwingArc

2D golf game (Java 17 / JavaFX) for Vanier College — *Program Development in a Graphical Environment*.

**Team:** Kian Dehghani, Yoonchan Rhie, Liam Bohdjalian

---

## Physics engine (Kian) — `physics-engine` branch

This branch covers the **first half** of the physics work. Full collision handling (`CollisionDetector`) is left for a later pass.

### What was added

| Piece | Location | Purpose |
| --- | --- | --- |
| `Vector2D` (expanded) | `swingarc.environment.Vector2D` | Math helpers for physics: `copy`, `set`, `dot`, `normalize` / `normalized`, `addInPlace`, `scaleInPlace`, `lengthSquared`, `nearlyEquals`, etc. |
| `Ball` | `swingarc.physics.Ball` | Ball state: position, velocity, radius, moving/grounded flags, surface friction, `launch()` / `stop()` |
| `PhysicsEngine` | `swingarc.physics.PhysicsEngine` | Per-frame update: gravity (airborne), wind (airborne), ground friction, stop when speed is low |
| `TerrainType` values | `swingarc.environment.TerrainType` | Friction and bounce filled in for fairway, rough, sand, water, and wall |
| Module wiring | `module-info.java` | Exports / opens `swingarc.physics` (and exports `swingarc.environment`) |
| Build / tests | `pom.xml` | JUnit Jupiter + Surefire so tests run from NetBeans or Maven |
| Unit tests | `src/test/java/physics/` | `Vector2DTest`, `BallTest`, `PhysicsEngineTest` |

### How the simulation works (so far)

- Coordinates match the level JSON: **+X right, +Y down** (1920×1080 canvas).
- While the ball is **airborne**, gravity increases downward velocity and wind applies a horizontal push.
- While the ball is **grounded**, surface friction damps velocity (friction comes from terrain / will be set by collisions later).
- When speed drops below `PhysicsEngine.STOP_SPEED`, the ball stops.

`Ball.setGrounded(...)` and `Ball.setSurfaceFriction(...)` are ready for `CollisionDetector` to drive once that half is built.

### Not in this half

- `CollisionDetector` (ground, hazards, wall bounces)
- Drag-to-aim input, gameplay canvas, HUD (team / other leads)

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
