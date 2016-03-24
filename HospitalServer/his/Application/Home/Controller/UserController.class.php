<?php
namespace Home\Controller;
use Think\Controller;
class UserController extends Controller {
    public function apply(){
        
        
        if (!isset($_SESSION['id'])) {
            //header("location:index.php/Home/index/login");
            $this->redirect('index/login');
    	}
        
        
        
        
        
    	$patient = M('patient');
    	$data = $patient->field('MAX(barcode)+1')->select();
    	$this->assign('barcode',$data[0]['max(barcode)+1']);
 	$this->display("User/apply");
      
    }

    public function doApply()
    {
    	$patient = M('patient');
    	$patient = M('patient');
    	$data['barcode'] = I('barcode');
    	$data['realname'] = I('realname');
    	$data['idcard'] = I('idcard');
    	$data['sex'] = I('sex');
    	$data['mobile'] = I('phone');
    	$data['address'] = I('address');
    	$patient->add($data);
    	$this->assign("barcode",I('barcode'));
    	$this->assign("realname",I('realname'));
    	$this->display('User/printcode');
    }

    public function listuser()
    {
        $patient = M('patient');
        $userdata = M('patient')->select();
        $this->assign("userdata",$userdata);

        $this->display('User/listuser');

    }

    public function delete()
    {
        $id = I('get.id');
         $res = M('patient')->where("id=$id")->delete();
         if($res){
            $this->success("删除成功");
         }else{
            $this->success("删除失败");
         }
    }


    public function progress()
    {
        $patient = M('patient');
        $res = $patient->query("SELECT realname,barcode,drugCode,startTime,remainTime FROM patient,infusion WHERE patient.`barcode` = infusion.`patientCode`");
        
        foreach ($res as $key => $value) {
            $times = explode(',',$value['remaintime']);
            $res[$key]['endTime'] = $res[$key]['starttime'] + array_sum($times) * 60;
        }

       $this->assign("progress",$res);
       $this->display("User/progress");
    }

}