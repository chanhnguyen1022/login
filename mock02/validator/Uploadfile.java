package mock02.validator;

import org.springframework.web.multipart.MultipartFile;

/*
* TramTran(^^)
*/
public class Uploadfile {
    private MultipartFile file;

    
    public Uploadfile() {
      
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
   
}
