import org.junit.Test;


public class GreetingTest {




    @Test
    public void testGreeting() {

        String []str =  SignUtil.Sign("zfc",3);
        if(null==str){

            System.out.print("null");
        }else{

            System.out.print("not null");
        }
        System.out.print(str[0]);

//        SignUtil.greeting();


    }
}