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
import utils.CmdUtils as CmdUtils
import utils.StringUtils as StringUtils
import cmd.ServiceUtils as ServiceUtils
import com.google.gson.JsonObject as JsonObject;
	
Utils u = new Utils();

for (int i = 1; i <= u.td.getRowNumbers(); i++) {
	request_id = ServiceUtils.call(u.v0, u.v0, u.serviceName)
	
	cmd = CmdUtils.generateCmd(u.command, u.td, i)
	cmd = cmd+" --request-id="+request_id
	response = CmdUtils.sendRequest('cmd/CmdWithOneArgs', cmd, "wait")
	WS.verifyEqual(StringUtils.stringContains(response.responseBodyContent,"tx hash"), true)
}


/* //CmdUtils.pl(u.command)
 * utils
 */

class Utils {
	public TestData td;
	public String command;
	public String serviceName;
	public String v0;
	
	public Utils(){
		td = findTestData('service/invocation/respond/IRISHUB-789')
		TestData faucet = findTestData('base/faucet')
		v0 = faucet.getValue('name', 1)

		serviceName = ServiceUtils.defineService(v0)
		ServiceUtils.bindService(v0, serviceName)	
		
		command = 'iriscli service respond'.concat(' --chain-id=').concat(GlobalVariable.chainId).concat(' --request-chain-id=').concat(GlobalVariable.chainId).concat(' --node=').
				concat(GlobalVariable.node).concat(' --from=').concat(v0)
		command = CmdUtils.addTxFee(command, findTestData('base/tx'), 1)
	}
}
