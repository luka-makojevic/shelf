package helpers.apiHelpers;

import java.io.File;
import static helpers.apiHelpers.BaseApiTest.files;


public class UploadFilesFromPTCDirectory
{

    public void uploadFilesFromPTCdir()
    {
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.7z"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.docx"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.exe"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.jar"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.mkv"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.mp3"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.mp4"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.pdf"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.pptx"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.rar"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.txt"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.xlsx"));
        files.add(new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.zip"));
    }
}
