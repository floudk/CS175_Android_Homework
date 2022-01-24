# HW6_PLAYER  
Homework 6 for course CS175 in SJTU.  
Build an app to display network pictures and videos.

------
## To do list

### For showing Pics
- [x] Display pictures from network, including dynamic images and static images.
- [x] Swipe left and right to switch between different pictures.
- [x] (Optional) Use gesture controls to zoom in, zoom out.
  The main code is in `PictureActivity`, which basically derives from the teacher’s demo code.   
  Especially, for optional part, I use `Matrix` related operations to make it.  
  For illustration purposes, I only two images, one for dynamic image (i.e, GIF), and another for 
  static image (i.e, JPG). More images can be added into the `view_pager` if needed.  
### For showing Videos
- [x] Play, pause and replay functions
  Create three buttons to play, pause and replay.
- [x] Play progress bar display, including time.
  Use another thread to getPosition from VideoView and update display messages.  
- [x] Click/slide to jump to the specified position. 
  Use `setOnTouchListener` to check the position of touch event and then set video to the new position.
- [ ] (Optional) Display full-screen mode in horizontal screen mode.

------

**N.B**, All display resources come from the Internet.