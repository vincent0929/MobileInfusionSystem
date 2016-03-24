<?php
namespace Home\Controller;
use Think\Controller;
class DrugController extends Controller {
    public function index(){
        
        
        
        if (!isset($_SESSION['id'])) {
            
            //header("location:index.php/Home/index/login");
            
            $this->redirect('index/login');
    	}
        
        
        
        
        
    	
    	$drug = M('drug');




		// import('ORG.Util.Page');// 导入分页类
		// $count      = $drug->count();// 查询满足要求的总记录数
		// $Page       = new Page($count,15);// 实例化分页类 传入总记录数和每页显示的记录数
		// $Page->setConfig('header','个药品');
		// $show       = $Page->show();// 分页显示输出
		// // 进行分页数据查询 注意limit方法的参数要使用Page类的属性
		// $list = $drug->order('id')->limit($Page->firstRow.','.$Page->listRows)->select();






    	$list = $drug->order('id')->select();

		$this->assign('data',$list);// 赋值数据集
		$this->assign('pageshow',$show);// 赋值分页输出

    	 $this->display("drug/index");   	
  	
 		// $user = M('admin');
       
   //     var_dump($user->select());

    }

//删除药品
    public function deleteDrug(){
    	$drug = M('drug');

    	
    	$id = I('id');

    	$result = $drug->where("id=".$id)->delete();
		if($result>=0){
			$this->success("删除成功！");
		}else{
			$this->error("删除失败！");
		}
    }


    //编辑药品
    public function editDrug(){

    	$drug = M('drug');
    	$id = I('id');

    	$list=$drug->where("id=".$id)->find();
		$this->assign("list",$list);

		$this->display('drug/edit');

    }

    //添加药品信息
    public function addDrug(){
        
          if (!isset($_SESSION['id'])) {
            
            //header("location:index.php/Home/index/login");
            
            $this->redirect('index/login');
    	}

        $drug = M('drug');


       $this->display('drug/add');

    }

    //保存修改后药品信息
    public function saveDrug(){


    	$drug = M('drug');

        $id = I('post.id','');
        // 采用htmlspecialchars方法对$_POST['name'] 进行过滤，如果不存在则返回空字符串
        // $data['id'] = I('post.id','','htmlspecialchars');
        $data['name'] = I('post.name','','htmlspecialchars'); 
        $data['specifications'] = I('post.specifications','','htmlspecialchars'); 
        $data['dw'] = I('post.dw','','htmlspecialchars'); 
        $data['price'] = I('post.price','','htmlspecialchars'); 
        $data['gen_date'] = I('post.gen_date','','htmlspecialchars'); 
        $data['pz_date'] = I('post.pz_date','','htmlspecialchars'); 
        $data['barcode'] = I('post.barcode','','htmlspecialchars');  

        if(empty($id)){
            $result =$drug->data($data)->add();
        }else{
            $result = $drug->where("id=".$id)->save($data);
        }
        if($result>=0){
            $this->success("修改成功！",'index');
        }else{
            $this->error("修改失败！",'index');
        }
    }
}