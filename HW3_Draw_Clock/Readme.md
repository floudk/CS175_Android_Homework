Homework3 for course CS175 in SJTU.
## To do
### Exercise 1
- [x] Refresh every second to make the pointer move.  
  Use Handler in file `ClockActivity` to redraw the object every second.
- [x] Draw minute pointer.  
  Imitate the hour pointer and second pointer drawing method, add the minute pointer to `ClockView`.
### Exercise 2
- [x] Add numbers to the dial(Complete function `drawHoursValues`).
  To avoid redundant calculations, drawHoursValues are performed at the same time as drawDegrees,
  Hence, I modified function `drawDegrees()` to do so, and just delete function `drawHoursValues()`  
  Only four numbers 3, 6, 9, 12 are drawn here.
- [x] Add an area to show time with text number.   
  Add function`drawBox()`,and modify drawing functions for accepting the `Data` argument to be ready for further tasks.
### Exercise 3
- [ ] Manually move the pointer to adjust the time.  
  It might be done by splitting the whole View into multiple views, and setting Drag or Touch detection for each,
  but it seems too complicated for me with so much homework from other courses.  
------------  

**Notice**:  
1. Program should be robust and avoid crashing.
2. Avoid using overly complex logic.

