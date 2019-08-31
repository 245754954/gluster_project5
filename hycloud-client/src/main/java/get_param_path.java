import java.io.IOException;

public class get_param_path {

    public static String get_relative_path() throws IOException {
//
//        File f = new File(".");
//        System.out.println("path"+f.getCanonicalPath());
//       // System.out.println("path"+f.getAbsolutePath());
        String param_name = "/a.param";

//        String path = get_param_path.class.getClassLoader().getResource(param_name).getPath();
        String path = get_param_path.class.getResource(param_name).getPath();
        System.out.println("the path is "+ path);
        return path;

    }
}
