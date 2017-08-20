package mock02.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import mock02.model.Course;
import mock02.model.User;
import mock02.service.ManageCourseService;
import mock02.service.ManageStudentService;
import mock02.validator.Validate;
import mock02.validator.Uploadfile;

/*
* TramTran(^^)
*/
@Controller
public class ManageCourseController {
    private HttpSession session;
    @Autowired
    private ManageCourseService manageCourseService;
    @Autowired
    ManageStudentService manageStudentService;
    
    Uploadfile ufile=null;
    
    
    @RequestMapping(value = "upload_file.html", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(ModelMap model, MultipartHttpServletRequest mrequest, HttpServletRequest request,
            HttpServletResponse response) {
        MultipartFile mpf = null;
        // 1. get the files from the request object
        try{
            Iterator<String> itr = mrequest.getFileNames();
             mpf = mrequest.getFile(itr.next());
            ufile = new Uploadfile();

            ufile.setFile(mpf);
            return mpf.getOriginalFilename();
            
        }
        catch(Exception e){
            return "Upload not success!";
        }
    }
    //
    @RequestMapping(value = "create_course.html", method = RequestMethod.POST)
    public String createCourse(ModelMap model,HttpServletRequest request, HttpServletResponse response) {
        session = request.getSession();
        String type = request.getParameter("type");
        
        if(ufile == null){//A.kiểm tra xem file đã được import chưa 
            System.out.println("loi chua up file");
            model.addAttribute("create_course","You not upload file yet!");
            
        }
        else{//B.đã dc import
            Course course = new Course();
            String nameC = request.getParameter("name_course");
            String des = request.getParameter("description_course");
            course.setCourseName(nameC);
            course.setDescription(des);
            Course c1 = manageCourseService.getCourse(nameC);//1.kiểm tra xem course đã tồn tại chưa theo username
            if(c1==null){//2:nếu course chưa tồn tại
                boolean result = manageCourseService.createCourse(course);//2.1:create course
                if(!result){//2.2 tao ko thanh cong
                    model.addAttribute("create_course","Create course not succuess!");
                }
                else{//2.3 tao thanh cong
                    Course c = manageCourseService.getCourse(nameC);//2.3.1: lấy course sau khi insert thành công để lấy id
                    User teacher =(User) session.getAttribute("teacher_cur");//2.3.2: lấy teacher đang quản lí course trong session
                    List<User> listStudent =  manageCourseService.readFile(ufile);//2.3.3: lấy ds student đã import
                    boolean result2 = manageStudentService.insertMember(teacher, c);//2.3.4:thêm khóa học do teacher quản lí
                    if(result2){//2.3.4: Tạo khóa học do teacher quản lí thành công
                        for(User u:listStudent){
                            String valid = Validate.formatUser(u);
                            if(valid!=null){//2.3.4.1: nếu sai định dạng
                                model.addAttribute("create_course", u.getEmail() +": Wrong format!"+valid+"");
                                continue;
                            }
                            else if(manageStudentService.getUser(u.getEmail())!=null){//2.3.4.2: student đã tồn tại
                                User uExist =manageStudentService.getUser(u.getEmail());
                                if(manageCourseService.checkUserofCouse(uExist, c)!=null){//1.kiểm tra student đã tồn tại trong khóa học đó 
                                    model.addAttribute("create_course",uExist.getEmail()+ " exist in course!");
                                }
                                else{//2. nếu chưa tồn tại trong khóa học thì thêm vào khóa học
                                    manageStudentService.insertMember(uExist, c);
                                }
                                continue;
                            }
                            else{//2.3.4.3: student chưa tồn tại và đúng định dạng
                                String result3 = manageStudentService.insertUser(u, c);//insert user vao course
                                if(result3!=null){//thêm không thành công thì báo lỗi và bỏ qua student, tiếp tục insert student tiếp theo
                                    model.addAttribute("create_course",u.getEmail()+" insert not succuess!");
                                    continue;
                                }
                            }
                            
                        }
                    }
                    else{//2.3.5: Tạo ko thanh công
                        model.addAttribute("create_course","Create course not succuess!");
                    }
                  
                } 
            }
            else{//3:đã tồn tại
                model.addAttribute("create_course", "Course name exist!");
            }
        }
        //
        if("grid".equals(type)){
            return "redirect:teacher_grid_home.html";
        }
        else{
            return "redirect:teacher_list_home.html";
        }
        
    }
    
    @RequestMapping(value = "delete_course.html", method = RequestMethod.POST)
    public @ResponseBody String deleteCourse(@RequestParam(value = "courseID") String id,ModelMap model,HttpServletRequest request, HttpServletResponse response) {
        session = request.getSession();
        int courseID =-1;
        try{
            courseID = Integer.parseInt(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(courseID!=-1){
            boolean result = manageCourseService.deleteCourse(courseID);
            if(result){
                return "1";
            }
            else{
                return "0";
            }
        }
        else{
            return "0";
        }
    }
    @RequestMapping(value = "update_course.html", method = RequestMethod.POST)
    public  String updateCourse(ModelMap model,HttpServletRequest request, HttpServletResponse response) {
        session = request.getSession();
        String nameC = request.getParameter("name_course");
        String des = request.getParameter("description_course");
        String id = request.getParameter("id_course");
        String upfrm = request.getParameter("up_frm");//neu update thi lay id

        int courseID = -1;
        try{
            courseID = Integer.parseInt(id);
        }
        catch(NumberFormatException e){
            courseID = -1;
        }
        //
        if(upfrm.equals("")||upfrm==null){//A.chưa import file thì chỉ cần updale thông tin course
            Course c = new Course();
            c.setIdCourse(courseID);
            c.setCourseName(nameC);
            c.setDescription(des);
            String result = manageCourseService.updateCourse(c);
            if(result!=null){model.addAttribute("create_course",result);}
        }
        else{//B.đã dc import, thì update course và đọc file
            //1.update course
            Course c = new Course();
            c.setIdCourse(courseID);
            c.setCourseName(nameC);
            c.setDescription(des);
            String result = manageCourseService.updateCourse(c);
            if(result!=null){
                model.addAttribute("create_course",result);
             }
            else{//đọc file
                List<User> listStudent =  manageCourseService.readFile(ufile);//1: lấy ds student đã import
                for(User u:listStudent){
                    String valid =  Validate.formatUser(u);
                    if(valid!=null){//2: nếu sai định dạng
                        model.addAttribute("create_course", u.getEmail() +": Wrong format!"+valid+"");
                        continue;
                    }
                    else if(manageStudentService.getUser(u.getEmail())!=null){//3: student đã tồn tại
                        User uExist =manageStudentService.getUser(u.getEmail());
                        if(manageCourseService.checkUserofCouse(uExist, c)!=null){//1.kiểm tra student đã tồn tại trong khóa học đó 
                            continue;
                        }
                        else{//2. nếu chưa tồn tại trong khóa học thì thêm vào khóa học
                            manageStudentService.insertMember(uExist, c);
                        }
                        
                    }
                    else{//3: student chưa tồn tại và đúng định dạng
                        String result3 = manageStudentService.insertUser(u, c);//insert user vao course
                        if(result3!=null){//thêm không thành công thì báo lỗi và bỏ qua student, tiếp tục insert student tiếp theo
                            model.addAttribute("create_course",u.getEmail()+" insert not succuess!");
                            continue;
                        }
                    }
                    
                }
                //end đọc file
            }
        }
        
        //chuyển hướng trang
        return "redirect:teacher_list_home.html";
    }
    
}
