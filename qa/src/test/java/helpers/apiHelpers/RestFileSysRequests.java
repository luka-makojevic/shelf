package helpers.apiHelpers;

import io.restassured.response.Response;
import models.FileSys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static helpers.apiHelpers.BaseApiTest.*;

public class RestFileSysRequests
{
    public Response setShelf(String shelfTitle, String tokenGenerated)
    {
        String shelfName = responseToJson.setShelfName(shelfTitle);
        String parsedJson = gson.toJson(shelfName);
        Response response = sendAuthorizedRequests.sendingPostReqForCreateShelf(tokenGenerated,parsedJson);
        return response;
    }

    public Integer getShelfId(String tokenGenerated)
    {
        Response response = sendAuthorizedRequests.sendingGetShelfReq(tokenGenerated);
        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
        Integer shelfId = shelfIdArray.get(0);
        return shelfId;
    }

    public FileSys setFolder(String tokenGenerated, String folderTitle)
    {
        Response response = sendAuthorizedRequests.sendingGetShelfReq(tokenGenerated);
        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
        Integer shelfId = shelfIdArray.get(0);
        fileSys.setValues(folderTitle,0, shelfId);
        return fileSys;
    }

    public List<Long> createFolderAndMoveItToTrash(String tokenGenerated)
    {
        setShelf("Shelfara123",tokenGenerated);
        setFolder(tokenGenerated,"Fascikla");
        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);
        Response response = sendAuthorizedRequests.sendingPutReqWithGeneratedTokenFLCShelf(tokenGenerated,fileSys.shelfId);

        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(folderId));

        sendAuthorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);
        return list;
    }
}
