package myexcel.ashish.com.myexcel.extras;

import myexcel.ashish.com.myexcel.application.ZApplication;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public interface ZUrls {

    public String getWorksListUrl = ZApplication.getBaseUrl() + "get_works_list/";
    public String addWorkUrl = ZApplication.getBaseUrl() + "add_work/";

    public String getDetailsList = ZApplication.getBaseUrl() + "get_details_list/";
    public String addDetailUrl = ZApplication.getBaseUrl() + "add_detail/";

    public String deleteWork = ZApplication.getBaseUrl() + "delete_work/";
    public String deleteDetail = ZApplication.getBaseUrl() + "delete_detail/";

}
