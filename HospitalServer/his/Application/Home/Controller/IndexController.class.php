<?php
namespace Home\Controller;
use Think\Controller;
class IndexController extends Controller {
    public function index(){
    	if (!isset($_SESSION['id'])) {
    		header("location:index.php/Home/index/login");
    	}
    	$current = M('infusion')->where('state=1')->count();
    	$wait = M('infusion')->where('state=0')->count();

    	$this->assign("current",$current);
    	$this->assign("wait",$wait);
       $this->display("index/index");
      
    }

    public function Login()
    {
    	if (isset($_SESSION['id'])) {
    		session_destroy();
    		$this->error("退出成功",U('index/login'));
    	}else{
    		$this->display('index/login');
    	}
    	
    }
    public function dologin()
    {

    	$data['username'] = I('username');
    	$data['password'] = I('password');
    	$res = M('admin')->where($data)->count();
    	if ($res) {
    		$_SESSION['id'] = $res;
    		$_SESSION['username'] = $data['username'];
    		$this->success("登陆成功",U('Index/index'));

    	}else{
    		$this->error("登陆失败",U('Index/login'));
    	}

    }
}