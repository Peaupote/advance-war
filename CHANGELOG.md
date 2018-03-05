# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
  - Animation while moving
  - Fight implemented in the view
  - Infantry, AirFighter unit
  - Commander Powerbar in PlayerPanel
### Changed
  - Path finding for the shortest path for unit move

## [0.0.6] - 2018-22-02
### Added
  - Unit movement
  - PathRenderer
  - Commanders
### Changed
  - Unit vision, reachableLocation, attack range
### Fixed
  - Fog of war bug

## [0.0.5] - 2018-14-02
### Added
  - Unit selection & cursor movements when unit selected
  - Day panel first implementation
  - All units classes & a few unit interfaces
  - Minimap
  - Coherent random map generation
### Changed
  - Movement type becomes an enum
  - convention : use of 4 spaces to indent

## [0.0.4] - 2018-02-11
### Added
  - User panel to finish his turn
### Changed
  - Split units and terrains in two seperated layers
  - Make Terrains singletons

## [0.0.3] - 2018-02-07
### Added
  - Units in the model
  - Add user interface components
### Changed
  - Improve implementation of terrains and buildings in the model
  - Move fogwar from the view to the model


## [0.0.2] - 2018-02-02
### Added
  - Terrains and buildings in the model
  - Fogwar on the map
### Changed
  - Add Renderer interface to split better model and view
  - Improve scrolling animation

## [0.0.1] - 2018-01-30
### Added
  - First implementation of MVC structure
  - Camera and cursor movement
