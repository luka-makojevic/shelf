package models;

public class FileSys
{
    public String folderName;
    public String shelfName;
    public Integer shelfId;
    public Integer parentFolderId;
    private Integer folderId;

    public void setValues(String folderName, Integer parentFolderId, Integer shelfId)
    {
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
        this.shelfId = shelfId;
    }

    public void setValuesForRenameShelf(String shelfName, Integer shelfId)
    {
        this.shelfName = shelfName;
        this.shelfId = shelfId;
    }

    public void setValuesForRenameFolder(String folderName, Integer folderId)
    {
        this.folderName = folderName;
        this.folderId = folderId;
    }


}


