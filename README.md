# OP-1Sequencer

An Elektron-style sequencer for the original OP-1 made in Java. 

![1](https://github.com/chr-hall/OP-1Sequencer/assets/117752515/1ecbf4cd-3d03-4d1d-9fcb-44da12d542e2)

### Features:
- 4 banks with 16 steps each
- Individual note and velocity values for each step
- Stable timing using the clock from OP-1
- Quick and fun to use


### How to set up:
Set the OP-1 to “beat match” in the tempo menu.

Connect OP-1 via USB. 

Compile and run the OP1SeqMain class. 

Press play on the OP-1 to start the sequencer.


### Controls:


The sequencer is controlled via the computer keyboard. Use the arrow keys to select a step, and press Enter to toggle it on or off. 

The graphics on the bottom left represents the OP-1 keyboard - the current selected note value is highlighted in brown. Activating a step shows that specific step's note value in green. Values are also displayed on the top left. 

Hold down A and use the Up and Down arrow keys to change the note value. If this is done on an active step, its note value will change in real time. You can move beyond the range of the OP-1 keyboard. 

Likewise, hold down S and use the Up and Down arrow keys to change velocity.


There are 4 pages of steps available. Hold down D and use the Up and Down arrow keys to move between them. What page you are on is displayed top the left of the steps.  

### Example:
![2](https://github.com/chr-hall/OP-1Sequencer/assets/117752515/096331e1-89ec-4332-a15d-e75b40053a14)

_Example of the sequencer in use. Light gray steps are active. Step 7 is selected and active with a note value of D#4 (highlighted in green on the OP-1 keyboard bottom left) and full velocity (127)_

### Notes:

The sequencer is tied to the MIDI sync signal from the OP-1 and doesn’t have its own clock. It starts and stops with the OP-1 and always resumes on step 1. For best results, start the OP-1 on an even beat (beat 1 of a loop).

The Sequencer uses the javax.sound.midi API to receive and send MIDI data. The GUI is made using Swing. 
