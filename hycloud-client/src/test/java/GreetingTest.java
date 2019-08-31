import cn.edu.nudt.hycloudclient.util.SignUtil;
import org.junit.Test;


public class GreetingTest {




    @Test
    public void testGreeting() {

////        String []str =  SignUtil.Sign("zfc",3);
//        if(null==str){
//
//            System.out.print("null");
//        }else{
//
//            System.out.print("not null");
//        }
//        System.out.println("the value of w_str :  "+str[0]);
//        System.out.println("the value of y_str:   "+str[1]);


    }



    @Test
    public void test_generate_param() {

        String []str = SignUtil.generate_x_and_p();

        System.out.println("the value of x_str :  "+str[0]);
        System.out.println("the value of y_str:   "+str[1]);
        System.out.println("the value of p_str:   "+str[2]);

    }


}