# Changelog

## Version 0.4.1

* Reactors now produce heat when they are running and drain heat when inactive/invalid.
* Reactor Controller Tier 2 now outputs redstone in relation to temperature, not energy filled.
* Meltdowns are dependent on the temperature of the Reactor, not the energy buffer.
* Configurable delay between Reactor reaching critical heat and a meltdown triggering.
* Reactors running at 125C are 100% efficient, whilst running hotter increases efficiency.
* Reactor Heat Sinks increase Reactor Controller heat cap by 50C.
* Reactor Vents can be used to slow, stop or even reverse heat gain in a reactor.
* Sound effects.
* Removed Redstone Flux dependency.

## Version 0.4.0

__WARNING: Version 0.4.0 is incompatible with all previous versions of Tiny Reactors.__
__All existing Tiny Reactors blocks have been heavily modified so I recommend removing all reactor structures before updating.__
__Previous configuration files will not work as they have been relocated to config/tinyreactors/config.cfg.__

* Energy Port blocks now use Block Properties to determine Tier.
* Reactor Controller generates internal power that distributes to all Energy Ports.
* _^ Reactor structures can now support more than 1 Energy Port._
* There are now 2 Tiers of Reactor Controller.
* Tier 2 Reactor Controller outputs redstone power equivalent to energy fill percentage.
* Tier 2 Reactor Controller supports redstone modes (Ignore, Active on Redstone, Deactivate on Redstone).
* Energy Port Renderer shows fill percentage on block.
* Capacitor blocks fill from all directions.
* Capaitor blocks output in all directions (apart from back into Energy Ports).
* Capacitor blocks display key information on the front face.
* Waila and Vanilla Information to display Tiers for tiered blocks.

## Pre-Version 0.4.0

All previous changelog information can be located with the various download files from the Tiny Reactors CurseForge page.