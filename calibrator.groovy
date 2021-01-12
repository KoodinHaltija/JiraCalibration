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
mylogger.setLevel(Level.DEBUG) // or INFO (prod) or DEBUG (development)
def CustomFieldManager = ComponentAccessor.getCustomFieldManager()
// ----END OF CONFIGURATIONS-------------------------------------------------------------------------------------------------------------------------




mylogger.info("Processing calibration dates for issue: $issue")



def CalibratedFieldValue=GetCustomFieldValue(CALIBRATED,mylogger,CustomFieldManager)
mylogger.debug("Current CalibratedFieldValue: ${CalibratedFieldValue}")


def TIMEINMONTHSFieldValue=GetCustomFieldValue(TIMEINMONTHS,mylogger,CustomFieldManager)
mylogger.debug("Current TIMEINMONTHSFieldValue: ${TIMEINMONTHSFieldValue}")

//Date CalibratedDate=new Date()
//long CurrentDateMillisecs=CurrentDate.getTime()
//mylogger.debug( "CurrentDate: $CurrentDate (CurrentDateMillisecs:$CurrentDateMillisecs)")

//def CalibrationDate=new Date().parse('yyyy-MM-dd',CalibratedFieldValue.toString()).format('yyyy-MM-dd')
//long CalibrationDateMillisecs=CalibrationDate.getTime()
//mylogger.debug( "CalibrationDate: $CalibrationDate  ..... CalibrationDateMillisecs:$CalibrationDateMillisecs ")


def DateAsString = CalibratedFieldValue.toString()
def kaak="1999-12-15"
Date CalibrationDate=new Date().parse('yyyy-MM-dd',DateAsString) //.format('yyyy-MM-dd')
long CalibrationDateMillisecs=CalibrationDate.getTime()
mylogger.debug( "CalibrationDate: $CalibrationDate  ..... CalibrationDateMillisecs:$CalibrationDateMillisecs ")
int MonthsValue = TIMEINMONTHSFieldValue as Integer

long okTimePeriodMillisecs=(MonthsValue)*30*24*60*60*1000
def NewCalibrationDateMillisecs=CalibrationDateMillisecs+okTimePeriodMillisecs


Date NewCalibrationDate= new Date(NewCalibrationDateMillisecs)

mylogger.debug( "NewCalibrationDate: $NewCalibrationDate   ")
mylogger.debug( "okTimePeriodMillisecs: $okTimePeriodMillisecs   ")


// import groovy.time.TimeCategory
def timeahead
use(TimeCategory) {
	timeahead=CalibrationDate + MonthsValue.months
	mylogger.debug( "timeahead: $timeahead   ")
}
mylogger.debug( "timeahead: $timeahead   ")



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
	 