## Homework4 for course CS175 in SJTU  
Develop a simple translator app 
and try to grasp the request package with Charles or WireShark.  
Only **Chinese to English** and **English to Chinese** are supported in this project.

### Todo list
- [x] Create basic UI components.  
  Basically, The main UI consists of  3 components, including *Input Region*,
  *Translate Button* and *Sow Result Region*.  
  After inputting the text, click "TRANSLATE" Button to get the translation.
- [x] Use given api to send request ans show responses  
    - [x] Automatically detect whether the input is Chinese or English.  
      Use Built-in function `Character.isIdeographic()` to automatically detect language.
    - [x] Build data class structure from the json file.  
      For different languages, I ues two type of class structures respectively.  
      However, it seems they are identical in the filed wanted.
    - [x] Get and show api return value with `handler`.


- [x] Use *WireShark* to grasp the request package and export related info.  
  Connect the mobile phone to the hotspot opened by the computer, 
  and run wireshark on the computer to capture packets.  
  The export file can be found in [catch_wireshark.pcapng](catch_wireshark.pcapng), showing all the connection processes, 
  including DNS, TCP and TLS.  
  My phone uses ip address: *192.168.137.135*.  
  DNS server is my computer, so the ip address is *192.168.137.1*.  
  
  
- [x] Cache the request to reduce network overhead.  

    I use a hashmap `mycache` in memory to cache all requests in running time.  
    When a duplicate translation request is sent, program just read it from `mycache`,
    other than from api request.

**N.B** This homework comes from Bytedance.
