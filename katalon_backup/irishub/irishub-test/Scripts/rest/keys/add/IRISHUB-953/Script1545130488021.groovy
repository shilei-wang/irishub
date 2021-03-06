import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.Map

import org.junit.After

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

import utils.CmdUtils
import utils.StringUtils as StringUtils
import rest.AccountUtils

password = findTestData('base/faucet').getValue('password', 1)
String userName1 = "user_"+ CmdUtils.generateRandomID()
Map user1 = AccountUtils.addNewKey(userName1, password)

//case 1
response = AccountUtils.deleteKey(userName1, password)
WS.verifyEqual(response.getStatusCode(), 200)
keysList = AccountUtils.getKeysList()
WS.verifyEqual(keysList.containsKey(userName1), false)

//case 2
String userName2 = "user_"+ CmdUtils.generateRandomID()
Map user2 = AccountUtils.addNewKey(userName2, "123456789")
response = AccountUtils.deleteKey(userName2, "123456789")
WS.verifyEqual(response.getStatusCode(), 200)
keysList = AccountUtils.getKeysList()
WS.verifyEqual(keysList.containsKey(userName1), false)

