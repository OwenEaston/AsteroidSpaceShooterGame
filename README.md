# Asteroid Space Shooter

A Java recreation of the classic **Atari - Asteroids (1979)** arcade game developed as part of the CE218 Game Programming module.
![Gameplay Demo](gif/gameplayGIF.gif)
## Features

* Player-controlled spaceship with physics-based movement
* Screen wrapping
* Asteroid destruction and splitting
* Enemy ships with projectile attacks
* Kamikaze enemy type that actively hunts the player
* Temporary spawn invincibility
* Rechargeable shield system
* Random power-ups
* Persistent high score system
* Screen shake and visual feedback effects
* Score and lives HUD

## Controls

| Key   | Action          |
| ----- | --------------- |
| ↑     | Accelerate      |
| ← / → | Rotate ship     |
| Space | Fire weapon     |
| Shift | Activate shield |

## Gameplay

Destroy Asteroids and Enemy Ships to gain a higher score all while avoiding the incoming collisions from those same targets, as well as your own bullets. The game becomings increasing chaotic and difficult as more enemy types are introduced and the game area becomes increasingly more cramped.

## Technical Highlights

* Object-oriented Java design
* Collision detection system
* Persistent file-based high score saving
* Procedurally generated asteroid shapes
* Custom enemy AI behaviour
* Power-up system
* Screen shake using AffineTransform

## Running the Game

Open:

src/game/Game.java

and run the `Game` class from your IDE.