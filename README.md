# Interactive Java Block Simulation 

Built using **Java Swing** and **AWT**. The program renders 20 dynamic blocks on-screen and allows real-time manipulation through advanced mouse and keyboard controls.

## Key Features & Controls

### Mouse Interactions
* **Left Click:** Raises the clicked block to the very top of the z-ordering (the end of the block list).
* **Shift + Left Click:** Lowers the topmost block to the very bottom (beginning) of the block list.
* **Right Click:** Deletes the topmost block at the clicked coordinates.
* **Shift + Right Click:** Deletes all blocks occupying that pixel coordinate.
* **Middle Click:** Triggers an explosion effect, converting the targeted block into 1-pixel radiant fragments[cite: 13].

### Keyboard Shortcuts
* **`N` Key:** Spawns a new randomly positioned block on the screen[cite: 13].
* **`S` Key:** Randomly shuffles and relocates all blocks within bounds[cite: 13].
* **`E` Key:** Triggers an all-block explosion effect[cite: 13].
* **`I` Key:** Triggers an all-block implosion effect.

### Simulation Controls & Extras
* **Playback Controls:** Fully functional `start`, `stop`, `step`, `reset`, and `quit` action buttons with collision physics (blocks bounce off walls).
* **Explosion Speed Control:** Dynamic radio buttons allowing users to adjust the radiation speed of exploding fragments.
* **Audio Integration:** Built-in background music playback support via `AudioSystem` and `Clip`.
* **Export Tool:** Custom save functionality allowing users to snapshot the panel drawing and rename saved files interactively.

## Tech Stack & Architecture
* **Language:** Java
* **Frameworks/Libraries:** Java Swing (`JFrame`, `JPanel`, `JButton`, `Timer`, `JRadioButton`), Java AWT (`Graphics`, `Color`, `MouseEvent`, `KeyEvent`), and Java Sound API.
* **Core Components:**
  * `BlocksMain.java`: Handles GUI layout, action listeners, mouse events, key adapters, and animation loops.
  * `BlockManager.java`: Manages the underlying `ArrayList` collection of blocks, z-ordering, collision detection, and particle explosion/implosion calculations.
  * `Block.java` & `ExplodingBlock.java`: Object-oriented blueprints handling position, size, velocity (`dx`, `dy`), and rendering logic.
