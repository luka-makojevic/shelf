package models;

public class FileSys
{
    public String folderName;
    public String shelfName;
    public Integer shelfId;
    public Integer parentFolderId;

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


}


