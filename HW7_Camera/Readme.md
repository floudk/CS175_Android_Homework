# HW7_CAMERA
Homework 7 for course CS175 in SJTU.  
Build a simple camera APP.

------
## To do list
- [x] Basic UI as shown in slide.  
  Simply set up the UI as shown in slide. Here I use the basic structre of given CameraDemo project, and do my part in the `VideoRecordActivity`.  
- [x] Custom storage file name.   
  If input is empty, I set the name still to be timestamp by default.  
- [x] Show information about the recent photo.  
  Use a textView to show the file path and timestamp of recent photo.  
- [x] Flip camera.  
  Use switch button and setClickListener() to check current `CameraInfo`, then switch it.
- [x] Preview of the last photo.    
  Traverse all files in the storage location to find the most recently modified file.  
- [x] Click the button to take a photo, long press the button to take a video, 
  the video ends after release.
  Use `onTouchListener()` to tell short and long click and release button to finish video recording.
  ![792d12d38a3409ae4c999c6c4d4d071](https://user-images.githubusercontent.com/50905239/144882187-38f93acc-a5bf-4898-8db5-eeaea5526e3e.jpg)

- [ ] (Optional) Two-finger zoom and display zoom factor.
- [ ] (Optional) Use the new Camera2 API.

------
