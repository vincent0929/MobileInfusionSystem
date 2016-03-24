<?php



require_once 'vendor/autoload.php';

use JPush\Model as M;
use JPush\JPushClient;
use JPush\JPushLog;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;

use JPush\Exception\APIConnectionException;
use JPush\Exception\APIRequestException;
class PushMsgClass{
	public function  pushMsg($patientname="",$seatId="",$nurseId=1, $content="默认")
	{
		
		$br = '<br/>';
		$spilt = ' - ';
	
		$app_key='936a16d10ea663d636388655';
		$master_secret = 'f69be71a1d98bb30fd816e5e';
		// JPushLog::setLogHandlers(array(new StreamHandler('jpush.log', Logger::DEBUG)));
		$client = new JPushClient($app_key, $master_secret);
		try {
			$result = $client->push()
			->setPlatform(M\all)
			->setAudience(M\alias(array($nurseId)))
			->setNotification(M\notification("病人 $patientname ,座位号 : $seatId $content . 正在呼叫..."))
			->printJSON()
			->send();
			echo 'Push Success.' . $br;
			echo 'sendno : ' . $result->sendno . $br;
			echo 'msg_id : ' .$result->msg_id . $br;
			echo 'Response JSON : ' . $result->json . $br;
		} catch (APIRequestException $e) {
			echo 'Push Fail.' . $br;
			echo 'Http Code : ' . $e->httpCode . $br;
		} catch (APIConnectionException $e) {
			echo 'Push Fail: ' . $br;
			echo 'Error Message: ' . $e->getMessage() . $br;
		}
	}
}



