# JiraCalibration
Small POC example how to calculate next calibration date (current calibration date and allowed time period known)  
To be executed as a Script Runner postfunction 

Add following custom fields to Jira issue:
 
"Calibration done", date picker to define last calibration date  
"Calibration valid period (in months)", single text field, use numbers as input  
"Next calibration required date", date picker to define next required calibration date (current date + months)  
