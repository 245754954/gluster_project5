package cn.edu.nudt.hycloudserver.controller;

import cn.edu.nudt.hycloudserver.Configure.ServerConfig;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/upload")
public class UploadController {
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "E:/temp/";

    @GetMapping("/")
    public String index() {
        return "/upload.html";
    }

    @RequestMapping(value = "/singleFileUpload", method = {RequestMethod.GET, RequestMethod.POST})
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("upload");
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }


    @RequestMapping(value = "/multiFileUpload", method = RequestMethod.POST)
    public String multiFileUpload(@RequestParam("file") MultipartFile[] files, RedirectAttributes redirectAttributes) {
        StringBuilder builder = new StringBuilder();

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
                return "redirect:uploadStatus";
            }
            try {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);


            } catch (IOException e) {

                e.printStackTrace();
            }


            builder.append(file.getOriginalFilename()).append(",");
        }

        if (builder.length() > 1) {
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" +    builder.toString() + "'");

            builder = builder.deleteCharAt(builder.length() - 1);
        }

        return "redirect:/uploadStatus";

    }




    /**
     * 实现文件上传
     * */
    @RequestMapping("/singleFileUpload1")
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        //得到配置文件中上传文件的存储路径
        String path = ServerConfig.getConfig().getStore_directory();
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }

/**
 * 实现多文件上传
 * */

    @RequestMapping(value="multiFileUpload1",method=RequestMethod.POST)
    public @ResponseBody String multifileUpload(@RequestParam("file") MultipartFile[] files) throws IOException {


        //得到配置文件中上传文件的存储路径
        String path = ServerConfig.getConfig().getStore_directory();

        for(MultipartFile file:files){

            if(file.isEmpty()){
                return "false";
            }
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

            if(file.isEmpty()){
                return "false";
            }else{
                path = ""+path+"/"+fileName+"";
                System.out.println(path);
                File dest = new File(path);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "true";
    }


    @RequestMapping("/fileDownload")
    public String downLoad(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String name =request.getParameter("filename");
        System.out.println("filename is "+name);


        String filename=name;
        //得到配置文件中上传文件的存储路径
        String filePath = ServerConfig.getConfig().getStore_directory();

        File file = new File(filePath + "/" + filename);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
