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
import java.sql.Timestamp
import java.text.DateFormat
import java.util.Date
import groovy.time.TimeCategory


// ----CONFIGURATIONS------------------------------------------------------------------------------------------------------------------------------
// JIRA Custom field definitions
def CALIBRATED="Calibration done"
def TIMEINMONTHS="Calibration valid period (in months)"
def NEXTCALIBRATION="Next calibration required date"


// set logging to Jira log
def Logger mylogger
mylogger = Logger.getLogger("Calibrator")
mylogger.setLevel(Level.INFO ) // or INFO (prod) or DEBUG (development)
def CustomFieldManager = ComponentAccessor.getCustomFieldManager()
// ----END OF CONFIGURATIONS-------------------------------------------------------------------------------------------------------------------------



mylogger.info("------------------------------------------------------------------------------------------------")
mylogger.info("Processing calibration dates for issue: $issue")



def CalibratedFieldValue=GetCustomFieldValue(CALIBRATED,mylogger,CustomFieldManager)
mylogger.debug("Current CalibratedFieldValue: ${CalibratedFieldValue}")


def TIMEINMONTHSFieldValue=GetCustomFieldValue(TIMEINMONTHS,mylogger,CustomFieldManager)
mylogger.debug("Current valid moths value: ${TIMEINMONTHSFieldValue}")


def DateAsString = CalibratedFieldValue.toString()
Date CalibrationDate=new Date().parse('yyyy-MM-dd',DateAsString) 
int MonthsValue = TIMEINMONTHSFieldValue as Integer

// from import groovy.time.TimeCategory
def timeahead
def getted
Timestamp Deadeline
use(TimeCategory) {
	timeahead=CalibrationDate + MonthsValue.months
    Deadeline=new Timestamp(timeahead.getTime())
}
mylogger.debug( "New calibration date: $timeahead   ")
mylogger.debug( "New calibration datestamp: $Deadeline   ")

// Set new calibration date field
def NextDatecustomField = CustomFieldManager.getCustomFieldObjects(issue).find { it.name == NEXTCALIBRATION }
assert NextDatecustomField: "Could not find custom field with name $NEXTCALIBRATION"
//DateFormat TheFormat = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy ")  // : Mon Mar 15 00:00:00 EET 2021
DateFormat TheFormat = new SimpleDateFormat("yyyy-mm-dd")  // : Mon Mar 15 00:00:00 EET 2021

String SStingTimeAhead = TheFormat.format(timeahead)
mylogger.info( "Setting new calibration datefield ($NEXTCALIBRATION): $Deadeline   ")
issue.setCustomFieldValue(NextDatecustomField, Deadeline)
mylogger.info("------------------------------------------------------------------------------------------------")


/* --------------------------------------------------------------------------------------------------------------------------------
 Get custom field value.
 --------------------------------------------------------------------------------------------------------------------------------- */
 
 def GetCustomFieldValue (CustomFieldName,mylogger,CustomFieldManager) {
							 
	 def CustomFieldObject=CustomFieldManager.getCustomFieldObjectsByName(CustomFieldName).find()
	 assert CustomFieldObject : "Problems with customfield (${CustomFieldName}). Check Jira Config and automation code"
	 mylogger.debug("==> ${CustomFieldName}: ${CustomFieldObject}")
	 CustomFieldValue=issue.getCustomFieldValue(CustomFieldObject)
	 assert CustomFieldValue :  "Problems with customfield (${CustomFieldName}). Check Jira Config and automation code"
	 mylogger.info("==> ${CustomFieldName}: ${CustomFieldValue}")
	 
	 return CustomFieldValue
							 
	 }
	 