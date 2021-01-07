// Jira postfunction example how to calculate next calibration date when current calibration date and time period (in months) is known
//
// Author: mika.nokka1@gmail.com
// MIT-licensed
//
//

import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import org.apache.log4j.Level

import java.util.regex.Pattern

import java.text.SimpleDateFormat



// ----CONFIGURATIONS------------------------------------------------------------------------------------------------------------------------------
// JIRA Custom field definitions
def CALIBRATED="Calibration done"
def TIMEINMONTHS="Calibration valid period (in months)"
def NEXTCALIBRATION="Next calibration required date"


// set logging to Jira log
def Logger mylogger
mylogger = Logger.getLogger("Calibrator")
mylogger.setLevel(Level.DEBUG) // or INFO (prod) or DEBUG (development)
def CustomFieldManager = ComponentAccessor.getCustomFieldManager()
// ----END OF CONFIGURATIONS-------------------------------------------------------------------------------------------------------------------------




mylogger.info("Processing calibration dates for issue: $issue")


CalibratedObject=CustomFieldManager.getCustomFieldObjectsByName(CALIBRATED).find()
CalibratedFieldValue=issue.getCustomFieldValue(CalibratedObject)
mylogger.debug("Current CalibratedFieldValue: ${CalibratedFieldValue}")

TIMEINMONTHSObject=CustomFieldManager.getCustomFieldObjectsByName(TIMEINMONTHS).find()
TIMEINMONTHSFieldValue=issue.getCustomFieldValue(TIMEINMONTHSObject)
mylogger.debug("Current TIMEINMONTHSFieldValue: ${TIMEINMONTHSFieldValue}")






/* --------------------------------------------------------------------------------------------------------------------------------
 Get custom field value.
 --------------------------------------------------------------------------------------------------------------------------------- */
 
 def GetCustomFieldValue (CustomFieldName,mylogger,CustomFieldManager) {
							 
	 def CustomFieldObject=CustomFieldManager.getCustomFieldObjectsByName(CustomFieldName).find()
	 assert CustomFieldObject : "Problems with customfield (${CustomFieldName}). Check Jira Config and automation code"
	 mylogger.debug("${CustomFieldName}: ${CustomFieldObject}")
	 CustomFieldValue=issue.getCustomFieldValue(CustomFieldObject)
	 assert CustomFieldValue :  "Problems with customfield (${CustomFieldName}). Check Jira Config and automation code"
	 mylogger.info("${CustomFieldName}: ${CustomFieldValue}")
	 
	 return CustomFieldValue
							 
	 }
	 