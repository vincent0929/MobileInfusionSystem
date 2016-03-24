<?php 
namespace Home\Controller;
use Think\Controller;
class NurseController extends Controller {

	public function listuser()
	{
		$nurse = M('nurse');

		$nursedata = $nurse->select();
		$this->assign("nursedata",$nursedata);
		$this->display("Nurse/listuser");


	}

	public function add()
	{
		$this->display("Nurse/add");
	}

	public function doadd()
	{
		$nurse = M('nurse');
		$data['realname'] = I('post.realname');
		$data['title'] = I('post.title');
		$data['username'] = I('post.username');
		$data['password'] = I('post.password');
		if($nurse->add($data)){
			$this->success("添加护士成功");
		}else{
			$this->success("添加失败");
		}



	}

	public function delete()
	{
		$id = I('get.id');

		$res = M('nurse')->where("id=$id")->delete();
		if($res){
			$this->success("删除护士成功");
		}else{
			$this->success("删除失败");
		}
	}




}












 ?>