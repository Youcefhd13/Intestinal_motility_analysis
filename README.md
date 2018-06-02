Intesitinal motility analysis plugin for Fiji

Make sure you have the lastest version of Fiji installed. You can find Fiji at the website: http://imagej.net/Fiji
Installation of the plugin: 
Unzip the plugin file you acquire, and extract the contents to the plugins folder of Fiji (example: ‘C:\Fiji.app\plugins’. Restart Fiji, Then, the plugin will then show up in the plugins menu of Fiji. 

How it works: 
Creating the Kymograph: 

1. Draw a line, segmented line or freehand drawing. Then add to the ROI list by typing ‘t’ on the keyboard or clicking ‘add’ in the ROI manager. Then click ‘Use selected polyline as Kymograph path’

2. You can now select parameters of the kymograph (you can update those later too):
- The line thickness: the number of points around the line drawn the to be averaged for kymograph extraction.
- (optional) Time points in kymograph: parameter determines the Y dimension size of the kymograph (time dimension). This is useful for longer recordings (<1000 frames) and limiting the visualized kymograph size to better identify contractions.

3. Click create Kymograph, a new window with the kymograph will appear. 

4. Click update Kymograph to update the kymograph image with adjusted parameters. Every update will create a new window.

Measuring contraction parameters:
1.	Select the kymograph you will work on. Click select contractions. A ROI manager window is automatically shown and the ‘line’ tool is selected. Create a line along a contraction. Then, press ‘t’ while the window is still active to add the selection to the ROI list or move  you mouse to the ROI manager window and select ‘add’.
2.	Repeat step 1 as many times as necessary. Note: Do not let the lines reach outside the borders of the kymograph image.
3.	Create results table: If you prefer results in abstract units (pixels). Tick the ‘No scale’ box and click ‘calculate results’. Otherwise, enter the necessary parameters* to calculate results in physical units and then select ‘calculate results’.
*Image scale is the number of pixels per mm, This depends on the equipment used to record.
frames per second. If you record with 200ms interval between snapped images, the value here is 5.
The results are calculated using basic arithmetics. Where Velocity = distance/time
The results table can be saved and processed in other software (i.e. Excel).


To report bugs or technical issues please contact Youcef.kazwiny@kuleuven.be



