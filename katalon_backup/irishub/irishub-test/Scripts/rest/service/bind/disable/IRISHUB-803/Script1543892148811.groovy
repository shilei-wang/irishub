import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.ResponseObject as ResponseObject
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint

import internal.GlobalVariable as GlobalVariable
import rest.ServiceUtils
import rest.BaseTx
import rest.AccountUtils
import rest.GetAddressByKey
import utils.CmdUtils as CmdUtils
import utils.StringUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject


faucet = findTestData('base/faucet')
v0 = faucet.getValue('name', 1)
password = faucet.getValue('password', 1)
defineData = findTestData('service/define/define/IRISHUB-624')
bindData = findTestData('service/bind/bind/IRISHUB-726')
serviceName = defineData.getValue("service-name", 1).concat("_").concat(CmdUtils.generateRandomID())

ServiceUtils.serviceDefineCorrectEg(v0, password, serviceName, defineData)
ServiceUtils.serviceBindCorrectEg(v0, password, serviceName, bindData)

String[] BaseTxInfo = BaseTx.baseTxProduce(v0, password)
//(String BaseTx, String provider, String service_name, String def_chain_id){
response = ServiceUtils.serviceBindingDisable(BaseTxInfo[0], BaseTxInfo[1], serviceName, GlobalVariable.chainId)
WS.verifyEqual(StringUtils.stringContains(response.responseBodyContent,"hash"), true)

AccountUtils.waitUntilNextBlock()

response = ServiceUtils.getServiceBinding(serviceName, GlobalVariable.chainId, GlobalVariable.chainId, BaseTxInfo[1]) 
re = CmdUtils.Parse(response.responseBodyContent).getAsJsonObject().get("value").getAsJsonObject()
WS.verifyEqual(re.get("available").getAsString(), "false")


